package com.example.my_widget_sample

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.RemoteViews
import es.antonborri.home_widget.HomeWidgetLaunchIntent
import es.antonborri.home_widget.HomeWidgetPlugin
import es.antonborri.home_widget.HomeWidgetProvider

class HomeScreenWidgetProvider: HomeWidgetProvider() {

    companion object {
        const val COUNT_ID = "_counter"
        const val DEFAULT_COUNT = -1
        const val UPDATE_ACTION = "UpdateCountButtonClicked"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
        widgetData: SharedPreferences
    ) {
        for (widgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId)
        }
    }

    fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {

        // widgetのレイアウトに対しての処理を記述する
        val views = RemoteViews(context.packageName, R.layout.widget_layout).apply {

            // 起動時のintentを設定
            val pendingIntent = HomeWidgetLaunchIntent.getActivity(context, MainActivity::class.java)
            setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            val dataStore = HomeWidgetPlugin.getData(context)
            val counter = dataStore.getInt(COUNT_ID, DEFAULT_COUNT)

            val counterText = "counter value is $counter"
            setTextViewText(R.id.tv_counter, counterText)

            val countIntent = Intent(context, HomeScreenWidgetProvider::class.java).apply {
                action = UPDATE_ACTION
            }
            val countPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                countIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
            setOnClickPendingIntent(R.id.bt_update, countPendingIntent)
        }

        appWidgetManager.updateAppWidget(widgetId, views)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        // null check
        if (context == null || intent == null) {
            return
        }

        when (intent.action) {
            UPDATE_ACTION -> {
                val dataStore = HomeWidgetPlugin.getData(context)
                var clickCount = dataStore.getInt(COUNT_ID, DEFAULT_COUNT)

                clickCount++

                dataStore.edit().putInt(COUNT_ID, clickCount).apply()

                val views = RemoteViews(context.packageName, R.layout.widget_layout)
                val counterText = "counter value is $clickCount by home widget"
                views.setTextViewText(R.id.tv_counter, counterText)

                val myWidget = ComponentName(context, HomeScreenWidgetProvider::class.java)
                val manager = AppWidgetManager.getInstance(context)
                manager.updateAppWidget(myWidget, views)
            }
        }
    }

}