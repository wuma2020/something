# java原生Logger使用

标签： java

---

# demo
```java
package cn.kkcoder.demos;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerTest {

	public static void main(String[] args) {
		//从类Logger中获取一个logger对象，并且名称为 "logone"
		Logger log = Logger.getLogger("logone");
		
		//设置log输出的等级
		log.setLevel(Level.ALL);
		ConsoleHandler con = new ConsoleHandler();//新建一个控制台处理器
		con.setLevel(Level.ALL);
//		log.addHandler(con);//把新的控制台handler添加到log的处理器中
		
		/*
		
		获取路径的不太好的方法： （不推荐使用）
		
		String basepath = Class.class.getClass().getResource("/").getFile();
		basepath = basepath.substring(1, basepath.length()-4);
		String threadPath = Thread.currentThread().getContextClassLoader().getResource("").toString();
		
		返回：
		
			file:/E:/eclipse_workplace/javase/bin/
			
		*/
		
		try {

			
			/*	一般项目的绝对路径，用下面这两个方法：（推荐使用）
			 * 	
			 * 		new File("").getAbsolutePath().toString();
			 * 		System.getProperty("user.dir");
			 * 
			 * 	返回： 
			 * 		E:\eclipse_workplace\javase
			 * 
			 * */
			String filePath = new File("").getAbsolutePath().toString();
//			String projectPath = System.getProperty("user.dir"); 			//E:\eclipse_workplace\javase
			
			
			FileHandler fileHandle = new FileHandler(filePath + "/log");
			//设置FileHandler 的格式化 的样式
			/*
			 * 
			    （1）new SimpleFormatter()的样式如下：
				Apr 01, 2018 5:39:47 PM cn.kkcoder.demos.LoggerTest main
				警告: 警告信息
				
				
				（2）new XmlFormatter()样式如下：
				<?xml version="1.0" encoding="UTF-8" standalone="no"?>
				<!DOCTYPE log SYSTEM "logger.dtd">
				<log>
				<record>
				  <date>2018-04-01T17:42:36</date>
				  <millis>1522575756849</millis>
				  <sequence>0</sequence>
				  <logger>logone</logger>
				  <level>WARNING</level>
				  <class>cn.kkcoder.demos.LoggerTest</class>
				  <method>main</method>
				  <thread>1</thread>
				  <message>警告信息</message>
				</record>
				
				...
			
			*/ 
//			fileHandle.setFormatter(new SimpleFormatter());
			
			
			log.addHandler(fileHandle);//添加handler
			
			/*log的默认等级为info*/
			
			log.severe("严重的信息");
			log.severe("严重信息");
			log.warning("警告信息");
			log.info("一般信息");
			log.fine("细微的信息");
			log.finest("最详细的信息");
			
		} catch (SecurityException | IOException e) {
			throw new RuntimeException("输出日子错误");
		}
		
	}
	
}

```

---

# Level 级别
    Level ALL
    Level SEVERE
    Level WARNING
    Level INFO
    Level CONFIG
    Level FINE
    Level FINER
    Level FINEST
    Level OFF


