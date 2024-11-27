package com.example.namesdaytracker.data

import android.util.Log

class Locale(val country: String, val countryName: String, val symbol: String, val names: Map<String, Map<String, List<String>>>) {

    fun getNames(day: String, month: String) : List<String> {
        val theDay: List<String>? = names[month]!![day]
        if (theDay != null) return theDay
        return listOf("-")
    }

    fun getNames(day: Int, month: Int) : List<String> {
        return getNames(day.toString(),month.toString())
    }

    fun printDayNames(day: String,month: String) : String {
        Log.d("Locale", "printDayNames: ${getNames(day,month)}")
        Log.d("Locale", "printDayNamesJoined: ${getNames(day,month).joinToString(", ")}")
        return getNames(day,month).joinToString (", " )
    }
}