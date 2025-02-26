package org.bedework.hlc.common;

import org.bedework.calfacade.BwPrincipal;
import org.bedework.util.logging.Logged;

/** All high level client interfaces extend this.
 *
 */
public interface HighLevelClient extends Logged {
  /**
   * @return true for guest mode
   */
  boolean isGuestMode();

  /**
   * @return principal for current user or null for guest mode.
   */
  BwPrincipal<?> getCurrentPrincipal();
}
