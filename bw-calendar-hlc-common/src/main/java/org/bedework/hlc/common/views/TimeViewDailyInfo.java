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

import org.bedework.base.response.GetEntitiesResponse;
import org.bedework.hlc.common.EventFormatter;
import org.bedework.util.logging.BwLogger;
import org.bedework.util.logging.Logged;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;

/** This class allows a TimeView class to provide information about each day
 * represented by that view. This can be used to build grids or tables.
 *
 * <p>This information is always returned as a hierarchical set of these
 * entries with the top being years, containing months, weeks and days.
 *
 * @author  Mike Douglass douglm   rpi.edu
 */
public class TimeViewDailyInfo implements Logged, Serializable {
  /** The view that created this.
   */
  private TimeView view;

  /** Date as Calendar object
   */
  private Calendar cal;

  /** true if this is just a filler - we insert filler into weeks so that they
   always havea  weekful of entries even as we cross month boundaries
   */
  private boolean filler;

  /** true if this is a day entry
   */
  private boolean dayEntry;

  /** More than one entry
   */
  private boolean multiDay;

  /** Date as YYYYMMDD
   */
  private String date;

  /** Short printable date
   */
  private String dateShort;

  /** Long printable date
   */
  private String dateLong;

  /** True if this is in the current month
   */
  private boolean currentMonth;

  /** Is first of period
   */
  private boolean firstDay;

  /** Is last of period
   */
  private boolean lastDay;

  /** Is first of week
   */
  private boolean firstDayOfWeek;

  /** Is last of week
   */
  private boolean lastDayOfWeek;

  /** Is first of month
   */
  private boolean firstDayOfMonth;

  /** Is last of month
   */
  private boolean lastDayOfMonth;

  /** Day of week
   */
  private int dayOfWeek;

  /** Day of month as numeric string
   */
  private int dayOfMonth;

  /** Week of year as numeric string
   */
  private String weekOfYear;

  /** Name of day of month
   */
  private String dayName;

  /** Month as numeric string
   */
  private String month;

  /** Name of month
   */
  private String monthName;

  /** Short name of month
   */
  private String shortMonthName;

  /** Year as numeric string
   */
  private String year;

  /** The entries this contains, if this is years next is months, then weeks
   * then days.
   */
  private TimeViewDailyInfo[] entries;

  /** The events - possibly empty
   */
  private Collection<EventFormatter> events;

  /** Constructor:
   *
   */
  public TimeViewDailyInfo() {
  }

  /** The view that created this.
   *
   * @param val a time view
   */
  public void setView(final TimeView val) {
    view = val;
  }

  /** The view that created this.
   *
   * @return TimeView
   */
  public TimeView getView() {
    return view;
  }

  /**
   * @param val Calendar representing date
   */
  public void setCal(final Calendar val) {
    cal = val;
  }

  /**
   * @param val true if this is just a filler
   */
  public void setFiller(final boolean val) {
    filler = val;
  }

  /**
   * @return boolean
   */
  public boolean getFiller() {
    return filler;
  }

  /**
   * @param val true if this is a day entry
   */
  public void setDayEntry(final boolean val) {
    dayEntry = val;
  }

  /**
   * @return boolean
   */
  public boolean getDayEntry() {
    return dayEntry;
  }

  /**
   * @param val true if this covers multiple days
   */
  public void setMultiDay(final boolean val) {
    multiDay = val;
  }

  /**
   * @return boolean
   */
  public boolean isMultiDay() {
    return multiDay;
  }

  /**
   * @param val the date in default form
   */
  public void setDate(final String val) {
    date = val;
  }

  /**
   * @return String
   */
  public String getDate() {
    return date;
  }

  /**
   * @param val the date in short form.
   */
  public void setDateShort(final String val) {
    dateShort = val;
  }

  /**
   * @return String
   */
  public String getDateShort() {
    return dateShort;
  }

  /**
   * @param val
   */
  public void setDateLong(final String val) {
    dateLong = val;
  }

  /**
   * @return String
   */
  public String getDateLong() {
    return dateLong;
  }

  /**
   * @param val
   */
  public void setCurrentMonth(final boolean val) {
    currentMonth = val;
  }

  /**
   * @return boolean
   */
  public boolean isCurrentMonth() {
    return currentMonth;
  }

  /**
   * @param val
   */
  public void setFirstDay(final boolean val) {
    firstDay = val;
  }

