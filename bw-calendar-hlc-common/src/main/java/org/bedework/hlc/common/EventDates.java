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

import org.bedework.base.ToString;
import org.bedework.base.response.GetEntityResponse;
import org.bedework.base.response.Response;
import org.bedework.calfacade.BwDateTime;
import org.bedework.calfacade.BwEvent;
import org.bedework.calfacade.base.BwTimeRange;
import org.bedework.calfacade.base.StartEndComponent;
import org.bedework.calfacade.exc.ValidationError;
import org.bedework.calfacade.svc.BwPreferences;
import org.bedework.calfacade.svc.EventInfo;
import org.bedework.calfacade.util.ChangeTable;
import org.bedework.util.calendar.IcalDefs;
import org.bedework.util.calendar.PropertyIndex.PropertyInfoIndex;
import org.bedework.util.misc.Util;
import org.bedework.util.timezones.DateTimeUtil;

import static org.bedework.base.response.Response.Status.exists;
import static org.bedework.base.response.Response.Status.validationError;

/** The dates and/or duration which define when an event happens. These are
 * stored in objects which allow manipulation of individual date and time
 * components.
 */
public class EventDates extends EntityDates {
  /** Starting values or date to go to
   */
  private TimeDateComponents startDate;

  /** This takes the 1 character values defined in BwEvent
   */
  private String endType = String.valueOf(BwEvent.endTypeDuration);

  /** Ending date for events
   */
  private TimeDateComponents endDate;

  /** Duration of event
   */
  private DurationBean duration;

  /**
   * @param principalHref user href
   * @param calInfo calendar info
   * @param hour24 trie for 24 hour
   * @param endTypeName dur or end
   * @param minIncrement for minutes
   */
  public EventDates(final String principalHref,
                    final CalendarInfo calInfo,
                    final boolean hour24, final String endTypeName,
                    final int minIncrement) {
    super(principalHref, calInfo, hour24, minIncrement);

    if (endTypeName != null) {
      if (BwPreferences.preferredEndTypeDuration.equals(endTypeName)) {
        endType = String.valueOf(StartEndComponent.endTypeDuration);
      } else if (BwPreferences.preferredEndTypeDate.equals(endTypeName)) {
        endType = String.valueOf(StartEndComponent.endTypeDate);
      }
    }
  }

  /** We set the time date components from the event.
   *
   * <p>If the end date is a date only value it is adjusted backwards by one
   * day to take account of accepted practice.
   *
   * @param val the event to use
   */
  public Response<?> setFromEvent(final BwEvent val) {
    try {
      getStartDate().setDateTime(val.getDtstart());

      BwDateTime dtEnd = val.getDtend();
      if (val.getDtstart().getDateType()) {
        dtEnd = dtEnd.getPreviousDay();
      }

      getEndDate().setDateTime(dtEnd);
      duration = DurationBean.makeDurationBean(val.getDuration());
      setEndType(String.valueOf(val.getEndType()));

      return new Response<>();
    } catch (final Throwable t) {
      return new Response<>().error(t);
    }
  }

  /** We set the time date components from the date.
   *
   * <p>If the end date is a date only value it is adjusted backwards by one
   * day to take account of accepted practice.
   *
   * @param val date/time
   */
  public Response<?> setFromDate(final String val) {
    try {
      getStartDate().setDateTime(val);
      getEndDate().setDateTime(val);

      return new Response<>();
    } catch (final Throwable t) {
      return new Response<>().error(t);
    }
  }

  /** We set the time date components to be today. The end date is
   * set to the current date + 1 hour.
   *
   * @param val the event to update
   */
  public Response<?> setNewEvent(final BwEvent val) {
    try {
      final java.util.Date now = new java.util.Date(System.currentTimeMillis());

      getStartDate().setDateTime(DateTimeUtil.isoDateTime(now));

      duration = DurationBean.makeOneHour();

      final BwDateTime end = getStartDate().getDateTime().addDuration(duration);
      getEndDate().setDateTime(end);

      return new Response<>();
    } catch (final Throwable t) {
      return new Response<>().error(t);
    }
  }

