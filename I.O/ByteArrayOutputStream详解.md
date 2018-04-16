# ByteArrayOutputStream详解

标签： java8源码

---

## 思路：
> 通过ByteArrayOutputStream 的内部 成员变量  `byte buf[];`来储存数据. 最大值 `MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8`

## 目的：
> 向ByteArrayOutputStream 流中输出数据.

## 特点
> 线程安全的

# 成员变量
        protected byte buf[];//存储数据的byte数组
        private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;//数组的最大值
        protected int count;//数组中的有效字节个数

# 构造方法

```java
/**构造方法总共有两种*/

//无参构造器（默认数组大小为32）
    public ByteArrayOutputStream() {
        this(32);//调用有参构造器，并传值32
    }
//有参构造器（设置数组大小）
     public ByteArrayOutputStream(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: "
                                               + size);
        }
        buf = new byte[size];//初始化byte数组的大小为size
    }
```
# 常用方法

## write(byte b[], int off, int len) ;
```java
//把含有数据的byte数组传递赋值给成员变量 buf.达到输出数据的目的.
   public synchronized void write(byte b[], int off, int len) {
        if ((off < 0) || (off > b.length) || (len < 0) ||
            ((off + len) - b.length > 0)) {
            throw new IndexOutOfBoundsException();
        }
        ensureCapacity(count + len);//判断 加入 len 长度后，数组大小是否足够。如果不够，则增加数组大小.
        System.arraycopy(b, off, buf, count, len);//把b中的数据赋值到buf中,从count开始复制，长度为 len.
        count += len;//修改 数组长度.
    }
```

### ensureCapacity(int minCapacity) ；
```java
/*判断minCapacity 是否 大于buf 的长度，若是，则增大buf长度*/
private void ensureCapacity(int minCapacity) {
        if (minCapacity - buf.length > 0)
            grow(minCapacity);//增加buf长度
    }

```

### grow(int minCapacity)；
```java
/*增加buf长度*/
  private void grow(int minCapacity) {
        int oldCapacity = buf.length;
        int newCapacity = oldCapacity << 1;//现有数组大小 X2
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;//若果 X2之后，还是小于minCapacity，则把数组大小改为minCapacity
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);//判断minCapacity是否大于MAX_ARRAY_SIZE最大值，若是，则数组大小改为Integer的最大值。否则，设置为MAX_ARRAY_SIZE
        buf = Arrays.copyOf(buf, newCapacity);//把buf引用指向新的数组（大小为newCapacity，内容为buf中的值）
    }
```

## writeTo(OutputStream out)；
```java
/**把该流中的数据，写入到指定的OutputStream流中*/
  public synchronized void writeTo(OutputStream out) throws IOException {
        out.write(buf, 0, count);//调用指定字节输出流out的write方法，把buf写入流 out 中
    }
```
## toString(String charsetName)；
```java
/**用指定编码把buf中的字节数据转换成String对象并返回*/
 public synchronized String toString(String charsetName)
        throws UnsupportedEncodingException
    {
        return new String(buf, 0, count, charsetName);
    }
```
> toString的默认编码格式是：IOS-8859-1
## toString();
```java
//toString的默认编码格式是：IOS-8859-1
 public synchronized String toString() {
        return new String(buf, 0, count);
    }
```
## byte toByteArray()[];
```java
   public synchronized byte toByteArray()[] {
        return Arrays.copyOf(buf, count);//返回一个新的byte数组，内容长度和buf相同
    }
```
