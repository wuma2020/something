package cn.kkcoder.helloworld;


import cn.kkcoder.util.FactoryUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;

/**
 * @Author : mkk
 */
public class Receive {

    public static void main(String[] args) {

        Connection connection = FactoryUtil.getConnection();
        Channel channel = null;
        try {
            //创建channel
            channel = connection.createChannel();

            //管道声明队列
            channel.queueDeclare(FactoryUtil.QUEUE_NAME,false,false,false,null);

            DeliverCallback deliverCallback = new DeliverCallback() {
                @Override
                public void handle(String s, Delivery delivery) throws IOException {
                    String msg = new String(delivery.getBody());
                    System.out.println( "receive " + msg + "  s : " + s );
                }
            };

            //消费
            channel.basicConsume(FactoryUtil.QUEUE_NAME,true,deliverCallback,consumerTag -> {});


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(channel != null){
                try {
                    channel.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
