# HashSet 详解

标签： java8源码

---

> 思路：内部就是用HashMap来存储数据.把set的值存放在map的key中.Haset 可以简单的理解为 HashMap 的key的集合. 因为其，依靠HashMap来完成各种操作.

###特点
    1.允许使用null，最多一个
    2.不保正元素顺序
    3.非同步的
    4.Iterator会触发快速失败机制

###签名
```java
public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable{}
```

###重要成员变量
    
     private transient HashMap<E,Object> map;
     private static final Object PRESENT = new Object();//用来放到HashMap的值中

###构造方法
```java
//空构造方法
    public HashSet() {
        map = new HashMap<>();
    }
//初始化大小的构造方法    
    public HashSet(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }
    
    public HashSet(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }
    
     HashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
    }
    
    public HashSet(Collection<? extends E> c) {
        map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
        addAll(c);
    }
```

`由此可以看出，HashSet其内部完全是依靠HashMap的来存储数据的.如下面的具体方法介绍也看的出这一点.`

###add(Object o);方法
```java
  public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
```
`由此看出，其实调用了map的put来添加元素,并且把值放在了map的key的位置`

###remove(Object o);方法
```java
    public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
    }
```

###Iterator();方法
```java
  public Iterator<E> iterator() {
        return map.keySet().iterator();
    }
```











