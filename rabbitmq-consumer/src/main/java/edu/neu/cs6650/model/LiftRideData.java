package edu.neu.cs6650.model;

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

  public String getSkierDayKey() {
    return "skierID:" + this.getSkierID() + ":"
        + "seasonID:" + this.getSeasonID() + ":"
        + "dayID:" + this.getDayID();
  }

  public String getSkierSeasonKey() {
    return "skierID:" + this.getSkierID() + ":"
        + "seasonID:" + this.getSeasonID();
  }

  public String getResortDaySkiersKey() {
    return "resortID:" + this.getResortID() + ":"
        + "seasonID:" + this.getSeasonID() + ":"
        + "dayID:" + this.getDayID() + ":skiers";
  }

  public static class LiftRideDataBuilder {

    private LiftRide liftRide;
    private int resortID;
    private String seasonID;
    private String dayID;
    private int skierID;

    public LiftRideDataBuilder(int liftID, int time) {
      this.liftRide = new LiftRide(liftID, time);
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
    return "LiftRideData{" +
        "liftRide=" + liftRide +
        ", resortID=" + resortID +
        ", seasonID=" + seasonID +
        ", dayID=" + dayID +
        ", skierID=" + skierID +
        '}';
  }
}
