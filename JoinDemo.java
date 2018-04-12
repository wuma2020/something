package cn.kkcoder.thread;
/**
 *  join的作用 ：等待线程对象销毁.
 *
 *      应用场景：当某个线程需要 某一之前启动的线程对象处理过的数据 时，使用.
 *
 */

/**
 * Created by static-mkk on 12/4/2018.
 */
public class JoinDemo {

	public static void main(String[] args) {
		try {

			JoinSerice joinSerice = new JoinSerice();
			joinSerice.start();
			/*System.out.println("未使用join获取number的值为： " + joinSerice.getNumber());
			控制台：未使用join获取number的值为： 3067*/
			joinSerice.join();//main线程等待joinService线程对象结束后，才会继续执行.
			System.out.println("使用join获取number的值为： " + joinSerice.getNumber());
			/*控制台：使用join获取number的值为： 100000*/
		}catch (InterruptedException e){

		}
	}
}


class JoinSerice extends Thread{
	volatile int number;

	public int getNumber() {
		return number;
	}

	@Override
	public void run() {
		super.run();
		for(int i=0;i<100000;i++){
			number++;
		}
	}

}