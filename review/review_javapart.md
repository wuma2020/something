
---

（1） 进程和线程的区别？

>    1.一个程序至少有一个进程，而一个进程至少有一个线程。
    2.进程有独立的地址空间，而线程没有独立的地址空间。
    3.一个进程挂掉，不会影响其他的进程。而一个线程挂掉，则整个进程会挂掉。所以，多进程的程序会多线程的程序健壮。
  

（2）String 和StringBuffer的区别？

 >   1.String 的类定义是 public final class String 。 其内部的存储 value的是 public final char[] value.所以，其是线程安全的，并且是不可变的。因为是final的。
    2.StringBuffer 的类定义为 ： public final class StringBuffer。 其内部存储数据的是 private transient char[] toStringCache。 transient的目的是使该变量不被序列化。因为是 char[] toStringCache，所以内容是可变的。StringBuffer几乎所有方法都加上了synchronized关键字，故事线程安全的。
    3.StringBuilder是线程不安全的。且内容可变。 故，如果不是多线程，StringBuilder效率会高于StringBuffer.

    
（4） linkedlist的实现？

 >   linkedList的内部是用一个双向链的结构来完成操作的。即一个内部是由静态内部类Node<E> ，其中有 E item 和 Node<E> next 和 Node<E> prev 三个成员变量.而类LinkedList有两个Node<E> 的成员变量 first 和 last. 每次添加元素，就把last设置成新元素对应的节点，并把之前的last的next节点，设置成新节点。
  
（5） TCP三次握手、四次挥手，为什么建立连接是三次，而断开连接要四次？
    
    https://github.com/jawil/blog/issues/14
    
（6） HTTP中POST 和 GET的区别和联系？
       
 >      post是把要被处理的数据提交给服务器。get是请求服务器数据。
       post：信息在http消息体内，而且没有长度限制，相对安全。不可以被缓存，不可以被记录或者收藏。
       get : 信息是以url的形式发送到服务器，即明文形式的，而且有长度限制，并且不安全，密码等信息千万不能用get。其可以被缓存，被记录或收藏。
       
（7） 重载和重写的区别？

>    重载：要求方法的方法名相同，参数的类型，数量，顺序有其一不同，就算是重载。并没有对返回类型做出限制。可以有不同的访问符.
    重写：在java中，子类想要覆盖父类的某一个方法，就在子类中写一个返回类型，方法名，参数列表都相同的方法，来达到覆盖父类相应方法的目的，叫做重写。如果需要调用父类的某个被子类覆盖的方法，需要在方法体的第一行使用super关键字显示的调用父类该方法。访问符要大于父类该方法对应的访问符，即public > protected > default > private.
    
（9） java的四大基本特征？

>    1.抽象：把实际情况，抽象成一个模型，一个类或者接口。
    2.封装：把具有共性的一类事或者物，写成成一个类或者方法。封装也是对一些数据的隐藏，只留下一些可以访问的接口来供外界访问，从而更加的安全。
    3.继承：子类继承父类的特点，同时也可以自己添加一些属于自己的特点。一个父类可以有多个子类，而一个子类只能继承一个父类。子类如果要覆盖即重写父类的方法，需要父类的该方法或变量的修饰符要 权限要小于 子类的修饰符。即，子类的方法或属性 的访问权限要小于 父类。
    4.多态：方法的重写，重载，子类的继承，都体现了多态这个特点。
    
（10）构造器方法是否可以重写？

>    构造器方法的要求是：修饰符必须为 public private protected 之一。而且方法名必须与类名一致。且 不能有返回类型，即：没有 void、 String等这样的返回类型。 
    构造函数不能被覆盖，即不能被重写。因为重写的要求是 返回类型，方法名，参数列表 都要相同，而构造函数 没有返回类型，void也是一种返回类型，返回空的类型。和没有返回类型有 质的区别。
    
