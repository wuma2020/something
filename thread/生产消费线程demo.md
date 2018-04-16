# 生产消费线程demo

标签： demo

---

# demo
```java
package cn.kkcoder.thread;


//------------------------------仓库类------------------------------------
/**
 *  仓库类
 * @author static-mkk
 * @time   28 Mar 2018
 *
 */
class Depot{
	
	private int capacity;				//仓库的最大容量
	private int size;					//仓库的实际产品个数
	
	/**
	 * 初始化 仓库最大容量
	 * 
	 * @param capacity 仓库最大容量
	 */
	public Depot(int capacity) {
		this.capacity = capacity;
		this.size = 0;
	}
	
	
	
	//----------------------------生产方法------------------------------------------	
	
	/**
	 * 	仓库的生产产品方法
	 * 
	 * @param number 想要生产产品的数量
	 */
	public synchronized void product(int number) {
		
		try {

			//number 要   > 0
			while(number >0) {
				
				//判断当前库存是否是最大容量，即，库存已满
				while(size>=capacity) {
					wait();
				}
				
				//判断想要生产的数量 与  实际产品个数 之和，是否大于 最大容量，取最小值
				int addNumber =  (number + size) > capacity ? (capacity-size) : number;
	
				//然后修改size，并输出相应实际生产量，仓库实际产品数.
				size += addNumber;
				
				System.out.println("实际生产数量 ： " + addNumber + " , 仓库库存为 ： " + size );
				
				//生产结束，唤醒消费线程或者生产线程
				notifyAll();
				
			}

		} catch (Exception e) {
			throw new RuntimeException("生产错误!");
		}		
		
	}
	
	//----------------------------消费方法---------------------------------------------
	
	/**
	 * 	仓库的消费方法
	 * 
	 * @param number  想要消费的数量
	 */
	public synchronized void customer(int number) {
		try {
			//判断消费数量   > 0
			while(number > 0) {
				//如果仓库实际产品数<0，则等待。即，释放消费的同步锁，让生产线程来生产
				while(size<=0) {
					wait();//等待生产线程的唤醒.
				}
				//判断消费数量是否大于仓库库存 ,取
				int deleteNumber  = size < number ? size : number;
				
				size -= deleteNumber;
				System.out.println("消费者希望消费： "+ number +"  实际消费 : "+ deleteNumber + "  仓库剩余： " + size);
				
				//消费结束，唤醒其他线程的消费或者生产
				notifyAll();
			}

		} catch (Exception e) {
			throw new RuntimeException("消费失败！");
		}		
		
	}
	
}


//---------------------------------------生产者类-----------------------------------------------------
/**
 * 	生产者线程类
 * @author static-mkk
 * @time   28 Mar 2018
 *
 */
class Producter{
	
	private Depot depot;
	
	public Producter(Depot depot) {
		this.depot = depot;
	}
	
	/**
	 *  生产线程方法
	 * @param number  希望生产的数量
	 */
	public void produce(int number) {
		new Thread(()-> {depot.product(number);}).start();
	}
	
}

//--------------------------------------------消费者类---------------------------------------------------
/**
 *  消费类
 * @author static-mkk
 * @time   28 Mar 2018
 *
 */
class Customer{
	
	private Depot depot;
	
	public Customer(Depot depot) {
		this.depot = depot;
	}
	
	/**
	 * 	消费线程方法
	 * @param number 希望消费的数量
	 */
	public void consume(int number) {
		new Thread(()-> {depot.customer(number);} ).start();;
	}
	
}

//---------------------------------测试类-------------------------------------------

/**
 * 
 * @author static-mkk
 * @time   28 Mar 2018 <br/>
 *
 *	 生产   消费 模型 demo
 */
public class ProductAndCustomDemo {

	public static void main(String[] args) {

		Depot d = new Depot(100);
		
		Customer c = new Customer(d);
		Producter p = new Producter(d);
		
		c.consume(50);
		p.produce(20);
		c.consume(150);
		c.consume(40);
		p.produce(60);
		p.produce(300);
		
	}

}
```

---

    由于是死循环，所以别忘了关闭程序.(部分输出结果)
    控制台：
    消费者希望消费： 150  实际消费 : 100  仓库剩余： 0
    实际生产数量 ： 20 , 仓库库存为 ： 20
    实际生产数量 ： 20 , 仓库库存为 ： 40
    实际生产数量 ： 20 , 仓库库存为 ： 60
    实际生产数量 ： 20 , 仓库库存为 ： 80
    实际生产数量 ： 20 , 仓库库存为 ： 100
    消费者希望消费： 40  实际消费 : 40  仓库剩余： 60
    消费者希望消费： 40  实际消费 : 40  仓库剩余： 20
    消费者希望消费： 40  实际消费 : 20  仓库剩余： 0
    实际生产数量 ： 100 , 仓库库存为 ： 100
    消费者希望消费： 50  实际消费 : 50  仓库剩余： 50
    消费者希望消费： 50  实际消费 : 50  仓库剩余： 0
    实际生产数量 ： 60 , 仓库库存为 ： 60
    实际生产数量 ： 40 , 仓库库存为 ： 100
    消费者希望消费： 150  实际消费 : 100  仓库剩余： 0
    实际生产数量 ： 20 , 仓库库存为 ： 20
    实际生产数量 ： 20 , 仓库库存为 ： 40
    实际生产数量 ： 20 , 仓库库存为 ： 60
    实际生产数量 ： 20 , 仓库库存为 ： 80
    实际生产数量 ： 20 , 仓库库存为 ： 100

---

> 总结：从结果中，我们也能清楚的看出来，当生产线程生产达到上限时，唤醒所有线程，消费线程（实际上也会进入生产线程，但是由于仓库已经满了，所以另外的生产线程会进入等待状态，等待被唤醒）会获取同步锁，进行消费，直到消费结果到仓库为0时，进入等待状态，释放同步锁，生产线程获取同步锁，进行生产。无限循环. 至于是哪个生产线程，这由CPU决定（也可以设置线程优先级进行优先生产）。消费线程同理.


