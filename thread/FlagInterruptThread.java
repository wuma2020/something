package cn.kkcoder.thread;

/**
 * 
 * @author static-mkk
 * @time   27 Mar 2018
 *		Flag线程类
 */
class FlagThread extends Thread{
	
	boolean flag = true;
	
	public void setFlagFalse() {
		this.flag = false;
	}
	
	@Override
	public void run() {
		
		try {
			
			int i=0;
			while(flag) {
				Thread.sleep(100);
				System.out.println("i = " + i);
				i++;
			}
			
		} catch (Exception e) {
		}
	}
	
}

/**
 * 
 * @author static-mkk
 * @time   27 Mar 2018<br/>
 *	
 *	自己设置标记来终止线程 demo.
 */
public class FlagInterruptThread {

	public static void main(String[] args) {
		try {
		FlagThread t = new FlagThread();
		//启动线程
		t.start();
		System.out.println("t线程 已经启动");
		
		//主线程睡眠 200 ms之后，设置Flag 为 false。为了让线程t的运行一段时间，以表明t在运行
		Thread.sleep(200);
		t.setFlagFalse();
		//打印语句和t.setFlagFalse是两个同时进行的线程，所以此时的线程t也许并没有终止
		System.out.println( "t 的falg设置为false 的 状态时， ：  " + t.getState());
		
		//这个主线程sleep 200 ms ，是为了有足够的时间让线程t终止.
		Thread.sleep(200);
		System.out.println("t最后的状态： " + t.getState());
		
		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}

