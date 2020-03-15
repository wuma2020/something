# vertx http server

    翻译自：http://tutorials.jenkov.com/vert.x/http-server.html

---


vertx使得创建一个http server很容易，所以你的应用可以接受http请求。你可以创建1或多个http server，取决于你的需求。下面我们看看怎么创建一个http server通过verticle，以及怎么处理请求。

## 创建一个http server

通过vertx实例创建，示例如下：
```java
HttpServer httpServer = vertx.createHttpServer();
```

使用verticle创建http server。使用这种方法，所有注册到http server上的handle都会被执行，并且是被执行这个verticle的线程执行。

## 启动一个http server

一旦你创建一个http server，你就可以使用它的`listen()`方法监听。实例如下：

```java
public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer();

        httpServer.listen(9999);
    }
```

这个listen()也有不同的重载的方法提供不同的操作。

## 设置请求 handle 在http server上

为了处理进来的请求，你必须设置一个handle在这个http server上。示例如下：

```java
public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer();

        httpServer.requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest event) {
                System.out.println("incoming request : " + event.body());
            }
        });

        httpServer.listen(9999);
    }
```


每次一个http请求进来，这个`handle()`方法会被调用。在这个`handle()`方法里面处理这个http请求。


## 请求头和参数

你可以访问这个请求头和请求参数，通过handle() 方法里面的`HttpServerRequest`参数。示例如下：

```java
    public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer();

        httpServer.requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest request) {
                request.headers();
                request.params();
                request.response();
            }
        });

        httpServer.listen(9999);
    }


```

## 处理post请求

如果是http post请求，你处理的形式会有点不同。你需要追加一个body handle处理这个http 请求。这个body handle在有request body的数据到达时被调用。示例如下：

```java
public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer();

        httpServer.requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest request) {
                if(HttpMethod.POST == request.method()){
                    //处理 body中的数据
                    request.handler(new Handler<Buffer>() {
                        @Override
                        public void handle(Buffer event) {
                            System.out.println("post request body : " + event.toString());
                        }
                    });
                }

            }
        });

        httpServer.listen(9999);
    }
    

```


如果你想要等到http post body中的所有数据都到达，你可以添加一个end handler。这个end handle 知道body中的所有数据到达，才会被调用。但是这个end handler不能够直接访问http post body。你需要收集body然后访问。示例如下：

```java
public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer();

        httpServer.requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest request) {
                if(HttpMethod.POST == request.method()){

                    Buffer buffer = Buffer.buffer();

                    //处理 body中的数据
                    request.handler(new Handler<Buffer>() {
                        @Override
                        public void handle(Buffer event) {
                            buffer.appendBuffer(event);
                            System.out.println("post request body : " + event.toString());
                        }
                    });

                    request.endHandler(event -> {
                        System.out.println("get the full body data" + buffer.toString()) ;
                    });
                }

            }
        });

        httpServer.listen(9999);
    }

```

## 发送http response

获取response方式如下：

```java
HttpServerResponse response = request.response();
```


可以通过`response`设置返回码，添加响应头信息，写响应数据等等。


```java
response.setStatusCode(200);
                response.putHeader("Content-length","8")
                        .putHeader("Content-type","JSON");
                response.write("response");
                response.end();

```

`write()`方法在response body中添加数据，可以多次调用。这个方法是异步的，在把数据入队之后马上返回。

`end()`方法调用后，这个response被结束。其也可以添加数据参数。

## 关闭 http server

```java
httpServer.close();
```

异步执行。也可以添加handler使得在真正关闭时做相应的提示等。





