# FilterInputStream_FilterOutputStream 

标签： java8源码

---

# 目的：

> 包装其他的流. 装饰模式. 实际上本身并没有做任何事，都是调用构造函数传进来的流来处理数据.

# FilterOutputStream源码
```java
package java.io;

public
class FilterOutputStream extends OutputStream {
    protected OutputStream out;
    public FilterOutputStream(OutputStream out) {
        this.out = out;
    }

    public void write(int b) throws IOException {
        out.write(b);
    }

    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        if ((off | len | (b.length - (len + off)) | (off + len)) < 0)
            throw new IndexOutOfBoundsException();

        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
    }

    public void flush() throws IOException {
        out.flush();
    }

    @SuppressWarnings("try")
    public void close() throws IOException {
        try (OutputStream ostream = out) {
            flush();
        }
    }
}

```

# FilterInputStream源码
```java
package java.io;
public
class FilterInputStream extends InputStream {
    protected volatile InputStream in;

    protected FilterInputStream(InputStream in) {
        this.in = in;
    }

    public int read() throws IOException {
        return in.read();
    }

    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte b[], int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    public int available() throws IOException {
        return in.available();
    }

    public void close() throws IOException {
        in.close();
    }

    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
    }

    public synchronized void reset() throws IOException {
        in.reset();
    }

    public boolean markSupported() {
        return in.markSupported();
    }
}

```

> 因为FilterInputStream 的构造方法是私有的，所以只能用其子类新建一个对象.

# 代码片段
```java
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    bout.write("我是外星人，来自China".getBytes());
    FilterOutputStream fout = new FilterOutputStream(new  BufferedOutputStream(bout));
    
    byte[] buf = "我是外星人，来自China".getBytes();
    FilterInputStream in = new BufferedInputStream(new ByteArrayInputStream(buf));
		
```

