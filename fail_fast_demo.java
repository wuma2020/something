package cn.kkcoder.wangkuiwu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author static-mkk 
 * @time 5 Mar 2018
 * @myblog  www.kkcoder.cn
 *  
 *  ArrayList的快速失败机制演示
 *
 *	总结：
 *		ArrayList是线程不安全的，当一个线程对其进行操作时，如果顶一个线程对其进行修改，就会触发arraylist的快速失败机制.
 *
 *		而：CopyOnWriteArrayList 和 Vector 对象是安全的.
 *				
 *
 */
public class fail_fast_demo {

	private static List<String> list = new ArrayList<>();
	
	public static void main(String[] args) {
		//启动两个线程
		new ThreadOne().start();
		new ThreadTwo().start();
		
		
	}
	
	/**
	 * 公共的打印方法
	 */
	static void printAll(){
		Iterator iterator = list.iterator(); 
		while(iterator.hasNext()){
			System.out.print(iterator.next());
		}
		System.out.println("");
		
	}
	
	
	/**
	 * 1.线程1，向list中添加数据
	 */
	private static class ThreadOne extends Thread{
		@Override
		public void run() {
			for(int i=0;i<5;i++){
				list.add(String.valueOf(i));
				printAll();
			}
		}
	}
	
	/**
	 * 2.线程2，向list中添加数据
	 */
	private static class ThreadTwo extends Thread{
		@Override
		public void run() {
			for(int i=10;i<15;i++){
				list.add(String.valueOf(i));
				printAll();
			}
		}
	}
	
	/**
	 * 		控制台打印的信息.
	 * 			报错：java.util.ConcurrentModificationException
	 * 
			0Exception in thread "Thread-0" 010
			01011
			0101112
			010111213
			01011121314
			java.util.ConcurrentModificationException
				at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:901)
				at java.util.ArrayList$Itr.next(ArrayList.java:851)
				at cn.kkcoder.wangkuiwu.fail_fast_demo.printAll(fail_fast_demo.java:34)
				at cn.kkcoder.wangkuiwu.fail_fast_demo$ThreadOne.run(fail_fast_demo.java:49)

	 * 
	 * 
	 * 
	 * 
	 */
	
}
