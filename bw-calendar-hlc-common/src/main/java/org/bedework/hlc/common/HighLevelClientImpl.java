package org.bedework.hlc.common;

import org.bedework.calfacade.BwPrincipal;
import org.bedework.llc.common.ClientTypes.ClientType;
import org.bedework.llc.common.LowLevelClient;
import org.bedework.util.logging.BwLogger;

public class HighLevelClientImpl implements HighLevelClient {
  protected final LowLevelClient cl;

  public HighLevelClientImpl(final LowLevelClient cl) {
    this.cl = cl;
  }

  public LowLevelClient getClient() {
    return cl;
  }

  @Override
  public boolean isGuestMode() {
    final var type = cl.getClientType();
    return (getCurrentPrincipal() == null) ||
            (type == ClientType.guest) ||
            (type == ClientType.publick) ||
            (type == ClientType.feeder) ||
            (type == ClientType.publicAuth);
  }

  @Override
  public BwPrincipal<?> getCurrentPrincipal() {
    return cl.getCurrentPrincipal();
  }

  /* ==========================================================
   *                   Logged methods
   * ========================================================== */

  private final BwLogger logger = new BwLogger();

  @Override
  public BwLogger getLogger() {
    if ((logger.getLoggedClass() == null) && (logger.getLoggedName() == null)) {
      logger.setLoggedClass(getClass());
    }

    return logger;
  }
}
