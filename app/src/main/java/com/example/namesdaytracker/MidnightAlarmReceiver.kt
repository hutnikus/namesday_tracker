package com.example.namesdaytracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MidnightAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            // Handle the alarm event (update the widget, etc.)
            Toast.makeText(context, "Midnight Alarm Triggered", Toast.LENGTH_SHORT).show()
            // You may want to call the updateWidget function here
        }
    }
}