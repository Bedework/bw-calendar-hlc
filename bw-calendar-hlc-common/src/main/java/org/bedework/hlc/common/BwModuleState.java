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

import org.bedework.calfacade.indexing.SearchResult;
import org.bedework.calfacade.indexing.SearchResultEntry;
import org.bedework.hlc.common.views.TimeView;
import org.bedework.util.misc.Util;
import org.bedework.util.servlet.filters.ConfiguredXSLTFilter.XSLTConfig;
import org.bedework.util.servlet.filters.PresentationState;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** This class will be exposed to JSP via the request. Do not expose the
 * client indirectly through this.
 *
 * @author Mike Douglass   douglm  rpi.edu
 */
public class BwModuleState implements Serializable {
  private final String moduleName;

  private boolean refresh;

  private PresentationState ps;

  private XSLTConfig xsltConfig;

  private static final int maxAppVars = 50; // Stop screwing around.

  //private EventState eventState;

  Map<String, String> vars = new HashMap<>();
  private TimeDateComponents viewStartDate;
  private CalendarInfo calInfo;
  private EventDates eventDates;

  /* ............................................................
   *                       View period
   * ............................................................ */

  // ENUM
  private String selectionType = BedeworkDefs.selectionTypeView;

  private String date;

  private String viewType; /* month etc */

  /** Index of the current view type
   */
  private int curViewPeriod = -1;

  /** MyCalendarVO version of the start date
   */
  private CalendarFormatter viewMcDate;

  /** The current view with user selected date (day, week, month etc)
   */
  private TimeView curTimeView;

  /* ............................................................
   *                   Searches
   * ............................................................ */

  private String query;

  private SearchResult searchResult;

  private List<SearchResultEntry> searchResultEntries;

  private String searchLimits = "fromToday";

  public BwModuleState(final String moduleName) {
    this.moduleName = moduleName;
  }

  /** flag refresh needed
   *
   * @param val true to force refresh
   */
  public void setRefresh(final boolean val) {
    refresh = val;
  }

  /**
   *
   * @return true if refresh needed
   */
  public boolean getRefresh() {
    return refresh;
  }

  /**
   * @param val PresentationState
   */
  public void setPresentationState(final PresentationState val) {
    ps = val;
  }

  /**
   * @return PresentationState
   */
  public PresentationState getPresentationState() {
    return ps;
  }

  /**
   * @param val XSLTConfig
   */
  public void setXsltConfig(final XSLTConfig val) {
    xsltConfig = val;
  }

  /**
   * @return XSLTConfig
   */
  public XSLTConfig getXsltConfig() {
    return xsltConfig;
  }

  /**
   * @return String
   */
  public String getModuleName() {
    return moduleName;
  }

  /**
   * @param val calendar info
   */
  public void setCalInfo(final CalendarInfo val) {
    calInfo = val;
  }

  /**
   * @return calendar info
   */
  public CalendarInfo getCalInfo() {
    return calInfo;
  }

  /** Set an object containing the dates.
   *
   * @param val EventDates  object representing date/times and duration
   */
  public void assignEventDates(final EventDates val) {
    eventDates = val;
  }

  /** Return an object containing the dates.
   *
   * @return EventDates  object representing date/times and duration
   */
  public EventDates getEventDates() {
    return eventDates;
  }

  /**
   * @return time date
   */
  public TimeDateComponents getViewStartDate() {
    if (viewStartDate == null) {
      viewStartDate = getEventDates().getNowTimeComponents();
    }

    return viewStartDate;
  }

  public void updateViewStartDate(final BwRequest req) {
    if (req.present("viewStartDate.year")) {
      getViewStartDate().setYear(req.getIntReqPar("viewStartDate.year"));
    }
    if (req.present("viewStartDate.month")) {
      getViewStartDate().setMonth(req.getIntReqPar("viewStartDate.month"));
    }
    if (req.present("viewStartDate.day")) {
      getViewStartDate().setDay(req.getIntReqPar("viewStartDate.day"));
    }
  }

  /** Date of the view as a MyCalendar object
   *
   * @param val the calendar
   */
  public void setViewMcDate(final CalendarFormatter val) {
    viewMcDate = val;
  }

