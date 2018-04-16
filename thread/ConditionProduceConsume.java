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