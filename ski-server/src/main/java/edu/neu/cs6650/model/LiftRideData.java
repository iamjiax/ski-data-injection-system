package edu.neu.cs6650.model;

import static edu.neu.cs6650.Constants.*;

import edu.neu.cs6650.exceptions.InvalidInputsException;
import java.util.Objects;

public class LiftRideData {

  private LiftRide liftRide;
  private int resortID;
  private int seasonID;
  private int dayID;
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

  public int getSeasonID() {
    return this.seasonID;
  }

  public int getDayID() {
    return this.dayID;
  }

  public int getSkierID() {
    return this.skierID;
  }

  public static class LiftRideDataBuilder {
    private LiftRide liftRide;
    private int resortID;
    private int seasonID;
    private int dayID;
    private int skierID;

    public LiftRideDataBuilder(int liftID, int time) throws InvalidInputsException {
      this.liftRide = new LiftRide(validate(liftID, LIFT_ID_MIN, LIFT_ID_MAX),
          validate(time, TIME_MIN, TIME_MAX));
    }
    public LiftRideDataBuilder resortID(int resortID) {
      this.resortID = resortID;
      return this;
    }

    public LiftRideDataBuilder seasonID(int seasonID) {
      this.seasonID = seasonID;
      return this;
    }

    public LiftRideDataBuilder dayID(int dayID) {
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

    private int validate(int value, int minValue, int maxValue) throws InvalidInputsException {
      if (value < minValue || value > maxValue) {
        throw new InvalidInputsException(MSG_INVALID_INPUTS);
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
