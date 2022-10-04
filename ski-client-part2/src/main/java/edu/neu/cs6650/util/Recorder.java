package edu.neu.cs6650.util;

import static edu.neu.cs6650.Constants.*;

import com.opencsv.CSVWriter;
import edu.neu.cs6650.model.RecordData;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Recorder implements Runnable {

  private final BlockingQueue<RecordData> recordQueue;
  private final CountDownLatch recordDoneSignal;
  private File file;

  public Recorder(BlockingQueue<RecordData> recordQueue, CountDownLatch recordDoneSignal, String filePath) {
    this.recordQueue = recordQueue;
    this.recordDoneSignal = recordDoneSignal;
    this.file = new File(filePath);
  }


  @Override
  public void run() {
    try {
      CSVWriter writer = new CSVWriter(new FileWriter(this.file));
      writer.writeNext(RECORD_HEADER);

      RecordData recordData = recordQueue.take();
      while (recordData.getResponseCode() != END_RECORD_CODE) {
        writer.writeNext(recordData.getData());
        recordData = recordQueue.take();
      }

      writer.close();
    } catch (InterruptedException | IOException e) {
      throw new RuntimeException(e);
    }
    this.recordDoneSignal.countDown();
  }


}
