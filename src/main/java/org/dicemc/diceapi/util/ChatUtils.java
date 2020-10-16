package org.dicemc.diceapi.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import org.bukkit.ChatColor;

public class ChatUtils {
    private final String codes = "0123456789abcdefklmnor";

    public String createHeader(String title) {
        return createHeader('6', title);
    }

    public String createHeader(char borderColor, String title) {
        return createHeader(title, "&" + borderColor + "=====[&f%title&" + borderColor + "]=====");
    }

    public String createHeader(String title, String format) {
        return formatMessage(format.replace("%title", title));
    }

    public String formatMessage(String message) {
        for (int i = 0; i < "0123456789abcdefklmnor".length(); i++) {
            char code = "0123456789abcdefklmnor".charAt(i);
            if (message.contains("&" + code))
                message = message.replaceAll("&" + code, ChatColor.getByChar(code).toString());
        }
        return message;
    }

    public String removeColorCodes(String s) {
        for (int i = 0; i < "0123456789abcdefklmnor".length(); i++) {
            if (s.contains("&" + "0123456789abcdefklmnor".charAt(i)))
                s = s.replaceAll("&" + "0123456789abcdefklmnor".charAt(i), "");
        }
        return s;
    }

    public boolean isNum(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String makeStringFromArray(String[] args) {
        return join(args);
    }

    public String join(String[] array) {
        return join(array, " ");
    }

    public String join(String[] array, String delimiter) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            result.append(array[i]);
            if (i < array.length - 1)
                result.append(delimiter);
        }
        return result.toString();
    }

    public String join(ArrayList<String> list) {
        return join(list, " ");
    }

    public String join(ArrayList<String> list, String delimiter) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i));
            if (i < list.size() - 1)
                result.append(delimiter);
        }
        return result.toString();
    }

    public String[] removeArgs(String[] array, int startIndex) {
        if (array.length == 0)
            return array;
        if (array.length < startIndex)
            return new String[0];
        String[] newSplit = new String[array.length - startIndex];
        System.arraycopy(array, startIndex, newSplit, 0, array.length - startIndex);
        return newSplit;
    }

    public String[] removeFirstArg(String[] array) {
        return removeArgs(array, 1);
    }

    public String toProperCase(String s) {
        if (s.isEmpty())
            return "";
        String[] unimportant = {
                "a", "an", "and", "but", "is", "are", "for", "nor", "of", "or",
                "so", "the", "to", "yet" };
        String[] split = s.split("\\s+");
        String result = "";
        for (int i = 0; i < split.length; i++) {
            String word = split[i];
            boolean capitalize = true;
            byte b;
            int j;
            String[] arrayOfString;
            for (j = (arrayOfString = unimportant).length, b = 0; b < j; ) {
                String str = arrayOfString[b];
                if (str.equalsIgnoreCase(word) &&
                        i > 0 && i < split.length - 1) {
                    capitalize = false;
                    break;
                }
                b++;
            }
            if (capitalize) {
                result = result + capitalize(word) + " ";
            } else {
                result = result + word.toLowerCase() + " ";
            }
        }
        return result.trim();
    }

    public String capitalize(String s) {
        if (s.isEmpty())
            return "";
        if (s.length() == 1)
            return s.toUpperCase();
        if (s.length() == 2) {
            String first = String.valueOf(s.charAt(0)).toUpperCase();
            String sec = String.valueOf(s.charAt(1)).toLowerCase();
            return first + sec;
        }
        s = s.toUpperCase();
        return s.charAt(0) + s.substring(1).toLowerCase();
    }

    public <E> E getRandomElement(ArrayList<E> list) {
        Random random = new Random();
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    public String getFriendlyTimestamp(Timestamp time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());
        String weekday = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        String ampm = (calendar.get(Calendar.AM_PM) == Calendar.AM) ? "a" : "p";
        return weekday + ", " + month + " " + day + ", " + ((hour > 12) ? (hour - 12) : hour) + ":" + ((min < 10) ? ("0" + min) : Integer.valueOf(min)) + ampm;
    }

    public String maxLength(String str, int length) {
        if (str.length() < length)
            return str;
        if (length > 3)
            return str.substring(0, length - 3) + "...";
        throw new IllegalArgumentException("Minimum length of 3 characters.");
    }
}
