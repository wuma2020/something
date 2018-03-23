package cn.kkcoder.thread;

public class NotifyAll {

	static Object obj = new Object();
	
	public static void main(String[] args) {

		NotifyAllClass t1 = new NotifyAllClass("t1");
		NotifyAllClass t2 = new NotifyAllClass("t2");
		NotifyAllClass t3 = new NotifyAllClass("t3");
		
		t1.start();
		t2.start();
		t3.start();
		
		try {
			System.out.println(Thread.currentThread().getName() + " sleep 3000 ms");
			Thread.currentThread().sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		synchronized (obj) {
			System.out.println(Thread.currentThread().getName() + " 唤醒所有线程");
			obj.notifyAll();
		}
		
	}

	
	static class NotifyAllClass extends Thread{
		
		public NotifyAllClass(String name) {
			super(name);
		}
		
		@Override
		public void run() {

			synchronized (obj) {
				try {
					System.out.println(Thread.currentThread().getName() + " 执行 wait ");
					obj.wait();
					System.out.println(Thread.currentThread().getName() + "继续 执行");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		
		}
		
	}
	
	
	
}

