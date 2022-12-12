package edu.neu.cs6650;

import static edu.neu.cs6650.Constants.*;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import redis.clients.jedis.JedisPool;

public class Main {

  public static void main(String[] args) throws Exception {
    ConnectionFactory mqFactory = new ConnectionFactory();
    mqFactory.setUri(MQ_URI_VPC_PRIVATE);
    Connection mqConnection = mqFactory.newConnection();
    JedisPool jedisPool = new JedisPool(REDIS_URI_VPC_PRIVATE);

    // start consumer threads
    for (int i = 0; i < THREAD_NUM; i++) {
      new Thread(new Consumer(mqConnection, QUEUE_NAME, jedisPool)).start();
    }

  }
}