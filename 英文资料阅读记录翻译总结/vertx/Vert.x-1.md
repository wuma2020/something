# Vert.x 学习

    翻译自 http://tutorials.jenkov.com/vert.x/index.html
    
---

Vertx是一个开源的、响应式的、多语言的运行在jvm上框架。vertx使用特别的方法优化高并发和网络编程的。

## Vertx 是一个工具或者平台

vertx就像一个工具库。你可以使用它在一个java应用中，创建一个实例，然后调用他的方法即可。

vertx也像是一个平台。你可以使用命令行启动它，并告诉他你运行的什么组件。

## vertx是响应式的

vertx号称响应式的工具库。响应式的应用包含发送message或者事件给彼此的组件。这和java EE的内部设计不一样。这种设计是得vertx适应不同的应用（社交或者游戏服务器）。vertx适应大多数java EE的应用。

在 [concurrency models][1]中，说明了，java ee使用并行的工作线程的方式，而vertx使用channels的组装线模式【这个后续会继续学习翻译】。

## vertx是多种语言的



  [1]: http://tutorials.jenkov.com/java-concurrency/concurrency-models.html