（9） 访问修饰符public protected default private的区别？

    public： 所有地方都可以访问到。
    protected ： 同包 或者 包外的子类，可以访问。  
    default ： 同包内可以访问，包外都访问不了。
    private ： 只在本类中可以访问。
  
（10） hashCode 和 equals() 的区别？

 >   hashCode相等，equals未必相等。  equals相等，hashCode一定相等.
    hashCode是什么？ 他是java根据一定的规则将相应的对象的相关信息映射成一个数值，成为散列值。 主要用于hashmap 或者 hashtable 这样的集合类用于降低equals的使用频率，从而提高效率。结合hashmap源码学习。

（11）抽象类和接口的区别？

 >   接口：接口用interface来代替class关键字。可以用public来修饰interface，也可以不用。interface 接口不能实例化。只能类被implements，然后由类来实现所有该接口中的方法。而接口中的方法只能声明，不能有方法体，即格式为 void testOne(); //默认的修饰符为public。 而对于成员变量，为 int i  = 1; //默认的修饰为static final.即，不可更改的常量. 
    抽象类：抽象类必须有abstract 关键字来修饰类，如 abstract class Test{}, 抽象类中可以没有抽象方法，而有抽象方法的类一定是抽象类。抽象类也是不能实例化的。抽象类被继承时，子类必须实现抽象类中的抽象方法。且抽象方法的可以有方法体，即自己实现该方法，接口不行。抽象类中的抽象方法不能是private，因为抽象类就是让子类来继承的，并且要重写其抽象方法的，所一相应的权限应该是要限制的。不能是provite.抽象类可以有构造函数，而接口没有。
    接口的使用会更加的灵活，但是如果接口中的方法有所改变，那么所有实现该接口的类都要修改。而，抽象类中的抽象方法改变，只需要在抽象类中修改即可。而且抽象类比接口更快。
    
（12）装箱和拆箱？


>    装箱：指的是将 基本类型 封装成 对应的 包装类型。如 int -> Interger ,long -> Long , byte -> Byte short —> Short ,boolean -> Biilean ,double -> Double ,char ->Character , float -> Float
    拆箱：指的是相反操作.
    例子： Integer i =0;
            for(int j =0;j<=3333;j++){
                i = i + 1;
            }
        这里的 + ，只能是基本类型，所以 每次 i + 1,会自动拆箱，而 赋值给i后，又自动装箱。所以，这里会有大量的无畏的装箱拆箱，因此，数据类型的使用一定要合适，避免浪费大量时间来装箱拆箱.

 
（13） 泛型的特点？

>    泛型的使用: public static <K,V> V getValueByKey(K key,V value){}  //前面使用的< , >来声明K , V 类型.返回值为V类型.
    泛型的特点：可以避免程序员手动的强制转换.但实际上，编译后，也是用了强制转换来达到保持类型一致的。而且这样做，在编译期间就可以发现类型错误信息。可读性也比较好。通常来说，如果Foo是Bar的子类型，G是一种带泛型的类型，则G<Foo>不是G<Bar>的子类型。
    在使用泛型时，任何具体的类型都被擦除，唯一知道的是你在使用一个对象。比如：List<String>和List<Integer>在运行事实上是相同的类型。他们都被擦除成他们的原生类型，即List。因为编译的时候会有类型擦除，所以不能通过同一个泛型类的实例来区分方法。例子如下：
        /*会导致编译时错误*/   
        public class Erasure{  
            public void test(List<String> ls){  
                System.out.println("Sting");  
            }  
            public void test(List<Integer> li){  
                System.out.println("Integer");  
            }  
        } 
    编译器实际上会把泛型类型，会在后面代码中自动强转成该类型，所以，不需要手动强转。因此，利用泛型也是会更加的灵活。

（14）java中的集合类的关系图？

![此处输入图片的描述][1]

