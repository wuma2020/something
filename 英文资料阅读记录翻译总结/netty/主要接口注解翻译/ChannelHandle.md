# ChannelHandler

https://netty.io/4.1/api/io/netty/channel/ChannelHandler.html

-------------

```java

* 处理IO事件和拦截IO操作，并转发到另一个在 ChannelPipeline的handler.类似servlet的filter.

子类型：
    channelhandler本身并不提供很多方法，但是其子类实现了很多方法。
        ChannelInboundHandler：处理进来的IO事件
        ChannelOutboundHandler：处理出去的IO事件
    另外也提供了几个方便的适配器类：
        ChannelInboundHandlerAdapter:处理进来的IO事件
        ChannelOutboundHandlerAdapter:处理出去的IO事件
        ChannelDuplexHandler:处理进来，出去的io事件

Context 对象：
    一个ChannelHandler对象是由ChannelHandlerContext对象提供的。一个
    ChannelHandler对象通过这个context对象来和ChannelPipeline相关联.
    用这个context对象，ChannelHandler可以通过上游事件或下游事件来动态
    的修改channelPipeline或者存储一些信息在handler上.


状态管理：
    一个channelhandler经常需要一些状态信息，这些信息通过成员变量进行保
    存.因为一个连接进来的channel的每一个channelhandler都有一个状态，
    所以必须每次有新的连接进来的channel就会创建一个新的channelhandle。

    由于一些原因，你不想创建很多handler，于是就可以使用AttrbuteKeys处理，由该context对象提供.

```