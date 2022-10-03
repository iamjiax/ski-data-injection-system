package edu.neu.cs6650;

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
  public static final int DAY_ID_MAX = 1;
  public static final int TIME_MIN = 1;
  public static final int TIME_MAX = 360;
}
