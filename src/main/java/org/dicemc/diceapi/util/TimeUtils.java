package org.dicemc.diceapi.util;

import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {
    public enum Time {
        SECOND(1),
        MINUTE(SECOND.getSeconds() * 60),
        HOUR(MINUTE.getSeconds() * 60),
        DAY(HOUR.getSeconds() * 24),
        YEAR(DAY.getSeconds() * 365);

        private final int seconds;

        public static final long MINECRAFT_SECOND_TICKS = 20L;

        public static final long MILLISECONDS_IN_SECOND = 1000L;

        Time(int seconds) {
            this.seconds = seconds;
        }

        public int getSeconds() {
            return this.seconds;
        }
    }

    public long futureTime(Time base, int modifier) {
        long now = System.currentTimeMillis();
        long add = 1000L * base.getSeconds() * modifier;
        return now + add;
    }

    public long futureTimeComplex(int[] modifiers, Time... bases) {
        long now = System.currentTimeMillis();
        if (modifiers.length != bases.length)
            throw new IllegalArgumentException("Length of modifiers (" + modifiers.length + ") does not match length of bases (" + bases.length + ")");
        if (modifiers.length == 0)
            return now;
        long add = 0L;
        for (int i = 0; i < modifiers.length; i++) {
            long t = (modifiers[i] * bases[i].getSeconds()) * 1000L;
            add += t;
        }
        return now + add;
    }

    public String futureTimeAsString(Time base, int modifier) {
        return futureTimeAsString(futureTime(base, modifier));
    }

    public String futureTimeAsString(long time) {
        return timeAsString(time, true);
    }

    public long pastTime(Time base, int modifier) {
        long now = System.currentTimeMillis();
        long sub = (base.getSeconds() * modifier) * 1000L;
        return now - sub;
    }

    public String pastTimeAsString(Time base, int modifier) {
        return pastTimeAsString(pastTime(base, modifier));
    }

    public String pastTimeAsString(long time) {
        return timeAsString(time, false);
    }

    private String timeAsString(long time, boolean future) {
        long given = time;
        long now = System.currentTimeMillis();
        if (future) {
            time -= now;
        } else {
            time = now - time;
        }
        if (future && time < 0L)
            throw new IllegalArgumentException("Given time (" + given + ") must be after current time (" + now + ")");
        if (!future && time < 0L)
            throw new IllegalArgumentException("Given time (" + given + ") must be before current time (" + now + ")");
        long second = 1000L;
        long minute = second * Time.MINUTE.getSeconds();
        long hour = second * Time.HOUR.getSeconds();
        long day = second * Time.DAY.getSeconds();
        long year = second * Time.YEAR.getSeconds();
        long years = time / year;
        long days = time % year / day;
        long hours = time % year % day / hour;
        long minutes = time % year % day % hour / minute;
        long seconds = time % year % day % hour % minute / second;
        String t = "";
        if (years > 0L)
            t = t + years + "y ";
        if (days > 0L)
            t = t + days + "d ";
        if (hours > 0L)
            t = t + hours + "h ";
        if (minutes > 0L)
            t = t + minutes + "m ";
        if (seconds > 0L)
            t = t + seconds + "s";
        return t.trim();
    }

    public String getFriendlyDate(long time, boolean hour12, boolean shortDisplay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int style = shortDisplay ? Calendar.SHORT : Calendar.LONG;
        Locale locale = Locale.getDefault();
        String month = calendar.getDisplayName(Calendar.MONTH, style, locale);
        int day = calendar.get(Calendar.DATE);
        String weekday = calendar.getDisplayName(Calendar.DAY_OF_WEEK, style, locale);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour12 && hour > 12) {
            hour -= 12;
        } else if (hour12 && hour == 0) {
            hour = 12;
        }
        int min = calendar.get(Calendar.MINUTE);
        String ap = hour12 ? calendar.getDisplayName(Calendar.AM_PM, style, locale) : "";
        return weekday + ", " + month + " " + day + ", " + year + " - " + hour + ":" + min + ap;
    }
}
