package edu.neu.cs6650;

import static edu.neu.cs6650.Constants.*;

import edu.neu.cs6650.model.LiftRideData;
import edu.neu.cs6650.model.RecordData;
import edu.neu.cs6650.util.Calculator;
import edu.neu.cs6650.util.Producer;
import edu.neu.cs6650.util.Recorder;
import edu.neu.cs6650.util.Sender;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    BlockingQueue<LiftRideData> producerQueue = new LinkedBlockingQueue<>(PQ_CAPACITY);
    BlockingQueue<RecordData> recordQueue = new LinkedBlockingQueue<>(RQ_CAPACITY);

    CountDownLatch p2StartSignal = new CountDownLatch(1);
    CountDownLatch doneSignal = new CountDownLatch(P1_THREAD_NUM + P2_THREAD_NUM);
    CountDownLatch recordDoneSignal = new CountDownLatch(1);

    final int p2CountToSend = TOTAL_COUNT - P1_THREAD_NUM * P1_CNT_PER_THREAD;
    // Shared variables
    AtomicInteger p2Count = new AtomicInteger();
    AtomicInteger failCount = new AtomicInteger();

    Producer producer = new Producer(producerQueue, TOTAL_COUNT);
    Recorder recorder = new Recorder(recordQueue, recordDoneSignal, RECORD_FILE);

    // Start thread to generate the data, and write record
    new Thread(producer).start();
    new Thread(recorder).start();

    long start = System.currentTimeMillis();
    // Phase 1
    for (int i = 0; i < P1_THREAD_NUM; i++) {
      // Requests count variable for Current Thread in Phase 1
      AtomicInteger curCount = new AtomicInteger();
      new Thread(new Sender(
          producerQueue,
          SERVER_PATH,
          P1_CNT_PER_THREAD,
          curCount,
          failCount,
          doneSignal,
          p2StartSignal,
          recordQueue
      )).start();
    }

    // Phase 2
    p2StartSignal.await();
    long startP2 = System.currentTimeMillis();
    for (int i = 0; i < P2_THREAD_NUM; i++) {
      new Thread(new Sender(
          producerQueue,
          SERVER_PATH,
          p2CountToSend,
          p2Count,
          failCount,
          doneSignal,
          recordQueue
      )).start();
    }
    doneSignal.await();
    long end = System.currentTimeMillis();

    // After all requests have been sent, put an end record to stop the recording thread
    recordQueue.put(new RecordData(0, "", 0, END_RECORD_CODE));
    recordDoneSignal.await();

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

    // Calculate the metrics
    Calculator calculator = new Calculator();
    calculator.calculate(RECORD_FILE);
    System.out.println("\nMean response time: " + calculator.getMean() + " ms");
    System.out.println("Median response time: " + calculator.getMedian() + " ms");
    System.out.println("Throughput: " + throughput + " /s");
    System.out.println("99th percentile response time: " + calculator.getP99() + " ms");
    System.out.println("Min response time: " + calculator.getMin() + " ms");
    System.out.println("Max response time: " + calculator.getMax() + " ms");
  }
}