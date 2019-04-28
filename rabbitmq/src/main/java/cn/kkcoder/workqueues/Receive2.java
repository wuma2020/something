package cn.kkcoder.workqueues;

import cn.kkcoder.util.FactoryUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author : mkk
 */
public class Receive2 {

    public static void main(String[] args) {

        //获取连接
        Connection connection = FactoryUtil.getConnection();


        try {

            //创建通道
            final Channel channel = connection.createChannel();


            //声明队列 ack设置为true
            channel.queueDeclare(FactoryUtil.WORK_QUEUE_NAME,true,false,false,null);

            channel.basicQos(1);

            //创建回调delivery
            DeliverCallback deliverCallback = (consumer,delivery) -> {
                String msg = new String(delivery.getBody());
                System.out.println("receive2 : " + msg);
                //这里会循环等待
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            };

            //从通道中消费
            channel.basicConsume(FactoryUtil.WORK_QUEUE_NAME, deliverCallback, consumer -> { });


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
