# Interrupt 终止线程介绍

标签： 多线程

---

# interrupted() 方法
```java
package cn.kkcoder.thread;

public class InterruptDemoOne {

	public static void main(String[] args) {
		BlockedThread t = new BlockedThread("t1");
		//开启线程t1
		t.start();
		System.out.println(t.getName() + "线程开启");
		try {
		//主程序sleep300 ms
			Thread.sleep(300);
		//向线程t发出终断的命令.
			t.interrupt();
			System.out.println("t在interrupt之后的状态 ： " + t.getState() );
		//t1线程被终止
			Thread.sleep(300);
			System.out.println("t的状态 ： " + t.getState() );
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class BlockedThread extends Thread{
	public BlockedThread(String name) {
		super(name);
	}
	
	@Override
	public void run() {
		try {
			int i=0;
			while(!isInterrupted()) {//当isInterrupted 的结果为false 时，会抛出异常且该线程终结.会在while循环之外抛出异常.
				Thread.sleep(100);
				System.out.println(Thread.currentThread().getName() + " : " + i );
				i++;
			}
		} catch (Exception e) {
		//抛出异常时，打印下面语句并终止改线程t1
			System.out.println(Thread.currentThread().getName() + " 退出线程 ");
		}
	}
}
```
    控制台：
    t1线程开启
    t1 : 0
    t1 : 1
    t1 退出线程 
    t在interrupt之后的状态 ： TIMED_WAITING
    t的状态 ： TERMINATED

---

> 注意：把while循环放在try语句块内部,当while的isInterrupted为false时，其会抛出一个异常，在while循环外部。所以如果while在try外部，无法捕获异常，从而退出while循环，造成死循环.

---

# 自己设置标记来终止线程
```java
package cn.kkcoder.thread;
/**
 * 
 * @author static-mkk
 * @time   27 Mar 2018
 *		Flag线程类
 */
class FlagThread extends Thread{
	
	boolean flag = true;
	
	public void setFlagFalse() {
		this.flag = false;
	}
	
	@Override
	public void run() {
		try {
			int i=0;
			while(flag) {
				Thread.sleep(100);
				System.out.println("i = " + i);
				i++;
			}
		} catch (Exception e) {
		}
	}
}

/**
 * 
 * @author static-mkk
 * @time   27 Mar 2018<br/>
 *	
 *	自己设置标记来终止线程 demo.
 */
public class FlagInterruptThread {

	public static void main(String[] args) {
		try {
		FlagThread t = new FlagThread();
		//启动线程
		t.start();
		System.out.println("t线程 已经启动");
		
		//主线程睡眠 200 ms之后，设置Flag 为 false。为了让线程t的运行一段时间，以表明t在运行
		Thread.sleep(200);
		t.setFlagFalse();
		//打印语句和t.setFlagFalse是两个同时进行的线程，所以此时的线程t也许并没有终止
		System.out.println( "t 的falg设置为false 的 状态时， ：  " + t.getState());
		
		//这个主线程sleep 200 ms ，是为了有足够的时间让线程t终止.
		Thread.sleep(200);
		System.out.println("t最后的状态： " + t.getState());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
```

    控制台：
    t线程 已经启动
    i = 0
    i = 1
    t 的falg设置为false 的 状态时， ：  RUNNABLE
    t最后的状态： TERMINATED

---

#  interrupted() 和 isInterrupted()的区别
> interrupted :谷歌翻译.
除非当前线程正在中断自己，这总是允许的，否则将调用此线程的checkAccess方法，这可能会导致抛出SecurityException。
如果此线程在调用Object类的wait（），wait（long）或wait（long，int）方法或join（），join（long），join（long，int） ，sleep（long）或sleep（long，int）这个类的方法，那么它的中断状态将被清除，并且会收到一个InterruptedException异常。
如果此线程在InterruptibleChannel的I / O操作中被阻塞，那么通道将被关闭，线程的中断状态将被设置，并且该线程将收到java.nio.channels.ClosedByInterruptException。
如果此线程在java.nio.channels.Selector中被阻塞，那么线程的中断状态将被设置，并且它将立即从选择操作中返回，可能具有非零值，就像调用了选择器的唤醒方法一样。
如果以前的条件都不成立，那么该线程的中断状态将被设置。
中断不活动的线程不需要任何影响。

---

> isInterrupted:
测试该线程是否被中断。此线程的中断状态不受此方法的影响。
线程中断被忽略，因为线程在中断时未处于活动状态，将通过返回false的此方法反映出来。
返回：如果此线程已被中断，则返回true;否则为假。

---

    综上：isInterrupted只返回线程是否被终断的标记（true：线程已被终断）.interrupted 会终断线程，抛出异常使线程结束，并把线程的标记改为false.