package com.coolerfall.xid;

import org.junit.Test;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class XidTest {

  @Test public void testXid() {
    for (int i = 0; i < 500; i++) {
      String id = Xid.get().toString();
      System.out.println(id);
    }
  }
}
