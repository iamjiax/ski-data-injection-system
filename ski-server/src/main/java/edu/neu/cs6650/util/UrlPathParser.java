package edu.neu.cs6650.util;

import static edu.neu.cs6650.util.Constants.*;

import edu.neu.cs6650.exceptions.InvalidInputsException;
import edu.neu.cs6650.exceptions.InvalidUrlException;
import edu.neu.cs6650.exceptions.MissingPathParametersException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class UrlPathParser {

  private int resortID;
  private String seasonID;
  private String dayID;
  private int skierID;

  private String method;
  private String endpoint;
  private String urlPath;

  public UrlPathParser(HttpServletRequest request, String endpoint)
      throws InvalidUrlException, MissingPathParametersException {
    String method = request.getMethod();
    String urlPath = request.getPathInfo();
    String queryString = request.getQueryString();

    switch (endpoint) {
      case ENDPOINT_SKIERS:
        validateSkiersUrl(method, urlPath, queryString);
        break;
      case ENDPOINT_RESORTS:
        validateResortsLongUrl(method, urlPath);
        break;
//      case ENDPOINT_STATISTICS:
//        //
//        break;
      default:
        throw new InvalidUrlException(MSG_PAGE_NOT_EXISTS);
    }

    this.method = method;
    this.endpoint = endpoint;
    this.urlPath = urlPath;
  }

  private void validateSkiersUrl(String method, String urlPath, String queryString)
      throws InvalidUrlException, MissingPathParametersException {

    if (urlPath == null || urlPath.isEmpty()) {
      throw new MissingPathParametersException(MSG_MISSING_PATH_PARA);
    }

    String[] urlParts = urlPath.split("/");
    Map<String, String> queryMap = new HashMap<>();
    try {
      if (queryString != null) {
        String[] queryParams = queryString.split("&");
        for (String query : queryParams) {
          String[] queryParts = query.split("=");
          queryMap.put(queryParts[0], queryParts[1]);
        }
      }
    } catch (Exception e) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }

    switch (method) {
      case POST:
        validateSkiersLongUrl(urlParts);
        break;
      case GET:
        if(urlParts.length == 8) {
          validateSkiersLongUrl(urlParts);
          break;
        } else if (urlParts.length == 3){
          validateSkiersVerticalUrl(urlParts, queryMap);
          break;
        }
      default:
        throw new InvalidUrlException(MSG_PAGE_NOT_EXISTS);
    }
  }

  // SkiersLongUrl： [/skiers]/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
  private void validateSkiersLongUrl(String[] urlParts)
      throws InvalidUrlException {
    final int LEN_PARTS = 8;
    if (urlParts.length != LEN_PARTS) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }

    final int IDX_RESORT_ID = 1;
    final int IDX_SEASONS = 2;
    final int IDX_SEASON_ID = 3;
    final int IDX_DAYS = 4;
    final int IDX_DAY_ID = 5;
    final int IDX_SKIERS = 6;
    final int IDX_SKIER_ID = 7;

    if (!urlParts[IDX_SEASONS].equals(SEASONS) ||
        !urlParts[IDX_DAYS].equals(DAYS) ||
        !urlParts[IDX_SKIERS].equals(SKIERS)) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }

    try {
      this.resortID = Validator.validateID(urlParts[IDX_RESORT_ID], RESORT_ID_MIN, RESORT_ID_MAX);
      this.seasonID = String.valueOf(
          Validator.validateID(urlParts[IDX_SEASON_ID], SEASON_ID_MIN, SEASON_ID_MAX));
      this.dayID = String.valueOf(
          Validator.validateID(urlParts[IDX_DAY_ID], DAY_ID_MIN, DAY_ID_MAX));
      this.skierID = Validator.validateID(urlParts[IDX_SKIER_ID], SKIER_ID_MIN, SKIER_ID_MAX);
    } catch (InvalidInputsException e) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }
  }

  // SkiersVerticalUrl： [/skiers]/{skierID}/vertical?resort=<resortID>&season=<seasonID>
  private void validateSkiersVerticalUrl(String[] urlParts, Map<String, String> queryMap)
      throws InvalidUrlException {
    final int LEN_PARTS = 3;
    if (urlParts.length != LEN_PARTS || !queryMap.containsKey("resort")) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }

    final int IDX_SKIER_ID = 1;
    final int IDX_VERTICAL = 2;
    String RESORT_ID = queryMap.get("resort");
    String SEASON_ID = queryMap.get("season");

    if (!urlParts[IDX_VERTICAL].equals("vertical")) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }

    try {
      this.skierID = Validator.validateID(urlParts[IDX_SKIER_ID], SKIER_ID_MIN, SKIER_ID_MAX);

      this.resortID = RESORT_ID == null ? null : Validator.validateID(RESORT_ID, RESORT_ID_MIN, RESORT_ID_MAX);
      this.seasonID = SEASON_ID == null ? null : String.valueOf(
          Validator.validateID(SEASON_ID, SEASON_ID_MIN, SEASON_ID_MAX));
    } catch (InvalidInputsException e) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }
  }

  // ResortsLongUrl： [/resorts]/{resortID}/seasons/{seasonID}/days/{dayID}/skiers
  private void validateResortsLongUrl(String method, String urlPath)
      throws InvalidUrlException {
    String[] urlParts = urlPath.split("/");
    final int LEN_PARTS = 7;
    if (urlParts.length != LEN_PARTS) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }

    final int IDX_RESORT_ID = 1;
    final int IDX_SEASONS = 2;
    final int IDX_SEASON_ID = 3;
    final int IDX_DAYS = 4;
    final int IDX_DAY_ID = 5;
    final int IDX_SKIERS = 6;

    if (!urlParts[IDX_SEASONS].equals(SEASONS) ||
        !urlParts[IDX_DAYS].equals(DAYS) ||
        !urlParts[IDX_SKIERS].equals(SKIERS)) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }

    try {
      this.resortID = Validator.validateID(urlParts[IDX_RESORT_ID], RESORT_ID_MIN, RESORT_ID_MAX);
      this.seasonID = String.valueOf(
          Validator.validateID(urlParts[IDX_SEASON_ID], SEASON_ID_MIN, SEASON_ID_MAX));
      this.dayID = String.valueOf(
          Validator.validateID(urlParts[IDX_DAY_ID], DAY_ID_MIN, DAY_ID_MAX));
    } catch (InvalidInputsException e) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }
  }


  public int getResortID() {
    return this.resortID;
  }

  public String getSeasonID() {
    return this.seasonID;
  }

  public String getDayID() {
    return this.dayID;
  }

  public int getSkierID() {
    return this.skierID;
  }

  public boolean isVerticalUrl() {
    return this.urlPath.split("/").length == 3;
  }

  public boolean isLongUrl() {
    return this.urlPath.split("/").length == 8;
  }

  @Override
  public String toString() {
    return "UrlPathParser{" +
        "resortID=" + resortID +
        ", seasonID='" + seasonID + '\'' +
        ", dayID='" + dayID + '\'' +
        ", skierID=" + skierID +
        '}';
  }
}
