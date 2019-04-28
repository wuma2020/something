package cn.kkcoder.routing;

import cn.kkcoder.util.FactoryUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * @Author : mkk
 */
public class ReceiveLogsDirect {

    static final String EXCHANGE_DIRECT = "direct";
    static final String EXCHANGE_NAME = "routing_log";

    static final String[] bindKey = {"apple","pan","pig"};


    public static void main(String[] args) {

        //获取连接
        Connection connection = FactoryUtil.getConnection();

        try {
            //创建通道
            Channel channel = connection.createChannel();

            //声明 exchange (交换器名字，交换器类型)
            channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_DIRECT);

            //获取queue name
            String queueName = channel.queueDeclare().getQueue();


            for(String bind : bindKey){
                if(bind.equals("apple")){
                    //绑定 接收的类型 设置 bind key 即，只接受指定交换机中指定 routing key 的队列的信息，对消息进行过滤
                    channel.queueBind(queueName,EXCHANGE_NAME,bind);
                }
            }

            //创建回调对象 delivery
            DeliverCallback deliverCallback = (consumerTag , delivery) -> {

                String msg = new String(delivery.getBody());
                System.out.println("receive logs direct get msg : " + msg );

            };

            //channel进行消费 (queue name , autoAck ,deliverCallback , CancelCallback)
            channel.basicConsume(queueName,true,deliverCallback, (consumer) ->{} );


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
