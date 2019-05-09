

## channel之间传输数据

-----

```
在java nio中，数据可以直接从一个channel传输到另一个channel。

```

## 示例

```java

try {

        RandomAccessFile fromFile = new RandomAccessFile("E://demo1.txt","rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("E://toChannel.txt","rw");
        FileChannel toChannel = toFile.getChannel();

        toChannel.transferFrom(fromChannel,0,fromChannel.size());


    } catch (IOException e) {
        e.printStackTrace();
    }


```

-------
`
public abstract long transferFrom(ReadableByteChannel src, long position,long count);
`

- src ： 指的是从哪个channel中输出数据.指的是哪个channel
- position ： 指得是目标文件的第几个位置开始输出数据。必须为非负数.
- count ： 被传输的数据的最大字节数，必须是非负数.

-----

# 说明

```
SocketChannel 的实现只能传输 在其内部缓冲区buffer中，已经准
备好的数据。即使后来socketChannel中有了更多数据，也不会传输.
所以，socketchannel不会传输整个数据到filechannel中.
transferTo(long position, long count,WritableByteChannel target)方法同理.
```