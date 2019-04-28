package cn.kkcoder.topics;

import cn.kkcoder.util.FactoryUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;

/**
 * @Author : mkk
 */
public class ReceiveLogsTopic {

    static final String EXCHANGE_NAME = "test_topic";
    static final String EXCHANGE_TYPE = "topic";

    static final String topic_bind_key = "*.*.c" ;


    public static void main(String[] args) {

        //获取连接
        Connection connection = FactoryUtil.getConnection();

        try {
            //创建通道
            Channel channel = connection.createChannel();

            //声明交换器
            channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE);

            //获取queue name
            String queuename = channel.queueDeclare().getQueue();

            //绑定bind key  . topic下的bind key 有特殊格式需要满足
            channel.queueBind(queuename,EXCHANGE_NAME,topic_bind_key);

            //消息回调
            DeliverCallback deliverCallback = (consumer , delivery) -> {
                String msg = new String(delivery.getBody());
                System.out.println("receive msg : " + msg);
            };

            //消费消息
            channel.basicConsume(queuename, true,deliverCallback,(c)->{});


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
