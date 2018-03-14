# PipedInputStream&PipedOutputStream

标签： java8源码

---
> 欢迎[star][1]

# 思路
> 向` PipedOutputStream `输出流中写数据,然后用`PipedInputStream`输入流读取数据.(需要配套使用)

# 目的
> 实现线程间的通信.

# PipedInputStream详解

## PipedInputStream成员变量
    private static final int DEFAULT_PIPE_SIZE = 1024;//默认buffer数组（即缓存区）大小为1024
    
    //在允许更改管道尺寸之前，这是一个常量。该字段将继续保持向后兼容
    protected static final int PIPE_SIZE = DEFAULT_PIPE_SIZE;
    protected byte buffer[];//缓存数组
    volatile boolean closedByReader = false;//关闭读
    boolean closedByWriter = false;//关闭写
    boolean connected = false;//是否已连接到对应的输出流
    protected int in = -1;//buffer中最后一个有效数值的索引
    protected int out = 0;//将要 读取的buffer的索引（已经读取的数据的索引值 +1 ）
    Thread readSide;//读线程
    Thread writeSide;//写线程

## PipedInputStream构造函数
```java
/**有4种*/

    public PipedInputStream() {//只初始化缓存数组大小(默认大小)的空构造函数
        initPipe(DEFAULT_PIPE_SIZE);//见（I）
    }
    public PipedInputStream(int pipeSize) {//只初始化缓存数组大小(指定大小pipeSize)的构造函数
        initPipe(pipeSize);
    }
     public PipedInputStream(PipedOutputStream src) throws IOException {//指定连接 输出流为src 的构造函数，并初始化缓存大小（默认大小）
        this(src, DEFAULT_PIPE_SIZE);
    }
     public PipedInputStream(PipedOutputStream src, int pipeSize)
            throws IOException {//指定连接 输出流为src 的，并指定缓存大小为pipeSize的构造函数
         initPipe(pipeSize);
         connect(src);//连接指定输出流.把见（II）
    }
//-----------------------------------------

//（I）构造函数中调用的方法initPipe
    private void initPipe(int pipeSize) {//初始化 缓存数组buffer的大小为pipeSize
         if (pipeSize <= 0) {
            throw new IllegalArgumentException("Pipe Size <= 0");
         }
         buffer = new byte[pipeSize];
    }

//(II)  构造函数中调用的方法connect  
    public void connect(PipedOutputStream src) throws IOException {
        src.connect(this);//调用输出流的connect方法.见（III）
    }

//(III) 把该 输入流的 的引用赋值给 输出流的成员变量sink(即PipedInputStream输入流对象)，并把原输入流（snk）的in = -1； out = 0 ;connect 设置为 true.
 public synchronized void connect(PipedInputStream snk) throws IOException {
        if (snk == null) {
            throw new NullPointerException();
        } else if (sink != null || snk.connected) {
            throw new IOException("Already connected");
        }
        sink = snk;
        snk.in = -1;
        snk.out = 0;
        snk.connected = true;
    }
    

```

## PipedInputStream常用方法

### int read();
```java
//读取输出流中索引为out的值，并返回该值
 public synchronized int read()  throws IOException {
        if (!connected) {//未连接
            throw new IOException("Pipe not connected");
        } else if (closedByReader) {//读取已关闭
            throw new IOException("Pipe closed");
        } else if (writeSide != null && !writeSide.isAlive()
                   && !closedByWriter && (in < 0)) {//写线程dead
            throw new IOException("Write end dead");
        }

        readSide = Thread.currentThread();
        int trials = 2;
        while (in < 0) {//in 初始为 -1
            if (closedByWriter) {//写关闭，直接返回 -1
                return -1;
            }
            if ((writeSide != null) && (!writeSide.isAlive()) && (--trials < 0)) {
                throw new IOException("Pipe broken");
            }
            /* 可能有一个写线程在等待*/
            notifyAll();
            try {
                wait(1000);//等待一秒
            } catch (InterruptedException ex) {
                throw new java.io.InterruptedIOException();
            }
        }
        int ret = buffer[out++] & 0xFF;//读取buffer[out]的值
        if (out >= buffer.length) {
            out = 0;
        }
        if (in == out) {
            /* 空 */
            in = -1;
        }
        return ret;//返回读取的值
    }

```

