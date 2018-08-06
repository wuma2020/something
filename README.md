


| 集合 | I/O |多线程 | JUC锁|java基础知识点整理|jvm |spring 相关 |MYSQL |shiro|检索
| :----: | :----: | :----: | :----: | :----: | :----: | :----: |:----: | :----: | :----: | 
| [集合](#集合) | [I/O](#I/O)|[多线程](#多线程) | [JUC锁](#JUC锁)|[java基础知识点整理](#java基础知识点整理) |[jvm ](#jvm )| [spring相关](#spring相关)|[MYSQL](#MYSQL)|[shiro](#shiro)|[检索](#检索)|

---

## java源码学习
### 集合

  - [java集合介绍][1]
  - [ArrayList详解][2]
  - [fast-fail机制详解][3]
        - [fast-fail测试demo][4]
  - [Linkedlist详解][5]
  - [Vector详解][6]
  - [Stack详解][7]
  - [Map构架][8]
  - [HashMap详解][9]
     - [HashMap put逻辑总结][10]
  - [HashMap与HashTable比较][11]
  - [HashSet介绍][12]

### I/O 
- [PipedOutputStream and PipedInputStream 详解][13]
- [BufferInputStream_BufferOutputStream 详解][14]
- [ByteArrayOutputStream介绍][15]
- [FilterInputStream_FilterOutputStream介绍][16]
- [FileOutputStream_FileInputStream demo][17]
- [FileDescriptor demo][18]

### 多线程

（整理自《java多线程编程核心》部分加了超链） 
- [多线程基础概念][19]
- [synchronized关键字][20] 
- [线程等待与唤醒][21]
- [interrupt 终止线程介绍][22]
    - [interrupt demo][23]
    - [interrupt flag 设置阻断demo][24]
- [生产消费线程demo][25]
- [synchronized非this锁][26]  
- [volatile介绍][27]
- [join介绍][28]
- [Condition的使用以及其生产消费线程示例][29] 
- [condition实现线程顺序执行][30]

### JUC锁
-   [JUC之公平锁详解1][31]
-   [LockSupport 介绍][32]
-   [ReentrantReadWriteLock 介绍][33]

---

# java基础知识点整理

1. [java基础知识点整理][34]

# java 反射
 1. [java reflect 使用介绍][35]
 2. [jdk 动态代理][36]

# jvm 
    （整理自《深入理解java虚拟机》 周志明·第二版）
1. [第二章-java内存区域与内存溢出异常][37]
2. [第六章-类文件结构][38]
3. [第七章-虚拟机-类加载机制][39]

# spring相关
1. [spring IOC 简易版][40]  非常推荐学习

# MYSQL

 1. mysql 
 
# Redis
1. [windows下redis的安装以及redisDesktopManager的安装及连接教程][41] 

# shiro
 1. [shiro 自定义 realm demo][42]
 2. [shiro 整合 spring][43] 
 3. [shiro 会话管理][44]

# 检索

1. 检索知识点

---

         better  is enough!

---


前面有些使用 `.java` 文件写的，后续会改成 `markdown` 语法的文本文件,这样看起来会比较清晰.


> 另外还要附上 [wangkuiwu][45] 的blog地址,其基于 java 1.6 或 1.7 的源码做分析的.几乎是讲解了整个源码.内容更全.

> 另附 CSDN [潘威威][46] 的源码解析 blog



        
        
            

        


  [1]: https://github.com/static-mkk/java8SourceLearn/blob/master/%E9%9B%86%E5%90%88/JavaCollection_.java
  [2]: https://github.com/static-mkk/java8SourceLearn/blob/master/%E9%9B%86%E5%90%88/ArrayListDetail.java
  [3]: https://github.com/static-mkk/java8SourceLearn/blob/master/%E9%9B%86%E5%90%88/fail_fast_detail.md
  [4]: https://github.com/static-mkk/java8SourceLearn/blob/master/%E9%9B%86%E5%90%88/fail_fast_demo.java
  [5]: https://github.com/static-mkk/java8SourceLearn/blob/master/%E9%9B%86%E5%90%88/LinkedListDeatil.java
  [6]: https://github.com/static-mkk/java8SourceLearn/blob/master/%E9%9B%86%E5%90%88/VectorDetail.java
  [7]: https://github.com/static-mkk/java8SourceLearn/blob/master/%E9%9B%86%E5%90%88/StackDetail.java
  [8]: https://github.com/static-mkk/java8SourceLearn/blob/master/%E9%9B%86%E5%90%88/Map%E6%9E%84%E6%9E%B6%E8%AF%A6%E8%A7%A3.md
  [9]: https://github.com/static-mkk/java8SourceLearn/blob/master/%E9%9B%86%E5%90%88/HashMap%E8%AF%A6%E8%A7%A3.md
  [10]: https://github.com/static-mkk/java8SourceLearn/blob/master/%E9%9B%86%E5%90%88/HashMap%E5%8E%9F%E7%90%86%E6%80%BB%E7%BB%93.md
  [11]: https://github.com/static-mkk/java8SourceLearn/blob/master/%E9%9B%86%E5%90%88/HashTable_HashMap%E6%AF%94%E8%BE%83.md
  [12]: https://github.com/static-mkk/java8SourceLearn/blob/master/%E9%9B%86%E5%90%88/HashSet%E4%BB%8B%E7%BB%8D.md
  [13]: https://github.com/static-mkk/java8SourceLearn/blob/master/I.O/PipedInputStream_PipedOutputStream.md
  [14]: https://github.com/static-mkk/java8SourceLearn/blob/master/I.O/BufferInputStream_BufferOutputStream.md
  [15]: https://github.com/static-mkk/java8SourceLearn/blob/master/I.O/ByteArrayOutputStream%E8%AF%A6%E8%A7%A3.md
  [16]: https://github.com/static-mkk/java8SourceLearn/blob/master/I.O/FilterInputStream_FilterOutputStream.md
  [17]: https://github.com/static-mkk/java8SourceLearn/blob/master/I.O/FileOutputStream_FileInputStream_demo.md
  [18]: https://github.com/static-mkk/java8SourceLearn/blob/master/I.O/FileDescriptor_demo.md
  [19]: https://github.com/static-mkk/java8SourceLearn/blob/master/thread/%E5%A4%9A%E7%BA%BF%E7%A8%8B_%E5%9F%BA%E7%A1%80%E6%A6%82%E5%BF%B5.md
  [20]: https://github.com/static-mkk/java8SourceLearn/blob/master/thread/synchronized%E5%85%B3%E9%94%AE%E5%AD%97.md
  [21]: https://github.com/static-mkk/java8SourceLearn/blob/master/thread/%E7%BA%BF%E7%A8%8B%E7%AD%89%E5%BE%85%E4%B8%8E%E5%94%A4%E9%86%92.md
  [22]: https://github.com/static-mkk/java8SourceLearn/blob/master/thread/Interrupt%E7%BB%88%E6%AD%A2%E7%BA%BF%E7%A8%8B%E4%BB%8B%E7%BB%8D.md
  [23]: https://github.com/static-mkk/java8SourceLearn/blob/master/thread/InterruptDemoOne.java
  [24]: https://github.com/static-mkk/java8SourceLearn/blob/master/thread/FlagInterruptThread.java
  [25]: https://github.com/static-mkk/java8SourceLearn/blob/master/thread/%E7%94%9F%E4%BA%A7%E6%B6%88%E8%B4%B9%E7%BA%BF%E7%A8%8Bdemo.md
  [26]: https://mp.csdn.net/mdeditor/79843747
  [27]: https://mp.csdn.net/mdeditor/79901661
  [28]: https://mp.csdn.net/mdeditor/79920177
  [29]: https://github.com/static-mkk/java8SourceLearn/blob/master/thread/Condition%E7%9A%84%E4%BD%BF%E7%94%A8%E4%BB%A5%E5%8F%8A%E5%85%B6%E7%94%9F%E4%BA%A7%E6%B6%88%E8%B4%B9%E7%BA%BF%E7%A8%8B%E7%A4%BA%E4%BE%8B.md
  [30]: https://github.com/static-mkk/java8SourceLearn/blob/master/thread/condition%E5%AE%9E%E7%8E%B0%E7%BA%BF%E7%A8%8B%E9%A1%BA%E5%BA%8F.md
  [31]: https://github.com/static-mkk/something/blob/master/thread/JUC%E4%B9%8B%E5%85%AC%E5%B9%B3%E9%94%81%E8%AF%A6%E8%A7%A31.md
  [32]: https://github.com/static-mkk/something/blob/master/thread/JUC/LockSupport.md
  [33]: https://github.com/static-mkk/something/blob/master/thread/JUC/ReentrantReadWriteLock.md
  [34]: https://github.com/static-mkk/something/blob/master/review/review_javapart.md
  [35]: https://github.com/static-mkk/something/blob/master/review/java_reflect.md
  [36]: https://github.com/static-mkk/something/blob/master/review/jdk%E5%8A%A8%E6%80%81%E4%BB%A3%E7%90%86.md
  [37]: https://github.com/static-mkk/something/blob/master/jvm/%E7%AC%AC%E4%BA%8C%E7%AB%A0-java%E5%86%85%E5%AD%98%E5%8C%BA%E5%9F%9F%E4%B8%8E%E5%86%85%E5%AD%98%E6%BA%A2%E5%87%BA%E5%BC%82%E5%B8%B8.md
  [38]: https://github.com/static-mkk/something/blob/master/jvm/%E7%AC%AC%E5%85%AD%E7%AB%A0-%E7%B1%BB%E6%96%87%E4%BB%B6%E7%BB%93%E6%9E%84.md
  [39]: https://github.com/static-mkk/something/blob/master/jvm/%E7%AC%AC%E4%B8%83%E7%AB%A0-%E8%99%9A%E6%8B%9F%E6%9C%BA-%E7%B1%BB%E5%8A%A0%E8%BD%BD%E6%9C%BA%E5%88%B6.md
  [40]: https://github.com/static-mkk/something/tree/master/spring/spring-ioc
  [41]: https://github.com/static-mkk/something/blob/master/redis/windows%E4%B8%8Bredis%E7%9A%84%E5%AE%89%E8%A3%85%E4%BB%A5%E5%8F%8Aredisdesktop%E7%9A%84%E5%AE%89%E8%A3%85%E8%BF%9E%E6%8E%A5.md
  [42]: https://github.com/static-mkk/something/tree/master/shiro_about/shiro-customRealm
  [43]: https://github.com/static-mkk/something/tree/master/shiro_about/shiro-spring
  [44]: https://github.com/static-mkk/something/tree/master/shiro_about/shiro-sessionmanager
  [45]: http://wangkuiwu.github.io
  [46]: http://blog.csdn.net/panweiwei1994