package edu.neu.cs6650;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import edu.neu.cs6650.model.LiftRideData;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Main {

  private final static String QUEUE_NAME = "liftRideQueue";
  private final static int THREAD_NUM = 1;

  public static void main(String[] args) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri("amqp://user:user@172.31.9.3:5672");
//    factory.setUri("amqp://guest:guest@localhost:5672");
    Connection connection = factory.newConnection();

    ConcurrentMap<Integer, List<LiftRideData>> liftRideDataMap = new ConcurrentHashMap<>();


    for (int i = 0; i < THREAD_NUM; i++) {
      new Thread(new Consumer(connection, QUEUE_NAME, liftRideDataMap)).start();
    }

  }
}