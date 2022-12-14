package edu.neu.cs6650.util;

import static edu.neu.cs6650.util.Constants.*;

import redis.clients.jedis.Jedis;

public class JedisTest {

  public static void main(String[] args) {
    Jedis jedis = new Jedis(REDIS_URI);
//    jedis.flushDB();
    System.out.println(jedis.ping());
  }
}
