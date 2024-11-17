package com.example.namesdaytracker

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.namesdaytracker.data.DataUtils
import com.example.namesdaytracker.data.Locale
import java.time.LocalDate


class MainActivity : ComponentActivity() {
    private val chosenLocales = listOf("cz","sk")
    private lateinit var locales: ArrayList<Locale>

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("widget","main activity created")

        setContentView(R.layout.activity_main)

        locales = DataUtils.getAllData(this)
        // random comment

        val today = LocalDate.now()

        val datePicker: DatePicker = findViewById(R.id.datePicker)
        datePicker.init(today.year,today.monthValue-1,today.dayOfMonth,::onDateChange)
        val yearPart = datePicker.findViewById<View>(Resources.getSystem().getIdentifier("android:id/year",null,null))
        if (yearPart != null) {
            yearPart.visibility = View.GONE
        }

        val todayButton: Button = findViewById(R.id.todayButton)
        todayButton.setOnClickListener(::onTodayButtonClick)

        val nameToday = DataUtils.getPrintableNamesByLocaleAndDate(locales,chosenLocales,today.dayOfMonth,today.monthValue)
        setTextViewText(nameToday)
    }

    private fun setTextViewText(text: String) {
        Log.d("files", "printDayNames: $text")
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = text
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onDateChange(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        Log.d("month",String.format("year %d, month %d, day %d",year,month,day))

        //+1 because the picker has january as 0
        setTextViewText(DataUtils.getPrintableNamesByLocaleAndDate(locales,chosenLocales,day,month+1))
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onTodayButtonClick(view: View) {
        Log.d("button press", "Button pressed")

        val today = LocalDate.now()
        setDatePickerDate(today.year,today.monthValue-1,today.dayOfMonth)
    }

    private fun setDatePickerDate(year: Int,month: Int,day: Int) {
        val datePicker: DatePicker = findViewById(R.id.datePicker)
        datePicker.updateDate(year,month,day)
    }
}

