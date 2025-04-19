package org.bedework.hlc.ro;

import org.bedework.base.response.GetEntitiesResponse;
import org.bedework.base.response.GetEntityResponse;
import org.bedework.calfacade.BwCollection;
import org.bedework.calfacade.BwGroup;
import org.bedework.calsvci.CollectionsI;
import org.bedework.hlc.common.HighLevelClient;

/** Provides high level operations on the calendar store.
 * The client may be operating in guest mode or as an
 * authenticated user.
 */
public interface ReadOnlyHighLevelClient extends HighLevelClient {
  /* -------------------------------------------------
   *                     Admin Groups
   * ------------------------------------------------- */

  /**
   * @return groups
   */
  GetEntitiesResponse<BwGroup<?>> getAdminGroups();

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
  GetEntityResponse<BwCollection> getHomeCollection();

  /**
   *
   * @return the cloned root of the public collection tree.
   */
  GetEntityResponse<BwCollection> getPublicCollection();

  /**
   * @param path for collection
   * @return response with collection object or error.
   */
  GetEntityResponse<BwCollection> getCollection(String path);

  /**
   * @param path of collection
   * @return true if open
   */
  public boolean getCollectionOpenState(String path);

  /**
   * @param path of collection
   * @param open true if open
   */
  public void setCollectionOpenState(String path,
                                     boolean open);

  /**
   *
   * @param val collection
   * @return never null - requestStatus set for not an external subscription.
   */
  CollectionsI.SynchStatusResponse getSynchStatus(BwCollection val);
}
