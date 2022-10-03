package edu.neu.cs6650.servlet;

import static edu.neu.cs6650.Constants.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import edu.neu.cs6650.exceptions.InvalidInputsException;
import edu.neu.cs6650.model.LiftRide;
import edu.neu.cs6650.model.LiftRideData;

import edu.neu.cs6650.model.LiftRideData.LiftRideDataBuilder;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import edu.neu.cs6650.util.UrlPathParser;
import edu.neu.cs6650.exceptions.InvalidUrlException;
import edu.neu.cs6650.exceptions.MissingPathParametersException;

@WebServlet(name = "SkierServlet", value = "/skiers/*")
public class SkierServlet extends HttpServlet {

  private Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType(CONTENT_TYPE_JSON);

    UrlPathParser urlPathParser;
    try {
      urlPathParser = new UrlPathParser(req, ENDPOINT_SKIERS);
    } catch (InvalidUrlException | MissingPathParametersException e) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write(getMsgJson(e.getMessage()));
      return;
    }

    int result = 34507;
    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().write(gson.toJson(result));
  }


  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType(CONTENT_TYPE_JSON);

    UrlPathParser urlPathParser;
    try {
      urlPathParser = new UrlPathParser(req, ENDPOINT_SKIERS);
    } catch (InvalidUrlException | MissingPathParametersException e) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write(getMsgJson(e.getMessage()));
      return;
    }

    LiftRideData liftRideData;
    try {
      String reqBody = getRequestBody(req);
      LiftRide liftRideBody = gson.fromJson(reqBody, LiftRide.class);

      liftRideData = new LiftRideDataBuilder(liftRideBody.getLiftID(), liftRideBody.getTime())
          .resortID(urlPathParser.getResortID())
          .seasonID(urlPathParser.getSeasonID())
          .dayID(urlPathParser.getDayID())
          .skierID(urlPathParser.getSkierID())
          .build();

    } catch (InvalidInputsException | JsonSyntaxException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(getMsgJson(MSG_INVALID_INPUTS));
      return;
    }

    String liftRideJsonStr = gson.toJson(liftRideData);
//    System.out.println("Get valid POST request: " + liftRideJsonStr);

    res.setStatus(HttpServletResponse.SC_CREATED);
  }

  private String getRequestBody(HttpServletRequest req)
      throws IOException {
    return req.getReader().lines().collect(Collectors.joining());
  }

  private String getMsgJson(String message) {
    return "{\"message\": \"" + message + "\"}";
  }
}
