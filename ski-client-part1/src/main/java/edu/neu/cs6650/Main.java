package edu.neu.cs6650;

import static edu.neu.cs6650.Constants.*;

import edu.neu.cs6650.model.LiftRideData;
import edu.neu.cs6650.util.Producer;
import edu.neu.cs6650.util.Sender;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    BlockingQueue<LiftRideData> bq = new LinkedBlockingQueue<>(BQ_CAPACITY);
    Producer producer = new Producer(bq, TOTAL_COUNT);

    AtomicInteger failCount = new AtomicInteger();
    CountDownLatch p2StartSignal = new CountDownLatch(1);
    CountDownLatch doneSignal = new CountDownLatch(P1_THREAD_NUM + P2_THREAD_NUM);
    final int p2CountToSend = TOTAL_COUNT - P1_THREAD_NUM * P1_CNT_PER_THREAD;
    // Shared variable for requests count in Phase 2
    AtomicInteger p2Count = new AtomicInteger();

    // Start thread to generate the data
    new Thread(producer).start();

    long start = System.currentTimeMillis();
    // Phase 1
    for (int i = 0; i < P1_THREAD_NUM; i++) {
      // Requests count variable for Current Thread in Phase 1
      AtomicInteger curCount = new AtomicInteger();
      new Thread(new Sender(
          bq,
          SERVER_PATH,
          P1_CNT_PER_THREAD,
          curCount,
          failCount,
          doneSignal,
          p2StartSignal
      )).start();
    }

    // Phase 2
    p2StartSignal.await();
    long startP2 = System.currentTimeMillis();
    for (int i = 0; i < P2_THREAD_NUM; i++) {
      new Thread(new Sender(
          bq,
          SERVER_PATH,
          p2CountToSend,
          p2Count,
          failCount,
          doneSignal
      )).start();
    }
    doneSignal.await();

    long end = System.currentTimeMillis();

    // Information for all phases
    System.out.println("Successful requests: " + (TOTAL_COUNT - failCount.get()));
    System.out.println("Unsuccessful requests: " + failCount);
    System.out.println("Total run time: " + (end - start) + " ms");
    int throughput = (int) (TOTAL_COUNT / (float)(end - start) * 1000);
    System.out.println("Throughput: " + throughput + " /s");

    // Information for Phase 2
    System.out.println("\nRequests for Phase 2: " + p2Count.get());
    System.out.println("Threads count for Phase 2: " + P2_THREAD_NUM);
    System.out.println("Run time for Phase 2: " + (end - startP2) + " ms");
    int throughputP2 = (int) (p2Count.get() / (float)(end - startP2) * 1000);
    System.out.println("Throughput for Phase 2: " + throughputP2 + " /s");

  }
}