package edu.neu.cs6650.model;

import java.util.Objects;

public class LiftRide {

  private int liftID;
  private int time;

  public LiftRide(int liftID, int time) {
    this.liftID = liftID;
    this.time = time;
  }

  public int getLiftID() {
    return this.liftID;
  }

  public int getTime() {
    return this.time;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LiftRide liftRide = (LiftRide) o;
    return getLiftID() == liftRide.getLiftID() && getTime() == liftRide.getTime();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getLiftID(), getTime());
  }

  @Override
  public String toString() {
    return "LiftRide{" +
        "liftID=" + liftID +
        ", time=" + time +
        '}';
  }
}
