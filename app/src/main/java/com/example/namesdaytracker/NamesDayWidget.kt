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
import com.example.namesdaytracker.data.DataUtils
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

const val ACTION_SCHEDULED_UPDATE = "com.example.namesdaytracker.SCHEDULED_UPDATE"

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

        scheduleNextUpdate(context)
    }

    override fun onEnabled(context: Context) {
        scheduleNextUpdate(context)

        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("widget","onReceive")

        if (intent.action == ACTION_SCHEDULED_UPDATE) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, NamesDayWidget::class.java))
            onUpdate(context, manager, ids)
        }
        super.onReceive(context, intent)
    }

    private fun scheduleNextUpdate(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Substitute AppWidget for whatever you named your AppWidgetProvider subclass
        val intent = Intent(context, NamesDayWidget::class.java)
        intent.setAction(ACTION_SCHEDULED_UPDATE)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val nextMidnight = ZonedDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT, ZoneId.systemDefault() )

        Log.d("dano",nextMidnight.toString())

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            nextMidnight.toInstant().toEpochMilli(),
            pendingIntent
        )
    }

    companion object {
        val chosenLocales: List<String> = listOf("cz","sk")
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    Log.d("widget","running update")
    val dataList = DataUtils.getAllData(context)
    val widgetText = DataUtils.getTodayPrintableNamesByLocale(dataList,NamesDayWidget.chosenLocales)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.names_day_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    views.setOnClickPendingIntent(R.id.widget_root,pendingIntent)
    views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}