package com.example.namesdaytracker.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.time.LocalDate


class DataUtils {
    companion object {

        fun getAllData(context: Context) : ArrayList<Locale> {
            Log.i("files", "enter getAllData()")

            val resultList = arrayListOf<Locale>()
            val files = context.assets.list("locales")
            Log.i("files", "Files list: ${files.toString()}")
            files?.forEach { fileName -> Log.i("files", "fileName: $fileName"); }
            files?.forEach { fileName ->
                // Read the JSON file
                val inputStream = context.assets.open("locales/$fileName")
                val jsonContent = inputStream.bufferedReader().use { it.readText() }

                try {
                    val jsonObject = JSONObject(jsonContent)
                    val mapType = object : TypeToken<Map<String, Any>>() {}.type
                    val nameMap: Map<String, Map<String, Map<String, List<String>>>> = Gson().fromJson(jsonContent, mapType)

                    resultList.add(Locale(
                        jsonObject.get("country").toString(),
                        jsonObject.get("countryName").toString(),
                        jsonObject.get("symbol").toString(),
                        nameMap["names"]!!
                    ))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return resultList
        }

        fun getNamesByLocaleAndDate(dataList:ArrayList<Locale>,locales:List<String>,day:Int,month:Int) : Map<String,List<String>> {
            // returns map[country] = list of names
            return dataList.filter { l -> isEnabled(l,locales) }
                .associate { l -> l.country to l.getNames(day,month) }
        }

        fun getStringNamesByLocaleAndDate(dataList:ArrayList<Locale>,locales:List<String>,day:Int,month:Int) : Map<String,String> {
            // returns map[country] = list of names
            return dataList.filter { l -> isEnabled(l,locales) }
                .associate { l -> l.country to l.getNames(day,month).joinToString(", ") }
        }

        private fun isEnabled(country:Locale, locales: List<String>): Boolean {
            return country.country.lowercase() in locales.map { s -> s.lowercase() }
        }

        fun getPrintableNamesByLocaleAndDate(dataList:ArrayList<Locale>,locales:List<String>,day:Int,month:Int) : String {
            // returns emoji, names, newline string
            return dataList.filter { l -> isEnabled(l,locales) }
                .map { l -> "${l.symbol} ${l.printDayNames(day.toString(),month.toString())}" }
                .joinToString ("\n")
        }

        fun getTodayPrintableNamesByLocale(dataList:ArrayList<Locale>,locales:List<String>) : String {
            // returns emoji, names, newline string
            val today = LocalDate.now()
            return getPrintableNamesByLocaleAndDate(dataList,locales,today.dayOfMonth,today.monthValue)
        }

    }
}