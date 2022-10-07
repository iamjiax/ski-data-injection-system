package edu.neu.cs6650.util;

import static edu.neu.cs6650.Constants.*;

import edu.neu.cs6650.model.LiftRideData;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Sender implements Runnable {

  private final BlockingQueue<LiftRideData> queue;
  private final SkiersApi skiersApi;
  private final AtomicInteger curCount;
  private final int countToSend;
  private final AtomicInteger failCount;
  private final CountDownLatch doneSignal;
  private final CountDownLatch p2StartSignal;

  public Sender(
      BlockingQueue<LiftRideData> queue,
      String serverPath,
      int countToSend,
      AtomicInteger curCount,
      AtomicInteger failCount,
      CountDownLatch doneSignal) {
    this.queue = queue;
    this.skiersApi = new SkiersApi(new ApiClient().setBasePath(serverPath));
    this.countToSend = countToSend;
    this.curCount = curCount;
    this.failCount = failCount;
    this.doneSignal = doneSignal;
    this.p2StartSignal = null;
  }

  public Sender(
      BlockingQueue<LiftRideData> queue,
      String server_path,
      int countToSend,
      AtomicInteger curCount,
      AtomicInteger failCount,
      CountDownLatch doneSignal,
      CountDownLatch p2StartSignal) {
    this.queue = queue;
    this.skiersApi = new SkiersApi(new ApiClient().setBasePath(server_path));
    this.countToSend = countToSend;
    this.curCount = curCount;
    this.failCount = failCount;
    this.doneSignal = doneSignal;
    this.p2StartSignal = p2StartSignal;
  }

  public void run() {
    try {
      while (this.curCount.getAndIncrement() < this.countToSend) {
        LiftRideData curData = queue.take();
        sendRequest(curData);
      }
      // Decrease the extra increment in the while loop
      this.curCount.getAndDecrement();

      if (this.p2StartSignal != null) {
        this.p2StartSignal.countDown();
      }
      this.doneSignal.countDown();
    } catch (InterruptedException ex) {
      throw new RuntimeException(ex);
    }
  }

  private void sendRequest(LiftRideData liftRideData) {
    int retryTimes = 0;
    while (retryTimes < MAX_RETRY_TIMES) {
      try {
        skiersApi.writeNewLiftRide(
            liftRideData.getLiftRide(),
            liftRideData.getResortID(),
            liftRideData.getSeasonID(),
            liftRideData.getDayID(),
            liftRideData.getSkierID());
        // return when write successfully
        return;
      } catch (ApiException e) {
        System.err.println(e.getCode() + ": " + e.getResponseBody());
        if (e.getCode() >= ERROR_CODE_LOWER_BOUND && e.getCode() < ERROR_CODE_UPPER_BOUND) {
          retryTimes++;
          System.err.println("Retry time: " + retryTimes);
        } else {
          // end retry if other error happens
          break;
        }
      }
    }
    // record fail times
    this.failCount.incrementAndGet();
  }
}
