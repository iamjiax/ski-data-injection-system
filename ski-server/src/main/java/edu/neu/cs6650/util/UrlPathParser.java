package edu.neu.cs6650.util;

import static edu.neu.cs6650.util.Constants.*;

import edu.neu.cs6650.exceptions.InvalidInputsException;
import edu.neu.cs6650.exceptions.InvalidUrlException;
import edu.neu.cs6650.exceptions.MissingPathParametersException;
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

    switch (endpoint) {
      case ENDPOINT_SKIERS:
        validateSkiersUrl(method, urlPath);
        break;
//      case ENDPOINT_RESORTS:
//        //
//        break;
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

  private void validateSkiersUrl(String method, String urlPath)
      throws InvalidUrlException, MissingPathParametersException {

    if (urlPath == null || urlPath.isEmpty()) {
      throw new MissingPathParametersException(MSG_MISSING_PATH_PARA);
    }

    String[] urlParts = urlPath.split("/");

    switch (method) {
      case POST:
        validateSkiersLongUrl(urlParts);
        break;
      case GET:
        // TODO: vertical url
        validateSkiersLongUrl(urlParts);
        break;
      default:
        throw new InvalidUrlException(MSG_PAGE_NOT_EXISTS);
    }
  }


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

}
