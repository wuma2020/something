package cn.kkcoder.thread;
/**
 * 
 * @author static-mkk
 * @time   27 Mar 2018<br/>
 *  
 *  终止线程的方法：
 *  	根据线程状态不同，进行不同处理.使线程终止.
 *	 
 */
public class InterruptDemoOne {

	public static void main(String[] args) {
		
		BlockedThread t = new BlockedThread("t1");
		t.start();
		System.out.println(t.getName() + "线程开启");
		
		try {
			Thread.sleep(300);
			t.interrupt();//向线程t发出终断的命令.
			System.out.println("t在interrupt之后的状态 ： " + t.getState() );
			Thread.sleep(300);
			System.out.println("t的状态 ： " + t.getState() );
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

/**
 * 
 * @author static-mkk
 * @time   27 Mar 2018
 *		线程类： 
 */
class BlockedThread extends Thread{
	public BlockedThread(String name) {
		super(name);
	}
	
	@Override
	public void run() {
		try {
			int i=0;
			while(!isInterrupted()) {//当isInterrupted 的结果为false 时，会抛出异常且该线程终结.会在while循环之外抛出异常.
				Thread.sleep(100);
				System.out.println(Thread.currentThread().getName() + " : " + i );
				i++;
			}
		} catch (Exception e) {
			System.out.println(Thread.currentThread().getName() + " 退出线程 ");
		}
		
	}
}

