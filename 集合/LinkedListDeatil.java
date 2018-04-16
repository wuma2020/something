package cn.kkcoder.wangkuiwu;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author static-mkk 
 * @time 6 Mar 2018
 * @myblog  www.kkcoder.cn
 *  
 *
 *	LinkedList 详细介绍.
 *
 *	LinkedList 的继承关系
 *
	 java.lang.Object
	   ↳     java.util.AbstractCollection<E>
	         ↳     java.util.AbstractList<E>
	               ↳     java.util.AbstractSequentialList<E>
	                     ↳     java.util.LinkedList<E>

 *
 *	LinkedList的类声明
 *
 *		public class LinkedList<E>
		    extends AbstractSequentialList<E>
		    implements List<E>, Deque<E>, Cloneable, java.io.Serializable {}
		    
 *
 *	特点：
 *		1.LinkedList 继承与AbstractSequentialList双向链表抽象类.所以可以被当做堆栈,队列或者是双端队列使用.
 *		2.LinkedList 实现了List接口.可以进行队列操作.
 *		3.LinkedList 实现了Deque接口,使其可以当做双向队列使用.
 *		4.LinkedList 实现了 Cloneable接口,所以其可以克隆.
 *		5.LinkedList 实现了 Serializable接口,所以其可以序列化去传输.
 *		6.LinkedList 是非同步的.
 *
 */
public class LinkedListDeatil {

	/**
	 * 
	 * 		看了wangkuiwu大神 关于LinkedList的解析.感觉很强
	 * 		但是不太容易理解.
	 * 
************我尝试着，通过讲解LinkedList的具体某个方法的内部实现来介绍 LinkedList的工作原理.
	 */
	
