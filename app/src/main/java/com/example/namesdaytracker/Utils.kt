package com.example.namesdaytracker

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.time.LocalDate

class Utils {



    companion object {
        fun readNamesDayJson(context: Context, fileName: String): Map<String, Map<String, List<String>>>? {
            val inputStream = context.assets.open(fileName)

            try {
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    val jsonContent = reader.readText()

                    // Using TypeToken to specify the type of the map
                    val mapType = object : TypeToken<Map<String, Any>>() {}.type

                    // Parsing JSON into a Map
                    return Gson().fromJson(jsonContent, mapType)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inputStream.close()
            }

            throw IOException("Error reading file")
        }

        fun readCombinedJson(context: Context, fileName: String): Map<String, Map<String, Map<String, List<String>>>>? {
            val inputStream = context.assets.open(fileName)

            try {
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    val jsonContent = reader.readText()

                    // Using TypeToken to specify the type of the map
                    val mapType = object : TypeToken<Map<String, Any>>() {}.type

                    // Parsing JSON into a Map
                    return Gson().fromJson(jsonContent, mapType)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inputStream.close()
            }

            throw IOException("Error reading file")
        }

        fun getTodayNames(context: Context): String {
            val today = LocalDate.now()
            val name = readNamesDayJson(context,"meniny_sk.json")
                ?.get(today.monthValue.toString())
                ?.get(today.dayOfMonth.toString())!!.joinToString(", ")
            if (name == "-") {
                return "Dnes nemá meniny nikto."
            }
            return name

        }

        fun getTodayCombinedNames(context: Context, localities: List<String>): Map<String, String>  {
            val today = LocalDate.now()
            val map = readCombinedJson(context,"meniny_combined.json")
            return getCombinedNamesByDate(map, today,localities)
        }

        fun getTodayNames(dateMap: Map<String, Map<String, List<String>>>?): String {
            val today = LocalDate.now()
            val name: String = try {
                dateMap
                    ?.get(today.monthValue.toString())
                    ?.get(today.dayOfMonth.toString())!!.joinToString(", ")
            } catch (e: Exception) {
                "error"
            }
            if (name == "-") {
                return "Dnes nemá meniny nikto."
            }
            return name

        }

        fun getNameByDate(dateMap: Map<String, Map<String, List<String>>>?, day: Int, month: Int): String {
            val name = dateMap
                ?.get((month).toString())
                ?.get(day.toString())!!.joinToString(", ")
            if (name == "-") {
                return "Dnes nemá meniny nikto."
            }
            return name
        }

        fun getNameByDate(dateMap: Map<String, Map<String, List<String>>>?, date: LocalDate): String {
            return getNameByDate(dateMap, date.dayOfMonth,date.monthValue)
        }

        fun getCombinedNamesByDate(map: Map<String, Map<String, Map<String, List<String>>>>?, date: LocalDate, localities: List<String>): Map<String, String> {
            val result: MutableMap<String, String> = mutableMapOf()
            for (loc in localities) {
                val names = getNameByLocaleAndDate(map,loc,date)
                result.put(loc,names)
            }
            return result
        }

        fun combinedNameMapToString(map: Map<String, String>): String {
            val countries: Map<String, String> = mapOf(Pair("sk","\uD83C\uDDF8\uD83C\uDDF0"),
                Pair("cz", "\uD83C\uDDE8\uD83C\uDDFF"))
            var result: String = ""
            for (key in map.keys) {
                result += countries[key] + " " + map[key] + "\n"
            }
            return result.trimEnd()
        }

        fun getNameByLocaleAndDate(map: Map<String, Map<String, Map<String, List<String>>>>?, locale: String, date: LocalDate): String {
            return try {
                map?.get(locale)?.get(date.monthValue.toString())?.get(date.dayOfMonth.toString())!!.joinToString(", ")
            } catch (e: Exception) {
                "-"
            }
        }
    }


}