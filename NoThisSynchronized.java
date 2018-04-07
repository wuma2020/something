package cn.kkcoder.thread;
/**
 * Created by static-mkk on 7/4/2018.
 *
 *  非this同步锁好处：
 *      如果在一个类中有很多个synchronized方法，这是虽然可以实现同步，但是会受到阻塞。影响效率。
 *      但是如果使用了同步代码块的非this锁，则该synchronized（非this）代码块和程序中的同步方法是异步的。
 *      不和其他线程争取this锁，可以提高运行效率。
 *
 */
public class NoThisSynchronized {
	public static void main(String[] args) {
		Service service = new Service();
		ThreadA a = new ThreadA(service);
		a.start();
		ThreadB b = new ThreadB(service);
		b.start();
	}
}


class Service{

	private String name;
	private String password;

	private String anyString = new String();

	public void setNameAndPassword(String name,String password){
		try {
			synchronized (anyString){
				System.out.println("线程名为： " + Thread.currentThread().getName()+ "进入了同步块" );
				this.name = name;
				Thread.sleep(3000);
				this.password = password;
				System.out.println("线程名为： " + Thread.currentThread().getName()+ "离开了同步块" );
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public synchronized void getName(){
		try {
			synchronized (this){
				System.out.println("进入getNmae的this同步块");
				System.out.println("当前线程是： "+  Thread.currentThread().getName() + "获取name" +this.name);
			}

		}catch (Exception e){
		}
	}

	public synchronized void getPassword(){
		try {
			synchronized (this){
				System.out.println("进入getPassword的this同步块");
				System.out.println("当前线程是： "+  Thread.currentThread().getName() + "获取password" +this.password);
			}
		}catch (Exception e){}
	}

}


/**
 *		两个线程类来设置数据
 */

class ThreadA extends  Thread{
	private Service service;
	public ThreadA(Service service){
		this.service = service;
	}

	@Override
	public void run() {
		super.run();
		service.setNameAndPassword("ThreadA" ,"AA");
		service.getName();
		service.getPassword();
	}
}


class ThreadB extends  Thread{
	private Service service;
	public ThreadB(Service service){
		this.service = service;
	}

	@Override
	public void run() {
		super.run();
		service.setNameAndPassword("ThreadB" ,"BB");
		service.getName();
		service.getPassword();
	}
}
