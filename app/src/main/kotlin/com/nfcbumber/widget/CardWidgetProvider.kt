package com.nfcbumber.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.nfcbumber.MainActivity
import com.nfcbumber.R
import com.nfcbumber.data.security.SecureStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Widget provider for displaying selected NFC card on home screen.
 */
@AndroidEntryPoint
class CardWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var secureStorage: SecureStorage

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update all widgets
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Widget added for the first time
    }

    override fun onDisabled(context: Context) {
        // Last widget removed
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Get selected card info
                val cardName = secureStorage.getString("widget_card_name_$appWidgetId", "No Card Selected")
                val cardType = secureStorage.getString("widget_card_type_$appWidgetId", "")

                // Create an Intent to launch MainActivity
                val intent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                // Build the widget layout
                val views = RemoteViews(context.packageName, R.layout.widget_card).apply {
                    setTextViewText(R.id.widget_card_name, cardName)
                    setTextViewText(R.id.widget_card_type, cardType)
                    setOnClickPendingIntent(R.id.widget_container, pendingIntent)
                }

                // Update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }

    companion object {
        fun updateWidget(
            context: Context,
            appWidgetId: Int,
            cardName: String,
            cardType: String
        ) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val secureStorage = SecureStorage(context)
            
            // Save card info
            secureStorage.putString("widget_card_name_$appWidgetId", cardName)
            secureStorage.putString("widget_card_type_$appWidgetId", cardType)

            // Create intent
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Update widget
            val views = RemoteViews(context.packageName, R.layout.widget_card).apply {
                setTextViewText(R.id.widget_card_name, cardName)
                setTextViewText(R.id.widget_card_type, cardType)
                setOnClickPendingIntent(R.id.widget_container, pendingIntent)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
