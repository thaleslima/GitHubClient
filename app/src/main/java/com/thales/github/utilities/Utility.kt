package com.thales.github.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object Utility {

    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnected && info.isAvailable
    }


    fun getFormattedDayMonthYear(date: String): String? {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val dayMonthYearFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

            return dayMonthYearFormat.format(dateFormat.parse(date))
        } catch (e: ParseException) {
            return null
        }
    }
}
