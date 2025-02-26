package org.bedework.hlc.admin.impl;

import org.bedework.hlc.admin.AdminHighLevelClient;
import org.bedework.hlc.rw.impl.ReadWriteHighLevelClientImpl;
import org.bedework.llc.common.LowLevelClient;

public class AdminHighLevelClientImpl
        extends ReadWriteHighLevelClientImpl
        implements AdminHighLevelClient {
  public AdminHighLevelClientImpl(final LowLevelClient cl) {
    super(cl);
  }
}
