package org.bedework.hlc.ro.impl;

import org.bedework.base.response.GetEntityResponse;
import org.bedework.calfacade.BwCalendar;
import org.bedework.calfacade.BwGroup;
import org.bedework.calfacade.BwPrincipal;
import org.bedework.calfacade.svc.BwCalSuite;
import org.bedework.hlc.common.HighLevelClientImpl;
import org.bedework.hlc.ro.ReadOnlyHighLevelClient;
import org.bedework.llc.common.LowLevelClient;
import org.bedework.llc.ro.ReadOnlyLowLevelClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ReadOnlyHighLevelClientImpl
        extends HighLevelClientImpl
        implements ReadOnlyHighLevelClient {
  /* The list of cloned admin groups for the use of the user client
   */
  protected static Collection<BwGroup<?>> adminGroupsInfo;

  protected static Collection<BwGroup<?>> calsuiteAdminGroupsInfo;

  protected static long lastAdminGroupsInfoRefresh;
  static long adminGroupsInfoRefreshInterval = 1000 * 60 * 5;

  private static final Object adminGroupLocker = new Object();

  private static Collection<BwCalSuite> suites;

  public ReadOnlyHighLevelClientImpl(final LowLevelClient cl) {
    super(cl);
  }

  public ReadOnlyLowLevelClient getROCl() {
    return (ReadOnlyLowLevelClient)cl;
  }

  /* ------------------------------------------------------------
   *                     Admin Groups
   * ------------------------------------------------------------ */

  @Override
  public Collection<BwGroup<?>> getAdminGroups()
  {
    return refreshAdminGroupInfo();
  }

  // ================== Collections ================

  @Override
  public GetEntityResponse<BwCalendar> getHomeCollection() {
    return null;
  }

  @Override
  public GetEntityResponse<BwCalendar> getPublicCollection() {
    return null;
  }

  @Override
  public GetEntityResponse<BwCalendar> getCollection(
          final String path) {
    return null;
  }

  protected Collection<BwGroup<?>> refreshAdminGroupInfo() {
    final var res = adminGroupsInfo; // Save in case adminGroupsInfo set to null
    if ((res != null) &&
            (System.currentTimeMillis() < (lastAdminGroupsInfoRefresh +
                                                   adminGroupsInfoRefreshInterval))) {
      return res;
    }

    synchronized (adminGroupLocker) {
      final Set<String> groupHrefs = new TreeSet<>();

      suites = new ArrayList<>();

      for (final BwCalSuite suite: getROCl().getCalSuites()) {
        final BwCalSuite cs = (BwCalSuite)suite.clone();

        // For the moment we skip suites if the group description starts with "INACTIVE"
        final String desc = cs.getGroup().getDescription();

        if ((desc != null) && desc.startsWith("INACTIVE")) {
          continue;
        }

        groupHrefs.add(cs.getGroup().getPrincipalRef());

        cs.setContext(null);
        cs.setDefaultContext(false);

        suites.add(cs);
      }

      adminGroupsInfo = new ArrayList<>();
      calsuiteAdminGroupsInfo = new ArrayList<>();

      final Map<String, BwPrincipal<?>> cloned = new HashMap<>();

      final var ags = getROCl().getAdminGroups(true);

      for (final var g: ags) {
        final var cg = cloneGroup(g, cloned);

        if (groupHrefs.contains(cg.getPrincipalRef())) {
          calsuiteAdminGroupsInfo.add(cg);
        }

        // Get the memberships for this group.
        final var mgs = getROCl().getAllAdminGroups(g);

        for (final var mg: mgs) {
          final var cmg = cloneGroup(mg, cloned);

          cg.addGroup(cmg);
        }

        adminGroupsInfo.add(cg);
      }

      lastAdminGroupsInfoRefresh = System.currentTimeMillis();

      return adminGroupsInfo;
    }
  }

  private BwGroup<?> cloneGroup(final BwGroup<?> g,
                                final Map<String, BwPrincipal<?>> cloned) {
    var cg = (BwGroup<?>)cloned.get(g.getPrincipalRef());

    if (cg != null) {
      return cg;
    }

    cg = g.shallowClone();
    cloned.put(g.getPrincipalRef(), cg);

    final var ms = g.getGroupMembers();
    if (ms == null) {
      return cg;
    }

    for (final var mbr: ms) {
      BwPrincipal<?> cmbr = cloned.get(mbr.getPrincipalRef());

      if (cmbr == null) {
        if (mbr instanceof BwGroup) {
          cmbr = cloneGroup((BwGroup<?>)mbr, cloned);
        } else {
          cmbr = (BwPrincipal<?>)mbr.clone();
        }
        cloned.put(mbr.getPrincipalRef(), cmbr);
      }
      cg.addGroupMember(cmbr);
    }

    return cg;
  }
}
