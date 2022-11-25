package edu.neu.cs6650;

public class Constants {

  public final static int THREAD_NUM = 16;
  // rabbitmq uri
  public static final String MQ_URI = "amqp://user:user@54.186.25.214:5672";
  public static final String MQ_URI_VPC_PRIVATE = "amqp://user:user@172.31.9.3:5672";
  public static final String MQ_URI_LOCAL = "amqp://guest:guest@localhost:5672";
  public final static String QUEUE_NAME = "liftRideQueue";
  // redis uri
  public static final String REDIS_URI = "redis://default:password@52.25.159.144:6379";
  public static final String REDIS_URI_VPC_PRIVATE = "redis://default:password@172.31.7.71:6379";
  public static final String REDIS_URI_LOCAL = "redis://default:password@localhost:6379";
}
