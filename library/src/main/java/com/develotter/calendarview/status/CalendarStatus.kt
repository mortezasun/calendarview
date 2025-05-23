package com.develotter.calendarview.status

import com.develotter.calendarview.enums.TypeSelectDay
import com.develotter.calendarview.enums.TypeViewCalender
import com.develotter.calendarview.enums.TypeWeekShow
import java.util.Locale

class CalendarStatus {



    private  var viewTypeSelected :TypeViewCalender  = TypeViewCalender.HORIZONTAL_PAGER


    private  var localInUse : Locale = Locale.getDefault()
    private  var countsMonthAfterAndBefore: Int = 50
    private  var typeSelectDay: TypeSelectDay = TypeSelectDay.Single
    private  var showLastMonth: Boolean = true
    private  var showNextMonth: Boolean = true
    private  var showRowWeekName : TypeWeekShow = TypeWeekShow.Fix
    private  var showRowMonthName : Boolean = false
    private  var showCalendarController : Boolean = true
    private  var showSelectedDayController : Boolean = true

    fun getViewTypeSelected(): TypeViewCalender = viewTypeSelected


    fun getShowLastMonth(): Boolean = showLastMonth
    fun getShowNextMonth(): Boolean = showNextMonth
    fun getShowRowWeekName(): TypeWeekShow = showRowWeekName
    fun getShowRowMonthName(): Boolean = showRowMonthName
    fun getTypeSelectDay(): TypeSelectDay = typeSelectDay
    fun getShowCalendarController(): Boolean = showCalendarController
    fun getShowSelectedDayController(): Boolean = showSelectedDayController

    fun getLocalInUse(): Locale = localInUse
    fun getCountsMonthAfterAndBefore(): Int = countsMonthAfterAndBefore

    fun setViewTypeSelected(viewType: TypeViewCalender): CalendarStatus  {
        if (this.viewTypeSelected != viewType) {
            this.viewTypeSelected = viewType
        }
        return this
    }


    fun setLocalInUse(localInUse: Locale): CalendarStatus {
        if (this.localInUse != localInUse) {
            this.localInUse = localInUse
        }
        return this
    }


    fun setTypeSelectDay(typeSelectDay: TypeSelectDay): CalendarStatus {
        this.typeSelectDay = typeSelectDay
        return this
    }
    fun setShowRowMonthName(showRowMonthName: Boolean): CalendarStatus {
        this.showRowMonthName = showRowMonthName
        return this
    }
    fun setShowRowWeekName(showRowWeekName: TypeWeekShow): CalendarStatus {
        this.showRowWeekName = showRowWeekName
        return this
    }


    fun setShowLastMonth(showLastMonth: Boolean): CalendarStatus {
        this.showLastMonth = showLastMonth
        return this
    }
    fun setShowNextMonth(showNextMonth: Boolean): CalendarStatus {
        this.showNextMonth = showNextMonth
        return this
    }

    fun setShowSelectedDayController(showSelectedDayController: Boolean): CalendarStatus {
        this.showSelectedDayController = showSelectedDayController
        return this
    }

    fun setShowCalendarController(showCalendarController: Boolean): CalendarStatus {
        this.showCalendarController = showCalendarController
        return this
    }

}