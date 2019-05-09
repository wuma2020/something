java nio 有三个核心组件：Channels 、 Buffers 、 Selectors.

## Channels and Buffers

```
    通常nio都是以channel作为开始的.
    一个channel就像是一个流一样，数据从channel中读
出来到buffer中，或者把buffer中的数据写到channel中.

```

### 下面是一些主要的channel的类型：

    这些channel覆盖了 UDP + TCP 的网络流和文件流.

    *FileChannel
    *DatagramChannel
    *SocketChannel
    *ServerSocketChannel
    

### 下面是一些主要的Buffer类型：

    ByteBuffer
    CharBuffer
    DoubleBuffer
    FloatBuffer
    IntBuffer
    LongBuffer
    ShortBuffer

## Selector

```
    一个selector可以监听多个channel.
    使用场景是：需要有许多连接的应用，但是传输的数据
的大小比较小的情况下。比如 文字通讯系统等。
``` 

![一个线程用一个selector处理三个channel](http://tutorials.jenkov.com/images/java-nio/overview-selectors.png)

一个线程用一个selector处理三个channel



```
重点：
一个线程使用一个selector，需要注册channel.然后调用
selector的select（）方法，然后该方法会阻塞在这里，
一直到有满足channel关注的事件发生时，然后该线程就会
处理这些事件.
``` 
