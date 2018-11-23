package com.xp.calendar.calendar;

/**
 * Created by panda on 2015/11/10.
 * custom实体类
 */

import java.io.Serializable;
import java.util.Calendar;

public class CustomDate implements Serializable {


    private static final long serialVersionUID = 1L;
    public int year;
    public int month;
    public int day;
    public int week;
    public Calendar calendar;

    public CustomDate(int year, int month, int day) {
        if (month > 12) {
            month = 1;
            year++;
        } else if (month < 1) {
            month = 12;
            year--;
        }
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public CustomDate() {
        this.year = DateUtil.getYear();
        this.month = DateUtil.getMonth();
        this.day = DateUtil.getCurrentMonthDay();
        this.calendar = DateUtil.getCalendar();

    }

    public static CustomDate modifiDayForObject(CustomDate date, int day) {
        CustomDate modifiDate = new CustomDate(date.year, date.month, day);
        return modifiDate;
    }

    @Override
    public String toString() {
        return year + "-" + month + "-" + day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

}
