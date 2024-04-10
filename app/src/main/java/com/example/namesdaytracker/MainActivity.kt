package com.example.namesdaytracker

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.time.LocalDate


class MainActivity : ComponentActivity() {
    private var dateMap: Map<String, Map<String, List<String>>>? = null
    private var combinedDateMap: Map<String, Map<String, Map<String, List<String>>>>? = null
    private val chosenLocales = listOf("cz","sk")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.dateMap = Utils.readNamesDayJson(this,"meniny_sk.json")
//        this.combinedDateMap = Utils.readNamesDayJson(this,"meniny_sk.json")
        combinedDateMap = Utils.readCombinedJson(this,"meniny_combined.json")
        Log.d("json", dateMap.toString())
        Log.d("json", combinedDateMap.toString())


        val today = LocalDate.now()

        val datePicker: DatePicker = findViewById(R.id.datePicker)
        datePicker.init(today.year,today.monthValue-1,today.dayOfMonth,::onDateChange)
        val yearPart = datePicker.findViewById<View>(Resources.getSystem().getIdentifier("android:id/year",null,null))
        if (yearPart != null) {
            yearPart.visibility = View.GONE
        }


//        setTextViewText(Utils.getNameByDate(dateMap, today))
        val nameToday = Utils.getCombinedNamesByDate(combinedDateMap,today, chosenLocales)
        setTextViewText(Utils.combinedNameMapToString(nameToday))

        Log.d("dano","main activity")



    }

    private fun setTextViewText(text: String) {
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = text
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onDateChange(datePicker: DatePicker, year: Int, month: Int, day: Int) {
//        setTextViewText(Utils.getNameByDate(dateMap, day,month+1))
        Log.d("month",String.format("year %d, month %d, day %d",year,month,day))

        val selectedDay = LocalDate.of(year,month+1,day)
        val nameToday = Utils.getCombinedNamesByDate(combinedDateMap,selectedDay, chosenLocales)
        setTextViewText(Utils.combinedNameMapToString(nameToday))
    }
}

