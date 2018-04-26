package cn.kkcoder.thread;

public class AllWhitOme {

	public static void main(String[] args) {

		TestClass a = new TestClass();
		TestClass b = new TestClass();
		
//		Thread t1 = new Thread(() -> {a.s1();} );
//		Thread t2 = new Thread(() -> {a.s2();});
//		t1.start();
//		t2.start();
//		
		
//		Thread t3 = new Thread(() -> {a.s1();});
//		Thread t4 = new Thread(() -> {b.s1();});
//		t3.start();
//		t4.start();

//		
//		Thread t5 = new Thread(() -> {a.s3();});
//		Thread t6 = new Thread(() -> {b.s4();});
//		t5.start();
//		t6.start();
//		
//		
//		Thread t7 = new Thread(() -> {a.s1();});
//		Thread t8 = new Thread(() -> {b.s3();});
//		t7.start();
//		t8.start();
		
		Thread t9 = new Thread(() -> {a.s3();});
		Thread t0 = new Thread(() -> {b.s4();});
		t9.start();
		t0.start();
		
	}

}

/**同步测试类*/
class TestClass{
	
	/**实例锁 方法 s1*/
	synchronized void s1() {
		try {
			for(int i=1;i<3;i++) {
				Thread.currentThread().sleep(100);
				System.out.println("我是 s1 i = " + i );
			}
		}catch (Exception e) {
		}
	}
	
	/**实例锁 方法 s2*/
	synchronized void s2() {
		try {
			for(int i=1;i<3;i++) {
				Thread.currentThread().sleep(100);
				System.out.println("我是 s2 i = " + i );
			}
		}catch (Exception e) {
		}
	}
	
	/**全局锁 方法 s3*/
	static synchronized void s3() {
		try {
			for(int i=1;i<3;i++) {
				Thread.currentThread().sleep(100);
				System.out.println("我是 s3 i = " + i );
			}
		}catch (Exception e) {
		}
	}
	
	/**全局锁 方法 s4*/
	static synchronized void s4() {
		try {
			for(int i=1;i<3;i++) {
				Thread.currentThread().sleep(100);
				System.out.println("我是 s4 i = " + i );
			}
		}catch (Exception e) {
		}
	}
	
	
}