1.为什么要使用netty？
i.nio和aio的api使用很复杂，而且nio还有一些问题。
ii.性能优化难度高，需要熟练的掌握多线程编程和网络编程。
iii.需要造很多轮子
ix.nio 的空轮训bug
2.netty的基本用法
public static void main(String[] args) throws InterruptedException {
        try {
            //boss线程池 用于处理accept接受请求
            NioEventLoopGroup boss = new NioEventLoopGroup();
            //work线程池 用于处理read和write的线程
            NioEventLoopGroup work = new NioEventLoopGroup();
            //netty服务的启动器
            ServerBootstrap bootstrap = new ServerBootstrap();

            //配置启动器类的属性
            bootstrap.group(boss, work)                          //指定工作的线程组
                    .channel(NioServerSocketChannel.class)      //指定底层使用的channel
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            System.out.println("init channel");
                            ch.pipeline()
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                    System.out.println(" channel in bound handler adapter ! read msg: " + msg);
                                    ctx.write(msg);
                                    ctx.flush();
                                }
                            });
                            ch.pipeline().addLast(new SomeHandle());
                        }
                    });

            ChannelFuture sync = bootstrap.bind(9999).sync();
            System.out.println("服务器启动");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

3.netty中的reactor线程模型




4.netty 的粘包、拆包（待补）
3.1 TCP的粘包拆包：tcp在传输数据时，他会根据TCP缓冲区的实际情况来划分包，即在发送数据包时，tcp可能会把几个业务数据的包，粘成一个数据包发出去；也有可能把讴歌业务数据包拆成多个数据包发出去。如果服务器不进行相应的粘包、拆包，就会无法读取到正确的数据。
如：客户端发送 asd ,连续发送100次，服务器端接受的数据，并不是100次的asd，
3.2 netty内置了许多解决粘包拆包的类。
3.3 netty拆包原理：不断的从tcp缓冲区中读取数据。然后放到一个类Cumulator对象中，用于保存从tcp缓存中的读到的数据。然后会对这个类中的数据进行拆包，拼接成一个业务数据包，并把拼接的数据发送到后面的handler进行处理，并把数据从这个类对象的buffetBuf中删除。而这个类对象会一直从缓冲区中读取导数据，然后一直循环的作拼接业务数据，发送业务数据，删除业务数据这样的步骤。如果拿到的数据没有拼接一个业务包，或者有多的数据也会在这里缓存，用于下次和读取到的数据再继续进行拆包，如此循环。
3.4 具体拆包代码流程：io.netty.handler.codec.ByteToMessageDecoder 这个类继承ChannelInboundHandlerAdapter，在有数据读进来的时候，就会触发该类的channelRead(ChannelHandlerContext ctx, Object msg)方法，

5.netty的编解码器
4.1 channelPipeline：netty中每个channel都会被默认构造一个pipeline对象，数据输入输出都会通过通过一系列的handler处理。


