package cn.kkcoder.thread;

/**
 *
 *  i-- 和 System.out.println() 一起使用，出现非线程安全问题demo
 *
 */

/**
 * Created by static-mkk on 6/4/2018.
 */
 class PrintThreadOne extends Thread{


	private  int i =5;
	@Override
	public void run() {
		//注意这里的 i-- 实际上，是在进入println方法之前执行的。并且是线程不安全的。故导致打印出的i是非同步的。
		System.out.println("i = " + i-- + "  当前线程：" + currentThread().getName() );
	}
}

public class PrintThread{
	public static void main(String[] args) {
		PrintThreadOne t = new PrintThreadOne();
		Thread t1 = new Thread(t);
		Thread t2 = new Thread(t);
		Thread t3 = new Thread(t);
		Thread t4 = new Thread(t);

		t1.start();
		t2.start();
		t3.start();
		t4.start();

		/*
			i = 4  当前线程：Thread-2
			i = 3  当前线程：Thread-3
			i = 5  当前线程：Thread-1
			i = 4  当前线程：Thread-4
		 */

	}
}

