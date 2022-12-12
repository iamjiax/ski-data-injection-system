package edu.neu.cs6650.servlet;

import static edu.neu.cs6650.util.Constants.*;

import edu.neu.cs6650.database.RedisClient;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import org.apache.commons.pool2.impl.GenericObjectPool;

import edu.neu.cs6650.util.*;
import edu.neu.cs6650.exceptions.*;
import edu.neu.cs6650.model.*;
import edu.neu.cs6650.model.LiftRideData.LiftRideDataBuilder;

@WebServlet(name = "SkierServlet", value = "/skiers/*")
public class SkierServlet extends HttpServlet {

  private ConnectionFactory Connectionfactory;
  private Connection connection;
  private GenericObjectPool<Channel> channelPool;
  private RedisClient redisClient;
  private Gson gson;


  @Override
  public void init() throws ServletException {
    this.Connectionfactory = new ConnectionFactory();
    try {
      this.Connectionfactory.setUri(MQ_URI_VPC_PRIVATE);
//      this.Connectionfactory.setUri(MQ_URI_VPC_PRIVATE);
      this.connection = this.Connectionfactory.newConnection();
      this.channelPool = new GenericObjectPool<>(new ChannelFactory(connection));
      this.channelPool.setMaxTotal(MAX_CHANNEL_NUM);
    } catch (Exception e) {
      throw new ServletException(ERROR_MQ_CONNECTION, e);
    }

    this.redisClient = new RedisClient(REDIS_URI_VPC_PRIVATE);
    this.gson = new Gson();
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

    String vertical = null;
    if (urlPathParser.isVerticalUrl()) {
      vertical = this.redisClient.getTotalVertical(
          String.valueOf(urlPathParser.getSkierID()),
          String.valueOf(urlPathParser.getResortID()),
          urlPathParser.getSeasonID());
    } else if (urlPathParser.isLongUrl()) {
      vertical = this.redisClient.getTotalVerticalForSomeDay(
          String.valueOf(urlPathParser.getResortID()),
          urlPathParser.getSeasonID(),
          urlPathParser.getDayID(),
          String.valueOf(urlPathParser.getSkierID())
      );
    }

    if (vertical == null) {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(getMsgJson(ERROR_REDIS));
      return;
    }
    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().write(gson.toJson(vertical));
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
//    System.out.println("Get valid POST request: " + liftRideJsonStr);

    Channel channel = null;
    try {
      channel = this.channelPool.borrowObject();
      channel.basicPublish("", QUEUE_NAME, null, liftRideJsonStr.getBytes());
//      System.out.println(" [x] Sent '" + liftRideJsonStr + "'");
//      System.out.println("_________________________________");
//      System.out.println("Channel pool active num: " + this.channelPool.getNumActive());
//      System.out.println("Channel pool idle num: " + this.channelPool.getNumIdle());
//      System.out.println("Channel wait num: " + this.channelPool.getNumWaiters());
//      System.out.println("_________________________________");
      this.channelPool.returnObject(channel);
    } catch (Exception e) {
      e.printStackTrace();
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      res.getWriter().write(getMsgJson(ERROR_MQ_TIMEOUT));
      if (channel != null) {
        this.channelPool.returnObject(channel);
      }
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
