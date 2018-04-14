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