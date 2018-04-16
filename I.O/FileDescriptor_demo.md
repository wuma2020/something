# FileDescriptor demo


---

# demo
```java
package cn.kkcoder.java8;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileDescriptorTest  {

	public static void main(String[] args) throws IOException {

		FileOutputStream out = new FileOutputStream(FileDescriptor.out);//标准输出(屏幕)的描述符
		
		out.write("请输入姓名：".getBytes());
		
		FileInputStream in = new FileInputStream(FileDescriptor.in);//标准输入(键盘)的描述符
		
		byte[] b = new byte[1024];
		in.read(b);
		out.write(b);
		
		
		out.close();
		in.close();
	}

}

```




