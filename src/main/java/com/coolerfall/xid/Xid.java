package com.coolerfall.xid;

import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class Xid {
  private static final byte[] ENCODING =
      {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
          'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v'};
  private static final int RAW_LEN = 12;
  private static final int ENCODE_LEN = 20;
  private static AtomicInteger ID_COUNTER = new AtomicInteger(randInt());
  private final byte[] id = new byte[RAW_LEN];

  /**
   * Keep single instance.
   */
  private Xid() {
    byte[] mid = HostId.readMachineId();
    int timestamp = (int) (System.currentTimeMillis() / 1000);
    String processName = ManagementFactory.getRuntimeMXBean().getName();
    int pid = Integer.parseInt(processName.split("@")[0]);
    int i = ID_COUNTER.incrementAndGet();

    id[0] = (byte) (timestamp >> 24);
    id[1] = (byte) (timestamp >> 16);
    id[2] = (byte) (timestamp >> 8);
    id[3] = (byte) (timestamp);
    id[4] = mid[0];
    id[5] = mid[1];
    id[6] = mid[2];
    id[7] = (byte) (pid >> 8);
    id[8] = (byte) pid;
    id[9] = (byte) (i >> 16);
    id[10] = (byte) (i >> 8);
    id[11] = (byte) i;
  }

  public static Xid get() {
    return new Xid();
  }

  @Override public String toString() {
    return new String(encode());
  }

  private byte[] encode() {
    byte[] result = new byte[ENCODE_LEN];
    result[19] = ENCODING[(toUnsigned(11) << 4) & 0x1f];
    result[18] = ENCODING[(toUnsigned(11) >> 1) & 0x1f];
    result[17] = ENCODING[(toUnsigned(11) >> 6) & 0x1f | (toUnsigned(10) << 2) & 0x1f];
    result[16] = ENCODING[toUnsigned(10) >> 3];
    result[15] = ENCODING[toUnsigned(9) & 0x1f];
    result[14] = ENCODING[(toUnsigned(9) >> 5) | (toUnsigned(8) << 3) & 0x1f];
    result[13] = ENCODING[(toUnsigned(8) >> 2) & 0x1f];
    result[12] = ENCODING[toUnsigned(8) >> 7 | (toUnsigned(7) << 1) & 0x1f];
    result[11] = ENCODING[(toUnsigned(7) >> 4) & 0x1f | (toUnsigned(6) << 4) & 0x1f];
    result[10] = ENCODING[(toUnsigned(6) >> 1) & 0x1f];
    result[9] = ENCODING[(toUnsigned(6) >> 6) & 0x1f | (toUnsigned(5) << 2) & 0x1f];
    result[8] = ENCODING[toUnsigned(5) >> 3];
    result[7] = ENCODING[toUnsigned(4) & 0x1f];
    result[6] = ENCODING[toUnsigned(4) >> 5 | (toUnsigned(3) << 3) & 0x1f];
    result[5] = ENCODING[(toUnsigned(3) >> 2) & 0x1f];
    result[4] = ENCODING[toUnsigned(3) >> 7 | (toUnsigned(2) << 1) & 0x1f];
    result[3] = ENCODING[(toUnsigned(2) >> 4) & 0x1f | (toUnsigned(1) << 4) & 0x1f];
    result[2] = ENCODING[(toUnsigned(1) >> 1) & 0x1f];
    result[1] = ENCODING[(toUnsigned(1) >> 6) & 0x1f | (toUnsigned(0) << 2) & 0x1f];
    result[0] = ENCODING[toUnsigned(0) >> 3];

    return result;
  }

  private int toUnsigned(int index) {
    int data = id[index];
    return data < 0 ? data & 0xff : data;
  }

  private static int randInt() {
    byte[] data = new byte[3];
    new Random().nextBytes(data);
    return toUnsigned(data[0]) << 16 | toUnsigned(data[1]) << 8 | toUnsigned(data[2]);
  }

  private static int toUnsigned(byte data) {
    return data < 0 ? data & 0xff : data;
  }
}
