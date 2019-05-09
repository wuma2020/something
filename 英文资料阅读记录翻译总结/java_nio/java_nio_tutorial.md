java nio 总览  from http://tutorials.jenkov.com/java-nio/index.html

java nio是 java 1.4版本以后，用来取代java io 和 java networking 相关api的包.

----

## Java NIO(Non-blocking IO)

```
    nio是一个非阻塞的io.
    
    比如，一个线程在要从一个channel中读取数据到buffer中，在数据从
    channel到buffer的过程中，该线程可以做其他的事情。一旦数据读取
    完成，这个线程可以接着来处理这个数据. 写同理.

```

## Channels and Buffers

```
    在标准的java io（bio）中，我们用字节流和字符流进行工作.但是在nio中，我们会用 channels 和 buffers 工作.
    数据总是从一个channel中读入到一个buffer中，也总是会从一个buffer中，写到一个channel中.
```

## Selectors 

```
    selector是nio中的一个概念。
    一个selector是一个java对象，他可以监视多个channel的事件，比如channel
    中打开连接，数据到达，发送完成等事件.
    所以一个单线程，可以监视多个channel中的data.

```
