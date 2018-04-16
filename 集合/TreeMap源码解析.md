# TreeMap 源码解析

标签： java8源码

---

`TreeMap签名`       

    public class TreeMap<K,V>
    extends AbstractMap<K,V>
    implements NavigableMap<K,V>, Cloneable, java.io.Serializable{}

> TreeMap 具有以下特点

    1.有序
    2.可复制
    3.可序列化传输
    4.键-值都不能为null
    5.非同步
    6.高效
    
> TreeMap 内部实现 `利用红黑树来存储信息.`


----------

待我研究研究数据结构之后，再回来详细叙述一下 TreeMap 的内部实现原理.

