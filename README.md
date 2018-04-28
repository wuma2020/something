# java8源码学习

----
    2018/4/28 日
    之前太乱了，现在整理了一下。持续更新。。。
    keep learn....
    
---


- 集合

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
    
- I/O 
    - [PipedOutputStream and PipedInputStream 详解][13]
    - [BufferInputStream_BufferOutputStream 详解][14]
    - [ByteArrayOutputStream介绍][15]
    - [FilterInputStream_FilterOutputStream介绍][16]
    - [FileOutputStream_FileInputStream demo][17]
    - [FileDescriptor demo][18]

- 多线程    
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



---
    2018/4/28 之前

---
> 主要是通过讲解某个`对象实例`具体的`方法`，来深入了解该类的原理.

因为，如果通篇讲把所有的源码解读，实在是一件非常耗时，并且不易理解.

所以我决定通过讲解常用方法的具体实现，来学习java8的源码.

`好处是 `：

    1. 熟悉常用api
    2. 详细了解某个方法的具体实现
    3. 详细了解该类的原理.
    4. 容易理解.

前面有些使用 `.java` 文件写的，后续会改成 `markdown` 语法的文本文件,这样看起来会比较清晰.


> 另外还要附上 [wangkuiwu][31] 的blog地址,其实基于 java 1.6的源码做分析的.几乎是讲解了整个源码.内容更全.

> 另附 CSDN [潘威威][32] 的源码解析 blog



        
        
            

        


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
  [31]: http://wangkuiwu.github.io
  [32]: http://blog.csdn.net/panweiwei1994