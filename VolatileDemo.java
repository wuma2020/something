package cn.kkcoder.thread;

/**
 * 首先说说Volatile的作用：
 *
 *      1.使变量在多个线程中可见.即,使多个线程从公共堆栈中获得变量值。
 *      2.volatile只用于修饰变量.synchronized可以修饰方法，变量和类.
 *      3.volatile不能保证原子性. 它唯一的作用就是 使 私有线程 从 公共堆栈中获取数据。仅仅保证读取的数据最新.
 *      4.多线程访问volatile不会阻塞，而synchronized会导致阻塞.
 *      5.synchronized 具有 互斥性 和 可见性。 即，使用synchronized有volatile的可见性的效果.
 *
 *
 */

/**
 * Created by static-mkk on 10/4/2018.
 *
 *  这个主要说明 volatile 的 可见性.
 *
 */
public class VolatileDemo {

	public static void main(String[] args) {
		ServicePart servicePart = new ServicePart();

		VolatileThreadOne one = new VolatileThreadOne();
		VolatileThreadTwo two = new VolatileThreadTwo();
		one.setService(servicePart);
		two.setServicePart(servicePart);

		Thread t1 = new Thread(one);
		Thread t2 = new Thread(two);
		t1.start();
		t2.start();

	}

}


class ServicePart {
	//加入volatile修饰之后，不同线程获取flag值时，会从公共堆栈中获取，即，最新的值.
	//即，当t2线程设置的flag的值为false，t1线程会马上知道flag的值为flase.
	//  否则，可能会导致，t1线程一直在其私有数据栈中 获取flag的值，即，一直为true.
	volatile private boolean flag = true;


	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void doHomework() {
		while (flag) {
			System.out.println("我一直在，根本停不下来");
		}
		System.out.println("停停停");
	}

}

class VolatileThreadOne implements Runnable{
	ServicePart ServicePart;

	public void setService(ServicePart ServicePart) {
		this.ServicePart = ServicePart;
	}

	@Override
	public void run() {
		this.ServicePart.doHomework();
	}
}

class VolatileThreadTwo implements Runnable{
	ServicePart servicePart;

	public void setServicePart(ServicePart servicePart) {
		this.servicePart = servicePart;
	}

	@Override
	public void run() {
		this.servicePart.setFlag(false);
	}
}