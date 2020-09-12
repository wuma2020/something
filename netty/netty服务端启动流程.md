https://www.cnblogs.com/dafanjoy/p/9810189.html  这个不错



1. ChannelFuture sync = bootstrap.bind(9999).sync();
bind()方法中做了很多工作:
i.校验  （io.netty.bootstrap.AbstractBootstrap#bind(java.net.SocketAddress)）
校验group和ChannelFactory工厂是否为空
ii.初始化和注册（io.netty.bootstrap.AbstractBootstrap#doBind）
final ChannelFuture initAndRegister() {
        Channel channel = null;
            channel = channelFactory.newChannel(); // 1创建一个channel
            init(channel); // 2。初始化这个channel
        ChannelFuture regFuture = config().group().register(channel);
        if (regFuture.cause() != null) {
            if (channel.isRegistered()) {
                channel.close();
            } else {
                channel.unsafe().closeForcibly();
            }
        }
        return regFuture;
    }
1.使用channelFactory创建一个channel （channel = channelFactory.newChannel();）
-  这里是通过反射创建了一个配置类中，设置的channel类型的实例
bootstrap.channel(NioServerSocketChannel.class)
- 该channel的构造方法 
public NioServerSocketChannel() {
       		 this(newSocket(DEFAULT_SELECTOR_PROVIDER));
    }
* newSocket(DEFAULT_SELECTOR_PROVIDER)实际上是利用DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider() ，创建一个ServerSocketChannel实例。SelectorProvider 是一个为selectors and selectable channels服务的抽象类。该类的所有方法都是现成安全的。
*  public NioServerSocketChannel(ServerSocketChannel channel) {
        super(null, channel, SelectionKey.OP_ACCEPT);
        config = new NioServerSocketChannelConfig(this, javaChannel().socket());
    }
在这里，先是直接调用父类的构造函数，然后又创建了一个NioServerSocketChannelConfig类实例，这个类是ChannelConfig的实现，是一个存放了channel的配置属性的set。 
在看看super(null, channel, SelectionKey.OP_ACCEPT);的目的是创建一个NioServerSocketChannel实例，用传进来的channel，并且注册监听事件
SelectionKey：该对象在每次一个channel注册到selector时被创建，他象征着一个在selector中注册关系。这个类作用很多，需要再看  ？？？？？
NioServerSocketChannel：一个ServerSocketChannel的实现，用于接收新进来的请求，使用NIO 的selector。
在io.netty.channel.nio.AbstractNioChannel#AbstractNioChannel中，才是真正的进行实例化的步骤。在该方法中：
protected AbstractNioChannel(Channel parent, SelectableChannel ch, int readInterestOp) {
        super(parent); //执行父类的构造函数  下面分析
        this.ch = ch; // 设置channel
        this.readInterestOp = readInterestOp;//设置监听的事件
       ch.configureBlocking(false);//设置为非阻塞模式
        }
在io.netty.channel.AbstractChannel#AbstractChannel(io.netty.channel.Channel)中，进行了最终的创建， 			 protected AbstractChannel(Channel parent) {
        this.parent = parent;
        id = newId();
        unsafe = newUnsafe();
        pipeline = newChannelPipeline();
    }
在该类中创建了id，unsafe，pipeline的对象实例。
id的目的是：标识一个全局的channel，一个id代表一个channel，不会重复。
unsafe操作不应该被用户调用。是不安全的。 暂时不去了解
pipeline：DefaultChannelPipeline的实例。默认的channelpipeline的实现。channelpipeline是一个存放handlers或者阻断入站事件和出站事件操作的list对象。http://www.oracle.com/technetwork/java/interceptingfilter-142169.html，是Intercepting Filter设计模式的实现。他给用户一个完全的控制，控制channel中的所有事件的处理逻辑。每一个channel都有他自己的pipeline。
pipeline中的handler是可以删除和添加的，因为pipeline是线程安全的。pipeline的相关知识点后面会详细再说。

2.初始化channel（io.netty.bootstrap.Bootstrap#init）（ init(channel);）
   void init(Channel channel) {
        ChannelPipeline p = channel.pipeline();
        p.addLast(config.handler());

        setChannelOptions(channel, options0().entrySet().toArray(newOptionArray(0)), logger);
        setAttributes(channel, attrs0().entrySet().toArray(newAttrArray(0)));
    }

- 会在其pipeline中添加配置类中的handler
- setChannelOptions（）设置channel的optinos
- setAttributes 设置channel的 属性
3.注册channel。  ChannelFuture regFuture = config().group().register(channel);
这里进入的是NioEventLoopGroup的父类的MultithreadEventLoopGroup的register()方法中。
 public ChannelFuture register(Channel channel) {
        return next().register(channel);
    }
调用了其父类的MultithreadEventExecutorGroup的next()返回一个EventExecutor实例，从其成员变量private final EventExecutorChooserFactory.EventExecutorChooser中获取，返回的是io.netty.channel.SingleThreadEventLoop，并且调用它的 register(io.netty.channel.Channel)方法，  
public ChannelFuture register(Channel channel) {
        return register(new DefaultChannelPromise(channel, this));
    }
- 这里会新建一个DefaultChannelPromise实例，然后继续注册。
ChannelPromise：是一个可写的channelfuture接口，channelfuture接口是异步的返回io操作结果的接口，这里会拿到相应的状态。
   public ChannelFuture register(final ChannelPromise promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        promise.channel().unsafe().register(this, promise);
        return promise;
    }
这里是，调用unsafe类来注册这个channel。具体的实现是io.netty.channel.AbstractChannel.AbstractUnsafe#register的。
这里会做相应的判断，进行异常抛出。
然后会新建一个线程，执行register0(promise);方法，
eventLoop.execute(new Runnable() {
                        @Override
                        public void run() {
                            register0(promise);
                        }
                    });

在register0(promise)中：
boolean firstRegistration = neverRegistered;
                doRegister();    // 这个方法是在这个channel被注册到这个evenloop中之后，才会调用这个方法，时刻回调方法
                neverRegistered = false;
                registered = true;
                pipeline.invokeHandlerAddedIfNeeded();  //添加handler，这个再看??
                safeSetSuccess(promise);
                pipeline.fireChannelRegistered();
                if (isActive()) {
                    if (firstRegistration) {
                        pipeline.fireChannelActive();
                    } else if (config().isAutoRead()) {
                        beginRead();
                    }
                }

                pipeline.fireChannelRegistered();//这个方法是一个channel被注册到eventloop后，会调用pipeline中的所有handler的channelRegistered()方法，执行注册步骤。

？？？？？这里需要再学习一下。。。

4.返回regFuture
iii.dobind0() doBind0(regFuture, channel, localAddress, promise);
