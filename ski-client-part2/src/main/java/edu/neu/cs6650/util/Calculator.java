package edu.neu.cs6650.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import edu.neu.cs6650.model.RecordData;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Calculator {

  private String filePath;
  private int mean;
  private int median;
  private int p99;
  private int min;
  private int max;

  public void calculate(String filePath) {
    this.filePath = filePath;
    List<RecordData> dataList = readData(filePath);
    Collections.sort(dataList, Comparator.comparingInt(RecordData::getLatency));
    this.mean = (int) Math.round(
        dataList.stream().mapToInt(r -> r.getLatency()).average().orElse(0));

    if (dataList.size() % 2 != 0) {
      this.median = dataList.get(dataList.size() / 2).getLatency();
    } else {
      this.median = (dataList.get(dataList.size() / 2 - 1).getLatency()
          + dataList.get(dataList.size() / 2).getLatency()) / 2;
    }
    this.p99 = dataList.get((int)(dataList.size() * 0.99)).getLatency();
    this.min = dataList.get(0).getLatency();
    this.max = dataList.get(dataList.size() - 1).getLatency();
  }

  private List<RecordData> readData(String filePath) {
    List<RecordData> dataList = new ArrayList<>();
    try {
      CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath)).withSkipLines(1).build();
      String[] nextRecord;

      while ((nextRecord = csvReader.readNext()) != null) {
        dataList.add(new RecordData(
            Long.parseLong(nextRecord[0]),
            nextRecord[1],
            Integer.parseInt(nextRecord[2]),
            Integer.parseInt(nextRecord[3])));
      }
      csvReader.close();
    } catch (FileNotFoundException e) {
      System.err.println("File not found.");
      throw new RuntimeException(e);
    } catch (IOException ie) {
      throw new RuntimeException(ie);
    }
    return dataList;
  }

  public String getFilePath() {
    return this.filePath;
  }

  public long getMean() {
    return this.mean;
  }

  public long getMedian() {
    return this.median;
  }

  public long getP99() {
    return this.p99;
  }

  public int getMin() {
    return this.min;
  }

  public int getMax() {
    return this.max;
  }
}
