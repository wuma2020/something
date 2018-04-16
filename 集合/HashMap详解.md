# HashMap详解

标签： java8源码
> 欢迎 [start][1]  

----------
# HashMap的关系图

<img src="http://wangkuiwu.github.io/media/pic/java/collection/collection10.jpg" alt="img">

# HashMap的特点

> `HashMap类签名`:` public class HashMap<K,V> extends AbstractMap<K,V> 
implements Map<K,V>, Cloneable, Serializable {}`

`特点：`

    1. HashMap 继承自AbstractMap抽象类，实现了Map接口.以键值对存储信息.
    2. HashMap 的键不能重复，且可以为null，最多只有一个键为null.
        其值可以重复，且可以为null，可以多个对象为null.
    3. HashMap 是线程不安全的.    
    4. HashMap 的键值对是无序的.
    
    
# HashMap的结构介绍

 1. 其成员变量介绍
                
         * DEFAULT_INITIAL_CAPACITY  : 默认初始容量 = 16
         * MAXIMUM_CAPACITY ：最大容量 = 2 的 30 次方
         * DEFAULT_LOAD_FACTOR ： 默认增长系数  = 0.75f
         * int size : HashMap 大小
         * int threshold : 阈值.用于判断是否需要调整HashMap的容量（threshold = 容
            量*加载因子）
         * Set<Map.Entry<K,V>> entrySet : 键值对集合
         * Node<K,V>[] table ： 存储新的Node数组 
         * int modCount ： 修改次数（用于触发快速失败机制）

2.内部类`Node<K,V>`介绍
                
```java
static class Node<K,V> implements Map.Entry<K,V> {//继承自Map.Entry<K,V> 实现其方法
        final int hash;   
        final K key;  
        V value;
        Node<K,V> next;  //指向下一个Node<K,V> .由此可知，HashMap的Node<K,V>是个单向的链表

        Node(int hash, K key, V value, Node<K,V> next) {//构造函数
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }

```    
         
# 通过常用方法了解HashMap的实现原理

 > 1. HashMap的构造方法
```java
  public HashMap() {//空的构造方法
        this.loadFactor = DEFAULT_LOAD_FACTOR;//初始化增长系数
        //所有的属性都是默认值
    }
    
   public HashMap(int initialCapacity) { //初始化指定容量的构造方法
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }  
    
    public HashMap(int initialCapacity, float loadFactor) {//初始化容量大小，并指定增长系数的构造方法
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }
    
```

> 2.`put(K key,V value)`;方法
```java
----------------put（K,V）--------------------------
public V put(K key, V value) { //调用putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) 方法
        return putVal(hash(key), key, value, false, true);
    }

--------------putVal（···）方法-------------------

 final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
//如果table为空，则通过resize()方法重新创建一个Node<K,V>复制给tab   这里已经把 table 赋值给 tab
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
//通过 数组下标最大值 与 hash 的与操作，返回数组下标，并取出值赋给 节点 p，如果p为空，则新建Node节点，并赋值给tab[i]
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {//如果p不会空，即，添加的元素已经存在，进入此判断
            Node<K,V> e; K k;
//如果p的hash和key等于传进来的key和p.hash 或者 key 不为空且 p.key等于传进来的key，则 把 p 赋值给 e.
//--------------------------未读---------------------------
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
//---------------------------------------------------

            if (e != null) { // 如果e != null 说明在当前容器中，存在一个相同的key值,于是替换key所对应的value
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);//空方法，留着给LinkedHashMap调用
                return oldValue;
            }
        }
        //修改次数 + 1
        ++modCount;
        if (++size > threshold)//如果size > 阈值，则重新改变大小.
            resize();
        afterNodeInsertion(evict);//空方法，留着给LinkedHashMap调用
        return null;
    }

```

> 附上整个put()的[逻辑图][3] 

## HashMap源码太难了，的确还讲不好.推荐几个好的关于HashMap源码解读的文章

# 优秀的HashMap 的源码解读的文章

 1. [简书][4]
 2. [Github 1][5]
 3. [Github 2][6]
 4. [cnblog][7]


  [1]: https://github.com/static-mkk/wangkuiwu
 
  [3]: https://upload-images.jianshu.io/upload_images/3778985-e6d288eb43812bcd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700
  [4]: https://www.jianshu.com/p/30bffabb2e5c
  [5]: https://github.com/vzardlloo/jdk_source_learning/blob/master/src/HashMap.md
  [6]: http://wangkuiwu.github.io/2012/02/10/collection-10-hashmap/
  [7]: http://www.cnblogs.com/chenssy/p/3521565.html
