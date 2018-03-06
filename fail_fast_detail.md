ArrayList 的快速失败原理解析.<br>

   产生原因：
   
    1.ArrayList类的父类AbtractList在内部，获取内部类Iterator对象.
    2.Iterator的成员有一个int类型成员变量(int)expectedModCount ,初始值为ArrayList对象成员变量(int)modCount赋值.
    3.modCount 为arraylist对象的修改次数的记录.  expectedModCount 是该arraylist对象第一次获取的Iterator对象是进行赋值的.
    4.当 同一个arraylist 对象,同时获取Iterator对象进行修改时，会导致不同的Iterator对象的成员变量(int)expectedModCount的值不同(如果arraylist对象被改变过的话)
    5.每次iterator对象进行遍历的时候,都会检查该iterator对象的expectedModCount是否与arraylist对象的modCount值相等.不相等，则抛出异常java.util.ConcurrentModificationException
   	

大神的描述：

 
 接下来，我们再系统的梳理一下fail-fast是怎么产生的。步骤如下：
 
    (01) 新建了一个ArrayList，名称为arrayList。
    (02) 向arrayList中添加内容。
    (03) 新建一个“线程a”，并在“线程a”中通过Iterator反复的读取arrayList的值。
    (04) 新建一个“线程b”，在“线程b”中删除arrayList中的一个“节点A”。
    (05) 这时，就会产生有趣的事件了。

    在某一时刻，“线程a”创建了arrayList的Iterator。此时“节点A”仍然存在于arrayList中，创建arrayList时，expectedModCount = modCount(假设它们此时的值为N)。
    在“线程a”在遍历arrayList过程中的某一时刻，“线程b”执行了，并且“线程b”删除了arrayList中的“节点A”。“线程b”执行remove()进行删除操作时，在remove()中执行了“modCount++”，此时modCount变成了N+1！
    “线程a”接着遍历，当它执行到next()函数时，调用checkForComodification()比较“expectedModCount”和“modCount”的大小；而“expectedModCount=N”，“modCount=N+1”,这样，便抛出ConcurrentModificationException异常，产生fail-fast事件。

    至此，我们就完全了解了fail-fast是如何产生的！
    即，当多个线程对同一个集合进行操作的时候，某线程访问集合的过程中，该集合的内容被其他线程所改变(即其它线程通过add、remove、clear等方法，改变了modCount的值)；这时，就会抛出ConcurrentModificationException异常，产生fail-fast事件。


`CopyOnWriteArrayList 是如何解决快速失败问题的.`
    
    解决办法：	把一个arraylist对象的所有元素，复制一份到Iterator对象的成员变量 Object[] snapshot中
                然后，进行查询.   该类不提供所有对arraylist对象操作的方法.
					
		总结          ： 	在进行查询时，使用CopyOnWriteArrayList可以避免快速失败机制
		
 `大神总结：`
 
 
    (01) 和ArrayList继承于AbstractList不同，CopyOnWriteArrayList没有继承于AbstractList，它仅仅只是实现了List接口。
    (02) ArrayList的iterator()函数返回的Iterator是在AbstractList中实现的；而CopyOnWriteArrayList是自己实现Iterator。
    (03) ArrayList的Iterator实现类中调用next()时，会“调用checkForComodification()比较‘expectedModCount’和‘modCount’的大小”；但是，CopyOnWriteArrayList的Iterator实现类中，没有所谓的checkForComodification()，更不会抛出ConcurrentModificationException异常！

   	
   			
`代码看起来更容易理解一些：`

ArrayList快速失败源码
```java

	package java.util;
	public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {
	
	//···

    // AbstractList中唯一的属性
    // 用来记录List修改的次数：每修改一次(添加/删除等操作)，将modCount+1
    protected transient int modCount = 0;

    // 返回List对应迭代器。实际上，是返回Itr对象。
    public Iterator<E> iterator() {
        return new Itr();
    }

    // Itr是Iterator(迭代器)的实现类
    private class Itr implements Iterator<E> {
        int cursor = 0;

        int lastRet = -1;

        // 修改数的记录值。
        // 每次新建Itr()对象时，都会保存新建该对象时对应的modCount；
        // 以后每次遍历List中的元素的时候，都会比较expectedModCount和modCount是否相等；
        // 若不相等，则抛出ConcurrentModificationException异常，产生fail-fast事件。
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size();
        }

        public E next() {
            // 获取下一个元素之前，都会判断“新建Itr对象时保存的modCount”和“当前的modCount”是否相等；
            // 若不相等，则抛出ConcurrentModificationException异常，产生fail-fast事件。
            checkForComodification();
            try {
                E next = get(cursor);
                lastRet = cursor++;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (lastRet == -1)
                throw new IllegalStateException();
            checkForComodification();

            try {
                AbstractList.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    //...
}
```



`CopyOnWriteArrayList 部分源码：`


```java
package java.util.concurrent;
import java.util.*;
import java.util.concurrent.locks.*;
import sun.misc.Unsafe;

public class CopyOnWriteArrayList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

    ...

    // 返回集合对应的迭代器
    public Iterator<E> iterator() {
        return new COWIterator<E>(getArray(), 0);
    }

    ...

    private static class COWIterator<E> implements ListIterator<E> {
        private final Object[] snapshot;

        private int cursor;

        private COWIterator(Object[] elements, int initialCursor) {
            cursor = initialCursor;
            // 新建COWIterator时，将集合中的元素保存到一个新的拷贝数组中。
            // 这样，当原始集合的数据改变，拷贝数据中的值也不会变化。
            snapshot = elements;
        }

        public boolean hasNext() {
            return cursor < snapshot.length;
        }

        public boolean hasPrevious() {
            return cursor > 0;
        }

        public E next() {
            if (! hasNext())
                throw new NoSuchElementException();
            return (E) snapshot[cursor++];
        }

        public E previous() {
            if (! hasPrevious())
                throw new NoSuchElementException();
            return (E) snapshot[--cursor];
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor-1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }

    ...

}

```



