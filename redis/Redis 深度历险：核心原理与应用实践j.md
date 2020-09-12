
# 第一章 redis基础类型

## redis的5中基本类型

>  string（字符串）、list（列表）、set（集合）、hash（集合）、zset（有序集合）


redis的所有数据结构都是以唯一的key的字符串为名称，然后通过唯一的key来获取相应的value数据。不同的数据结构的差异就在于value的结构不一致。存储的方式就是把用户信息结构体使用json序列化成字符串，然后将序列化之后的字符串塞进到redis中。redis的字符串是动态字符串，是可以修改的，有点类似java的arraylist.动态扩展。最大长度为512M.



1. String（字符串）：

```bash
键值对
127.0.0.1:6379> set name mkk
OK
127.0.0.1:6379> get name
"mkk"
127.0.0.1:6379> exists name
(integer) 1
127.0.0.1:6379> del name
(integer) 1
127.0.0.1:6379> exists name
(integer) 0
127.0.0.1:6379> get name
(nil)
127.0.0.1:6379> 

# 批量键值对
127.0.0.1:6379> set name1 name1
OK
127.0.0.1:6379> set name2 name2
OK
127.0.0.1:6379> mget name1 name2 name3
1) "name1"
2) "name2"
3) (nil)
127.0.0.1:6379> mset name5 name5value name6 name6value
OK
127.0.0.1:6379> mget name5 name6 name7
1) "name5value"
2) "name6value"
3) (nil)
127.0.0.1:6379> 
```

-  过期和set命令扩展

过期应用场景：对key设置过期时间，到点自动删除，这个功能常用来控制缓存的失效时间。该自动删除机制比较复杂。

```
127.0.0.1:6379> set mkk codehole
OK
127.0.0.1:6379> get mkk "codehole"
(error) ERR wrong number of arguments for 'get' command
127.0.0.1:6379>
127.0.0.1:6379> expire mkk 3  #设置key为mkkk的过期时间为3秒
(integer) 1
127.0.0.1:6379>
127.0.0.1:6379> get mkk
(nil)
127.0.0.1:6379> 
127.0.0.1:6379> setex mkkk 7 valuemkkk   # setex 等同于set 和 exprise
OK
127.0.0.1:6379> get mkkk
"valuemkkk"
127.0.0.1:6379>
127.0.0.1:6379> get mkkk
(nil)
127.0.0.1:6379> 


127.0.0.1:6379> setnx notex 11  #setnx表示，如果不存在key为notex的就设置对象，如果存在就不设置
(integer) 1
127.0.0.1:6379> get notex
"11"
127.0.0.1:6379> setnx notex 11
(integer) 0
127.0.0.1:6379>  
```

- 计数



2. 列表list

这里的list相当于java中的linkedlist，这里是链表结构。不是数组，意味着其插入和删除比较快，索引定位比较慢。当链表弹出最后一个元素后，该结构会被删除，内存被回收。
该结构常用来做异步队列使用。将需要延后处理的任务的结构体序列化成字符串塞进redis列表，另一个线程进行轮询数据处理。

其底层使用快速链表【待补】

```bash
127.0.0.1:6379> rpush books b1 b2 b3
(integer) 3
127.0.0.1:6379> lindex books b2   #错误
(error) ERR value is not an integer or out of range
127.0.0.1:6379> lindex books 2    # 查找index为2的列表的值，从0开始
"b3"
127.0.0.1:6379>
127.0.0.1:6379> lrange books 0 -1  # 获取  【0，-1】返回内的数据  -号表示倒数第几个
1) "b1"
2) "b2"
3) "b3"
127.0.0.1:6379> lrange books 1 -1
1) "b2"
2) "b3"
127.0.0.1:6379> lrange books 2 0
(empty list or set)
127.0.0.1:6379> lrange book 2 -3

```

3. hash（字典）

redis中的hash和java中的hashmap的结构类似，是无序字典。内部结构是数组+链表的二维结构。redis中的hash的值只能是字符串。redis的rehash的方式和java的hashmap的不同。hashmap是一次性全部hash。
hash结构也可以用来存储用户信息，不同于字符串一次性需要序列化整个对象，hash可以对用户结构的每个字段进行单独的存储。这样可以值获取用户信息下的部分内容。节省网络流量。

```hash
127.0.0.1:6379> hset mit java "xiao ming"
(integer) 1
127.0.0.1:6379> hget mit java
"xiao ming"
127.0.0.1:6379> HGETALL mit
1) "java"
2) "xiao ming"
127.0.0.1:6379>
```

4. set(集合)

redis的集合相当于java中的hashset，它内部的键值对是无须的，唯一的。他的内部实现相当于一个特殊的字典，字典中的所有的value都是一个NULL.set有去重功能。可以用来春初中奖用户的ID。

```
127.0.0.1:6379> sadd mitset xioaming   # 添加集合mitset一个元素xiaoming
(integer) 1
127.0.0.1:6379> sadd mitset xiaohong
(integer) 1
127.0.0.1:6379> sadd mitset xiaobai xiaolan # 批量添加
(integer) 2
127.0.0.1:6379> smembers mitset    # 查询集合mitset的所有元素
1) "xioaming"
2) "xiaolan"
3) "xiaohong"
4) "xiaobai"
127.0.0.1:6379> sismember mitset xiaohong   # 查询集合mitset是否存在值xiaoming
(integer) 1
127.0.0.1:6379> scard mitset   # 查询集合mitset的元素个数
(integer) 4
127.0.0.1:6379> spop mitset    # 弹出集合mitset的一个元素
"xioaming"
127.0.0.1:6379> scard mitset
(integer) 3
127.0.0.1:6379> 

```


