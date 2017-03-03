package com.thales.github.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class Utility {

    private Utility() {

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected() && info.isAvailable();
    }


    public static String getFormattedDayMonthYear(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat dayMonthYearFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

            return dayMonthYearFormat.format(dateFormat.parse(date));
        } catch (ParseException e) {
            return null;
        }
    }
}
