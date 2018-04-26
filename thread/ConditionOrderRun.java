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
