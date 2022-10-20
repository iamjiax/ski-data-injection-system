package edu.neu.cs6650.util;

import static edu.neu.cs6650.util.Constants.MSG_INVALID_PATH_PARA;

import edu.neu.cs6650.exceptions.InvalidUrlException;

public class Validator {
  public static int validateID(int id, int idMin, int idMax)
      throws InvalidUrlException {
    if (id < idMin || id > idMax) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }
    return id;
  }

  public static int validateID(String idStr, int idMin, int idMax)
      throws InvalidUrlException {
    int id;
    try {
      id = Integer.parseInt(idStr);
    } catch (NumberFormatException e) {
      throw new InvalidUrlException(MSG_INVALID_PATH_PARA);
    }
    return validateID(id, idMin, idMax);
  }

}
