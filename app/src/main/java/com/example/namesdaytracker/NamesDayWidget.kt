package com.example.namesdaytracker

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime


/**
 * Implementation of App Widget functionality.
 */
class NamesDayWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }

        _scheduleNextUpdate(context)
    }

    override fun onEnabled(context: Context) {
        _scheduleNextUpdate(context)

        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private val ACTION_SCHEDULED_UPDATE = "com.example.namesdaytracker.SCHEDULED_UPDATE"

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_SCHEDULED_UPDATE) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, NamesDayWidget::class.java))
            onUpdate(context, manager, ids)
        }
        super.onReceive(context, intent)
    }

    private fun _scheduleNextUpdate(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Substitute AppWidget for whatever you named your AppWidgetProvider subclass
        val intent = Intent(context, NamesDayWidget::class.java)
        intent.setAction(ACTION_SCHEDULED_UPDATE)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val nextMidnight = ZonedDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT, ZoneId.systemDefault() )
//        var nextMidnight = ZonedDateTime.of(LocalDate.now(), LocalTime.now().plusSeconds(15), ZoneId.systemDefault() )

        Log.d("dano",nextMidnight.toString())

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            nextMidnight.toInstant().toEpochMilli(),
            pendingIntent
        )
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
//    val widgetText = context.getString(R.string.appwidget_text)

    val widgetText = Utils.getTodayNames(context)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.names_day_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}