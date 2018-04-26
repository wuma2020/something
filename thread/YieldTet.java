package cn.kkcoder.thread;

public class YieldTet {

	static Object obj = new Object();
	
	public static void main(String[] args) {
		TestYield t1 = new TestYield("t1");
		TestYield t2 = new TestYield("t2");
		
		t1.start();
		t2.start();
	}

	static class TestYield extends Thread{
		public TestYield(String name) {
			super(name);
		}
		
		@Override
		public void run() {

			synchronized(obj) {
				for(int i=0;i<=5;i++) {
					
					System.out.println(Thread.currentThread().getName() + " i = " + i);
					
					if(i%3==0) {
						System.out.println(Thread.currentThread().getName() + " 执行yield");
						Thread.yield();
					}
				}
			}
			
		}
		
		
	}
	
}

