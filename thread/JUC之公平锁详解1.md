# JUC之公平锁详解

标签（JUC）： JUC源码解读

---

#  1.首先是一些预备知识

## 1.AQS -- 指AbstractQueuedSynchronizer类

```
AQS是java中管理锁的抽象类，是独占锁和共享锁的公共父类.
    锁的许多公共方法都在这个类中实现
   //该类的声明 
    public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {}
```

## 2.AQS锁分为独占锁和共享锁

```
1. 独占锁： 在某一时刻只能被一个线程所占有的锁。
    该锁有ReentrantLock，ReentrantReadWriteLock.WriteLock 等等
    该锁同时也分为公平锁和非公平锁
    1.1 公平锁：通过CLH等待线程按照先来先得的规则，公平的获取锁.
    1.2 非公平锁：无视CLH队列，而直接获取锁.
2. 共享锁：能被多个线程同时拥有的锁.
    有：ReentrantReadWriteLock.ReadLock，CyclicBarrier，
    CountDownLatch和Semaphore 等
```

## 3.CLH队列 -- Craig, Landin, and Hagersten lock queue

```
CLH队列就是AQS中等待锁的线程的队列.
CLH队列实际上就是AQS(即AbstractQueuedSynchronizer类)的内部的一个
名为Node的静态内部类。是一个队列的结构，用于存放获取某个锁的被
阻塞的线程。

CLH是一个非阻塞的FIFO队列。也就是说往里面插入或移除一个节点的
时候，在并发条件下不会阻塞，而是通过自旋锁和CAS保证节点插入和
移除的原子性。
```

### CLH队列的数据结构
```
 static final class Node {
        static final Node SHARED = new Node();
        static final Node EXCLUSIVE = null;
        static final int CANCELLED =  1;
        static final int SIGNAL    = -1;
        static final int CONDITION = -2;
        static final int PROPAGATE = -3;
        volatile int waitStatus;
        volatile Node prev;
        volatile Node next;
        volatile Thread thread;
        Node nextWaiter;
        final boolean isShared() {
            return nextWaiter == SHARED;
        }
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {    // Used to establish initial head or SHARED marker
        }

        Node(Thread thread, Node mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }
```

## 4.CAS函数 -- Compare And Swap

```
CAS 函数，是比较并交换函数，它是原子操作函数；即，通过CAS操作的数据都是以原子方式进行的
```

# 2.ReentrantLock的数据结构


```
//该类的声明
public class ReentrantLock implements Lock, java.io.Serializable {}

1.有声明可知：该类实现了Lock接口，并实现了序列化.
2.该类内部有一个Sync类.该类为ASQ的子类，且有两个子类，分别为
FairSync和NonFairSync类，所以ReentrantLock是公平锁还是非公平锁
是通过内部的成员变量 Sync 来实现的.
            //成员变量
             private final Sync sync;
3.该ReentrantLock锁本身为独占锁.
4.FairSync和NonFairSync类为该类的静态final内部类

```


# 3.ReentrantLock中获取公平锁详解

## 1.lock()方法来获取锁
```
//公平锁获取方法
static final class FairSync extends Sync {
    final void lock() {
    /*
    因为这里是ReentrantLock的公平锁。是可重入的独占锁
    这里的1，值得是该锁被获取的次数.
    内部用成员变量status来维护
    */
        acquire(1);
    }
}
```

## 2.acquire() 方法

```
//通过下面if来实现公平锁的获取
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
    
主要分为4步来获取（详细内容下面会说）
    1.tryAcquire(arg):尝试获取锁，如果获取成功则返回。否则（其他
        线程已经获取锁，导致该线程无法获取，进行等待），进入
        CLH线程等待队列进行等待.
    2.addWaiter(Node.EXCLUSIVE):添加当前线程到等待队列的末尾.
    3.acquireQueued() : 获取锁，用公平的方式.
    4.selfInterrupt()：使"当前线程"会调用selfInterrupt()来自
        己给自己产生一个中断。目的是防止在第三步中导致当前线程被
        终断，并擦除了终断标记带来的不公平性.

```

### 1.tryAcquire(arg)
```
    //源码如下
protected final boolean tryAcquire(int acquires) {
    //获取当前线程
    final Thread current = Thread.currentThread();
    //获取当前状态  成员变量int state 进行存储
    int c = getState();
    //c==0 表明该锁没有被任何线程获取
    if (c == 0) {
        /*
        如果该锁未被任何线程获取，则判断
        该线程是否为阻塞队列的第一个线程，
        如果是，则，获取该锁，并设置锁相应状态.返回 true.
        
            下面的方法具体实现紧接着给出
        */
        if (!hasQueuedPredecessors() &&
            compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    /*
    如果，该独占锁当前的拥有者，就是该线程，则更新锁的状态.
    就是说，该线程连续两次获取该锁.
    */
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0)
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
    }
```

#### tryAcquire()中的方法详解

