# BufferInputStream_BufferOutputStream

标签： java8源码

---

> 欢迎 [star][1]

# 思路
> 主要是从输入或输出流中一次读取 8192(默认) 个字节加载到内存中，用作缓存.

# 目的
> 提高数据输入或输出的速度.

# BufferInputStream详解
## BufferInputStream 类签名
    public
    class BufferedInputStream extends FilterInputStream {}

## BufferInputStream 成员变量
    protected volatile byte buf[];//缓存数组
    protected int count;//缓存数组中的有效字节
    protected int marklimit;//标记最大值
    protected int markpos = -1;//缓冲区标记位置
    protected int pos;//缓冲区当前索引
    private static int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;//最大缓存大小
    private static int DEFAULT_BUFFER_SIZE = 8192;//默认缓存大小

## BufferInputStream 构造方法
```java
/**共两个构造方法*/
    //为指定输入流提供缓存，且缓存大小为默认缓冲大小的构造器.
    public BufferedInputStream(InputStream in) {
        this(in, DEFAULT_BUFFER_SIZE);
    }
    //为指定输入流提供缓存，但缓存大小为指定大小的构造器.
    public BufferedInputStream(InputStream in, int size) {
        super(in);//由类签名可以看出，继承自FilterInputStream，in为其父类成员变量.
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        buf = new byte[size];//初始化缓存数组的大小
    }
```

## BufferInputStream 的常用方法

### read(byte[] b);
```java
//实际上这是其父类的read(byte b[])方法，调用了read(b, 0, b.length)方法，其重写了该方法，故，调用其自己的该方法.
    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }
```

### read(byte b[], int off, int len);
```java
   public synchronized int read(byte b[], int off, int len)
        throws IOException
    {
        getBufIfOpen(); // 获取缓存数组,这里的作用是检查缓存数组是否为null.若是,则抛异常. 见（I）
        if ((off | len | (off + len) | (b.length - (off + len))) < 0) {//数组越界
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int n = 0;
        for (;;) {
            int nread = read1(b, off + n, len - n);//把缓存数组中的数据copy到数组b中. 见（II）
            if (nread <= 0)//nread 复制的字节个数
                return (n == 0) ? nread : n;
            n += nread;//n 为从缓存数组 读取到字节数组b中的实际长度
            if (n >= len)//如果实际长度>=len,则停止读，并返回n
                return n;//
            InputStream input = in;
            //如果 n < len 但，输入流中的数据已经读取完，则返回 n
            if (input != null && input.available() <= 0)
                return n;
        }
    }
    
//---------------------------------------------

    // (I) getBufIfOpen（）方法详解
        private byte[] getBufIfOpen() throws IOException {
        byte[] buffer = buf;//把内部缓存数组引用赋值给 buffer
        if (buffer == null)//空缓存抛出异常
            throw new IOException("Stream closed");
        return buffer;
    }
    
    // (II)  私有方法
        private int read1(byte[] b, int off, int len) throws IOException {
        int avail = count - pos;//剩余未读数组长度
        if (avail <= 0) {
            if (len >= getBufIfOpen().length && markpos < 0) {
            //如果该缓存数组长度 <= len 读取到数组b中的长度，以及 标记位点 < 0  ,若true，则执行下面语句.
                return getInIfOpen().read(b, off, len);//调用并返回 传进来的输入流的方法read(byte[] b, int off, int len).即，当指定读取的长度，大于缓存数组长度时，直接读取指定长度的数组内容.
            }
            fill();//作用就是把修饰的输入流的数据加载到缓存数组中    见fill() 解析
            avail = count - pos;//新的剩余未读取长度
            if (avail <= 0) return -1;
        }
        int cnt = (avail < len) ? avail : len;//需要读取的字节长度
        System.arraycopy(getBufIfOpen(), pos, b, off, cnt);
        pos += cnt;//更新 缓存数组中的 pos（读取） 位点
        return cnt;//这一次，读取的数组数量
    }
```
### fill();
```java
//缓存数组的灵魂方法
//灵魂方法太复杂，推荐解析地址 http://wangkuiwu.github.io/2012/05/12/BufferedInputStream/
 private void fill() throws IOException {
        byte[] buffer = getBufIfOpen();//获取缓存数组
        if (markpos < 0)
            pos = 0;//markpos >= 0，则该输入流被标记，否则没被标记.        
                //以下是被标记的情况
                else if (pos >= buffer.length) //如果pos >= 输入流数组长度
                    if (markpos > 0) {  /* can throw away early part of the buffer */
                        int sz = pos - markpos;
                        System.arraycopy(buffer, markpos, buffer, 0, sz);
                        pos = sz;
                        markpos = 0;
                    } else if (buffer.length >= marklimit) {
                        markpos = -1;   /* buffer got too big, invalidate mark */
                        pos = 0;        /* drop buffer contents */
                    } else if (buffer.length >= MAX_BUFFER_SIZE) {
                        throw new OutOfMemoryError("Required array size too large");
                    } else {            /* grow buffer */
                        int nsz = (pos <= MAX_BUFFER_SIZE - pos) ?
                                pos * 2 : MAX_BUFFER_SIZE;
                        if (nsz > marklimit)
                            nsz = marklimit;
                        byte nbuf[] = new byte[nsz];
                        System.arraycopy(buffer, 0, nbuf, 0, pos);
                        if (!bufUpdater.compareAndSet(this, buffer, nbuf)) {
                            // Can't replace buf if there was an async close.
                            // Note: This would need to be changed if fill()
                            // is ever made accessible to multiple threads.
                            // But for now, the only way CAS can fail is via close.
                            // assert buf == null;
                            throw new IOException("Stream closed");
                        }
                        buffer = nbuf;
                    }
        count = pos;
        int n = getInIfOpen().read(buffer, pos, buffer.length - pos);//把修饰的输入流的数组 copy 到该 缓存数组中.
        if (n > 0)
            count = n + pos;//更新缓存数组中的有效字节
    }
```

