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

          // For GET request: /resorts/{resortID}/seasons/{seasonID}/day/{dayID}/skiers
          // Schema: resort_season_day:<resortID>_<seasonID>_<dayID>:skiersSet
          t.sadd("resort_season_day:" + data.getResortID() + "_" + data.getSeasonID() + "_" + data.getDayID() + ":skiersSet", Integer.toString(data.getSkierID()));

          // For GET request: /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
          // Schema: resort_season_day_skier:<resortID>_<seasonID>_<dayID>_<skierID>:totalVertical
          t.incrBy("resort_season_day_skier:" + data.getResortID() + "_" + data.getSeasonID() + "_" + data.getDayID() + "_" + data.getSkierID() + ":totalVertical", (long) data.getLiftRide().getLiftID() * 10);

          // For GET request: /skiers/{skierID}/vertical?resort=<resortID>&season=<seasonID>
          // Schema: skier_resort_season:<skierID>_<resortID>_<seasonID>:totalVertical
          t.incrBy("skier_resort_season:" + data.getSkierID() + "_" + data.getResortID() + "_" + data.getSeasonID() + ":totalVertical", (long) data.getLiftRide().getLiftID() * 10);

          // For GET request: /skiers/{skierID}/vertical?resort=<resortID>
          // Schema: skier_resort:<skierID>_<resortID>:totalVertical
          t.incrBy("skier_resort:" + data.getSkierID() + "_" + data.getResortID() + ":totalVertical", (long) data.getLiftRide().getLiftID() * 10);

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
