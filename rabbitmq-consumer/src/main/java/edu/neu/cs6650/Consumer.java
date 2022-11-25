package edu.neu.cs6650;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import edu.neu.cs6650.model.LiftRideData;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

public class Consumer implements Runnable {

  private final Connection mqConnection;
  private final String queueName;
  private final JedisPool jedisPool;
  private Gson gson;

  public Consumer(Connection mqConnection, String queueName,
      JedisPool jedisPool) {
    this.mqConnection = mqConnection;
    this.queueName = queueName;
    this.jedisPool = jedisPool;
    this.gson = new Gson();
  }

  @Override
  public void run() {
    try {
      Channel channel = mqConnection.createChannel();
      channel.queueDeclare(this.queueName, false, false, false, null);
      System.out.println(
          "[**" + Thread.currentThread().getName() + "**] Waiting for messages...");

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String dataJson = new String(delivery.getBody(), StandardCharsets.UTF_8);
//        System.out.println(
//            "[**" + Thread.currentThread().getName() + "**] Received: " + dataJson);

        LiftRideData data = gson.fromJson(dataJson, LiftRideData.class);

        try (Jedis jedis = jedisPool.getResource()) {
          Transaction t = jedis.multi();
          t.sadd(data.getSkierDayKey(), dataJson);
          t.sadd(data.getSkierSeasonKey(), data.getSkierDayKey());
          t.sadd(data.getResortDaySkiersKey(), String.valueOf(data.getSkierID()));
          t.exec();
//          System.out.println(
//              "[**" + Thread.currentThread().getName() + "**] Wrote: " + dataJson);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      };

      channel.basicConsume(this.queueName, true, deliverCallback, consumerTag -> {
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
