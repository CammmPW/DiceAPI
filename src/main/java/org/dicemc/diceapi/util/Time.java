package org.dicemc.diceapi.util;

public class Time {
    private final long _y;

    private final long _d;

    private final long _h;

    private final long _m;

    private final long _s;

    private final long _ms;

    private final long _total;

    public Time(long milliseconds) {
        this._total = milliseconds;
        long second = 1000L;
        long minute = second * 60L;
        long hour = minute * 60L;
        long day = hour * 24L;
        long year = day * 365L;
        this._y = milliseconds / year;
        this._d = milliseconds % year / day;
        this._h = milliseconds % year % day / hour;
        this._m = milliseconds % year % day % hour / minute;
        this._s = milliseconds % year % day % hour % minute / second;
        this._ms = milliseconds % year % day % minute % second;
    }

    public long getYears() {
        return this._y;
    }

    public long getDays() {
        return this._d;
    }

    public long getTotalDays() {
        return this._d + this._y * 365L;
    }

    public long getHours() {
        return this._h;
    }

    public long getTotalHours() {
        return this._h + getTotalDays() * 24L;
    }

    public long getMinutes() {
        return this._m;
    }

    public long getTotalMinutes() {
        return this._m + getTotalHours() * 60L;
    }

    public long getSeconds() {
        return this._s;
    }

    public long getTotalSeconds() {
        return this._s + getTotalMinutes() * 60L;
    }

    public long getMilliseconds() {
        return this._ms;
    }

    public long getTotalMilliseconds() {
        return this._total;
    }
}
