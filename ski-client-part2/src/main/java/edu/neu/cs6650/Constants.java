package edu.neu.cs6650;

public class Constants {
  // Paths
  public static final String SERVER_IP = "34.217.122.2";
  public static final String SERVER_PATH = "http://" + SERVER_IP + "/ski-server_war";
  public static final String SERVER_PATH_SPRING = "http://" + SERVER_IP + "/ski-server-spring_war";
  public static final String SERVER_PATH_LOCAL = "http://localhost:8080/ski-server_war_exploded";
  public static final String SERVER_PATH_LOCAL_SPRING = "http://localhost:8080/ski-server-spring_war_exploded";
  public static final String POST = "POST";
  // ID range
  public static final int SKIER_ID_MIN = 1;
  public static final int SKIER_ID_MAX = 100000;
  public static final int RESORT_ID_MIN = 1;
  public static final int RESORT_ID_MAX = 10;
  public static final int LIFT_ID_MIN = 1;
  public static final int LIFT_ID_MAX = 40;
  public static final String SEASON_ID = "2022";
  public static final String DAY_ID = "1";
  public static final int TIME_MIN = 1;
  public static final int TIME_MAX = 360;
  // Requests and Threads
  public static final int TOTAL_COUNT = 200000;
  public static final int P1_THREAD_NUM = 32;
  public static final int P1_CNT_PER_THREAD = 1000;
  public static final int P2_THREAD_NUM = 200;
  public static final int PQ_CAPACITY = P2_THREAD_NUM;
  public static final int RQ_CAPACITY = P2_THREAD_NUM;

  public static final int MAX_RETRY_TIMES = 5;

  // Response code
  public static final int CODE_POST_SUCCESS= 201;
  public static final int ERROR_CODE_LOWER_BOUND = 400;
  public static final int ERROR_CODE_UPPER_BOUND = 600;
  // Record
  public static final int END_RECORD_CODE= -1;
  public static final String RECORD_FILE = "record.csv";
  public static final String[] RECORD_HEADER = { "start time", "request type", "latency", "response code"};

}
