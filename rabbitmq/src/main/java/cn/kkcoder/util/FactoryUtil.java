package cn.kkcoder.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author : mkk
 */
public class FactoryUtil {

    //queue name p-q-c
    public static final String QUEUE_NAME = "test_queue";

    //vhosts
    public static final String VHOSTS = "/test_vhosts";

    //queue name p-q-cc
    public static final String WORK_QUEUE_NAME = "test_work_queue";


    static ConnectionFactory connectionFactory = new ConnectionFactory();




    //------------------------methods-----------------------------------
    /**
     * 获取rabbitmq连接
     * @return
     */
    public static Connection getConnection(){

        connectionFactory.setUsername("wuma");
        connectionFactory.setPassword("123456789");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost(VHOSTS);

        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return connection;
    }


}
