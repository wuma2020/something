# synchronized关键字

标签： 多线程

---

# 目的
> 保证指定某类的的方法或方法块或者该类只能同时被一个类对象调用.

# synchronized的基本规则

## 规则一
> 当一个线程访问 `某对象` 的 synchronized 方法或者代码块时,其他线程对该 `该对象的` synchronize 方法或则语句块 的访问会被 `阻塞`. 

### demo1 多个线程访问同一对象的synchronized方法
```java
//此时出现同步锁现象.
package cn.kkcoder.thread;

/**
 * Created by static-mkk on 21/3/2018.
 *
 */
public class ThreadTest_one_runnable {

    public static void main(String[] args) {

        MyRunnable myRunnable = new MyRunnable();

        Thread t1 = new Thread(myRunnable,"t1");
        Thread t2 = new Thread(myRunnable,"t2");

        t1.start();
        t2.start();

    }
}

class MyRunnable implements Runnable{


    @Override
    public void run() {
        synchronized (this){
            try {
                for (int i=0;i<=10;i++){
                    Thread.currentThread().sleep(100);
                    System.out.print(Thread.currentThread().getName() + " : " + i + "     ");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```
    控制台：
    t1 : 0     t1 : 1     t1 : 2     t1 : 3     t1 : 4     t1 : 5     t1 : 6     t1 : 7     t1 : 8     t1 : 9     t1 : 10     t2 : 0     t2 : 1     t2 : 2     t2 : 3     t2 : 4     t2 : 5     t2 : 6     t2 : 7     t2 : 8     t2 : 9     t2 : 10     


### demo2 多个线程访问不同对象的synchronized方法
```java
//此时<不会>出现同步锁现象
package cn.kkcoder.thread;
/**
 * Created by static-mkk on 21/3/2018.
 *
 *  当一个线程获取某对象的 synchronized 方法或代码块 ，其他线程再来访问是，其他线程会出现堵塞状态.
 *
 */
public class ThreadTest_one {

    public static void main(String[] args) {

        Thread t1 = new ThreadOne("t1");
        Thread t2 = new ThreadOne("t2");

        t1.start();
        t2.start();

    }

}

class  ThreadOne extends Thread{

    public ThreadOne(String name){
        super(name);
    }


    @Override
    public void run() {

        synchronized(this) {
            try {

                for (int i = 0; i <= 5; i++) {
                    Thread.sleep(100);
                    System.out.print(Thread.currentThread().getName() + " ： " + i + "   ");

                }
            }catch (InterruptedException e){

            }

        }
    }
}
```
    
    控制台：
    t2 ： 0   t1 ： 0   t1 ： 1   t2 ： 1   t2 ： 2   t1 ： 2   t2 ： 3   t1 ： 3   t1 ： 4   t2 ： 4   t2 ： 5   t1 ： 5   


## 规则二
> 当一个线程访问某synchronized方法或则语句块，其他线程可以访问该对象的非synchronized方法或代码块

### demo3
```java

```
### demo4
```java

```
