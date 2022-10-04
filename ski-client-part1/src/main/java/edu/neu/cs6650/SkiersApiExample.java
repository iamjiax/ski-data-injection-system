package edu.neu.cs6650;

import static edu.neu.cs6650.Constants.*;

import edu.neu.cs6650.model.LiftRideData;
import edu.neu.cs6650.util.LiftRideGenerator;
import io.swagger.client.*;
import io.swagger.client.api.SkiersApi;

public class SkiersApiExample {

  private static final int TEST_NUM = 10000;

  public static void main(String[] args) {

    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(SERVER_PATH);
    SkiersApi skiersApi = new SkiersApi(apiClient);
    long start = System.currentTimeMillis();

    for (int i = 0; i < TEST_NUM; i++) {
      LiftRideData liftRideData = LiftRideGenerator.randomLiftRideData();
      try {
        // POST
        skiersApi.writeNewLiftRide(
            liftRideData.getLiftRide(),
            liftRideData.getResortID(),
            String.valueOf(liftRideData.getSeasonID()),
            String.valueOf(liftRideData.getDayID()),
            liftRideData.getSkierID());
      } catch (ApiException e) {
        System.err.println(e.getCode() + ": " + e.getResponseBody());
        e.printStackTrace();
      }
    }

    long end = System.currentTimeMillis();
    System.out.println("Total run time: " + (end - start) + " ms");
    long latency = (end - start) / TEST_NUM;
    System.out.println("Average latency: " + latency + " ms");
    int throughput32 = (int) (32 / ((float)latency / 1000));
    System.out.println("Expected throughput for 32 threads: " + throughput32);
    int throughput200 = (int) (200 / ((float)latency / 1000));
    System.out.println("Expected throughput for 200 threads: " + throughput200);
  }
}
