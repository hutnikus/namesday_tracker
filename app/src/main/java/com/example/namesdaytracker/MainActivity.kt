package com.example.namesdaytracker

import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.time.LocalDate


class MainActivity : ComponentActivity() {
    private var dateMap: Map<String, Map<String, List<String>>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.dateMap = Utils.readNamesDayJson(this,"meniny_sk.json")

        val today = LocalDate.now()


        val datePicker: DatePicker = findViewById(R.id.datePicker)
        datePicker.init(today.year,today.monthValue-1,today.dayOfMonth,::onDateChange)

        setTextViewText(Utils.getNameByDate(dateMap, today))

        Log.d("dano","main activity")

    }

    private fun setTextViewText(text: String) {
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = text
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onDateChange(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        setTextViewText(Utils.getNameByDate(dateMap, day,month+1))
    }
}

