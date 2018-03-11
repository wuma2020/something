# HashTable详解

标签： java8源码
> 欢迎 [start][1] 

----------

> 由于 Hashtable  不建议使用. 用到 Hashtable 的地方 应该用 `ConcurrentHashMap` 来代替，所以下面仅介绍 `Hashtable` 的特性和`与HashMap` 的 `区别`.

```java
//Hashtable类签名
public class Hashtable<K,V>
    extends Dictionary<K,V>
    implements Map<K,V>, Cloneable, java.io.Serializable {}
```


`相同点：`
    
    1. 都是以键值（<K,V>）对存储信息.
    2. 都实现了 Map 接口的操作，和默认方法.
    3. 都实现了 Cloneable 接口，可以获取克隆对象.
    4. 都实现了 Serializable 接口，实现序列化.
    
    
`不同点：`


|   不同点  |    HashMap    |  Hashtable  |
| --------   | -----:  | :----:  |
|  数据结构  |	 数组+链表+红黑树	|  数组+链表  |
|继承的类不同|	继承AbstractMap|	继承Dictionary|
|是否线程安全|	否	|是|
|性能高低|	高|	低|
|默认初始化容量|	16|	11|
|扩容方式不同|	原始容量x2|	原始容量x2 + 1|
|底层数组的容量为2的整数次幂|	要求一定为2的整数次幂|	不要求|
|确认key在数组中的索引的方法不同|	i = (n - 1) & hash;|	index = (hash & 0x7FFFFFFF) % tab.length;|
|遍历方式|	Iterator(迭代器)|	Iterator(迭代器)和Enumeration(枚举器)|
|Iterator遍历数组顺序|	索引从小到大|	索引从大到小|

> 该不同表引自此 [文章][2]


  [1]: https://github.com/static-mkk/wangkuiwu
  [2]: http://blog.csdn.net/panweiwei1994/article/details/77428710