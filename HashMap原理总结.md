# HashMap原理总结

标签： java8源码

---
> 1.`Node<K,V>[] table;` 这个就是存储数据的成员变量 table.

> 2.`Node<K,V>` 内部类构造：
```java
static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
        
//        ······具体的方法实现没有贴上来.
        
}
```

> 3.下面讲述这个`put(K key,V value)`逻辑.

    put(K key  ,V value);
     
     1. 通过 key 的 hash 值，经过特定运算，得到一个 int 类型的值，并作为 table 数组的 下标 index.
        i. 这里需要注意的是，两个不同的 key 可能经过特定运算后，会得到相同的值。所
        以，table数组 类型是一个 Node<K,V> 的链表结构.
    
     2.计算得到下标值(index)之后.进行判断.
        i.若table[index] == null
            -直接新建一个Node<K,V> 对象 e ，并赋值.即，table[index] = e;
        ii.若table[index] != null 
            （说明该key计算出的下标index对应的值，已经存在.造成的原因是:
             1.两个相同的key，计算后，得到相同index，造成 table[index] != null.
             2.两个不同的key经过特定运算之后，得到相同的index,因此，table[index] 
               != null.）
            
            -若是情况1<key相同，index相同>,则会把该index对应的Node的value
             用新传进来的value进行覆盖.
             
            -若是情况2<key不同，index相同>,则会判断该index下的Node链表
             是否为 treeNode.
                *若该Node是treeNode，则直接红黑树插入键值对.
                *若该Node不是treeNode，则遍历列表.
                    ^如果该链表长度大于8，则把该链表改成红黑树插入键值对.
                    ^如果该链表长度不大于8，直接插入。若key存在，则直接覆盖value
     
     3.把map的size + 1； 并判断是否是否需要扩容                
        i.需要，则进行扩容，结束.
        ii.不需要,则结束.
                    
                    
                    
            

