package edu.neu.cs6650;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import edu.neu.cs6650.model.LiftRideData;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class Consumer implements Runnable {

  private Connection connection;
  private String queueName;
  private ConcurrentMap<Integer, List<LiftRideData>> liftRideDataMap;
  private Gson gson;

  public Consumer(Connection connection, String queueName,
      ConcurrentMap<Integer, List<LiftRideData>> liftRideDataMap) {
    this.connection = connection;
    this.queueName = queueName;
    this.liftRideDataMap = liftRideDataMap;
    this.gson = new Gson();
  }

  @Override
  public void run() {
    try {
      Channel channel = connection.createChannel();
      channel.queueDeclare(this.queueName, false, false, false, null);
      System.out.println(
          "[**" + Thread.currentThread().getName() + "**] Waiting for messages...");

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String dataJson = new String(delivery.getBody(), StandardCharsets.UTF_8);
//        System.out.println(
//            "[**" + Thread.currentThread().getName() + "**] Received: " + dataJson);
        LiftRideData data = gson.fromJson(dataJson, LiftRideData.class);
        liftRideDataMap.putIfAbsent(data.getSkierID(), new ArrayList<>());
        liftRideDataMap.get(data.getSkierID()).add(data);
      };

      channel.basicConsume(this.queueName, true, deliverCallback, consumerTag -> {
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
