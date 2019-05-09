# Selector（重点内容）

-------------

    nio 中的selector是一个组件，用于监视 nio 中的多个channel的实
    例，然后来确定那个channel准备好了进行读或者写操作。
    这样的话，一个线程可以监视多个channel，就可以处理多个网络连接。

## 为什么使用selector？

```
最大的好处就是一个线程可以处理很多个channel，这样就
可以减少多余线程的创建和销毁和切换.对于操作系统来
说，线程的切换是一个比较耗时的操作。而且线程还会占用
os内存。所以，最好少用线程。但是现代的操作系统一般都
是多cpu的，所以不用多线程一定程度上也是浪费cpu的计算
能力。所以，最适合的才是最好的。
```

## 创建一个selector

```
Selector s = Selector.open();
```

## 注册channel到selector上

    注意必须使用支持非阻塞的channel，像FileChannel是不支持的，
    socket 的 channel都是很好支持。

```
//设置channel为非阻塞模式
channel.configureBlocking(false);
//注册到某个selector以及相应的事件
channel.register(selector,SelectionKeys.OP_READ);

```


    register（xx,xx）的第二个参数表示，通过这个
    selector的，并且有你感兴趣的事件的channel。当
    channel出发这个事件时，selector就会筛选出这个
    channel。

### 一般有4种事件

- connect  ->  SelectionKey.OP_CONNECT
- accept   ->  SelectionKey.OP_ACCEPT
- read     ->  SelectionKey.OP_READ
- write    ->  SelectionKey.OP_WRITE


        解释：当一个channel触发一个事件时，也就是说它准备好应对这个事件。
             一个channel向一个server channel连接成功，该channel就会触
             发connect事件；server channel接收一个连接，就会触发accept
             事件；一个channel的数据准备好被read，就会触发read事件；一
             个channel准备好写数据，他就会触发write事件。


## SelectionKey

```
    当一个channel注册到一个selector上时，他会返回一个SelectionKey。这
    个对象包含以下几个属性：
        
        1. 感兴趣的事件
        2. 准备好的事件
        3. 这个channel对象
        4. 这个selector对象
        5. 一个追加对象的操作

    1.感兴趣的事件：我们在注册selector时设置的事件。可以通过
      int interest = selectionKey.interestOps()方法获取.可以通过
      boolean isInterestedInAccept  = interestSet & SelectionKey.OP_ACCEPT;
      方法来进行判断是哪个操作。其他三个事件同理.

    2.准备好的事件：channel已经准备好的事件。可以通过调用以下方法判断    
        selectionKey.isAcceptable();
        selectionKey.isConnectable();
        selectionKey.isReadable();
        selectionKey.isWritable();

    3.获取channel：Channel  channel  = selectionKey.channel();

    4.获取selector：Selector selector = selectionKey.selector(); 

    5.追加一个对象： selectionKey.attach(theObject);
                    Object attachedObj = selectionKey.attachment();

```

## 通过selector来筛选channel

    通过int select()方法来获取所有满足注册时事件的channel，返回的int表示
    有几个满足条件的channel.
    然后调用Set<SelectionKey> selectedKeys = selector.selectedKeys(); 
    即selectedKeys()方法，会返回满足事件的所有的selectionKey，从而获取所
    需的channel，selector，read event,interest event...
    最后我们在用iterator来遍历这个set，每次遍历的时候都要从set中删除这个
    selectionKey，否则在下一次select()方法执行时，该selectionKey会再一
    次被放到这个selectedKeys的set中，就会重复出来，造成bug.