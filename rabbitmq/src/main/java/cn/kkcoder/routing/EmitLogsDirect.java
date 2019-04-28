package cn.kkcoder.routing;

import cn.kkcoder.util.FactoryUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author : mkk
 */
public class EmitLogsDirect {

    static final String EXCHANGE_DIRECT = "direct";
    static final String EXCHANGE_NAME = "routing_log";
    //设置 message 的 routing key  用于后面订阅者的 bind key 的分类订阅
    static final String[] routeKey = {"apple","pan","pig"};

    public static void main(String[] args) {

        //获取连接
        Connection connection = FactoryUtil.getConnection();

        //创建管道

        Channel channel = null;
        try {
            //获取通道
            channel = connection.createChannel();

            //设置 交换机
            channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_DIRECT);


            for(String r : routeKey ){

                String msg =  r + " +  msg !";
                //利用channel发布
                channel.basicPublish(EXCHANGE_NAME,r,null,msg.getBytes());
                System.out.println("send msg ! and routing key : " + r);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //发送之后需要关闭通道
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
