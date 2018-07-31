# ReentrantReadWriteLock 


---

# ReentrantReadWriteLock 介绍
```
ReentrantReadWriteLock 继承自 ReadWriteLock 
ReadWriteLock，顾名思义，是读写锁。它维护了一对相关的锁 — — “读取锁”和“写入锁”，一个用于读取操作，另一个用于写入操作。
“读取锁”用于只读操作，它是“共享锁”，能同时被多个线程获取。
“写入锁”用于写入操作，它是“独占锁”，写入锁只能被一个线程锁获取。

注意：不能同时存在读取锁和写入锁！

ReadWriteLock是一个接口。ReentrantReadWriteLock是它的实现类，ReentrantReadWriteLock包括子类ReadLock和WriteLock。

我不太明白 为什么，读取操作是可以多个线程访问的，那为什么读取操作还需要加锁 。。。。。

```

# ReentrantReadWriteLock 示例
```java
package cn.kkcoder.juclock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock 使用 demo
 */
public class ReentrantReadWriteLockDemo {

    public static void main(String[] args) {

        MyCount myCount = new MyCount("MyCountID",10000);
        User user = new User("username" ,myCount);

        for (int i=0; i<3 ;i++)
        {
            user.getCount();
            user.setCouet( i * 10000);
        }
    }
}

class User{
    private  String name;
    private  MyCount myCount;
    private ReentrantReadWriteLock mylock;

    public User(String name, MyCount myCount) {
        this.name = name;
        this.myCount = myCount;
        mylock = new ReentrantReadWriteLock();
    }

    /**
     * 新建一个线程，用 ReentrantReadWriteLock 中的 WriteLock 来进行上锁，同时只能一个线程访问
     * @param count
     */
    public void setCouet(final int count){
        //创建一个操作set的线程
        new Thread(){
            @Override
            public void run() {
             //执行操作
                mylock.writeLock().lock();
                try {
                    System.out.println(Thread.currentThread().getName() +" setCount start");
                    myCount.setCount(count);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() +" setCount end");
                }finally {
                    mylock.writeLock().unlock();
                }

            }
        }.start();
    }

    /**
     * R用 ReentrantReadWriteLock的 readLock 上锁
     * @return
     */
    public void getCount(){
        new Thread(){
            @Override
            public void run() {
                mylock.readLock().lock();
                try {
                    System.out.println(Thread.currentThread().getName() +" getCount start");
                    myCount.getCount();
                    //这里等待10 ms 不然速度太快，看不出区别.
                    Thread.sleep(10);
                    System.out.println(Thread.currentThread().getName() +" getCount end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mylock.readLock().unlock();
                }
            }
        }.start();
    }
}

/**
 * 账户类
 */
class MyCount{
    private String id;
    private int count;

    public MyCount(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public int getCount (){
        System.out.println(Thread.currentThread().getName() + "getCount : " + count);
        return count;
    }
    public void setCount(int count){
        System.out.println(Thread.currentThread().getName() + "setCount : " + count);
        this.count = count;
    }

}

```
---
    控制台：
    Thread-0 getCount start
    Thread-0getCount : 10000
    Thread-0 getCount end
    Thread-1 setCount start
    Thread-1setCount : 0
    Thread-1 setCount end
    Thread-3 setCount start
    Thread-3setCount : 10000
    Thread-3 setCount end
    Thread-2 getCount start
    Thread-2getCount : 10000
    Thread-4 getCount start
    Thread-4getCount : 10000
    Thread-4 getCount end
    Thread-2 getCount end
    Thread-5 setCount start
    Thread-5setCount : 20000
    Thread-5 setCount end

---

    结果说明：(01) 观察Thread2和Thread-4的运行结果，我们发现，Thread-2启动并获取到“读取锁”，在它还没运行完毕的时候，Thread-3也启动了并且也成功获取到“读取锁”。
    因此，“读取锁”支持被多个线程同时获取。
    (02) 观察Thread-1,Thread-3,Thread-5这三个“写入锁”的线程。只要“写入锁”被某线程获取，则该线程运行完毕了，才释放该锁。
    因此，“写入锁”不支持被多个线程同时获取。


