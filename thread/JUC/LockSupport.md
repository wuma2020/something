# LockSupport 


---

# LockSupport介绍

```
1.LockSupport是用来创建锁和其他同步类的基本线程阻塞原语。 
2.LockSupport中的park() 和 unpark() 的作用分别是阻塞线程和解除阻塞线程，
而且park()和unpark()不会遇到“Thread.suspend 和 Thread.resume所可能引发的
死锁”问题。
3.因为park() 和 unpark()有许可的存在；调用 park() 的线程和另一个试图将其 
unpark() 的线程之间的竞争将保持活性。
```

    这段话我自己也不太清楚，暂时就先记住第一二两点.
    日后在来深一点学习
 
---------

# LockSupport 示例

```java

package cn.kkcoder.juclock;

import java.util.concurrent.locks.LockSupport;

/**
 * juc 中 LockSupport 学习
 */
public class LockSupportTest {

    private  static Thread mainThread;
    public static void main(String[] args) {

        ThreadA ta = new ThreadA("ta");
        mainThread = Thread.currentThread();

        System.out.println(Thread.currentThread().getName() + "启动 线程 ta");
        ta.start();

        System.out.println(Thread.currentThread().getName() + "阻塞 mainThread 线程");
        LockSupport.park(mainThread);

        System.out.println(Thread.currentThread().getName() + "线程继续执行");
    }

   static class ThreadA extends Thread {
        public ThreadA(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "唤醒主线程");
            LockSupport.unpark(mainThread);
        }
    }

}
```

# 相同逻辑 Object 的 wait() 的实现 示例

```java
package cn.kkcoder.juclock;

/**
 * 和使用LockSupport 的方法 作比较
 */
public class LockSupportTestComp {

    public static void main(String[] args) throws InterruptedException {

        ThreadA1 ta1 = new ThreadA1("ta");

        synchronized (ta1){//主线程获取 ta1 线程的锁
        System.out.println(Thread.currentThread().getName() + "启动了ta1 线程");
        ta1.start();

        System.out.println("mainThread 进入等待");
        ta1.wait();

        System.out.println("mainThread 继续执行" );
        }
    }

    static class ThreadA1 extends  Thread {
        public ThreadA1(String name) {
            super(name);
        }

        @Override
        public void run() {
            synchronized (this){//获取当前对象的同步锁 就是ta1
            System.out.println("ta 先等待一会再执行");
            System.out.println(Thread.currentThread().getName() + "唤醒该线程对象锁的线程");
            this.notify();
            }
        }
    }
}

```

# Tips
    park和wait的区别。wait让线程阻塞前，必须通过synchronized获取同步锁。



