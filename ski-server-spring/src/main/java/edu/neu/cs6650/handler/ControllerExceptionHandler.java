package edu.neu.cs6650.handler;

import edu.neu.cs6650.exceptions.ErrorMessage;
import edu.neu.cs6650.exceptions.InvalidUrlException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(InvalidUrlException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleInvalidUrlException(InvalidUrlException e) {
    return new ErrorMessage(e.getMessage());
  }
}
