package cn.kkcoder.helloworld;

import cn.kkcoder.util.FactoryUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * @Author : mkk
 */
public class Send {

    public static void main(String[] args) {
        Connection connection = FactoryUtil.getConnection();

        Channel channel = null;

        try {
            //创建一个通道
            channel = connection.createChannel();

            //创建队列声明
            channel.queueDeclare(FactoryUtil.QUEUE_NAME,false,false,false,null);

            String msg = "hello world!";

            //发布消息
            channel.basicPublish("",FactoryUtil.QUEUE_NAME,null,msg.getBytes());

            System.out.println("send : " + msg);


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
