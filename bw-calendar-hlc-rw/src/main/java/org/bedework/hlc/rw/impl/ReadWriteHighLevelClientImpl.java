package org.bedework.hlc.rw.impl;

import org.bedework.hlc.ro.impl.ReadOnlyHighLevelClientImpl;
import org.bedework.hlc.rw.ReadWriteHighLevelClient;
import org.bedework.llc.common.LowLevelClient;

public class ReadWriteHighLevelClientImpl
        extends ReadOnlyHighLevelClientImpl
        implements ReadWriteHighLevelClient {
  public ReadWriteHighLevelClientImpl(final LowLevelClient cl) {
    super(cl);
  }
}
