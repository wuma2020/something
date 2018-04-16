package cn.kkcoder.wangkuiwu;

import java.util.Iterator;
import java.util.Vector;

/**
 * 
 * @author static-mkk 
 * @time 7 Mar 2018
 * @myblog  www.kkcoder.cn
 *  
 *		Vector 详细介绍
 *
 *		Vector类声明
		 	public class Vector<E>
			    extends AbstractList<E>
			    implements List<E>, RandomAccess, Cloneable, java.io.Serializable
			    
 *
 *     	Vector特点
 *     		1.Vector 继承自AbstractList,实现了List接口,所以其是一个队列,支持相关的添加删除修改遍历等功能
 *     		2.Vector 实现了RandomAccess接口,所以其具有快速随机访问的功能.
 *     		3.Vector 实现了Cloneable 接口,所以其具有可以克隆的功能.
 *     		4.Vector 实现了 Serializable 接口,所以其具有可以序列化传输的功能.
 *     
 *     	重要功能: Vector是线程安全的.  而，ArrayList 是线程不安全的.
 *				
 *			--- 因为 Vector的public方法都用synchronized修饰      
 *			--- synchronized：同步锁.只能等到一个线程调用结束后,才可以被其他线程调用   因此 , 线程安全.
 *			
 */
public class VectorDetail {

	
	public static void main(String[] args) {
		
		String s1 = "s1";
		String s2 = "s2";
		String s3 = "s3";
		
		
		//下面开始讲述Vector的空构造器-------------------------------------------------------------------------
		Vector<String> vector = new Vector<>(); 
		
/*
 * 	Vector ： 通过把对象存入到成员变量 Object数组中,来存储信息.(与ArrayList类似)
 * 
 * 	1.首先需要了解一下 Vector 的几个成员变量
 * 		
 * 		i.		protected Object[] elementData;   	存储对象的数组
 * 		ii.		protected int elementCount;      	存储对象的总个数
 * 		iii.	protected int capacityIncrement;  	Vector增长系数.用于确定动态增加数组大小的确定.
 *
 *
 *
 *	2.使用空构造器是，会默认设置Vector的数组对象的大小为10, 并且把增量设置为0
		  	i.
	  			public Vector() {
		        	this(10);												//初始值设置为10
		    	}
		    ii.
				public Vector(int initialCapacity) {
			        this(initialCapacity, 0);       						//把增量设置为0
			    }
				
		    iii.  设置Object数组大小为10, 增量为0
	    	   public Vector(int initialCapacity, int capacityIncrement) {
			        super();
			        if (initialCapacity < 0)
			            throw new IllegalArgumentException("Illegal Capacity: "+
			                                               initialCapacity);
			        this.elementData = new Object[initialCapacity];
			        this.capacityIncrement = capacityIncrement;
			    }
    	
 *
 *
 * 
 */
		/*
		 *  Vector 的另外三种构造器
		vector = new Vector<>(initialCapacity);                             //指定队列   大小
		vector = new Vector<>(initialCapacity, capacityIncrement);			//指定队列   大小 和 增量
		vector = new Vector<>(Collection <? extends Collection> c)	;		//指定包含指定 集合的队列
		*/
		
		
		//Vector 添加方法的具体实现 .-----------------------------------------------------------------------
		vector.add(s1);          
		vector.addElement(s2);  //作用同add();方法   返回空,add();方法返回boolean类型
		
/*
 * 		注意 ：	这里的方法都用 synchronized修饰. 保证Vector是安全的.
 * 
 *  	思路 ：   
 *  
 *  			---modCount + 1 修改次数 + 1
 *  			---判断加入该对象后数量是否大于当前容量
 *  					---是：增大容量
 *  					---否：返回void 继续执行下面代码
 *  			---把s1保存到成员对象数组elementData
 * 
 * 
 		 public synchronized boolean add(E e) {
		        modCount++;
		        ensureCapacityHelper(elementCount + 1);
		        elementData[elementCount++] = e;
		        return true;
		    }
 
 
 * 
 */
		
		//Vector 的删除方法的具体实现-----------------------------------------------
		vector.remove(s1);
		
/*
 * 		强调 ：Vector 是线程安全的. 其内部的方法,基本都加了synchronized修饰符.  
 * 
 * 		i. 调用removeElement(Object obj);方法删除
 * 
		   public boolean remove(Object o) {
		        return removeElement(o);
		    }
 * 
 * 		ii.
 * 			--- 1.modCount + 1 ;修改次数 + 1
 * 			--- 2.调用 index(Object obj); 方法，返回该对象对应的索引
 * 			--- 3.如果索引 >0,则调用 removeElementAt(int index); 方法删除对应索引的对象
 * 
		  	public synchronized boolean removeElement(Object obj) {
		        modCount++;
		        int i = indexOf(obj);
		        if (i >= 0) {
		            removeElementAt(i);
		            return true;
		        }
		        return false;
		    }
		    
 			--- 4.removeElementAt(int index); 
 					--> modCount 修改次数 + 1
 					--> 检查index 
		 			--> elementCount 对象数量 -1 ;并把相应索引的对象赋值为null
		 			
			 public synchronized void removeElementAt(int index) {
			        modCount++;
			        if (index >= elementCount) {
			            throw new ArrayIndexOutOfBoundsException(index + " >= " +
			                                                     elementCount);
			        }
			        else if (index < 0) {
			            throw new ArrayIndexOutOfBoundsException(index);
			        }
			        int j = elementCount - index - 1;
			        if (j > 0) {
			            System.arraycopy(elementData, index + 1, elementData, index, j);
			        }
			        elementCount--;
			        elementData[elementCount] = null; 
			    }

 * 
 * 
 * 
 */
		
		
		//Vector 部分API-----------------------------------------------------------------------------------------
		
		vector.size();			//返回vector 大小
		vector.get(0);
		vector.contains(s1); 	//返回，是否包含s1的boolean对象
		vector.elements();		//返回“Vector中全部元素对应的Enumeration
		int index =0;
		vector.set(index, s3);	//把索引为index的值设置为s3
		vector.clear();
		vector.elementAt(0);	//返回指定索引的值
		vector.iterator();
		vector.toArray();		//返回Object类型的数组.
		String[] s= null;
		vector.toArray(s);		//返回任意类型的对象数组,自己制定.

		//Vector 中比较重要的一个方法
/*
 * 		i.判断数组下标是否越界，如果越界，就增加 数组大小.
 * 
  		 private void ensureCapacityHelper(int minCapacity) {
	        // overflow-conscious code
	        if (minCapacity - elementData.length > 0)
	            grow(minCapacity);
	   	}
 * 
 
 		// 当Vector的容量不足以容纳当前的全部元素，增加容量大小。
        // 若 容量增量系数>0(即capacityIncrement>0)，则将容量增大当capacityIncrement
        // 否则，将容量增大一倍。
 
 
	 	private void grow(int minCapacity) {
	        int oldCapacity = elementData.length;
	        int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
	                                         capacityIncrement : oldCapacity);
	        if (newCapacity - minCapacity < 0)
	            newCapacity = minCapacity;
	        if (newCapacity - MAX_ARRAY_SIZE > 0)
	            newCapacity = hugeCapacity(minCapacity);
	        elementData = Arrays.copyOf(elementData, newCapacity);
	    }

 
 * 
 * 
 * 
 */
		
		
		//Vector 的各种遍历方法-----------------------------------------------------
		
		//1.通过Iterator									最慢
		@SuppressWarnings("rawtypes")
		Iterator  iterator  = vector.iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next());
		}
		
		//2.通过索引for循环                                                			  最快
		int size = vector.size();
		for(int i=0;i<size;i++){
			System.out.println(vector.get(i));
		}
		
		//3.通过增强for循环
		for(String temp : vector){
			System.out.println(temp);
		}
		
		
	}
	
}
