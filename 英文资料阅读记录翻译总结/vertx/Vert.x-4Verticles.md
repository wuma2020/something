# Vert.x Verticles

    翻译自：http://tutorials.jenkov.com/vert.x/verticles.html
    
---

`verticle`这个术语表示你在vertx发布的一个verticle实例的组件，有点像java ee中的`Servlet`或者事件驱动。然而，verticles工作方式的线程模型和servlet，ejb不同。这一章介绍如何创建和发布一个verticles，以及verticles是如何相互通过事件总线通信的。

## 实现一个verticle

通过继承实现：
```java
class MyVerticle extends AbstractVerticle{

    @Override
    public void start() throws Exception {
        System.out.println("MyVerticle start");
    }

    @Override
    public void stop() throws Exception {
        System.out.println("MyVerticle stop");
    }
}

```

## start()
重写start(）方法。该方法在这个verticle发布的时候被调用。

通常在`start()`方法中创建HTTP 或者 TCP server,注册事件handles在事件总线上，发布其他verticles，或者你的verticle需要做的工作。

这个`AbstractVerticle`类也包含了其他重载的`start()`,使用Future作为参数的。这个Future可以告诉vertx是否这个verticle部署成功。即，在部署成功后，调用这个Future。

## stop()

`stop()`在vertx终止和你的verticle停止的时候调用。

## 发布一个verticle

像下面这样发布：
```java
        vertx.deployVerticle(new MyVerticle());
```

一旦vertx发布了一个verticle，这个verticle的start()方法就会被调用。

这个verticle会被一部的发布，所以当这个`deployVerticle()`方法返回时，这个verticle的start()方法也许不会马上执行。如果你需要确切的知道这个verticle被发布了，你可以提供一个`Handle`作为参数回调。示例如下：

```java
vertx.deployVerticle(new MyVerticle(),event -> {
            System.out.println("delpoy complete");
        });

```

## 发布一个verticle从另一个Verticle

示例如下：
```java
class MyVerticle extends AbstractVerticle{

    @Override
    public void start() throws Exception {
        System.out.println("MyVerticle start");
        vertx.deployVerticle(new SecondVerticle());
    }

    @Override
    public void stop() throws Exception {
        System.out.println("MyVerticle stop");
    }
}


```

## 使用事件总线

verticle通过事件总线监听接受的信息，或者通过事件总线写出信息。下面介绍两者。

### 监听消息

verticle想要通过事件总线获取消息，他就要在一个地址上进行监听。

多个verticles可以在一个地址监听。说明不是一个地址和一个verticle一一对应的。多个verticle可以监听消息和发送消息在同一个地址上。

一个verticle可以通过AbstractVerticle的内部的vertx，获取事件总线的引用。

下面是一个在一个地址监听的示例：

```java
class SecondVerticle extends AbstractVerticle{

    @Override
    public void start() throws Exception {

        vertx.eventBus().consumer("/tmp/loacl.sock",event -> {
            System.out.println("received message: " + event.body());
        });

    }
}

```

这个例子展示了verticle注册了一个`consumer`，在事件总线上。这个consumer注册在这个地址上，意味着，他消费被事件总线发送到这个地址的消息。


### 发送消息

发送消息可以通过`send()`或者`publish()`方法，在事件总线上。

`publish()`方法把消息发送到所有的再这个地址上监听的verticle。

`send()`方法只发送其中一个verticle，具体发送到哪一个，取决于vertx docs说的"non-strict round robin" 算法.即，平均的分发到内个verticle。

下面给出一个通过send和publish方法发送消息的示例：


```java
    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new RecevieBusVerticle("T1"));
        vertx.deployVerticle(new RecevieBusVerticle("T2"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        vertx.deployVerticle(new SendMessageVerticle());
}

```

```java
class RecevieBusVerticle extends AbstractVerticle{

    private String name;

    public RecevieBusVerticle(String name) {
        this.name = name;
    }

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("aa",event -> {
            System.out.println( this.name + " received message : " + event.body());
        });

    }
}
```


```java

class SendMessageVerticle extends AbstractVerticle{
    @Override
    public void start() throws Exception {
        vertx.eventBus().publish("aa","publish");
        vertx.eventBus().send("aa","send ");
    }
}

```


结果：publish方法的消息都能收到，而send方法的只有一个可以。

`
T1 received message : publish
T1 received message : send 
T2 received message : publish
`


















