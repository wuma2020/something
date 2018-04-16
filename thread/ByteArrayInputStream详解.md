# ByteArrayInputStream详解

---

# 思路
> `ByteArrayInputStream` 必须在构造函数中传入byte[]，用于初始化其内部成员变量 `byte[] buf`，然后利用其 read() 方法，把该流里的数据(buf)输出到指定数组中.

# 目的
> 从该ByteArrayInputStream流中获取数据.

# 成员变量
    protected byte buf[];//该输入流中的数据
    protected int pos;//已经读取的字节索引 + 1（即，下一个要读的字节索引）
    protected int count;//buf的长度
    protected int mark = 0;//当前读取字节索引

# 构造方法
```java
/**两种构造方法，都用于传入数据到该输出流中.即，输入流在新建对象时，就有数据.也许是个null引用*/
//1.将 成员变量引用buf指向传入的byte[] buf
public ByteArrayInputStream(byte buf[]) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }
//2.将 成员变量引用buf指向传入的byte[] buf,并把pos和mark设置成offset，并设置cout即设置准确的成员变量buf的长度
public ByteArrayInputStream(byte buf[], int offset, int length) {
        this.buf = buf;
        this.pos = offset;
        this.count = Math.min(offset + length, buf.length);
        this.mark = offset;
    }

```
# 常用方法
## int read(byte b[], int off, int len)
```java
//将buf读入到 指定byte数组的指定开始位置（off）以及指定长度（len）
   public synchronized int read(byte b[], int off, int len) {
        if (b == null) {//读入的数组不能为null
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {//正确设置起始位置（off）和长度（len）
            throw new IndexOutOfBoundsException();
        }

        if (pos >= count) {//要读的字节所以大于buf长度（数组越界了）
            return -1;
        }

        int avail = count - pos;
        if (len > avail) {//指定长度大于 buf 未读取的长度，修改len为未读取的长度
            len = avail;
        }
        if (len <= 0) {
            return 0;
        }
        System.arraycopy(buf, pos, b, off, len);//把buf中的数据从 pos位置开始，向b中赋值.b的起始位置off，长度为len.
        pos += len;//设置要读取的位置（pos）为pos = pos + len.
        return len;
    }
```
## int read();
```java
  public synchronized int read() {
/*如果要读的字节索引pos<字节数组长度，则返回索引为pos的字节数组的值.否则，返回-1*/
        return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }
```
## void mark(int readAheadLimit)

```java
//把 pos 赋值 给mark    参数无意义
    public void mark(int readAheadLimit) {
        mark = pos;
    }
```
## void reset()
```java
//把 mark 赋值给 pos 
//接合mark方法，可以反复读取某一段字节数组
    public synchronized void reset() {
        pos = mark;
    }
```
## long skip(long n)；
```java
//跳过n个字节，把pos的值向后移动n
    public synchronized long skip(long n) {
        long k = count - pos;//剩余未读取的数组长度
        if (n < k) {
        //如果，跳过的数n < 剩余数组长度k，则把n 赋值给 k
        //如果k<0,则k=0;否则,k=n.
            k = n < 0 ? 0 : n;
        }
        pos += k;//把pos的值向后移动n
        return k;//返回跳过的个数
    }
```



