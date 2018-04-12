# join介绍

标签： 多线程

---

# join的作用

> 等待线程销毁再执行当前线程join()方法后面的代码.

# join(long)与sleep(long)的区别

> jion(long)方法，会释放该当前线程的锁.其内部是使用wait(long)来实现的.而sleep(long)并不会释放当前线程的锁.

## join(long)源码
```java
 public final synchronized void join(long millis)
    throws InterruptedException {
        long base = System.currentTimeMillis();
        long now = 0;

        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (millis == 0) {
            while (isAlive()) {
                wait(0);
            }
        } else {
            while (isAlive()) {
                long delay = millis - now;
                if (delay <= 0) {
                    break;
                }
                wait(delay);//等待delay（这里也就是millis）时间。释放当前线程的锁.
                now = System.currentTimeMillis() - base;
            }
        }
    }
```

# join的使用demo
```java
/**
 * Created by static-mkk on 12/4/2018.
 */
public class JoinDemo {

	public static void main(String[] args) {
		try {

			JoinSerice joinSerice = new JoinSerice();
			joinSerice.start();
			/*
			System.out.println("未使用join获取number的值为： " + joinSerice.getNumber());
			控制台：未使用join获取number的值为： 3067
			*/
			
		
			//main线程等待joinService线程对象结束后，才会继续执行.因为join()方法内部依靠wait()实现。此时，拥有joinService线程对象的为主线程，因此，主线程将进入等待状态，并释放锁，知道线程joinService销毁.
			
			joinSerice.join();
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
```

# join会抛出InterruptException异常
> try catch 就好

# join(long)会出现一些解释意外

> 不深入了解