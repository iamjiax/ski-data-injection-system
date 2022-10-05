package edu.neu.cs6650.util;

import static edu.neu.cs6650.Constants.*;

import edu.neu.cs6650.model.LiftRideData;
import edu.neu.cs6650.model.RecordData;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Sender implements Runnable {

  private final BlockingQueue<LiftRideData> producerQueue;
  private final SkiersApi skiersApi;
  private final AtomicInteger curCount;
  private final int countToSend;
  private final AtomicInteger failCount;
  private final CountDownLatch doneSignal;
  private final CountDownLatch p2StartSignal;
  private final BlockingQueue<RecordData> recordQueue;

  public Sender(
      BlockingQueue<LiftRideData> producerQueue,
      String server_path,
      int countToSend,
      AtomicInteger curCount,
      AtomicInteger failCount,
      CountDownLatch doneSignal,
      BlockingQueue<RecordData> recordQueue) {
    this.producerQueue = producerQueue;
    this.skiersApi = new SkiersApi(new ApiClient().setBasePath(server_path));
    this.countToSend = countToSend;
    this.curCount = curCount;
    this.failCount = failCount;
    this.doneSignal = doneSignal;
    this.p2StartSignal = null;
    this.recordQueue = recordQueue;
  }

  public Sender(
      BlockingQueue<LiftRideData> producerQueue,
      String server_path,
      int countToSend,
      AtomicInteger curCount,
      AtomicInteger failCount,
      CountDownLatch doneSignal,
      CountDownLatch p2StartSignal,
      BlockingQueue<RecordData> recordQueue) {
    this.producerQueue = producerQueue;
    this.skiersApi = new SkiersApi(new ApiClient().setBasePath(server_path));
    this.countToSend = countToSend;
    this.curCount = curCount;
    this.failCount = failCount;
    this.doneSignal = doneSignal;
    this.p2StartSignal = p2StartSignal;
    this.recordQueue = recordQueue;
  }

  public void run() {
    try {
      while (this.curCount.getAndIncrement() < this.countToSend) {
        LiftRideData curData = producerQueue.take();
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

  private void sendRequest(LiftRideData liftRideData) throws InterruptedException {
    int retryTimes = 0;
    while (retryTimes < MAX_RETRY_TIMES) {
      long start = System.currentTimeMillis();
      try {
        skiersApi.writeNewLiftRide(
            liftRideData.getLiftRide(),
            liftRideData.getResortID(),
            String.valueOf(liftRideData.getSeasonID()),
            String.valueOf(liftRideData.getDayID()),
            liftRideData.getSkierID());

        long end = System.currentTimeMillis();
        RecordData recordData = new RecordData(start, POST, (int) (end - start), CODE_POST_SUCCESS);
        this.recordQueue.put(recordData);
        // return when write successfully
        return;

      } catch (ApiException e) {
        System.err.println(e.getCode() + ": " + e.getResponseBody());
        if (e.getCode() >= ERROR_CODE_LOWER_BOUND && e.getCode() < ERROR_CODE_UPPER_BOUND) {

          long end = System.currentTimeMillis();
          RecordData recordData = new RecordData(start, POST, (int) (end - start), e.getCode());
          this.recordQueue.put(recordData);

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
