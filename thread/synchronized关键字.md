# synchronized关键字

标签： 多线程

---
> 欢迎 [star][1]

---
# 目的
> 保证指定某类的的方法或方法块或者该类只能同时被一个类对象调用.

# synchronized的基本规则

## 规则一
> 当一个线程访问 `某对象` 的 synchronized 方法或者代码块时,其他线程对该 `该对象的` synchronize 方法或则语句块 的访问会被 `阻塞`. 

### demo1 多个线程访问同一对象的synchronized方法
```java
//此时出现同步锁现象.
package cn.kkcoder.thread;

/**
 * Created by static-mkk on 21/3/2018.
 *
 */
public class ThreadTest_one_runnable {

    public static void main(String[] args) {

        MyRunnable myRunnable = new MyRunnable();

        Thread t1 = new Thread(myRunnable,"t1");
        Thread t2 = new Thread(myRunnable,"t2");

        t1.start();
        t2.start();

    }
}

class MyRunnable implements Runnable{


    @Override
    public void run() {
        synchronized (this){
            try {
                for (int i=0;i<=10;i++){
                    Thread.currentThread().sleep(100);
                    System.out.print(Thread.currentThread().getName() + " : " + i + "     ");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```
    控制台：
    t1 : 0     t1 : 1     t1 : 2     t1 : 3     t1 : 4     t1 : 5     t1 : 6     t1 : 7     t1 : 8     t1 : 9     t1 : 10     t2 : 0     t2 : 1     t2 : 2     t2 : 3     t2 : 4     t2 : 5     t2 : 6     t2 : 7     t2 : 8     t2 : 9     t2 : 10     


### demo2 多个线程访问不同对象的synchronized方法
```java
//此时<不会>出现同步锁现象
package cn.kkcoder.thread;
/**
 * Created by static-mkk on 21/3/2018.
 *
 *  当一个线程获取某对象的 synchronized 方法或代码块 ，其他线程再来访问是，其他线程会出现堵塞状态.
 *
 */
public class ThreadTest_one {

    public static void main(String[] args) {

        Thread t1 = new ThreadOne("t1");
        Thread t2 = new ThreadOne("t2");

        t1.start();
        t2.start();

    }

}

class  ThreadOne extends Thread{

    public ThreadOne(String name){
        super(name);
    }


    @Override
    public void run() {

        synchronized(this) {
            try {

                for (int i = 0; i <= 5; i++) {
                    Thread.sleep(100);
                    System.out.print(Thread.currentThread().getName() + " ： " + i + "   ");

                }
            }catch (InterruptedException e){

            }

        }
    }
}
```
    
    控制台：
    t2 ： 0   t1 ： 0   t1 ： 1   t2 ： 1   t2 ： 2   t1 ： 2   t2 ： 3   t1 ： 3   t1 ： 4   t2 ： 4   t2 ： 5   t1 ： 5   


## 规则二
> 当一个线程访问某synchronized方法或则语句块，其他线程可以访问该对象的非synchronized方法或代码块

### demo3
```java
package cn.kkcoder.thread;
/**
 * 
 * @author static-mkk
 *<br/>
 *  一个线程访问某对象的synchronized的方法或语句块时，其他线程可以访问该对象的非同步方法.
 *  
 */
public class ThreadTwo {

	public static void main(String[] args) {
		ThreadNoAndHaveSynchronized a = new ThreadNoAndHaveSynchronized();
		
		Thread t1 = new Thread(()->{a.haveSynchronized();});
		Thread t2 = new Thread(()-> {a.noSynchronized();});
		
		t1.start();
		t2.start();
	}

}

/**
 * 
 * @author static-mkk
 *<br/>
 * 测试类
 */
class ThreadNoAndHaveSynchronized {
	
/**
 *   同步的方法
 */
	public void haveSynchronized () {
		synchronized(this) {
			for(int i=0;i<=5;i++) {
				try {
					Thread.currentThread().sleep(100);
				} catch (InterruptedException e) {
					new RuntimeException("sleep 失败.");
				}
				System.out.println("我是 1 de : "+ " 的 i = " + i);
			}
		}
	}
	
	/**
	 *  非同步的方法
	 */
	public void noSynchronized() {
		for(int i=0;i<=5;i++) {
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				new RuntimeException("sleep 失败.");
			}
			System.out.println("我是 2 de : "+  " 的 i = " + i);
		}
	}
	
}
```
    控制台：
    我是 1 de :  的 i = 0
    我是 2 de :  的 i = 0
    我是 1 de :  的 i = 1
    我是 2 de :  的 i = 1
    我是 1 de :  的 i = 2
    我是 2 de :  的 i = 2
    我是 1 de :  的 i = 3
    我是 2 de :  的 i = 3
    我是 1 de :  的 i = 4
    我是 2 de :  的 i = 4
    我是 1 de :  的 i = 5
    我是 2 de :  的 i = 5

## 规则三

