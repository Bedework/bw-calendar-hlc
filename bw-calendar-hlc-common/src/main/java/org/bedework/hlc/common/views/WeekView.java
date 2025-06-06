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

package org.bedework.hlc.common.views;

import org.bedework.caldav.util.filter.FilterBase;
import org.bedework.hlc.common.BedeworkDefs;
import org.bedework.hlc.common.CalendarFormatter;
import org.bedework.hlc.common.CalendarInfo;

/** This class represents a week of events. The firstDay and lastDay are set
 * to be the latest and earliest including the curDay.
 *
 * @author  Mike Douglass douglm   rpi.edu
 */
public class WeekView extends TimeView {
  /** Constructor:
   *
   * @param  curDay    MyCalendarVO representing current day.
   * @param  filter    non-null to filter the results.
   */
  public WeekView(final CalendarFormatter curDay,
                  final FilterBase filter) {
    super(curDay.getCalendar(), "Week",
          CalendarInfo.getInstance().getFirstDayOfThisWeek(curDay.getCalendar().getTimeZone(),
                                                           curDay.getCalendar().getTime()),
          CalendarInfo.getInstance().getLastDayOfThisWeek(curDay.getCalendar().getTimeZone(),
                                                          curDay.getCalendar().getTime()),
          curDay.getPrevWeek().getDateDigits(),
          curDay.getNextWeek().getDateDigits(),
          true,  // showdata
          filter);

    viewPeriod = BedeworkDefs.weekView;
  }
}

