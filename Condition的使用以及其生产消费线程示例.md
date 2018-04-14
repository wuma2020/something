# Condition的使用以及其生产消费线程示例

标签： 多线程

---

# Condition 介绍
>    Condition : 条件类. 可以通过建立多个Condition对象，来实现不同类线程的精准的调用.从而达到更高效的效果.

---

# Condition Tips
>    如何确定哪些线程在condition上，取决于哪些线程阻塞在对应的condition上

---

# Condition的唤醒部分线程 demo
```java
package cn.kkcoder.thread;

/**
 *  ReentrantLock : 重入锁.
 *  Condition : 条件类. 可以通过建立多个Condition对象，来实现不同类线程的精准的调用.
 *              从而达到更高效的效果.
 *
 *            Tips: 如何确定哪些线程在condition上，取决于哪些线程阻塞在对应的condition上
 *
 */


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by static-mkk on 14/4/2018.
 */
public class ConditionAndReentrantLockDemoOne {

	public static void main(String[] args) {
		ConditionServerDemoOne one = new ConditionServerDemoOne();
		ConditionThreadA conditionThreadA = new ConditionThreadA(one);
		conditionThreadA.start();
		ConditionThreadB conditionThreadB = new ConditionThreadB(one);
		conditionThreadB.start();
		ConditionThreadB conditionThreadc = new ConditionThreadB(one);
		conditionThreadc.start();
		ConditionThreadB conditionThreadd = new ConditionThreadB(one);
		conditionThreadd.start();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		one.signalB_ALL();
	}
}


/**
 * service类
 */
class ConditionServerDemoOne{

	private Lock lock = new ReentrantLock();
	Condition condition1 = lock.newCondition();
	Condition condition2 = lock.newCondition();

	/**
	 * condition1的await方法
	 */
	public void awaitA(){
		try {
			lock.lock();
			System.out.println("A的awaitA的前"+" : " + Thread.currentThread().getName());
			condition1.await();
			System.out.println("A的await后"+" : " + Thread.currentThread().getName());
		}catch (InterruptedException e){
		}
		finally {
		lock.unlock();
		}
	}

	/**
	 * 唤醒所有condition1上的线程
	 */
	public void singalA_All(){
		try {
			lock.lock();
			System.out.println("A的singall前"+" : " + Thread.currentThread().getName());
			condition1.signalAll();//唤醒condition1上的所有线程
			System.out.println("A的signall后"+" : " + Thread.currentThread().getName());
		}catch (Exception e){
		}finally {
			lock.unlock();
		}
	}

	/**
	 * condition2的await方法
	 */
	public void awaitB(){
		try {
			lock.lock();
			System.out.println("B的awaitB的前"+" : " + Thread.currentThread().getName());
			condition2.await();
			System.out.println("B的awaitB之后"+" : " + Thread.currentThread().getName());
		}catch (InterruptedException e){
		}finally {
			lock.unlock();
		}
	}

	/**
	 * 唤醒所有的condition2上的线程
	 */
	public void signalB_ALL(){
		try {
			lock.lock();
			System.out.println("B的唤醒之前 " + Thread.currentThread().getName());
			condition2.signalAll();//唤醒所有condition2上的线程
			System.out.println("B的唤醒之后 ： "+ Thread.currentThread().getName());
		} finally {
			lock.unlock();
		}
	}

}

class ConditionThreadA extends Thread{
	ConditionServerDemoOne one ;

	public ConditionThreadA(ConditionServerDemoOne conditionServerDemoOne) {
		this.one = conditionServerDemoOne;
	}

	@Override
	public void run() {
		super.run();
		one.awaitA();
	}
}

class ConditionThreadB extends Thread{
	ConditionServerDemoOne one ;

	public ConditionThreadB(ConditionServerDemoOne conditionServerDemoOne) {
		this.one = conditionServerDemoOne;
	}

	@Override
	public void run() {
		super.run();
		one.awaitB();
	}
}
```
    
    控制台：  （提示，别忘了关闭程序，因为有个A线程一直存货存活）
    A的awaitA的前 : Thread-0
    B的awaitB的前 : Thread-1
    B的awaitB的前 : Thread-3
    B的awaitB的前 : Thread-2
    B的唤醒之前 main
    B的唤醒之后 ： main
    B的awaitB之后 : Thread-1
    B的awaitB之后 : Thread-3
    B的awaitB之后 : Thread-2


---

# Condition的消费生产模式demo
```java
package cn.kkcoder.thread;


/**
 *  用condition来实现生产/消费线程 demo
 *
 *      主要目的：了解condition的自己的阻塞队列。
 *              即，某个线程在某个condition 阻塞时，该线程就会被添加到该condition的阻塞队列中。
 *              这也就解释了，condition 的精准的唤醒某一类线程的功能.
 *
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by static-mkk on 14/4/2018.
 */
public class ConditionProduceConsume {

	public static void main(String[] args) {
		ConditionDepot conditionDepot = new ConditionDepot();

		for (int i=0;i<6;i++){

			new ProduceThread(""+i , conditionDepot , i).start();
			new ConsumeThread(""+i,conditionDepot).start();
		}

	}
}

class ConditionDepot{
	Lock lock = new ReentrantLock();

	Condition produce = lock.newCondition();//生产条件队列
	Condition consume = lock.newCondition();//消费条件队列

	Object[] depot = new Object[5];
	int count=0;//实际仓库中的产品数量
	int pos =0;//实时仓库索引
	/**
	 * 生产
	 */
	public void produceM(Object obj){
		try {
			lock.lock();
			while(count>5){
				//不生产
				produce.await();
			}
			depot[pos] = obj;
			pos++;
			count++;
			System.out.println("线程：" + Thread.currentThread().getName() + "生产,此时有： " +count );
			consume.signalAll();//唤醒消费线程（如果count<0 则不消耗）

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 消费
	 */
	public void consume(){
		try {
			lock.lock();
			while(count<1){
				//不消费
				consume.await();
			}
			depot[--pos] = null;
			count--;
			System.out.println("线程：" + Thread.currentThread().getName() + "消耗,此时有： " +count );
			produce.signalAll();//唤醒生产线程（如果count>5 则不生产）

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

}

/**
 * 生产线程
 */
class ProduceThread extends Thread{
	ConditionDepot conditionDepot;
	Object ojb;
	public ProduceThread(String name, ConditionDepot conditionDepot, Object ojb) {
		super(name);
		this.conditionDepot = conditionDepot;
		this.ojb = ojb;
	}

	@Override
	public void run() {
		super.run();
		conditionDepot.produceM(ojb);
	}
}

/**
 * 消费线程
 */
class ConsumeThread extends Thread{
	ConditionDepot conditionDepot;

	public ConsumeThread(String name, ConditionDepot conditionDepot) {
		super(name);
		this.conditionDepot = conditionDepot;
	}

	@Override
	public void run() {
		super.run();
		conditionDepot.consume();
	}
}
```
    
    控制台：
    线程：0生产,此时有： 1
    线程：0消耗,此时有： 0
    线程：1生产,此时有： 1
    线程：1消耗,此时有： 0
    线程：5生产,此时有： 1
    线程：2消耗,此时有： 0
    线程：2生产,此时有： 1
    线程：4生产,此时有： 2
    线程：3生产,此时有： 3
    线程：3消耗,此时有： 2
    线程：4消耗,此时有： 1
    线程：5消耗,此时有： 0
