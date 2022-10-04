package edu.neu.cs6650.model;

import java.util.Objects;

public class RecordData {

  private final long startTime;
  private final String requestType;
  private final int latency;
  private final int responseCode;

  public RecordData(long startTime, String requestType, int latency, int responseCode) {
    this.startTime = startTime;
    this.requestType = requestType;
    this.latency = latency;
    this.responseCode = responseCode;
  }

  public long getStartTime() {
    return this.startTime;
  }

  public String getRequestType() {
    return this.requestType;
  }

  public int getLatency() {
    return this.latency;
  }

  public int getResponseCode() {
    return this.responseCode;
  }

  public String[] getData() {
    return new String[]{String.valueOf(this.startTime),
        this.requestType,
        String.valueOf(this.latency),
        String.valueOf(this.responseCode)};
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RecordData that = (RecordData) o;
    return startTime == that.startTime && latency == that.latency
        && responseCode == that.responseCode
        && Objects.equals(requestType, that.requestType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startTime, requestType, latency, responseCode);
  }

  @Override
  public String toString() {
    return "RecordData{" +
        "startTime=" + startTime +
        ", requestType='" + requestType + '\'' +
        ", latency=" + latency +
        ", responseCode=" + responseCode +
        '}';
  }
}
