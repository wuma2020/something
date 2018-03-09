# Map构架详解

标签（空格分隔）： java8源码 Map构架

---

> 欢迎 [start][1] 跪求 [start][2]
[<img src="http://www.kkcoder.cn:8080/blog/comment/gitPic.png"/>][3]

---
# map构架图
![Map构架图][4]


> map结构简介

 1. `Map接口`:以 `键值对` 储存内容的映射接口.
 2. `SortedMap接口`:继承Map接口，所以其也是以`键值对`储存信息的.另外，通过其内部的`comparator()`方法来对map的键值对进行排序.
 3. `NavigableMap接口`:继承自SortedMap接口，其有一系列的导航方法.如：`获取大于/等于某对象的键值对` ···等方法
 4. `AbstractMap抽象类`:实现了Map接口的大部分方法.避免重复编码.
 5. `TreeMap类`:继承AbtractMap类并实现NavigableMap接口中的方法.所以其是`有序的键值对`.
 6. `HashMap类`:继承自AbstractMap类，是`无序的键值对`.
 7. `WeakHashMap类`:同样是`无序的键值对`，但是其键是`弱键`.
 8. `HashTable类`:继承自Directionary类，并自己实现Map接口的类,其特点是：`无序的键值对，并且线程安全`.
 

> 下面简单介绍一下上图中的各个接口内容

# Map接口

> 签名如下：`public interface Map<K,V> {}`

Map接口的特点:

 1. Map中的键值对不能包含重复的键,每个键只能对应一个值.并且Map中的键可为null，且最多一个.值可重复，并且可以为null.

        Map<String,String> sMap = new HashMap<>(); 
		sMap.put(null, "ss");
		sMap.put("key2", null);
		sMap.put(null, "ss2");
		System.out.println(sMap.keySet()+":"+sMap.values());
		
    `控制台输出：[null, key2]:[ss2, null]`

 2. Map中提供了以`键值集` `键集` `值集` 来查看map内容.
 3. `entrySet()` 方法返回键值集对象.
 4. 也提供了许多其他访问，修改的方法。后续会详解.

# Map.Entry接口

 1. Map.Entry接口是Map接口的内部接口.
 2. Map通过 `entrySet()` 获取Map.Entry的`键值对集合`，从而通过该集合实现对键值对的操作。

# AbstractMap抽象类

> 类签名 :  `public abstract class AbstractMap<K,V> implements Map<K,V> {}`

 1. AbstractMap抽象类主要是实现Map接口中的方法.详细方法请看`HashMap`中的详解

# SortedMap接口

 1. `SortedMap的排序`：`自然排序` 或者 `用户指定比较器`。 插入有序 SortedMap 的所有元素都必须实现 Comparable 接口（或者被指定的比较器所接受）。
 2. `SortedMap`实现类必须提供四种
   
         (01) void（无参数）构造方法，它创建一个空的有序映射，按照键的自然顺序进行排序。
        (02) 带有一个 Comparator 类型参数的构造方法，它创建一个空的有序映射，根据指定的比较器进行排序。
        (03) 带有一个 Map 类型参数的构造方法，它创建一个新的有序映射，其键-值映射关系与参数相同，按照键的自然顺序进行排序。
        (04) 带有一个 SortedMap 类型参数的构造方法，它创建一个新的有序映射，其键-值映射关系和排序方法与输入的有序映射相同。无法保证强制实施此建议，因为接口不能包含构造方法。
  
# NavigableMap接口

> `接口签名`： public interface NavigableMap<K,V> extends SortedMap<K,V> { }


     NavigableMap接口特点

 1. 返回小于、小于等于、大于等于、大于指定键的Map.Entry对象.
 2. 返回小于、小于等于、大于等于、大于指定键的键集
 3. 返回正序/反序的键集.
 4. 获取键值对的子集.

# Dictionary抽象类

> 类签名：`public abstract class Dictionary<K,V> {}`

        其也是 键值对 的类，类似于AbstractMap，也提供了常用的一些方法.








 


  [1]: https://github.com/static-mkk/wangkuiwu
  [2]: https://github.com/static-mkk/wangkuiwu
  [3]: https://github.com/static-mkk/wangkuiwu
  [4]: http://wangkuiwu.github.io/media/pic/java/collection/collection09.jpg