  /** Update the event from the components. Little validation takes place at this
   * point.
   *
   * <p>If the end date is a date only value it is adjusted forwards by one
   * day to take account of accepted practice.
   *
   * @param ei the event to update
   * @return Response validationError for an error
   *              ok if something changed
   *              exists if nothing changed.
   */
  public Response<?> updateEvent(final EventInfo ei) {
    final BwEvent ev = ei.getEvent();
    final ChangeTable changes = ei.getChangeset(principalHref);
    boolean updated = false;

    final PropertyInfoIndex endPi;
    if (ev.getEntityType() != IcalDefs.entityTypeTodo) {
      endPi = PropertyInfoIndex.DTEND;
    } else {
      endPi = PropertyInfoIndex.DUE;
    }

    try {
      /* Event initial values */
      final BwDateTime esdt = ev.getDtstart();
      BwDateTime eedt = ev.getDtend();
      String edur = ev.getDuration();
      final char evEndtype = ev.getEndType();

      final BwDateTime start = getStartDate().getDateTime();

      // XXX A todo can have no date and time

      if ((esdt == null) || !esdt.equals(start)) {
        ev.setDtstart(start);
        updated = true;
      }

      ev.setNoStart(false);

      final TimeDateComponents eTdc = getEndDate();
      eTdc.setDateOnly(start.getDateType());
      BwDateTime end = eTdc.getDateTime();

      if ((getEndType() == null) ||
          (getEndType().length() != 1)) {
        err.emit(ValidationError.invalidEndtype);
        return new Response<>().error(validationError);
      }

      final char endtype = getEndType().charAt(0);

      if (evEndtype != endtype) {
        ev.setEndType(endtype);
        updated = true;
      }

      /* If it's a date type start - truncate the duration to days */
      if (start.getDateType() &&
          (ev.getEndType() == StartEndComponent.endTypeDuration)) {
        getDuration().setHours(0);
        getDuration().setMinutes(0);
        getDuration().setSeconds(0);

        if (getDuration().isZero()) {
          err.emit(ValidationError.invalidDuration);
          return new Response<>().error(validationError);
        }
      }

      if (ev.getEndType() == StartEndComponent.endTypeDate) {
        if (end.getDateType()) {
          // Adjust forward 1 day
          end = end.getNextDay();
        }
      }

      if ((eedt == null) || !eedt.equals(end)) {
        ev.setDtend(end);
        updated = true;
      }

      String dur = getDuration().toString();

      if ((edur == null) || !edur.equals(dur)) {
        ev.setDuration(dur);
        updated = true;
      }

      /* Flag any changes */
      if ((esdt == null) || !esdt.equals(start)) {
        changes.changed(PropertyInfoIndex.DTSTART,
                        esdt, start);
      }

      /* If we switched from date to duration make new date null and old
       * duration null.
       *
       * If we switched from duration to date make old date null and new
       * duration null.
       *
       * otherwise if the endtype is duration flag a duration change
       *
       * else flag an end date change
       */

      if (evEndtype != endtype) {
        if (endtype == StartEndComponent.endTypeDuration) {
          end = null;
          edur = null;
        } else {
          eedt = null;
          dur = null;
        }

        changes.changed(endPi, eedt, end);
        changes.changed(PropertyInfoIndex.DURATION, edur, dur);
      } else if (endtype == StartEndComponent.endTypeDuration) {
        if (Util.cmpObjval(edur, dur) != 0) {
          changes.changed(PropertyInfoIndex.DURATION, edur, dur);
        }
      } else if (!eedt.equals(end)) {
        changes.changed(endPi, eedt, end);
      }

      if (!updated) {
        return new Response<>().setStatus(exists);
      }

      return new Response<>();
    } catch (final Throwable t) {
      err.emit(t);
      return new Response<>().error(validationError);
    }
  }

  /** Return an object representing an events start date.
   *
   * @return TimeDateComponents  object representing date and time
   */
  public GetEntityResponse<TimeDateComponents> getStartDate() {
    if (startDate == null) {
      final var resp = getNowTimeComponents();
      if (!resp.isOk()) {
        return resp;
      }
      startDate = resp.getEntity();
    }

    return new GetEntityResponse<TimeDateComponents>()
            .setEntity(startDate);
  }

  /** Return an object representing an events end date.
   *
   * @return TimeDateComponents  object representing date and time
   */
  public GetEntityResponse<TimeDateComponents> getEndDate() {
    if (endDate == null) {
      final var resp = getNowTimeComponents();
      if (!resp.isOk()) {
        return resp;
      }
      endDate = resp.getEntity();
    }

    return new GetEntityResponse<TimeDateComponents>()
            .setEntity(endDate);
  }

  /** Return an object representing an events duration.
   *
   * @return Duration  object representing date and time
   */
  public DurationBean getDuration() {
    if (duration == null) {
      duration = new DurationBean();
    }

    return duration;
  }

  /**
   *
   * @param   val     String defining end type
   */
  public void setEndType(final String val) {
    endType = val;
  }

  /**
   *
   * @return String defining end type
   */
  public String getEndType() {
    return endType;
  }

  /**
   * @return TimeRange object
   */
  public BwTimeRange getTimeRange() {
    return new BwTimeRange(getStartDate().getDateTime(),
                         getEndDate().getDateTime());
  }

  /* ========================================================
   *                        Object methods
   * ======================================================== */

  @Override
  public String toString() {
    return new ToString(this)
            .append("startDate", startDate)
            .append("endType", getEndType())
            .append("endDate", endDate)
            .append(duration)
            .toString();
  }
}

