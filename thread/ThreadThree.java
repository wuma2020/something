package cn.kkcoder.thread;
/**
 * 
 * @author static-mkk
 * 
 * @time 2018-3-22
 * <br/>
 *  当一个线程已经获取某对象的同步锁，其他线程不能获取该对象的其他同步锁修饰的方法或者语句块.
 */
public class ThreadThree {

	public static void main(String[] args) {

		TwoSynchronizedMethod syn = new TwoSynchronizedMethod();
		
		Thread t1 = new Thread( ()-> {syn.synchronizedOne();} );
		Thread t2 = new Thread( ()-> {syn.synchronizedTwo();} );
		
		t1.start();
		t2.start();
		
	}

}

class TwoSynchronizedMethod{
	
	/**同步方法 一*/
	synchronized void synchronizedOne(){
		
		for(int i=0;i<=5;i++) {
			try {
				Thread.currentThread().sleep(100);
				System.out.println( "我是 one and i = " + i );
			} catch (Exception e) {
				new RuntimeException("one sleep fail");
			}
		}
	}
	/**同步方法 二*/
	synchronized void synchronizedTwo(){
		
		for(int i=0;i<=5;i++) {
			try {
				Thread.currentThread().sleep(100);
				System.out.println( "我是 two and i = " + i );
			} catch (Exception e) {
				new RuntimeException("two sleep fail");
			}
		}
	}
	
	
}