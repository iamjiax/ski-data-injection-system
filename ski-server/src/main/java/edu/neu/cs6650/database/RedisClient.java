package edu.neu.cs6650.database;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient {

  private final JedisPool jedisPool;

  public RedisClient(String redisUri) {
    this.jedisPool = new JedisPool(redisUri);
  }

  public String getTotalVerticalForSomeDay(String resortId, String seasonId, String dayId,
      String skierId) {
    try (Jedis jedis = this.jedisPool.getResource()) {
      // Example "resort_season_day_skier:1_1_1_90213:totalVertical"
      String queryKey =
          "resort_season_day_skier:" + resortId + "_" + seasonId + "_" + dayId + "_" + skierId
              + ":totalVertical";
      return jedis.get(queryKey);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public String getTotalVertical(String skierId, String resortId, String seasonId) {
    try (Jedis jedis = this.jedisPool.getResource()) {
      String result = null;
      if (resortId != null && seasonId != null) {
        // Example "skier_resort_season:90213_1_1:totalVertical"
        result = jedis.get(
            "skier_resort_season:" + skierId + "_" + resortId + "_" + seasonId + ":totalVertical");
      }

      if (resortId != null) {
        // Example "skier_resort:90213_1:totalVertical"
        result = jedis.get("skier_resort:" + skierId + "_" + resortId + ":totalVertical");
      }
      return result == null ? "0" : result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public long getNumOfSkiers(String resortId, String seasonId, String dayId) {
    try (Jedis jedis = this.jedisPool.getResource()) {
      // Example "resort_season_day:1_1_1:skiersSet"
      String queryKey =
          "resort_season_day:" + resortId + "_" + seasonId + "_" + dayId + ":skiersSet";
      return jedis.scard(queryKey);
    }
  }
}