  /**
   * @return boolean
   */
  public boolean isFirstDay() {
    return firstDay;
  }

  /**
   * @param val
   */
  public void setLastDay(final boolean val) {
    lastDay = val;
  }

  /**
   * @return boolean
   */
  public boolean isLastDay() {
    return lastDay;
  }

  /**
   * @param val
   */
  public void setFirstDayOfWeek(final boolean val) {
    firstDayOfWeek = val;
  }

  /**
   * @return boolean
   */
  public boolean isFirstDayOfWeek() {
    return firstDayOfWeek;
  }

  /**
   * @param val
   */
  public void setLastDayOfWeek(final boolean val) {
    lastDayOfWeek = val;
  }

  /**
   * @return boolean
   */
  public boolean isLastDayOfWeek() {
    return lastDayOfWeek;
  }

  /**
   * @param val
   */
  public void setFirstDayOfMonth(final boolean val) {
    firstDayOfMonth = val;
  }

  /**
   * @return boolean
   */
  public boolean isFirstDayOfMonth() {
    return firstDayOfMonth;
  }

  /**
   * @param val
   */
  public void setLastDayOfMonth(final boolean val) {
    lastDayOfMonth = val;
  }

  /**
   * @return boolean
   */
  public boolean isLastDayOfMonth() {
    return lastDayOfMonth;
  }

  /**
   * @param val
   */
  public void setDayOfWeek(final int val) {
    dayOfWeek = val;
  }

  /**
   * @return int
   */
  public int getDayOfWeek() {
    return dayOfWeek;
  }

  /**
   * @param val
   */
  public void setDayOfMonth(final int val) {
    dayOfMonth = val;
  }

  /**
   * @return int
   */
  public int getDayOfMonth() {
    return dayOfMonth;
  }

  /**
   * @param val
   */
  public void setWeekOfYear(final String val) {
    weekOfYear = val;
  }

  /**
   * @return String
   */
  public String getWeekOfYear() {
    return weekOfYear;
  }

  /**
   * @param val
   */
  public void setDayName(final String val) {
    dayName = val;
  }

  /**
   * @return String
   */
  public String getDayName() {
    return dayName;
  }

  /**
   * @param val
   */
  public void setMonth(final String val) {
    month = val;
  }

  /**
   * @return String
   */
  public String getMonth() {
    return month;
  }

  /**
   * @param val
   */
  public void setMonthName(final String val) {
    monthName = val;
  }

  /**
   * @return String
   */
  public String getMonthName() {
    return monthName;
  }

  /**
   * @param val
   */
  public void setShortMonthName(final String val) {
    shortMonthName = val;
  }

  /**
   * @return String
   */
  public String getShortMonthName() {
    return shortMonthName;
  }

  /**
   * @param val
   */
  public void setYear(final String val) {
    year = val;
  }

  /**
   * @return String
   */
  public String getYear() {
    return year;
  }

  /**
   * @param val
   */
  public void setEntries(final TimeViewDailyInfo[] val) {
    entries = val;
  }

  /**
   * @return TimeViewDailyInfo[]
   */
  public TimeViewDailyInfo[] getEntries() {
    return entries;
  }

  /**
   * @return Collection
   */
  public GetEntitiesResponse<EventFormatter> getEvents() {
    if (events == null) {
      final var resp = getDaysEvents(cal);
      if (resp.isError()) {
        return resp;
      }

      events = resp.getEntities();
    }

    return new GetEntitiesResponse<EventFormatter>()
            .setEntities(events);
  }

  /** Return the events for the day as an array of value objects
   *
   * @param   date    Calendar object defining day
   * @return  Collection  one days events,  never null, length 0 for no events.
   */
  private GetEntitiesResponse<EventFormatter> getDaysEvents(final Calendar date) {
    if (!getDayEntry()) {
      error("*******Not a day entry*****");
      throw new IllegalStateException("Not a day entry");
    }

    try {
      return new GetEntitiesResponse<EventFormatter>()
              .setEntities(view.getDaysEvents(date));
    } catch (final Throwable t) {
      return new GetEntitiesResponse<EventFormatter>().error(t);
    }
  }

  /* =============================================================
   *                   Logged methods
   * ============================================================= */

  private final BwLogger logger = new BwLogger();

  @Override
  public BwLogger getLogger() {
    if ((logger.getLoggedClass() == null) && (logger.getLoggedName() == null)) {
      logger.setLoggedClass(getClass());
    }

    return logger;
  }
}

