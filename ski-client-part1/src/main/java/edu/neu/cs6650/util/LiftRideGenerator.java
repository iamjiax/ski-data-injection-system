package edu.neu.cs6650.util;

import static edu.neu.cs6650.Constants.*;

import edu.neu.cs6650.model.LiftRideData;
import edu.neu.cs6650.model.LiftRideData.LiftRideDataBuilder;
import java.util.concurrent.ThreadLocalRandom;

public class LiftRideGenerator {
  public static LiftRideData randomLiftRideData() {
    Integer liftID = ThreadLocalRandom.current().nextInt(LIFT_ID_MIN, LIFT_ID_MAX + 1);
    Integer time = ThreadLocalRandom.current().nextInt(TIME_MIN, TIME_MAX + 1);
    Integer resortID = ThreadLocalRandom.current().nextInt(RESORT_ID_MIN, RESORT_ID_MAX + 1);
    String seasonID = SEASON_ID;
    String dayID = DAY_ID;
    Integer skierID = ThreadLocalRandom.current().nextInt(SKIER_ID_MIN, SKIER_ID_MAX + 1);
    return new LiftRideDataBuilder(liftID, time)
        .resortID(resortID)
        .seasonID(seasonID)
        .dayID(dayID)
        .skierID(skierID)
        .build();
  }
}
