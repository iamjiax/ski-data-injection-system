package edu.neu.cs6650.servlet;

import static edu.neu.cs6650.util.Constants.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import edu.neu.cs6650.util.*;
import edu.neu.cs6650.exceptions.*;
import edu.neu.cs6650.model.*;
import edu.neu.cs6650.model.LiftRideData.LiftRideDataBuilder;

@WebServlet(name = "SkierServlet", value = "/skiers/*")
public class SkierServlet extends HttpServlet {

  protected ConnectionFactory factory;
  protected Connection connection;
  protected Gson gson;

  @Override
  public void init() throws ServletException {
    factory = new ConnectionFactory();
    try {
      factory.setUri(MQ_URI);
      connection = factory.newConnection();
    } catch (Exception e) {
      throw new ServletException(ERROR_MQ_CONNECTION, e);
    }
    gson = new Gson();
  }

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

    LiftRide liftRideBody;
    try {
      String reqBody = getRequestBody(req);
      liftRideBody = gson.fromJson(reqBody, LiftRide.class);
      Validator.validateID(liftRideBody.getLiftID(), LIFT_ID_MIN, LIFT_ID_MAX);
      Validator.validateID(liftRideBody.getTime(), TIME_MIN, TIME_MAX);
    } catch (InvalidInputsException | JsonSyntaxException e) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(getMsgJson(MSG_INVALID_INPUTS));
      return;
    }

    LiftRideData liftRideData = new LiftRideDataBuilder(liftRideBody.getLiftID(), liftRideBody.getTime())
        .resortID(urlPathParser.getResortID())
        .seasonID(urlPathParser.getSeasonID())
        .dayID(urlPathParser.getDayID())
        .skierID(urlPathParser.getSkierID())
        .build();

    String liftRideJsonStr = gson.toJson(liftRideData);
    System.out.println("Get valid POST request: " + liftRideJsonStr);

    try (Channel channel = connection.createChannel()) {
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      channel.basicPublish("", QUEUE_NAME, null, liftRideJsonStr.getBytes());
      System.out.println(" [x] Sent '" + liftRideJsonStr + "'");
    } catch (TimeoutException e) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(getMsgJson(ERROR_MQ_TIMEOUT));
      return;
    }

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
