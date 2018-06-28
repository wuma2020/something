# 获取非公平锁

标签： JUC

---

# 和获取公平锁的区别
```
非公平锁和公平锁在获取锁的方法上，流程是一样的；它们的区别主要
表现在“尝试获取锁的机制不同”。简单点说，“公平锁”在每次尝试获取
锁时，都是采用公平策略(根据等待队列依次排序等待)；而“非公平锁”
在每次尝试获取锁时，都是采用的非公平策略(无视等待队列，直接尝试
获取锁，如果锁是空闲的，即可获取状态，则获取锁)。

两者只是尝试获取锁的代码不同
```

## 有区别的代码
```
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {//索命锁可以被获取
        if (compareAndSetState(0, acquires)) {
            //把当前线程设置成 当前锁的拥有线程，并返回 true
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        //如果当前线程就是 之前锁的 拥有线程，就更新锁的状态，返回true
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}

--------------------区别------------------------

公平锁和非公平锁，它们尝试获取锁的方式不同。公平锁在尝试获取锁
时，即使“锁”没有被任何线程锁持有，它也会判断自己是不是CLH等待
队列的表头；是的话，才获取锁。而非公平锁在尝试获取锁时，如果
“锁”没有被任何线程持有，则不管它在CLH队列的何处，它都直接获取
锁。
```

# 总结
```
公平锁和非公平锁的区别，是在获取锁的机制上的区别。表现在，在尝
试获取锁时 —— 公平锁，只有在当前线程是CLH等待队列的表头时，才
获取锁；而非公平锁，只要当前锁处于空闲状态，则直接获取锁，而不
管CLH等待队列中的顺序。
只有当非公平锁尝试获取锁失败的时候，它才会像公平锁一样，进入
CLH等待队列排序等待。
```

# 公平锁和非公平锁在释放锁上方法是一样的.
