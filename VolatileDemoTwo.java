package cn.kkcoder.thread;

/**
 *  这个主要证明 volatile 不具有原子性.
 *      即： 会出现多线程 异步操作，出现脏读.
 *
 *          在方法中，原子类方法执行顺序随机.
 *
 *      控制台输出顺序乱序，即异步操作.
 */

/**
 * Created by static-mkk on 10/4/2018.
 */
public class VolatileDemoTwo {

	public static void main(String[] args) {
		VolatileService volatileService = new VolatileService();

		VolatileDemoTwoThreadOne t1 = new VolatileDemoTwoThreadOne();
		t1.setVolatileService(volatileService);

		VolatileDemoTwoThreadTwo t2 = new VolatileDemoTwoThreadTwo();
		t2.setVolatileService(volatileService);

		t1.start();
		t2.start();
	}
}

class VolatileService{
	volatile public int number;
	public void sayNumber(){
		while(number < 1000){

			number++;
			if(number%50==0){
				System.out.println(number);
			}
		}
	}
}

class VolatileDemoTwoThreadOne extends Thread{

	VolatileService volatileService;

	public void setVolatileService(VolatileService volatileService) {
		this.volatileService = volatileService;
	}

	@Override
	public void run() {
		super.run();
		volatileService.sayNumber();
	}
}

class VolatileDemoTwoThreadTwo extends Thread{

	VolatileService volatileService;

	public void setVolatileService(VolatileService volatileService) {
		this.volatileService = volatileService;
	}

	@Override
	public void run() {
		super.run();
		volatileService.sayNumber();
	}
}