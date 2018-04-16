# Thread_Runnable_demo

标签： java8多线程

---

# Thread demo
```java
package cn.kkcoder.thread;

public class MyThread extends Thread{

	@Override
	public void run() {
		for(int i=0;i <=5;i++){
			System.out.println("我是"+this.getName()+"的 i = " + i);
			}
		}
	
	public static void main(String[] args) {

		MyThread m1 = new MyThread();
		MyThread m2 = new MyThread();
		
		m1.start();
		m2.start();
	}

}

```

# Runnable demo
```java
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
		
	}
}
class Runnable2 implements Runnable{
	
	private int count = 10;
	@Override
	public void run() {
		for(int i=0;i<=5;i++){
			System.out.println("我是 " +this.getClass().getName() + " 的 count = " + this.count);
			count--;
		}
		
	}
	
}
```

# java8 lambda runnable demo
```java
		Runnable run = ()-> System.out.println("我是线程 + ");
		Thread t = new Thread(run);
		t.start();
```

# start() 和 run() 方法的区别
    
    t.start()：新建线程，并执行该线程中的run()方法.只能调用一次.
    t.run() : 就像调用普通方法那样调用run()方法.并不会新建一个线程. 




