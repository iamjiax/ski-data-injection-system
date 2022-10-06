package edu.neu.cs6650.handler;

import static edu.neu.cs6650.util.Constants.*;

import edu.neu.cs6650.util.Validator;
import edu.neu.cs6650.exceptions.InvalidUrlException;
import edu.neu.cs6650.model.LiftRide;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/skiers")
public class SkierController {

  @GetMapping(value = "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}", produces = CONTENT_TYPE_JSON)
  public int getSkierDayVertical(
      @PathVariable("resortID") int resortID,
      @PathVariable("seasonID") String seasonID,
      @PathVariable("dayID") String dayID,
      @PathVariable("skierID") int skierID) throws InvalidUrlException {

    Validator.validateID(resortID, RESORT_ID_MIN, RESORT_ID_MAX);
    Validator.validateStrID(seasonID, SEASON_ID_MIN, SEASON_ID_MAX);
    Validator.validateStrID(dayID, DAY_ID_MIN, DAY_ID_MAX);
    Validator.validateID(skierID, SKIER_ID_MIN, SKIER_ID_MAX);

    int result = 34507;
    return result;
  }

  @PostMapping(value = "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}", produces = CONTENT_TYPE_JSON)
  @ResponseStatus(HttpStatus.CREATED)
  public void postNewListRide(
      @PathVariable("resortID") int resortID,
      @PathVariable("seasonID") String seasonID,
      @PathVariable("dayID") String dayID,
      @PathVariable("skierID") int skierID,
      @RequestBody LiftRide liftRide) throws InvalidUrlException {

    Validator.validateID(resortID, RESORT_ID_MIN, RESORT_ID_MAX);
    Validator.validateStrID(seasonID, SEASON_ID_MIN, SEASON_ID_MAX);
    Validator.validateStrID(dayID, DAY_ID_MIN, DAY_ID_MAX);
    Validator.validateID(skierID, SKIER_ID_MIN, SKIER_ID_MAX);

    Validator.validateID(liftRide.getLiftID(), LIFT_ID_MIN, LIFT_ID_MAX);
    Validator.validateID(liftRide.getTime(), TIME_MIN, TIME_MAX);
  }

}
