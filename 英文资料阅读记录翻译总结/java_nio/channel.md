## nio 中channel和stream的不同

-  可以在同一个channel中进行读写操作，而在bio一个流中，只能读或者写.
-  channel可以异步的读和写 .
- channel总是将数据读到一个buffer中，或者从一个buffer中写数据到channel中.

## channel 的几种实现

- FileChannel           读或写数据到一个文件
- DatagramChannel       在网络中，通过UDP协议读或写数据.
- SocketChannel         在网络中，通过TCP协议读或写数据.
- ServerSocketChannel   在网络上在，监听连接进来的TCP连接,web server端的code.每个连接进来，都会建立一个对应的SocketChannel.


## 例子

```
package cn.kkcoder.netty.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * 用FileChannel来操作文件
 * @Author : mkk
 */
public class ChannelDemo1 {

    public static void main(String[] args) {

        RandomAccessFile file = null;
        FileChannel inChannel = null;

        try {

            file = new RandomAccessFile(new File("E://demo1.txt"),"rw");
            inChannel = file.getChannel();

            //创建一个buffer
            ByteBuffer buf = ByteBuffer.allocate(48);

            //从channel中读取所有数据到buffer中，bytesRead是数据的大小
            int bytesRead = inChannel.read(buf);

            while (bytesRead != -1) {

                System.out.println("Read " + bytesRead);
                buf.flip();

                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }

                buf.clear();
                bytesRead = inChannel.read(buf);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(inChannel != null){
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}



```
