package cn.kkcoder.PublishSubscribe;

import cn.kkcoder.util.FactoryUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author : mkk
 */
public class Publish {

    static final String EXCHANGE_NAME = "log";

    public static void main(String[] args) {

        //获取连接
        Connection connection = FactoryUtil.getConnection();

        Channel channel = null;
        try {
            //获取通道
            channel = connection.createChannel();

            //声明交换器  第一个参数：交换器名字     第二个参数：交换器类型 fanout
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

            //向指定交换器发送消息
            String msg = "exchanges name !";
            channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());

            System.out.println("publish msg!");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(channel != null){
                try {
                    channel.close();
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }


    }


}