5. zset(有序列表)
类似于java的sortedset和hashmap的结合体，既能保证value的唯一性，又能进行排序，内部通过跳跃列表的数据结构实现。跳跃链表【待补】。其维护一个score，代表这个排序的权重。
可以用在，展示粉丝列表，value值是粉丝id，score是关注时间。我们可以对粉丝列表按关注时间排序。还可以用来存储学生成绩，value是学生id，score是他们的考试成绩。

```bash

127.0.0.1:6379> zadd zmit  9.0 "xiaohong"   # 向有序列表zmit中添加权重为9.0的值为“xiaohong”的元素
(integer) 1
127.0.0.1:6379> zadd zmit 9.2 "xiaobai"
(integer) 1
127.0.0.1:6379>
127.0.0.1:6379> zadd zmit 8.0 "xiaolang"
(integer) 1
127.0.0.1:6379> zrange zmit
(error) ERR wrong number of arguments for 'zrange' command
127.0.0.1:6379> zrange zmit 0 -1
1) "xiaolang"
2) "xiaohong"
3) "xiaobai"
127.0.0.1:6379>
127.0.0.1:6379>
127.0.0.1:6379> zrevrange zmit 0 -1
1) "xiaobai"
2) "xiaohong"
3) "xiaolang"
127.0.0.1:6379> zrem zmit xiaobai
(integer) 1
127.0.0.1:6379> zrange 0 -1
(error) ERR wrong number of arguments for 'zrange' command
127.0.0.1:6379> zrange zmit 0 -1
1) "xiaolang"
2) "xiaohong"


```


容器行数据结构的通用规则

1. create if not exist 

如果容器不存在，就创建

2. drop if no elements

如果容器中元素为空时，就清楚容器。释放内存。

过期时间：redis中的过期时间指的是针对对象的过期时间，如果一个hash对象


## 应用1：分布式锁

1. 问题

分布式系统并发修改字段，需要保证读取和保存状态是原子操作，并且具有锁，保证数据一致性。原子操作指不会被线程调度机制打断的操作。

2. 介绍

分布式锁：使用setnx（set if not exises ）指令，只允许被一个客户端占用这个key，用完之后，调用del来删除key。在此期间，所有其他客户端相对该key进行操作，只能等待该客户端执行结束。但是如果setnx之后到del命令之间的的命令执行失败就会导致死锁。所以一般当我们拿到一个key的锁之后，给他加上一个过期时间。比如5s。这样保证即使中间出现异常，也会在5s之后自动释放锁。但是如果在setnx和expire之间进程出现异常，还是会导致死锁的。在redis2.8之后，set指令得到了扩展，添加了一些参数，使之可以以原子方式执行，如 set key1 value1 ex 5 nx .这个指令就是sernx和expire组合到了一起。
超时问题：如果在加锁和释放锁之间的执行逻辑很长，超出了锁的超时限制，就会出现数据不一致问题。因为这个时期锁过期了，第二个线程重新持有这个锁，第一个线程执行完后，就会再一次释放锁，第三个线程就会再第二个线程执行期间，拿到了锁。导致问题。可以使用一个随机数，再释放锁的时候，先匹配随机数是否一致，然后再释放锁。需要Lua脚本处理，其可以保证多个指令原子执行。
可重入性：一个锁可以被同一个线程多次获取，并计数，释放相同次数，才能完全释放锁。不建议使用，增加了代码的复杂度。

分布式锁在集群中，如果一个节点向主节点申请一个分布式锁，之后主节点挂掉了，但是这时候数据还没有来得及同步数据到从节点，此时从节点变成主节点，那么此时如果有另一个客户端获取相同的锁，就会被允许，就会造成系统中同一个锁被两个客户端占有。
优化方案：使用redlock，就是在获取锁时，需要多个不同的redis实例，没有主从关系，然后向半数的节点发送获取锁信息，只有半数实例获取锁成功，才会认为加锁失败。释放锁时，需要向所有节点发送del指令。
提高了高可用性，但是降低了性能。

2. 应用2：延时队列

异步消息队列：redis的list的数据结构常用来做异步消息队列使用，使用rpush/lpush操作入队列，使用lpop和rpop来出队列。rpush 和 rpop 先进后出，实现栈功能。 lpush和rpop先进先出，实现队列。
如果队列为空，lpop和rpop就会死循环读取数据，会导致空轮询问题。使用blpop和brpop这种阻塞读时，再读取空队列时，线程会进入休眠状态，知道数据过来。
空闲连接问题：如果一个线程一直阻塞，redis的客户端连接就成了闲置连接，时间过久，服务器就会主动断开连接，减少资源占用。这个时候，blpop和brpop就会排除异常。所以写客户端需要注意重试。
客户端再请求加锁时没有成功，如何处理：
1.直接抛出异常
2. sleep一会再试
3. 将请求转移到延时队列，过一会再试








