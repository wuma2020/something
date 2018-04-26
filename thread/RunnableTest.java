package cn.kkcoder.thread;

public class RunnableTest {
	public static void main(String[] args) {
		Runnable2 r1 = new Runnable2();
		
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r1);
		Thread t3 = new Thread(r1);
		
		t1.start();
		t2.start();
		t3.start();
		
		
		Runnable run = ()-> System.out.println("我是线程 + ");
		Thread t = new Thread(run);
		t.start();
		
		
	}
}
class Runnable2 implements Runnable{
	
	private int count = 10;
	@Override
	public synchronized void run() {
		for(int i=0;i<=5;i++){
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("我是 " +this.getClass().getName() + " 的 count = " + this.count);
			count--;
		}
		
	}
	
}