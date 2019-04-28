package cn.kkcoder.topics;


import cn.kkcoder.util.FactoryUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author : mkk
 */
public class SendLogsMsg {

    static final String EXCHANGE_NAME = "test_topic";
    static final String EXCHANGE_TYPE = "topic";

    static final String[] topic_route_key = {"a.b.c" ,  "pig.dog.cat" , "dag.cap.pen"};


    public static void main(String[] args) {

        Connection connection = FactoryUtil.getConnection();

        Channel channel = null;
        try {
            //创建通道
            channel = connection.createChannel();

            //声明交换器
            channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE);

            //设置route key 使用topic模式的key  *.#.anyword.#
            for(String s : topic_route_key){
                String msg = "topic route key : " + s;
                channel.basicPublish(EXCHANGE_NAME,s,null,msg.getBytes());
                System.out.println(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
