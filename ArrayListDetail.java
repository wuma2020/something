package cn.kkcoder.wangkuiwu;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * @author static-mkk 
 * @time 4 Mar 2018
 * @myblog  www.kkcoder.cn
 *
 */
public class ArrayListDetail {

	/**
	 * 	ArrayList 详解
	 * 
	 * 	定义： 是一个数组队列.能动态增长.继承 AbstractList抽象类，实现List，RandomAccess,Cloneable, java.io.Serializable这些接口.
	 * 	特点:	1.ArrayList实现了List接口，提供了添加，删除，修改，遍历等功能.
	 * 		 	2.ArrayList实现了RandomAccess接口，提供快速访问功能.比如：根据元素序号获取对应元素的功能.
	 * 			3.ArrayList实现了Cloneable接口，提供被克隆的能力.
	 * 			4.ArrayList实现了java.io.Serializable 接口，提供了 其能被序列化然后传输的功能.
	 * 			
	 * Tips:	1.ArrayList是线程不安全的。<因此在多线程中因该使用的是Vector或者是CopyOnWriteArrayList>
	 * 
	 * 
	 * 
	 */
	
	public static void main(String[] args) {
		
		
		ArrayList<String> arrayList = new ArrayList<>();     	//默认空参构造方法
		arrayList = new ArrayList<>(arrayList);					//创建一个包含指定集合((Collection<? extends E> collection)的ArrayList对象
		int initialCapacity = 3;
		arrayList = new ArrayList<>(initialCapacity); 			//创建一个指定容量大小为initialCapacity的ArrayList对象.默认为10.
																//每次会增加上一次大小容量的一半.
		ArrayList<String> b = new ArrayList<>();
		
		arrayList.add("2");										//添加String类型对象2
		arrayList.addAll(b);                            		//把 b添加带arrayList中
		arrayList.clear();										//清空集合arrayList
		arrayList.contains("2");								//判断arrayList是否含有2，是，返回true，否则，返回false
		arrayList.containsAll(b);								//判断arrayList是否含有数组队列b，是，返回true，否则，返回false
		
		int i=0;
		arrayList.remove(i);									//返回指定位置  <i>对应的对象
		
		arrayList.iterator();									//返回Iterator迭代器,用于遍历数组
		arrayList.size();										//返回arrayList大小
		arrayList.toArray();									//返回 Object 数组
		arrayList.add(i, "3"); 									//在指定i位置添加object对象 "3"
		arrayList.indexOf("3");									//返回 值为该对象"3" 的位置. 
		arrayList.set(i, "5");									//把位置为i的对象修改成"5"
		
		
		
		/**
		 * 
		 * ArrayList  API
		 * 
		 *  Collection中定义的API
			boolean             add(E object)												//添加对象
			boolean             addAll(Collection<? extends E> collection)					//把指定集合添加到该集合
			void                clear()														//清除集合
			boolean             contains(Object object)
			boolean             containsAll(Collection<?> collection)
			boolean             equals(Object object)
			int                 hashCode()
			boolean             isEmpty()
			Iterator<E>         iterator()
			boolean             remove(Object object)
			boolean             removeAll(Collection<?> collection)
			boolean             retainAll(Collection<?> collection)
			int                 size()
			<T> T[]             toArray(T[] array)
			Object[]            toArray()
			AbstractCollection中定义的API
			void                add(int location, E object)
			boolean             addAll(int location, Collection<? extends E> collection)
			E                   get(int location)
			int                 indexOf(Object object)
			int                 lastIndexOf(Object object)
			ListIterator<E>     listIterator(int location)
			ListIterator<E>     listIterator()
			E                   remove(int location)
			E                   set(int location, E object)
			List<E>             subList(int start, int end)
			// ArrayList新增的API
			Object               clone()
			void                 ensureCapacity(int minimumCapacity)
			void                 trimToSize()
			void                 removeRange(int fromIndex, int toIndex)
		 * 
		 * 
		 */
		
		
		/**
		 * 
		 * ArrayList 数据结构 
		 * 
		 * 		ArrayList 继承关系
		 * 
		 * 		java.lang.Object
				   ↳     java.util.AbstractCollection<E>
				         ↳     java.util.AbstractList<E>
				               ↳     java.util.ArrayList<E>
				               
				               
		 *      ArrayList类声明
		 * 
		 * public class ArrayList<E> extends AbstractList<E>
        		implements List<E>, RandomAccess, Cloneable, java.io.Serializable {}
         *
         *
		 */
		
		/**
		 * 
		 * ArrayList 主要就是用 内部的一个成员变量  Object[] elementData;来存储数据
		 * 
		 * 总结：
			(01) ArrayList 实际上是通过一个数组去保存数据的。当我们构造ArrayList时；若使用默认构造函数，则ArrayList的默认容量大小是10。
			(02) 当ArrayList容量不足以容纳全部元素时，ArrayList会重新设置容量：新的容量=“(原始容量x3)/2 + 1”。
			(03) ArrayList的克隆函数，即是将全部元素克隆到一个数组中。
			(04) ArrayList实现java.io.Serializable的方式。当写入到输出流时，先写入“容量”，再依次写入“每一个元素”；当读出输入流时，先读取“容量”，再依次读取“每一个元素”。
		 * 
		 * 
		 * 源码部分就直接看 <a href="http://wangkuiwu.github.io/2012/02/03/collection-03-arraylist/" >这里</a>的 源码吧
		 * 
		 * 也可以看 这里  1.8 没有分析的源码
		 * 
		 */
		
		
		/**
		 * ArrayList的遍历方式
		 * 
		 * 		Tips: 
		 * 			结果表明：	方式2， 即通过索引遍历，效率最高.
		 * 						方式1，即iterator遍历效率最差.
		 */
		
		/*
		 * 		1.Iterator
		 */
		Iterator iterator  =  arrayList.iterator();
		Object obj = null;
		while(iterator.hasNext()){
			obj = iterator.next();
		}
		
		/*
		 * 		2.通过索引遍历
		 */
		int size = arrayList.size();
		for(int j=0;j<size;j++){
			obj = arrayList.get(j);
		}
		/**
		 * 		3.增强for循环
		 */
		for(Object arr : arrayList){
			obj = arr;
		}
	
	/**
	 * 
	 * 		ToArray 异常
	 * 
	 * 			因为 java 不支持向下转型.
	 * 			所以尽量使用 java.util.ArrayList.toArray(T[] contents)
	 * 
	 */
		
	arrayList.toArray(new String[size]);
	
	
	}
	
	
}
