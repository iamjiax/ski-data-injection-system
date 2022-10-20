package edu.neu.cs6650.model;

import static edu.neu.cs6650.Constants.*;

import io.swagger.client.model.LiftRide;
import java.util.Objects;

public class LiftRideData {

  private LiftRide liftRide;
  private int resortID;
  private String seasonID;
  private String dayID;
  private int skierID;

  private LiftRideData(LiftRideDataBuilder builder) {
    this.liftRide = builder.liftRide;
    this.resortID = builder.resortID;
    this.seasonID = builder.seasonID;
    this.dayID = builder.dayID;
    this.skierID = builder.skierID;
  }

  public LiftRide getLiftRide() {
    return this.liftRide;
  }

  public int getResortID() {
    return this.resortID;
  }

  public String getSeasonID() {
    return this.seasonID;
  }

  public String getDayID() {
    return this.dayID;
  }

  public int getSkierID() {
    return this.skierID;
  }

  public static class LiftRideDataBuilder {

    private LiftRide liftRide;
    private int resortID;
    private String seasonID;
    private String dayID;
    private int skierID;

    public LiftRideDataBuilder(int liftID, int time) {
      this.liftRide = new LiftRide()
          .liftID(validate(liftID, LIFT_ID_MIN, LIFT_ID_MAX))
          .time(validate(time, TIME_MIN, TIME_MAX));
    }

    public LiftRideDataBuilder resortID(int resortID) {
      this.resortID = resortID;
      return this;
    }

    public LiftRideDataBuilder seasonID(String seasonID) {
      this.seasonID = seasonID;
      return this;
    }

    public LiftRideDataBuilder dayID(String dayID) {
      this.dayID = dayID;
      return this;
    }

    public LiftRideDataBuilder skierID(int skierID) {
      this.skierID = skierID;
      return this;
    }

    public LiftRideData build() {
      return new LiftRideData(this);
    }

    private int validate(int value, int minValue, int maxValue) {
      if (value < minValue || value > maxValue) {
        throw new IllegalArgumentException();
      }
      return value;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LiftRideData that = (LiftRideData) o;
    return getResortID() == that.getResortID() && getSeasonID() == that.getSeasonID()
        && getDayID() == that.getDayID() && getSkierID() == that.getSkierID()
        && getLiftRide().equals(
        that.getLiftRide());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getLiftRide(), getResortID(), getSeasonID(), getDayID(), getSkierID());
  }

  @Override
  public String toString() {
    return "LiftRideInfo{" +
        "liftRide=" + liftRide +
        ", resortID=" + resortID +
        ", seasonID=" + seasonID +
        ", dayID=" + dayID +
        ", skierID=" + skierID +
        '}';
  }
}
