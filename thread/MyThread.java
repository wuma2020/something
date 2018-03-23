package cn.kkcoder.thread;

public class MyThread extends Thread{

	@Override
	public void run() {
		for(int i=0;i <=5;i++){
			System.out.println("我是"+this.getName()+"的 i = " + i);
			}
		}
	
	public static void main(String[] args) {

		MyThread m1 = new MyThread();
		MyThread m2 = new MyThread();
		
		m1.start();
		m2.start();
		
	}

}
