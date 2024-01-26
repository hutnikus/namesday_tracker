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
    }


}