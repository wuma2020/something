package cn.kkcoder.wangkuiwu;

import java.util.Stack;
/**
 * 
 * @author static-mkk 
 * @time 8 Mar 2018
 * @myblog  www.kkcoder.cn
 *  
 *	Stack 详细介绍
 *
 *	Stack 的类声明
 *
 *		public	class Stack<E> extends Vector<E> {	}
 *
 * 	特点：
 * 		由于stack 完全是继承 Vector ,所以其具有Vector的所有特点.
 * 		详情请看 关于 Vector 详解部分  <a href="https://github.com/static-mkk/wangkuiwu/blob/master/VectorDetail.java">vector详解</a>
 *
 *		所以,Stack 和 Vector一样，内部是通过Object数组来存储信息,并且线程安全的.
 *
 *	下面,讲述一下Stack的自己扩赞的几个方法及其原理	.
 *
 */
public class StackDetail {

	public static void main(String[] args) {

		String s1 = "1";
		String s2 = "2";
		String s3 = "3";
		//Stack 空的构造函数
		Stack<String> s = new Stack<>(); 
		
		//Stack 的push(); 方法-----------------------------------------------------------------------
		s.push(s1);
		
		/*
		 *  实际上，就是调用父类，也就是vector的addElement(E e);方法向成员变量对象数组中添加元素.
		 *  		Vector 的 addElement(E e) 方法解析请看 上述关于Vector 详细分析的内容
		 *  		  <a href="https://github.com/static-mkk/wangkuiwu/blob/master/VectorDetail.java">vector详解</a>
		 *  
		 *  public E push(E item) {
		        addElement(item);
		        return item;
		    }
		 * 
		 */
		
		 
		//Stack 返回栈顶元素，并不删除---------------------------------------------------------
		s.peek();
		/*
		  	  	实际就是返回object数组的最后一个元素的值.
		  	  	同样是调用了 Vector 的方法 elementAt(int index);
		  
			  public synchronized E peek() {
			        int     len = size();
			        if (len == 0)
			            throw new EmptyStackException();
			        return elementAt(len - 1);
			    }
		 */
		
		//Stack 的查询方法  由栈底向栈顶方查询
		s.search(s1);
		/*
		
		 1.调用lastIndexOf(Object o ); 查询.
			 public synchronized int search(Object o) {
			        int i = lastIndexOf(o);
			
			        if (i >= 0) {
			            return size() - i;
			        }
			        return -1;
			    }
			    
		2. lastIndexOf(Object o );的查询逻辑. 
		
			 if (o == null) {
	            for (int i = index; i >= 0; i--)  //从后往前查，也就是从栈底向栈顶查
	                if (elementData[i]==null) 
	                    return i;
		        } else {
		            for (int i = index; i >= 0; i--)
		                if (o.equals(elementData[i]))
		                    return i;
		        }
			    
		 * 
		 */
		
	}

}
