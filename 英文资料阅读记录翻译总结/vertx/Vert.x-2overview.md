# Vert.x overview

    翻译自 http://tutorials.jenkov.com/vert.x/overview.html
    
---

vertx是一个运行在jvm上的工具或者平台。什么意识呢？他的内部是怎么设计的呢？本章会来介绍他们。

## Verticles

Vertx通过Verticles来发布和执行组件。你可以把Verticles想象成EJBs模式中的servlets或者事件驱。这里是一个简单地图说明了一个vertx平台，有4个verticles在运行。
![此处输入图片的描述][1]

## 事件总线

verticles是事件驱动的，说明他们在没有接收到message的时候是不会运行的。verticles可以通过事件总线和彼此交流。下面这个图简单地说明了通过事件总线交流。
![此处输入图片的描述][2]


message可以是java 对象，string，json，二进制数据等等。

verticles可以发送或者监听一个`地址`。一个地址就像一个命名过的channel。当一个message向一个地址发送，所有的监听这个地址的verticles都会接受到消息。verticles也可以订阅或者取消订阅这个地址，不需要发送者知道。这使得在发送消息和接受消息之间是松耦合的。

所有的消息的处理都是异步的。一个消息从一个verticles发送给另一个verticles，那个消息会先被发送到时间总线上，然后再由总线控制发送。之后消息会出队列，发送给监听在这个地址的verticles。

## vertx的线程模型

verticles是单线程模型。意味着，一个verticles只能总是在一个线程运行。也就是说，你不用想你的verticles内部是多线程的（除非你手动开启其他的线程）

vertx能够充分利用所有的CPU。vertx为每个cpu创建一个线程。每个线程可以发送消息给多个verticles。请记住，verticles是事件驱动的，他只在有消息接收的时候才会运行。所以一个verticles不需要他专属的线程（？我也不懂）。一个单线程可以分发消息给多个verticles。

当一个线程分发一个消息给一个verticles，处理这个消息的verticles会被那个线程执行。这个消息的分发和处理的逻辑被执行，在一个叫handle（listenter object）的方法中。一旦这个线程处理消息的逻辑结束，这个消息就会把这个消息分发给另一个verticles。

![此处输入图片的描述][3]

## vertx 服务

vertx有一些内置的服务：
    - HTTP SERVER
    - JDBC CONNECTOR
    - MONGODB connector
    - SMTP  MAIL
    - Message queue connectors



  [1]: http://tutorials.jenkov.com/images/vertx/vertx-overview-1.png
  [2]: http://tutorials.jenkov.com/images/vertx/vertx-overview-2.png
  [3]: http://tutorials.jenkov.com/images/vertx/vertx-overview-3.png