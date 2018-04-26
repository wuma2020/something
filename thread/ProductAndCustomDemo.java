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
