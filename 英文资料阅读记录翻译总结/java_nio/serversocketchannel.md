## NIO ServerSocketChannel

ServerSocketChannel是一个监听TCP连接的channel，就像ServerSocket一样。使用方法也差不多。

## example 

```java

package cn.kkcoder.netty.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * @Author : mkk
 */
public class ServerSocketChannelDemo {

    public static void main(String[] args) {

        try {

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.socket().bind(new InetSocketAddress(9999));

            //非阻塞模式下，accept()方法会马上返回一个SocketChannel，因为是马上返回，如果此时没有连接，就会返回null
            //如果是阻塞模式下，accept()方法会阻塞.
            serverSocketChannel.configureBlocking(false);

            //这里实际上只有一个连接能进来，因为一个连接进来之后，会一直在循环的处理那个连接的数据。
            while(true){

                SocketChannel client = serverSocketChannel.accept();

                if(client != null){

                    ByteBuffer buffer = ByteBuffer.allocate(64);
                    int read = client.read(buffer);

                    //获取socketchannel中的数据
                    while(read != -1){
                        buffer.flip();
                        ArrayList<Byte> list = new ArrayList<>();
                        while(buffer.hasRemaining()){
                            list.add(buffer.get());
                        }
                        list.stream().forEach((s) -> {
                            System.out.print(new String(String.valueOf(s)));
                        });

                        buffer.clear();
                        read = client.read(buffer);
                    }

                }

                System.out.println("3s" );
                Thread.sleep(3000);

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}



```