### int read(byte b[], int off, int len);
```java 
//把输出流中的内容读到 参数 字节数组b中
 public synchronized int read(byte b[], int off, int len)  throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
        int c = read();//先读取缓存buffer中的第一个字节，如果值 < 0，则直接返回 -1. 即读取错误.
        if (c < 0) {
            return -1;
        }
        b[off] = (byte) c;
        int rlen = 1;//已经读到 b 中的数据的长度
        while ((in >= 0) && (len > 1)) {
            int available;//buffer中未读取的数据长度
            if (in > out) {//buffer中的数据大小 大于 out 即，读取buffer字节数组的索引
                available = Math.min((buffer.length - out), (in - out));
            } else {
                available = buffer.length - out;
            }

            // A byte is read beforehand outside the loop
            if (available > (len - 1)) {
                available = len - 1;
            }
            System.arraycopy(buffer, out, b, off + rlen, available);//把buffer中的数据复制到b中，从off + rlen中（因为r已经读取过索引为0的值并赋值到b中）。长度为available
            out += available;//更新  读取到的数组索引 + 1
            rlen += available;//更新 已经读到b中的数据的长度
            len -= available;//需要 再读取得长度

            if (out >= buffer.length) {
                out = 0;
            }
            if (in == out) {//将要读取的索引 和 buffer数组 大小相同，则已经读完.
                /* now empty */
                in = -1;
            }
        }
        return rlen;//返回已经读取到b中的数据的长度值
    }

```
# PipedOutputStream详解

## 成员变量
    private PipedInputStream sink;//管道输入流

## 构造函数
```java
/**共2种*/
    public PipedOutputStream() {//空构造器
    }
    //连接指定管道输入流的构造器
    public PipedOutputStream(PipedInputStream snk)  throws IOException {
        connect(snk);//见PipedInputStream构造函数中（III）讲解
    }
    
    //---------------------------------------

```

## 常用方法
### void write(byte b[], int off, int len)
```java
//把b中内容写到成员变量PipedInputStream sink中
    public void write(byte b[], int off, int len) throws IOException {
        if (sink == null) {
            throw new IOException("Pipe not connected");
        } else if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                   ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        sink.receive(b, off, len);//调用PipedInputStream类中的receive方法写数据.见（IV）
    }
    
    //--------------------------------------
    //(IV)
     synchronized void receive(byte b[], int off, int len)  throws IOException {
        checkStateForReceive();//常规检查
        writeSide = Thread.currentThread();
        int bytesToTransfer = len;//传输数组的长度
        while (bytesToTransfer > 0) {
            if (in == out)
                awaitSpace();
            int nextTransferAmount = 0;//剩余要读取的长度
            if (out < in) {
                nextTransferAmount = buffer.length - in;
            } else if (in < out) {
                if (in == -1) {
                    in = out = 0;
                    nextTransferAmount = buffer.length - in;//buffer数组的长度 默认为1024
                } else {
                    nextTransferAmount = out - in;
                }
            }
            if (nextTransferAmount > bytesToTransfer)//如果buffer默认长度大于b的len
                nextTransferAmount = bytesToTransfer;
            assert(nextTransferAmount > 0);
            System.arraycopy(b, off, buffer, in, nextTransferAmount);//将b中的数据赋值到buffer中
            bytesToTransfer -= nextTransferAmount;
            off += nextTransferAmount;//
            in += nextTransferAmount;//将in 设置成复制到buffer数组中的最后一个有效数值的索引
            if (in >= buffer.length) {//
                in = 0;
            }
        }
    }

```

# demo
> 管道输入/输出流不建议在同一个线程使用.因笔者偷懒，就在一个文件中写了.
```java
package cn.kkcoder.java8;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class PipedInputStream_Output {

	public static void main(String[] args) throws IOException {

		PipedOutputStream out = new PipedOutputStream();
		PipedInputStream in = new PipedInputStream(out);

		
		String s = "https://github.com/static-mkk";
		byte[] b = s.getBytes();
		
		out.write(b);
		
		int inlen = in.available();//管道输出流中有效数组的长度

		byte[] read = new byte[1024];
		in.read(read);
		
		String sByte = new String(read,0,inlen); 
		
		System.out.println(sByte);
		
	}

}

```


  [1]: https://github.com/static-mkk/java8SourceLearn