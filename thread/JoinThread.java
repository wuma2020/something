package cn.kkcoder.thread;

public class JoinThread {

	public static void main(String[] args) {
		Son s = new Son();
		Thread t1 = new Thread(s);
		t1.start();
		try {
			t1.join();
			System.out.println(Thread.currentThread().getName());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Son implements Runnable{

	@Override
	public void run() {

		System.out.println(Thread.currentThread().getName() + " Son 的 run ");
		System.out.println("son finish ");
	}
	
	
}