> 当一个线程已经访问某对象的 `synchronized方法或方法块`，其他线程则无法获取该对象的 其他`synchronized 方法或方法块`.

```java
package cn.kkcoder.thread;
/**
 * 
 * @author static-mkk
 * 
 * @time 2018-3-22
 * <br/>
 *  当一个线程已经获取某对象的同步锁，其他线程不能获取该对象的其他同步锁修饰的方法或者语句块.
 */
public class ThreadThree {

	public static void main(String[] args) {

		TwoSynchronizedMethod syn = new TwoSynchronizedMethod();
		
		Thread t1 = new Thread( ()-> {syn.synchronizedOne();} );
		Thread t2 = new Thread( ()-> {syn.synchronizedTwo();} );
		
		t1.start();
		t2.start();
		
	}

}

class TwoSynchronizedMethod{
	
	/**同步方法 一*/
	synchronized void synchronizedOne(){
		
		for(int i=0;i<=5;i++) {
			try {
				Thread.currentThread().sleep(100);
				System.out.println( "我是 one and i = " + i );
			} catch (Exception e) {
				new RuntimeException("one sleep fail");
			}
		}
	}
	/**同步方法 二*/
	synchronized void synchronizedTwo(){
		for(int i=0;i<=5;i++) {
			try {
				Thread.currentThread().sleep(100);
				System.out.println( "我是 two and i = " + i );
			} catch (Exception e) {
				new RuntimeException("two sleep fail");
			}
		}
	}
}
```
    控制台：
    我是 one and i = 0
    我是 one and i = 1
    我是 one and i = 2
    我是 one and i = 3
    我是 one and i = 4
    我是 one and i = 5
    我是 two and i = 0
    我是 two and i = 1
    我是 two and i = 2
    我是 two and i = 3
    我是 two and i = 4
    我是 two and i = 5
    
# 实例锁 与 全局锁

>  `实例锁`: 把锁加在实例对象上的锁. 就是在，类的非静态方法，非静态语句块 的其他 的普通方法 或 普通语句块 上加锁.

> `全局锁`： 针对类的安全锁. synchronized 关键字在 class或clossloder上，或者 静态方法  静态语句块.

## demos

### 同一个线程 访问 同一个对象的 不同同步锁. 
```java
package cn.kkcoder.thread;

public class AllWhitOme {

	public static void main(String[] args) {

		TestClass a = new TestClass();
		TestClass b = new TestClass();
		
		Thread t1 = new Thread(() -> {a.s1();} );
		Thread t2 = new Thread(() -> {a.s2();});
		t1.start();
		t2.start();
		
		
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
```
    
    控制台：
    我是 s1 i = 1
    我是 s1 i = 2
    我是 s2 i = 1
    我是 s2 i = 2

> 当同步锁释放后才能继续访问.

### 不同线程 访问 不同对象 的同一个 实例锁
```java
package cn.kkcoder.thread;

public class AllWhitOme {

	public static void main(String[] args) {

		TestClass a = new TestClass();
		TestClass b = new TestClass();
		
		Thread t3 = new Thread(() -> {a.s1();});
		Thread t4 = new Thread(() -> {b.s1();});
		t3.start();
		t4.start();
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
```
    
    控制台：
    我是 s1 i = 1
    我是 s1 i = 1
    我是 s1 i = 2
    我是 s1 i = 2
    
> 会 同时访问.

### 不同线程 访问不同对象 的 全局锁
```java
package cn.kkcoder.thread;

public class AllWhitOme {

	public static void main(String[] args) {

		TestClass a = new TestClass();
		TestClass b = new TestClass();
		Thread t5 = new Thread(() -> {a.s1();});
		Thread t6 = new Thread(() -> {b.s2();});
		t5.start();
		t6.start();
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
```
    控制台：
    我是 s3 i = 1
    我是 s3 i = 2
    我是 s4 i = 1
    我是 s4 i = 2

> 不能同时访问

### 不同线程 同时访问 不同对象的 实例锁 和 全局锁
```java
package cn.kkcoder.thread;

public class AllWhitOme {

	public static void main(String[] args) {

		TestClass a = new TestClass();
		TestClass b = new TestClass();
		Thread t7 = new Thread(() -> {a.s1();});
		Thread t8 = new Thread(() -> {b.s3();});
		t7.start();
		t8.start();
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
``` 
    控制台：
    我是 s1 i = 1
    我是 s3 i = 1
    我是 s1 i = 2
    我是 s3 i = 2

> 可以同时访问

### 不同线程 同时访问 不同对象的 全局锁
```java
package cn.kkcoder.thread;

public class AllWhitOme {

	public static void main(String[] args) {

		TestClass a = new TestClass();
		TestClass b = new TestClass();
		
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
```
    控制台：
    我是 s3 i = 1
    我是 s3 i = 2
    我是 s4 i = 1
    我是 s4 i = 2
    
> 不能同时访问.


  [1]: https://github.com/static-mkk/java8SourceLearn