（15）HashMap 实现原理， 源码解析？

 >   1.8的源码解析。[https://blog.csdn.net/zxt0601/article/details/77413921][2]

（16）HashTable 实现原理  源码解析？
（18）ConcurrentHashMap 实现原理？


（17）ArrayList 和 Vector 以及 LinkedList的区别？

>    LinkedList是一个双向链表结构.线程不安全.适合于中间部位添加和删除.
    ArrayList 是一个数组结构.线程不安全.适合于查询和修改，以及尾部的添加和删除.
    Vector    是一个数组结构。但是关键的添加，删除等方法都已经用synchronized修饰，是线程安全的.适合于查询，以及尾部的添加和删除.


（19）关于异常？

>    http://wangkuiwu.github.io/2012/04/14/exception/
    
（20）线程的各种状态？

    5种状态。 
    新建状态：在new 线程之后的状态.
    就绪状态：在执行了start()方法之后，线程和其他线程等待cpu调用的状态。此时并没有执行线程的run()方法.
    运行状态：当线程获取cpu的调度之后，线程就进入了运行状态，此时才是真正的执行run()方法.
    阻塞状态：正在运行的线程暂时让出CPU。然后cpu可以处理其他的就绪线程。导致阻塞的原因有很多。此时线程并没有执行结束，而是暂停执行.
                1.sleep() 2.等待获取某个对象的锁.  3.等待其他触发条件 condition 4. 在使用I/O流是导致阻塞.
    死亡状态：run()方法执行结束，线程正常死亡。因为某些异常导致run()方法终止，而使线程死亡.
    
（21）线程的终止的方法？

    1.正常结束终止  
    2.interrupt()终止
    3.自己设置一个flag，while循环判断，用于终止线程.
    4.不安全的方法，直接stop()，resume()...
    
（22）synchronized 和 lock 的区别？

>    synchronized实现的功能 使用lock都能实现，而lock实现的功能 synchronized并不一定能实现.所以，lock更加灵活.并且lock的性能比synchronized好.
    
（23）多线程进行线程交互？


（24）sleep 和 wait的区别？

 >   最大的区别就是 sleep会使线程进行阻塞状态，但是并不释放对象锁，而wait会使线程进入阻塞状态，并释放原先持有的锁.
    sleep是Thread类静态方法. wait是Object的方法.
    
（25）产生死锁的原因？

    多个线程由于线程的逻辑不正确，都进入了持续的等待获取同一个锁的等待状态。
    产生死锁的原因：
        一.因为系统资源不足。
        二.进程运行推进的顺序不合适。
        三.资源分配不当。
        
（26）什么是守护线程？

    用户线程在运行的时候， 后台提供的一种服务的线程。比如垃圾回收线程。 
    只要有用户线程存在，服务线程就不会退出。当用户线程都不存在时，jvm就会退出，守护线程自然也就不存在了.
    
（27）java线程池技术和原理？

    参考文章：
    http://www.importnew.com/19011.html
    http://www.cnblogs.com/dolphin0520/p/3932921.html
    
（28）java并发包concurrent及常用的类？

    这个内容有点多，参考文章：
    并发包诸类概览：http://www.raychase.net/1912
    线程池：http://www.cnblogs.com/dolphin0520/p/3932921.html
    锁：http://www.cnblogs.com/dolphin0520/p/3923167.html
    集合：http://www.cnblogs.com/huangfox/archive/2012/08/16/2642666.html
  
（29） volatile关键字？
（30）   IO和NIO区别？
（31）序列化与反序列化？
（32）Java类加载器及如何加载类(双亲委派)？
    阅读文章：
    https://www.ibm.com/developerworks/cn/java/j-lo-classloader/（推荐）
    http://blog.csdn.net/zhoudaxia/article/details/35824249
  
  
（8） 别人整理的知识点。
    1. https://www.nowcoder.com/discuss/31667
    
    


  [1]: http://wangkuiwu.github.io/media/pic/java/collection/collection01.jpg
  [2]: http://wangkuiwu.github.io/media/pic/java/collection/collection01.jpg