  /** Date of the view as a MyCalendar object
   *
   * @return MyCalendarVO date last set
   */
  public CalendarFormatter getViewMcDate() {
    return viewMcDate;
  }

  /** The current view (day, week, month etc)
   *
   * @param val the current time view
   */
  public void setCurTimeView(final TimeView val) {
    curTimeView = val;
    
    if (val != null) {
      curDayFmt = val.getCurDayFmt();
      firstDayFmt = val.getFirstDayFmt();
      lastDayFmt = val.getLastDayFmt();
    }
  }

  /**
   *
   * @return current view (day, week, month etc)
   */
  public TimeView getCurTimeView() {
    return curTimeView;
  }
  
  private TimeView.CalFmt curDayFmt;
  private TimeView.CalFmt firstDayFmt;
  private TimeView.CalFmt lastDayFmt;
  
  public TimeView.CalFmt getCurDayFmt() {
    return curDayFmt;
  }

  public TimeView.CalFmt getFirstDayFmt() {
    return firstDayFmt;
  }

  public TimeView.CalFmt getLastDayFmt() {
    return lastDayFmt;
  }

  /** This often appears as the request parameter specifying the view.
   * It should be one of the viewTypeNames
   *
   * @param  val   String viewType
   */
  public void setViewType(final String val) {
    viewType = Util.checkNull(val);

    if (viewType == null) {
      return;
    }

    Integer i = BwRequest.viewTypeMap.get(viewType);

    if (i == null) {
      i = BedeworkDefs.defaultView;
      viewType = BedeworkDefs.viewPeriodNames[i];
    }

    if (i != curViewPeriod) {
      curViewPeriod = i;
      refresh = true;
    }
  }

  public String getViewType() {
    return viewType;
  }

  /** Index of the view type set when the page was last generated
   *
   * @param val  int valid view index
   */
  public void setCurViewPeriod(final int val) {
    curViewPeriod = val;
  }

  /**
   * @return view index
   */
  public int getCurViewPeriod() {
    return curViewPeriod;
  }

  /* ====================================================================
   *                   Variables
   * ==================================================================== */

  /** Called to set a variable to a value
   *
   * @param   name     name of variable
   * @param   val      new value of variable - null means remove.
   * @return  boolean  True if ok - false for too many vars
   */
  public boolean setVar(final String name, final String val) {
    if (val == null) {
      vars.remove(name);
      return true;
    }

    if (vars.size() > maxAppVars) {
      return false;
    }

    vars.put(name, val);
    return true;
  }

  /** Called to return a variable
   *
   * @param   name     name of variable
   * @return  value or null
   */
  public String getVar(final String name) {
    return vars.get(name);
  }

  /* ....................................................................
   *                   Searches
   * .................................................................... */

  /**
   *
   * @param val - the search result
   */
  public void setSearchResult(final SearchResult val) {
    searchResult = val;
  }

  /**
   *
   * @return search result
   */
  public SearchResult getSearchResult() {
    return searchResult;
  }

  /** Set query from last search
   *
   * @param val the query
   */
  public void setQuery(final String val) {
    query = val;
  }

  /** Get query from last search
   *
   * @return count found in last search
   */
  public String getQuery() {
    return query;
  }

  /** Set search limits
   *
   * @param val the limits
   */
  public void setSearchLimits(final String val) {
    searchLimits = val;
  }

  /**
   * @return search limits
   */
  public String getSearchLimits() {
    return searchLimits;
  }

  /* ....................................................................
   *                   View state
   * .................................................................... */

  /**
   * @param val the selection type
   */
  public void setSelectionType(final String val) {
    selectionType = val;
  }

  /**
   * @return String
   */
  public String getSelectionType() {
    return selectionType;
  }

  /** Last date used to position ourselves
   *
   * @param val date
   */
  public void setDate(final String val) {
    date = val;
  }

  /**
   *
   * @return Last date used to position ourselves
   */
  public String getDate() {
    return date;
  }

  /* later
  public EventState getEventState() {
    if (eventState == null){
      eventState = new EventState(this);
    }
    return eventState;
  }
  */
}