> 作用：就是让“当前线程”尝试获取锁。获取成功返回true，失败则返回false。

```
/*判断当前线程是否在队首*/
public final boolean hasQueuedPredecessors() {
        Node t = tail;
        Node h = head;
        Node s;
        return h != t &&
            ((s = h.next) == null || s.thread != Thread.currentThread());
    }

/* 
compareAndSwapInt()是sun.misc.Unsafe类中的一个本地方法。
对此，我们需要了解的是 compareAndSetState(expect, update)
是以原子的方式操作当前线程；若当前线程的状态为expect，则设置
它的状态为update。 
*/
protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
    
/*
setExclusiveOwnerThread()的作用就是，设置线程t为当前拥有“独
占锁”的线程。
*/
protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }    
    
setState() //设置Reentrantlock的状态。state == 0时，表示可获取锁对象。
            //由于锁是可重入的，所以 state 可以 > 0

```


### 2.addWaiter(Node.EXCLUSIVE)

> 作用：就是将当前线程添加到CLH队列中。这就意味着将当前线程添加到等待获取“锁”的等待线程队列中了。

```
/*
    创建当前线程对应的Node节点，并加入到队列的尾部
*/
private Node addWaiter(Node mode) {
    //新建当前线程对应的node节点
    Node node = new Node(Thread.currentThread(), mode);
    //未节点
    Node pred = tail;
    if (pred != null) {
        //如果未节点不为null,则把新节点的前一个节点设置成未节点
        node.prev = pred;
        if (compareAndSetTail(pred, node)) {
        /*
        compareAndSetTail CAS函数，也是通过“本地方法”实现的。
        compareAndSetTail(expect, update)
        会以原子的方式进行操作，它的作用是判断CLH队列的队尾是
        不是为expect，是的话，就将队尾设为update。
        */
            //把末节点的下一个节点设置成新节点
            pred.next = node;
            return node;
        }
    }
      // 若CLH队列为空，则调用enq()新建CLH队列，然后再将“当前线程”添加到CLH队列中。
    enq(node);
    return node;
}


------------------  enq(node);方法详解-----------------------

private Node enq(final Node node) {
    for (;;) {
        Node t = tail;
        if (t == null) { // Must initialize
            if (compareAndSetHead(new Node()))
                tail = head;//新建一个node节点，头结点 赋值 于 未节点 
        } else {//未节点不为null，把新节点放到末节点位置
            node.prev = t;
            if (compareAndSetTail(t, node)) {
                t.next = node;
                return t;
            }
        }
    }
}

```

### 3.acquireQueued()
> 执行CLH队列中的线程，如果当前线程获取到锁对象则返回，反之，当前线程进行休眠，等待唤醒后获取锁对象时返回。

```

//这部分是真正执行公平机制的地方，也很难理解.我就不太理解.
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
            /*
              final Node predecessor() throws NullPointerException {
                Node p = prev;
                if (p == null)
                    throw new NullPointerException();
                else
                    return p;
            }
        */
            //返回该节点的上一个节点
            final Node p = node.predecessor();
            if (p == head && tryAcquire(arg)) {//如果上一个节点就是头节点，并且获取到了锁，则把新节点设置成头节点
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return interrupted;
            }
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}

-------------------------------------------
shouldParkAfterFailedAcquire（） -> // 返回“当前线程是否应该阻塞”
parkAndCheckInterrupt() -> //判断“当前线程”是否需要被阻塞

```

### 4.selfInterrupt()

```
//当前线程设置阻塞
static void selfInterrupt() {
    Thread.currentThread().interrupt();
}

//请参看：interrupted（）， isInterrupted() 方法的区别
```

# 关于公平的体现
```

//在acquireQueued()中的
if (p == head && tryAcquire(arg)) {  ... }
这里的 p == head 是体现公平的严格保证，因为p == head 说明该节点
的上一个节点，也就是 p ，就是头节点，说明该节点为头节点的下一个节点，而根
据队列的先进先出原则，头节点对应的线程一旦释放锁，那么下一个
应该获取锁的线程就是第二个节点，即该节点。如果不是第二个节点
，即使被唤醒，也不应该直接获取同步锁，而是应该按照队列的顺序
获取。 所以，有了 p == head 条件，则可以解决掉那些
不是第二个节点而被唤醒 而要获取锁的 线程 造成的不公平现象.

```
# 总结

```
01) 先是通过tryAcquire()尝试获取锁。获取成功的话，直接返回；
    尝试失败的话，再通过acquireQueued()获取锁。
(02) 尝试失败的情况下，会先通过addWaiter()来将“当前线程”加入到"
    CLH队列"末尾；然后调用acquireQueued()，在CLH队列中排序等待获取
    锁，在此过程中，线程处于休眠状态。直到获取锁了才返回。
    如果在休眠等待过程中被中断过，则调用selfInterrupt()来自己产生一个中断。
```