package cn.kkcoder.workqueues;

import cn.kkcoder.util.FactoryUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Author : mkk
 */
public class Send {

    public static void main(String[] args) {

        //获取连接
        Connection connection = FactoryUtil.getConnection();

        Channel channel = null;
        try {
            //创建通道 channel
            channel = connection.createChannel();


            //这里的第一个true，指的是持久化。即对任务和消息内容的持久化，保证即使rabbitmq重启也不会丢失任务.
            channel.queueDeclare(FactoryUtil.WORK_QUEUE_NAME, true,false,false,null);


            for(int i=0 ; i<=30 ; i++){
                String msg = "work queues " + i;

                //向指定队列名中发送消息
                channel.basicPublish("",FactoryUtil.WORK_QUEUE_NAME, MessageProperties.TEXT_PLAIN,msg.getBytes());

            }


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
