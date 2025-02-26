package org.bedework.hlc.ro;

import org.bedework.base.response.GetEntityResponse;
import org.bedework.calfacade.BwCalendar;
import org.bedework.calfacade.BwGroup;
import org.bedework.hlc.common.HighLevelClient;

import java.util.Collection;

/** Provides high level operations on the calendar store.
 * The client may be operating in guest mode or as an
 * authenticated user.
 */
public interface ReadOnlyHighLevelClient extends HighLevelClient {
  /* ------------------------------------------------------------
   *                     Admin Groups
   * ------------------------------------------------------------ */

  /**
   * @return groups
   */
  Collection<BwGroup<?>> getAdminGroups();

  // ================== Collections ================

  /** This varies depending on:<ul>
   * <li><em>guest:</em>public collections.</li>
   * <li><em>regular user:</em>/user/xxx/</li>
   * <li><em>web submit/public auth:</em>calsuite home</li>
   * <li><em>admin:</em>current calsuite home</li>
   * </ul>
   *
   * @return appropriate home collection
   */
  GetEntityResponse<BwCalendar> getHomeCollection();

  /**
   *
   * @return the cloned root of the public collection tree.
   */
  GetEntityResponse<BwCalendar> getPublicCollection();

  /**
   * @param path for collection
   * @return response with collection object or error.
   */
  GetEntityResponse<BwCalendar> getCollection(String path);
}
