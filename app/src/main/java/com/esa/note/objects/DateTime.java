package com.esa.note.objects;

import android.content.Context;

import com.esa.note.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTime {
    public static long currentDate() {
        try {
            return Long.parseLong(new SimpleDateFormat("yyyyMMddkkmm", Locale.getDefault()).format(new Date()));
        }
        catch (Exception e) {
            return 0;
        }
    }

    public static String dateText(Context context, long date) {
        try {
            String text;
            String year, month, day, am_pm, hour, min;

            String getDateToNumber = String.valueOf(date);

            year = getDateToNumber.substring(0, 4);
            month = getDateToNumber.substring(4, 6);
            if (Integer.parseInt(month) <= 9) {
                month = month.substring(1);
            }
            day = getDateToNumber.substring(6, 8);
            if (Integer.parseInt(day) <= 9) {
                day = day.substring(1);
            }
            hour = getDateToNumber.substring(8, 10);
            min = getDateToNumber.substring(10);
            if (Integer.parseInt(hour) >= 13) {
                am_pm = context.getResources().getString(R.string.pm);
                int newHour = Integer.parseInt(hour) - 12;
                hour = String.valueOf(newHour);
            }
            else {
                if (Integer.parseInt(hour) <= 9) {
                    hour = hour.substring(1);
                }
                am_pm = context.getResources().getString(R.string.am);
            }
            if (Integer.parseInt(min) <= 9) {
                min = min.substring(1);
            }

            if (Locale.getDefault().getDisplayLanguage().equals("한국어")) {
                year += context.getResources().getString(R.string.year);
                month += context.getResources().getString(R.string.month);
                day += context.getResources().getString(R.string.day);
                hour += context.getResources().getString(R.string.hour);
                min += context.getResources().getString(R.string.minute);
                text = year+" "+month+" "+day+" "+am_pm+" "+hour+" "+min;
            }
            else {
                text = monthToEnglish(month)+" "+day+" "+year+" "+hour+":"+min+" "+am_pm;
            }
            return text;
        }
        catch (Exception e) {
            return "";
        }

    }

    private static String monthToEnglish(String string) {
        int month = Integer.parseInt(string);
        if (month == 1) {
            return "January";
        }
        else if (month == 2) {
            return "February";
        }
        else if (month == 3) {
            return "March";
        }
        else if (month == 4) {
            return "April";
        }
        else if (month == 5) {
            return "May";

        }
        else if (month == 6) {
            return "June";
        }
        else if (month == 7) {
            return "July";
        }
        else if (month == 8) {
            return "August";
        }
        else if (month == 9) {
            return "September";
        }
        else if (month == 10) {
            return "October";
        }
        else if (month == 11) {
            return "November";
        }
        else {
            return "December";
        }
    }
}
