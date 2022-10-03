package edu.neu.cs6650;

import edu.neu.cs6650.model.LiftRideData;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

  private final BlockingQueue<LiftRideData> queue;
  private final int count;

  public Producer(BlockingQueue<LiftRideData> queue, int count) {
    this.queue = queue;
    this.count = count;
  }

  public void run() {
    try {
      for (int i = 0; i < this.count; i++) {
        queue.put(produce());
      }
    } catch (InterruptedException ex) {
      throw new RuntimeException(ex);
    }
  }

  private LiftRideData produce() {
    return LiftRideGenerator.randomLiftRideData();
  }

}

