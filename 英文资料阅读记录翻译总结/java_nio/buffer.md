
# 非常不好用，很容易出问题.
--------

## 基本buffer用法（4步）

- 1 向buffer中写入数据，如channel.read(byteBuffer);
- 2 调用 byteBuffer.flip(); 
- 3 从 byteBuffer 中读取数据，byteBuffer.get();
- 4 调用 byteBuffer.clear() 或者 byteBuffer.compact();

## 为什么需要flip()？

这里需要了解buffer的数据结构，请移步.

```
flip()是在写模式变成读模式是需要做的操作。用于把
position设置为0，limit设置为写操作时的position位
置，实际就是有效数据的大小.
```

## 获取一个 buffer 对象实例

`必须使用xxxBuffer.allocate(buffer大小);方法获取`
    
    ByteBuffer buf = ByteBuffer.allocate(48);  
    CharBuffer buf = CharBuffer.allocate(1024);


## 向buffer中写数据（2种方式）

```
    1.从channel中向buffer中读     --  channel.read(xxxBuffer);
    
    2.xxxBuffer.put(xxx); //put方法有很多重载，可以具体设置插入的位置等功能
```

## 从buffer中读取数据（2种方式）

```
    1.channel.write(xxxBuffer);
    2.xxxBuffer.get();//也有一些重载方法
```

## rewind() 方法

` 使得postion回到0位置，limit不动，所以buffer中的数据可以反复读`

## clear() and compact() 区别

```

clear():是把position设置为0，limit设置成capacity.
compact():是把postion设置为0，并把上次未读的数据复制到头位置，
    把limit设置成有效数据的位置.


```