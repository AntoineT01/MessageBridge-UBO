package com.ubo.tp.message.common.utils;

import java.util.Random;

public class RandomUtils {
  private RandomUtils() {
  }

  private static final Random random = new Random();

  public static int randomInt() {
    return random.nextInt(9999);
  }
}
