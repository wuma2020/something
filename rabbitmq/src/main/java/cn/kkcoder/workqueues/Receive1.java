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
public class Receive1 {

    public static void main(String[] args) {

        //获取连接
        Connection connection = FactoryUtil.getConnection();


        try {
            //创建通道
            final Channel channel = connection.createChannel();


            //声明一个队列（队列名，ack，。。。） ack为确认任务是否完成
            //即，如果消费线程完成队列中的任务后，会返回一个ack给rabbitmq，来让rabbitmq知道该任务是否完成
            //  如果完成则从队列中删除这个任务和消息，没有的话就不删除，给另外的消费线程消费.
            //  主要是避免 消费线程突然终止，造成 队列中的任务没有执行，并且被删除，造成的任务丢失的情况.
            channel.queueDeclare(FactoryUtil.WORK_QUEUE_NAME,true,false,false,null);

            //设置消费线程同一时刻只有一个任务消费，只有当消费线程返回ack给rabbitmq之后，说明消费任务结束，此时再给
            // 该消费者消费新的任务
            channel.basicQos(1);

            //创建回调delivery
            DeliverCallback deliverCallback = (consumer,delivery) -> {
                String msg = new String(delivery.getBody());
                System.out.println("receive1 : " + msg);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            };

            //从通道中消费
            channel.basicConsume(FactoryUtil.WORK_QUEUE_NAME, deliverCallback, consumer -> { });


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
