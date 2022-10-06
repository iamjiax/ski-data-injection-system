package edu.neu.cs6650.model;

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
}