	public static void main(String[] args) {
		
		
		//下面具体介绍LinkedList构造器方法---------------------------------------------------
		LinkedList<String> linkedList = new LinkedList<>(); 
/*
 * 
 * 	1.这里需要介绍一下LinkedList类的成员变量和一个内部类
 * 		i.	Node<E> 内部类    
 * 
	 		 private static class Node<E> {
		        E item;
		        Node<E> next;
		        Node<E> prev;
		
		        Node(Node<E> prev, E element, Node<E> next) {
		            this.item = element;
		            this.next = next;
		            this.prev = prev;
		        }
	    	}	
 * 
 * 		ii.  transient Node<E> first;    LinkedList的第一个节点. 实际上，是一个Node<E> 类型的对象.
 * 		iii. transient Node<E> last;	LinkedList的最后一个节点. 同上.
 * 
 * 	2.介绍一下空构造器方法的具体实现.
 * 
		    public LinkedList() {
		    }
 * 
 * 	显然，空构造器并没有做任何处理.
 * 
 */
		
		
		
		//下面会详细讲述整个add();的实现.----------------------------------------------------
		linkedList.add("StringObject");
/*
 *
 * 	1.这里需要介绍一下LinkedList类的成员变量和一个内部类
 * 		i.	Node<E> 内部类    
 * 
	 		 private static class Node<E> {
		        E item;
		        Node<E> next;
		        Node<E> prev;
		
		        Node(Node<E> prev, E element, Node<E> next) {
		            this.item = element;
		            this.next = next;
		            this.prev = prev;
		        }
	    	}	
 * 
 * 		ii.  transient Node<E> first;    LinkedList的第一个节点. 实际上，是一个Node<E> 类型的对象.
 * 		iii. transient Node<E> last;	LinkedList的最后一个节点. 同上.
 * 
 *     
 *	2.现在来详细介绍linkedList的add()方法的具体实现.
 *     	
 *     i.调用linkLast(e);方法,
	  		public boolean add(E e) {
	        linkLast(e);
	        return true;
	    	}
 * 
 * 	  	ii.
 * 			---创建一个Node<E>实例对象newNode
 * 				并且把linkedList实例对象中的成员变量last作为参数,给newNode的成员prev进行赋值
 * 				并且把添加的对象"StringObject"作为参数，给newNode的成员element进行赋值
 * 				并且把newNode的成员变量next设置成null
 * 
 * 			---做判断	如果LinkedList的成员变量last为null，则newNode为LinkedList的第一个节点对象
 * 						否则,在LinkedList中的成员变量(Node类型的)last的成员(Node类型的)next赋值为newNode
 * 
 * 			---把LinkedList的size + 1 ;  并把（修改次数）modCount + 1(目的是触发快速失败机制 fast-fail <参见ArrayList的Fastfail> ).
 * 			 void linkLast(E e) {
		        final Node<E> l = last;
		        final Node<E> newNode = new Node<>(l, e, null);
		        last = newNode;
		        if (l == null)
		            first = newNode;
		        else
		            l.next = newNode;
		        size++;
		        modCount++;
		    }
 * 	
 * 
 */
		
		
		
		//下面讲述linkedList的remove方法------------------------------------------------------------
		linkedList.remove("StringObject");
		
/*
 * 		思路:
 * 			遍历整个链表，从first开始，直到某一个Node的next成员变量为null为止.(也就是成员变量last)
 * 			找到 第一个 与 该remove对象相等的 Node.item .然后对整个Node进行删除
 * 	
 * 		1.
 * 		---判断remove对象是否为null 
 * 									若是,则进入if进行删除
 * 									若否,则进入else进行删除 
 * 		
 * 		---调用具体的删除方法unlink(x);
 * 
 * 		public boolean remove(Object o) {
	        if (o == null) {
	            for (Node<E> x = first; x != null; x = x.next) {
	                if (x.item == null) {
	                    unlink(x);
	                    return true;
	                }
	            }
	        } else {
	            for (Node<E> x = first; x != null; x = x.next) {
	                if (o.equals(x.item)) {
	                    unlink(x);
	                    return true;
	                }
	            }
	        }
	        return false;
	    }
 * 
 * 		2.	
 * 			目的：对传进来的节点进行删除.
 * 			做法：
 * 				---把该节点的上一个节点的下一个节点设置成下一个节点的上一个节点
 * 				---并把该节点的item设置成null
 * 				---并size - 1; modCount + 1; 返回该删除节点的成员变量item(也就是，要删除的对象).
 * 			E unlink(Node<E> x) {
		        // assert x != null;
		        final E element = x.item;
		        final Node<E> next = x.next;
		        final Node<E> prev = x.prev;
		
		        if (prev == null) {
		            first = next;
		        } else {
		            prev.next = next;
		            x.prev = null;
		        }
		
		        if (next == null) {
		            last = prev;
		        } else {
		            next.prev = prev;
		            x.next = null;
		        }
		
		        x.item = null;
		        size--;
		        modCount++;
		        return element;
		    }
 * 
 * 
 * 
 */
		
		//删除指定索引的节点--------------------------------------------------------------------
		linkedList.remove(0);
		
/*
 *	 思路：
 *		检查索引  
 *		找到指定索引对应的node节点对象
 *		用unlink(Node<E> e)方法删除该节点   ---参见上述该方法讲述
 *
 *	1.
	  public E remove(int index) {
	        checkElementIndex(index); //检查元素索引
	        return unlink(node(index));
	    }
 * 	2. node(int index); 方法解析
 * 		
 * 		---遍历到index-1所在的Node<E>对象
 * 		---返回该对象的下一个节点的Node<E>对象
 * 
 * 		Node<E> node(int index) {
        // assert isElementIndex(index);

        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }
 * 
 * 
 */
		//下面就列一下常用的api-----------------------------------------------------------
		linkedList.get(0);
		linkedList.isEmpty();
		linkedList.size();
		linkedList.clear();
		linkedList.addFirst("newObject");
		linkedList.getFirst();
		linkedList.element();//返回第一个Node（first）的element
		linkedList.indexOf("newObject");
		
		// ···
		
		
		
		//LinkedList的遍历-----------------------------------------------------------------
		 
		//1.迭代器遍历                                次之
		Iterator itera = linkedList.iterator();
		while(itera.hasNext()){
			System.out.println(itera.next());
		}
		
		//2.快速随机访问                           坚决不要用,因为效率太差.参见get(int index);源码
		int size = linkedList.size();
		for(int i =0 ; i<size;i++){
			linkedList.get(i);
		}
		
		//3.增强for循环                                 最快
		for(String s : linkedList){
			System.out.println(s);
		}
		
		//4.
		
		
		
		
	}
	
}
