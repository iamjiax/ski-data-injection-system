package edu.neu.cs6650;

import static edu.neu.cs6650.Constants.*;

import edu.neu.cs6650.exceptions.InvalidUrlException;
import edu.neu.cs6650.exceptions.MissingPathParametersException;
import javax.servlet.http.HttpServletRequest;

public class UrlPathParser {

  private int resortID;
  private int seasonID;
  private int dayID;
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
      throws InvalidUrlException, MissingPathParametersException {
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

    this.resortID = validateID(urlParts[IDX_RESORT_ID], RESORT_ID_MIN, RESORT_ID_MAX);
    this.seasonID = validateID(urlParts[IDX_SEASON_ID], SEASON_ID_MIN, SEASON_ID_MAX);
    this.dayID = validateID(urlParts[IDX_DAY_ID], DAY_ID_MIN, DAY_ID_MAX);
    this.skierID = validateID(urlParts[IDX_SKIER_ID], SKIER_ID_MIN, SKIER_ID_MAX);
  }

  private int validateID(String idStr, int idMin, int idMax)
      throws InvalidUrlException {
    int id;
    try {
      id = Integer.parseInt(idStr);
    } catch (NumberFormatException e) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }

    if (id < idMin || id > idMax) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }
    return id;
  }

  public int getResortID() {
    return this.resortID;
  }

  public int getSeasonID() {
    return this.seasonID;
  }

  public int getDayID() {
    return this.dayID;
  }

  public int getSkierID() {
    return this.skierID;
  }
}
