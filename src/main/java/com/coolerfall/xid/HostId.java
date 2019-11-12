package com.coolerfall.xid;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class HostId {
  private static final Path PATH = Paths.get("/sys/class/dmi/id/product_uuid");
  private static final Random RANDOM = new Random();

  /**
   * Read machine id for current machine.
   *
   * @return machine id bytes
   */
  static byte[] readMachineId() {
    byte[] id = new byte[3];
    byte[] mid;

    try {
      mid = Files.readAllBytes(PATH);
      if (mid.length == 0) {
        mid = readFallbackId();
      }
    } catch (Exception e) {
      mid = readFallbackId();
    }

    if (mid.length == 0) {
      RANDOM.nextBytes(id);
    } else {
      try {
        byte[] data = MessageDigest.getInstance("MD5").digest(mid);
        id = Arrays.copyOf(data, 3);
      } catch (NoSuchAlgorithmException e) {
        RANDOM.nextBytes(id);
      }
    }

    return id;
  }

  private static byte[] readFallbackId() {
    try {
      return InetAddress.getLocalHost().getHostName().getBytes();
    } catch (Exception e) {
      return new byte[0];
    }
  }
}
