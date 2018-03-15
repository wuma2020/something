# FileOutputStream_FileInputStream demo

标签： java8源码

---
>  欢迎 [star][1] 

# 思路

> `FileOutputStream` 文件输出流：用于向一个 文件系统 中写数据. 字节输出流.
`FileInputStream` 文件输入流：用于从一个 文件系统 中读取数据. 字节输入流.

# 源码
> 由于其主要方法是 `native `修饰的，也就是 不是用java写的，所以就不分析了.

# demo
```java
package cn.kkcoder.java8;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOutput_InputStream {
	
	static final String sPath = "fileWriteAndRead.txt";//设置路径
	
	public static void main(String[] args) throws IOException {

		testWrite();//FileOutputStream写文件
		testRead();//FileInputStream 读文件
	}

	private static void testRead() throws IOException {
		FileInputStream in = new FileInputStream(sPath);
		int len = in.available();//输入流中的有效字节
		byte[] b = new byte[len];
		in.read(b); //向字节数组 b中写入数据
		System.out.println(new String(b,0,b.length));
		in.close();
		
	}

	private static void testWrite() throws IOException {
		File file = new File(sPath); 
		String fPath = file.getCanonicalPath()+ file.getPath();
		System.out.println(fPath);//绝对路径
		FileOutputStream out = new FileOutputStream(file);
		String sContext = "我是太空人，来自china";
		out.write(sContext.getBytes());
		out.flush();
		out.close();
	}

}

```


  [1]: https://github.com/static-mkk/java8SourceLearn