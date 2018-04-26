# condition实现线程顺序

标签： 多线程

---

# 思路

> 利用不同的条件，把同一类的线程阻塞到同一个condition对象上，然后根据条件（代码的逻辑），按照一定的顺序执行不同的线程。

---

# 代码部分

```java
package cn.kkcoder.thread;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  使用 Condition 实现 线程顺序执行
 * Created by static-mkk on 26/4/2018.
 */
public class ConditionOrderRun {
	private  static  int nextNumber = 1;
	private static Lock lock = new ReentrantLock();
	private static Condition con1 = lock.newCondition();
	private static Condition con2 = lock.newCondition();
	private static Condition con3 = lock.newCondition();

	public static void main(String[] args) {

		Thread threadA = new Thread(()->{
			try {
				lock.lock();
				while(nextNumber != 1){
					con1.await();
				}

				System.out.println("AAA 名称： " + Thread.currentThread().getName());

				nextNumber = 2;

				con2.signalAll();//唤醒阻塞到condition2上的所有线程
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		});

		Thread threadB = new Thread(()->{
			try {
				lock.lock();
				while(nextNumber != 2){
					con2.await();
				}
				System.out.println("BBBB + 名称: " + Thread.currentThread().getName());
				nextNumber = 3;
				con3.signalAll();

			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		});

		Thread threadC = new Thread(()->{
			try {
				lock.lock();
				while(nextNumber != 3){
					con3.await();
				}
				System.out.println("CCCC + 名称: " + Thread.currentThread().getName());
				nextNumber = 1;
				con1.signalAll();

			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		});

		Thread[] arrayTA= new Thread[5];
		Thread[] arrayTB = new Thread[5];
		Thread[] arrayTC = new Thread[5];

		for(int i=0;i<=4;i++){
			arrayTA[i] = new Thread(threadA);
			arrayTA[i].start();
			arrayTB[i] = new Thread(threadB);
			arrayTB[i].start();
			arrayTC[i] = new Thread(threadC);
			arrayTC[i].start();
		}
	}
}

```

    控制台：
    AAA 名称： Thread-3
    BBBB + 名称: Thread-7
    CCCC + 名称: Thread-5
    AAA 名称： Thread-6
    BBBB + 名称: Thread-10
    CCCC + 名称: Thread-8
    AAA 名称： Thread-9
    BBBB + 名称: Thread-4
    CCCC + 名称: Thread-11
    AAA 名称： Thread-15
    BBBB + 名称: Thread-16
    CCCC + 名称: Thread-14
    AAA 名称： Thread-12
    BBBB + 名称: Thread-13
    CCCC + 名称: Thread-17

-----

# 代码逻辑

    首先是 static int 变量 nextNumber ，通过对这个数值的控制，来实现依次启用不同类型的线程。
    main方法中利用for循环启动了15个线程，分为3类。分别为ThreadA，ThreadB，ThreadC，三类线程均对nextNumber的值进行判断，然后进行相应的代码处理。
    
    nextNumber初始值为1,。
    假设B类线程先执行，因为nextNumber！=2成立，所以，B类线程都会阻塞在con2上，同理，B类线程会阻塞在con3上。只有A类线程执行，才会打印语句，并设置nextNumber=2，唤醒所有con2上的线程。而此时，假设A类的其中一个线程执行，会因为nextNumber！=1.这个条件而被阻塞在con1上，同理如果此时C类线程执行，会因为nextNumber！=3成立而阻塞在con3上。因此，之后B类线程能够执行。
    然后以此类推。
    最后实现了顺序执行一类线程。但是，一类线程中的某个线程获取执行是随机的。

---



