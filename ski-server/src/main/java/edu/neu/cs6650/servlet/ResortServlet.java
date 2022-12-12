package edu.neu.cs6650.servlet;

import static edu.neu.cs6650.util.Constants.*;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import edu.neu.cs6650.database.RedisClient;
import edu.neu.cs6650.exceptions.InvalidUrlException;
import edu.neu.cs6650.exceptions.MissingPathParametersException;
import edu.neu.cs6650.util.UrlPathParser;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@WebServlet(name = "ResortServlet", value = "/resorts/*")
public class ResortServlet extends HttpServlet {
  private RedisClient redisClient;
  private Gson gson;
  private Cache<String, Response> cache;


  @Override
  public void init() throws ServletException {
    this.redisClient = new RedisClient(REDIS_URI_VPC_PRIVATE);
    this.gson = new Gson();

    this.cache = Caffeine.newBuilder()
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .maximumSize(100)
        .build();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType(CONTENT_TYPE_JSON);

    /********  No cache   ********/
//    Response response = this.getResponse(req);
    /********  cache   ********/
    Response response = this.cache.get(req.getPathInfo(), path -> this.getResponse(req));
    res.setStatus(response.getStatusCode());
    res.getWriter().write(gson.toJson(response.getMessage()));

  }


  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType(CONTENT_TYPE_JSON);
    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    res.getWriter().write(getMsgJson("Unsupported operation."));
  }

  private String getMsgJson(String message) {
    return "{\"message\": \"" + message + "\"}";
  }

  private Response getResponse(HttpServletRequest req) {
    Response response = new Response();
    UrlPathParser urlPathParser;
    try {
      urlPathParser = new UrlPathParser(req, ENDPOINT_RESORTS);
    } catch (InvalidUrlException | MissingPathParametersException e) {
      response.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
      response.setMessage(getMsgJson(e.getMessage()));
      return response;
    }

    long numOfSkiers = this.redisClient.getNumOfSkiers(
        String.valueOf(urlPathParser.getResortID()),
        urlPathParser.getSeasonID(),
        urlPathParser.getDayID());

    if (numOfSkiers != 0) {
      response.setStatusCode(HttpServletResponse.SC_OK);
      response.setMessage(Long.toString(numOfSkiers));
    } else {
      response.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
      response.setMessage("Data Not Found");
    }
    return response;
  }

}

