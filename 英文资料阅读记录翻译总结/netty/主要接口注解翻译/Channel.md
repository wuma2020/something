
# channel

https://netty.io/4.1/api/io/netty/channel/Channel.html

---------

```java
public interface Channel
extends AttributeMap, ChannelOutboundInvoker, java.lang.Comparable<Channel>

* 连接 网络嵌套字或者一个能处理读，写，连接，绑定 I/O操作的组件。

它提供给用户以下几点：
    1.当前channel的连接状态（比如是否连接，是否打开 is it Open()?）;
    2.channel的配置参数（如接收到的buffer的大小）;
    3.该channel支持的I/O操作;
    4.channelPipeline：处理channel中所有I/O 操作以及处理相关请求。 

 
* 所有的I/O操作都是异步的
    所有的I/O操作都是异步的，所以所有的i/o方法调用后都会立即返回，而不
    会管被请求的i/o操作是否完成。然后你会得到一个ChannelFuture实例，用
    来通知你，这个I/O操作是成功，失败，还是取消了。。。


* channels 是分级的
    一个channel会有一个父亲，具体的取决于它怎样创建的。比如，
    SocketChannel是ServerSocketChannel的accept()方法创建的，所以其
    parent()方法将返回ServerSocketChannel。

    具体的分级结构，取决于具体的channel所属的传输实现.For example, 
    you could write a new Channel implementation that creates the 
    sub-channels that share one socket connection, as BEEP and SSH do.

* Downcast to access transport-specific operations
    Some transports exposes additional operations that is 
    specific to the transport. Down-cast the Channel to sub-type 
    to invoke such operations. For example, with the old I/O 
    datagram transport, multicast join / leave operations are 
    provided by DatagramChannel.

* 释放资源
    当你对channel操作完成之后，调用ChannelOutboundInvoker.close() 
    or ChannelOutboundInvoker.close(ChannelPromise)方法去正确的释放
    资源。这很重要。
```