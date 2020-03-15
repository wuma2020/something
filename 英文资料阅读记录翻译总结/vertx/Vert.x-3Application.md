# Vert.x Application



    翻译自 http://tutorials.jenkov.com/vert.x/your-first-vertx-application.html
    
---

## 创建vertx实例

第一步创建一个vertx实例。

```java
 public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

    }

```

你可以通过Vertx.vertx()创建Vertx实例


Vertx的实例，会在内部创建一定数量的线程，去处理交换verticles之间的信息。这些线程不是守护线程，所以即使main方法被终止，这些线程也会阻止jvm停止。


## 创建一个verticle

这个vertx实例本身并不会做太多工作，除了管理所有的线程，创建事件总线等通信和基础的任务。为了让你的应用更有用，你需要在vertx实例内部发布一个或者多个verticles（组件）。


在你发布一个verticles实例之前，你需要创建它。你可以通过继承`AbstrackVerticle`创建。下面是一个例子。

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

一个verticle有一个`start()`和`stop()`方法，这两个方法分别在这个verticle实例被发布(deploy)和取消发布的时候执行。你可以在`start()`方法里面执行必要的初始化工作，在`stop()`方法中执行清理的工作


## 发布一个verticle

一旦你创建一个verticle实例，你需要通过vertx实例发布它。通过`deployVerticle()`方法。下面是一个例子

```java
public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MyVerticle());

    }


```

你也可以通过class的全限定名作为参数去发布。

```java
vertx.deployVerticle("vertx.jk.MyVerticle");
```

还有更多的对发布verticle可用的操作。比如，你可以说明发布多少给定的verticle实例。



















