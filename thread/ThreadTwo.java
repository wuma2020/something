package cn.kkcoder.thread;
/**
 * 
 * @author static-mkk
 *<br/>
 *  一个线程访问某对象的synchronized的方法或语句块时，其他线程可以访问该对象的非同步方法.
 *  
 */
public class ThreadTwo {

	public static void main(String[] args) {
		ThreadNoAndHaveSynchronized a = new ThreadNoAndHaveSynchronized();
		
		Thread t1 = new Thread(()->{a.haveSynchronized();});
		Thread t2 = new Thread(()-> {a.noSynchronized();});
		
		t1.start();
		t2.start();
	}

}

/**
 * 
 * @author static-mkk
 *<br/>
 * 测试类
 */
class ThreadNoAndHaveSynchronized {
	
/**
 *   同步的方法
 */
	public void haveSynchronized () {
		synchronized(this) {
			for(int i=0;i<=5;i++) {
				try {
					Thread.currentThread().sleep(100);
				} catch (InterruptedException e) {
					new RuntimeException("sleep 失败.");
				}
				System.out.println("我是 1 de : "+ " 的 i = " + i);
			}
		}
	}
	
	/**
	 *  非同步的方法
	 */
	public void noSynchronized() {
		for(int i=0;i<=5;i++) {
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				new RuntimeException("sleep 失败.");
			}
			System.out.println("我是 2 de : "+  " 的 i = " + i);
		}
	}
	
}