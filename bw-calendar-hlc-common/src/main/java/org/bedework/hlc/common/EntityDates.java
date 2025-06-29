/* ********************************************************************
    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.
*/

package org.bedework.hlc.common;

import org.bedework.base.response.GetEntityResponse;

import java.io.Serializable;

/** The dates and/or duration which define when an entity happens. These are
 * stored in objects which allow manipulation of individual date and time
 * components.
 * <p>
 * This is the base class for entity specific classes.</p?
 */
public class EntityDates implements Serializable {
  protected String principalHref;
  private final CalendarInfo calInfo;

  protected boolean hour24;

  protected int minIncrement;

  /** Used for generating labels
   */
  private TimeDateComponents forLabels;

  /** Constructor
   *
   * @param principalHref of current user
   * @param calInfo calendar info
   * @param hour24 24 hour mode flag
   * @param minIncrement for minutes
   */
  public EntityDates(final String principalHref,
                     final CalendarInfo calInfo,
                     final boolean hour24, final int minIncrement) {
    this.principalHref = principalHref;
    this.calInfo = calInfo;
    this.hour24 = hour24;
    this.minIncrement = minIncrement;
  }

  /**
   * @param val 24 hour mode flag
   */
  public void setHour24(final boolean val) {
    if (val != hour24) {
      forLabels = null;
      hour24 = val;
    }
  }

  /**
   * @return 24 hour mode flag
   */
  public boolean getHour24() {
    return hour24;
  }

  /**
   * @return  TimeDateComponents for labels
   */
  @SuppressWarnings("UnusedDeclaration")
  public TimeDateComponents getForLabels() {
    if (forLabels == null) {
      getNowTimeComponents();
    }

    return forLabels;
  }

  /** Return an initialised TimeDateComponents representing now
   *
   * @return TimeDateComponents  initialised object
   */
  public GetEntityResponse<TimeDateComponents> getNowTimeComponents() {
    try {
      final TimeDateComponents tc =
              new TimeDateComponents(calInfo, minIncrement,
                                     hour24);

      tc.setNow();

      forLabels = tc;

      return new GetEntityResponse<TimeDateComponents>()
              .setEntity(tc);
    } catch (final Throwable t) {
      return new GetEntityResponse<TimeDateComponents>()
              .error(t);
    }
  }
}

