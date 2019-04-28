package cn.kkcoder.PublishSubscribe;

import cn.kkcoder.util.FactoryUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * @Author : mkk
 */
public class Subscribe {

    static final String EXCHANGE_NAME = "log";

    public static void main(String[] args) {

        //获取连接
        Connection connection = FactoryUtil.getConnection();

        //创建通道
        try {
            //获取通道
            final Channel channel = connection.createChannel();

            //声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
            String queuename = channel.queueDeclare().getQueue();

            //queue name 和 交换机绑定
            channel.queueBind(queuename,EXCHANGE_NAME,"");

            //回调消息
            DeliverCallback deliverCallback = (consumertag,delivery) -> {
                String msg = new String(delivery.getBody(),"UTF-8");
                System.out.println("fanout msg : " + msg + "  queue name :" + queuename);
//                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            };

            //channel 消费消息
            channel.basicConsume(queuename,true,deliverCallback,(consumertag)->{});

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