### mark  and reset
```java
/**两者配合使用，可以反复读取某一段数据*/

    public synchronized void mark(int readlimit) {
        marklimit = readlimit;
        markpos = pos;
    }
    
    public synchronized void reset() throws IOException {
    getBufIfOpen(); // Cause exception if closed
    if (markpos < 0)
        throw new IOException("Resetting to invalid mark");
    pos = markpos;
}
```

# BufferOutputStream详解
## BufferOutputStream 类签名
    public
    class BufferedOutputStream extends FilterOutputStream {}

## BufferOutputStream 成员变量
    protected byte buf[];//缓存数组
    protected int count;//数组中有效字节长度
    
## BufferOutputStream 构造方法
```java
/**共两种构造方法*/
    public BufferedOutputStream(OutputStream out) {
        this(out, 8192);
    }
    public BufferedOutputStream(OutputStream out, int size) {
    super(out);
    if (size <= 0) {
        throw new IllegalArgumentException("Buffer size <= 0");
    }
    buf = new byte[size];
}
```
## BufferOutputStream 常用方法
### void write(byte b[], int off, int len)
```java
//将指定字节数组b，写到缓存中
   public synchronized void write(byte b[], int off, int len) throws IOException {
        if (len >= buf.length) {
            flushBuffer();
            out.write(b, off, len);
            return;
        }
        if (len > buf.length - count) {
            flushBuffer();
        }
        System.arraycopy(b, off, buf, count, len);
        count += len;
    }
//---------------------------------------------
    /** Flush the internal buffer */
    private void flushBuffer() throws IOException {
        if (count > 0) {
            out.write(buf, 0, count);
            count = 0;
        }
    }
    

```

# demo

```java
package cn.kkcoder.java8;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BufferStream {

	public static void main(String[] args) throws IOException {

		String path = "fileWriteAndRead.txt"; 
		
		OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path)));
		out.write("我是世界上最帅气的人，呵呵.".getBytes());
		
		//注意，这里写完之后最好要flush一下，或者close  ,不然数据可能还在缓存中
		out.flush();
		out.close();
		
		InputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		
		int len = in.available();
		byte[] b = new byte[len];
		in.read(b);

		System.out.println(new String(b));
		
		in.close();
	}
}
```


  [1]: https://github.com/static-mkk/java8SourceLearn