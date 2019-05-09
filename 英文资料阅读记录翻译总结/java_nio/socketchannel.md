## example

```java

package cn.kkcoder.netty.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @Author : mkk
 */
public class SocketChannelDemo {

    public static void main(String[] args) {


        try {
            //设置socketchannel端口和地址和非阻塞模式
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost",9999));

            //如果已经连接，就发出数据，然后断开
            while(socketChannel.finishConnect()){

                String msg = new String(socketChannel.getLocalAddress().toString().getBytes(Charset.forName("utf-8")) );

                ByteBuffer buffer = ByteBuffer.allocate(128);
                buffer.clear();//清空

                buffer.put(msg.getBytes());

                buffer.flip();//复位操作

                //向channel中写入数据
                while(buffer.hasRemaining()){
                    socketChannel.write(buffer);
                }

                System.out.println("数据发送结束");
                //关闭连接
                socketChannel.close();
                break;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}


```