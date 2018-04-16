package cn.kkcoder.thread;

/**
 * Created by static-mkk on 8/4/2018.
 *
 *      在jdk的bin目录下使用jps命令。可以检测到是否出现死锁的情况.
 *      执行 jstack -l 进程ID  可以看到详细的信息
 *
 */
public class DeadLock {

	public static void main(String[] args) {
		try {

			DeadThread d = new DeadThread();
			d.setUserame("a");
			Thread t1 = new Thread(d);
			t1.start();
			//小于run方法中的睡眠时间。达到在t1线程未需要lock2对象锁时，t2线程已经获取lock2对象锁
			//这就造成了 死锁 。
			Thread.sleep(100);

			d.setUserame("b");
			Thread t2 = new Thread(d);
			t2.start();

		}catch (Exception e){}
	}

}


/**
 * 造成多线程死锁的线程类
 */
class DeadThread extends Thread{

	public String username;
	public Object lock1 = new Object();
	public Object lock2 = new Object();

	public void setUserame(String name){
		this.username = name;
	}

	/**
	 * 造成死锁的写法
	 */
	@Override
	public void run() {
		if(username.equals("a")){
			synchronized (lock1){
				try {
					System.out.println("username: "+ username);
					Thread.sleep(2000);
				}catch (InterruptedException e){
				}
				synchronized (lock2){
					System.out.println("执行顺序： lock1 -> lock2");
				}
			}
		}


		if(username.equals("b")){
			synchronized(lock2){
				try {
					System.out.println("usename ： "+ username);
					Thread.sleep(2000);
				}catch (InterruptedException e){

				}

				synchronized (lock1){
					System.out.println("执行的顺序为： lock2 -> lock1");
				}
			}
		}

	}
}