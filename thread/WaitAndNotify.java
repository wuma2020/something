package cn.kkcoder.thread;
/**
 * 
 * @author static-mkk
 * @time   23 Mar 2018 <br/>
 *	
 *	wait() notify() notifyAll() 介绍
 */
public class WaitAndNotify {

	public static void main(String[] args) throws InterruptedException {

		notifyThread t = new notifyThread("t1");
		
		synchronized(t) {
			
			System.out.println(Thread.currentThread().getName() + " .start()");
			t.start();
			
			System.out.println(Thread.currentThread().getName() + "  .wait()");
			t.wait();
			
			System.out.println(Thread.currentThread().getName() + " .结束");
		}
		
	}

}

/**
 * 
 * @author static-mkk
 * @time   23 Mar 2018<br/>
 *		线程类
 */
class notifyThread extends Thread{
	
	public notifyThread(String name) {
		super(name);
	}
	
	@Override
	public void run() {
		synchronized(this) {
			System.out.println(Thread.currentThread().getName() + "  call notify()");
			notify();
		}
			
	}
	
}