
# EventLoop

https://netty.io/4.1/api/index.html

------

```java

EventLoop
* 处理所有已经注册的channel的所有IO事件.


java.lang.Object
    java.util.concurrent.AbstractExecutorService
        io.netty.util.concurrent.AbstractEventExecutor
            io.netty.util.concurrent.AbstractScheduledEventExecutor
                io.netty.util.concurrent.SingleThreadEventExecutor
                    io.netty.channel.SingleThreadEventLoop
                        io.netty.channel.nio.NioEventLoop

一个死循环的线程，一直把相应的channel注册到Selector中.


```