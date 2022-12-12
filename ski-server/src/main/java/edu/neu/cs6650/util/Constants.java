package edu.neu.cs6650.util;

public class Constants {

  // Endpoints
  public static final String ENDPOINT_RESORTS = "/resorts";
  public static final String ENDPOINT_STATISTICS = "/statistics";
  public static final String ENDPOINT_SKIERS = "/skiers";
  // Path parameters
  public static final String SKIERS = "skiers";
  public static final String SEASONS = "seasons";
  public static final String DAYS = "days";
  // Request methods
  public static final String POST = "POST";
  public static final String GET = "GET";
  // Response content type
  public static final String CONTENT_TYPE_JSON = "application/json";
  // Exception Message
  public static final String MSG_PAGE_NOT_EXISTS = "page not exists";
  public static final String MSG_INVALID_PATH_PARA = "invalid path parameters";
  public static final String MSG_MISSING_PATH_PARA = "missing path parameters";
  public static final String MSG_INVALID_INPUTS = "invalid inputs";
  // ID range
  public static final int SKIER_ID_MIN = 1;
  public static final int SKIER_ID_MAX = 100000;
  public static final int RESORT_ID_MIN = 1;
  public static final int RESORT_ID_MAX = 10;
  public static final int LIFT_ID_MIN = 1;
  public static final int LIFT_ID_MAX = 40;
  public static final int SEASON_ID_MIN = 2022;
  public static final int SEASON_ID_MAX = 2022;
  public static final int DAY_ID_MIN = 1;
  public static final int DAY_ID_MAX = 3;
  public static final int TIME_MIN = 1;
  public static final int TIME_MAX = 360;
  // rabbitmq uri
  public static final String MQ_URI = "amqp://user:user@35.90.168.246:5672";
  public static final String MQ_URI_VPC_PRIVATE = "amqp://user:user@172.31.9.3:5672";
  public static final String MQ_URI_LOCAL = "amqp://guest:guest@localhost:5672";
  public static final int MAX_CHANNEL_NUM = 200;
  public static final String QUEUE_NAME = "liftRideQueue";

  public static final String ERROR_MQ_CONNECTION = "Error connecting to rabbitmq broker";
  public static final String ERROR_MQ_TIMEOUT = "Error connecting to rabbitmq broker";

  // redis uri
  public static final String REDIS_URI = "redis://default:password@35.89.202.141:6379";
  public static final String REDIS_URI_VPC_PRIVATE = "redis://default:password@172.31.7.71:6379";
  public static final String REDIS_URI_LOCAL = "redis://default:password@localhost:6379";

  public static final String ERROR_REDIS = "Error connecting to REDIS";
}
