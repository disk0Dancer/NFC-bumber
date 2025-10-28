package com.nfcbumber.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.nfcbumber.R
import com.nfcbumber.data.security.SecureStorage
import com.nfcbumber.presentation.widget.CardEmulationActivity

/**
 * Widget provider for displaying selected NFC card on home screen.
 * Each widget is bound to a specific card and launches fullscreen card emulation on tap.
 */
class CardWidgetProvider : AppWidgetProvider() {

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

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        // Widget size changed, update layout
        updateAppWidget(context, appWidgetManager, appWidgetId)
    }

    override fun onEnabled(context: Context) {
        // Widget added for the first time
    }

    override fun onDisabled(context: Context) {
        // Last widget removed
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // Clean up saved data for deleted widgets
        val secureStorage = SecureStorage(context)
        for (appWidgetId in appWidgetIds) {
            secureStorage.remove("widget_card_id_$appWidgetId")
            secureStorage.remove("widget_card_name_$appWidgetId")
            secureStorage.remove("widget_card_type_$appWidgetId")
            secureStorage.remove("widget_card_color_$appWidgetId")
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        try {
            val secureStorage = SecureStorage(context)
            
            // Get widget size
            val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
            val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

            // Determine layout based on size
            val layoutId = getLayoutForSize(minWidth, minHeight)

            // Get selected card info
            val cardId = secureStorage.getLong("widget_card_id_$appWidgetId", -1L)
            val cardName = secureStorage.getString("widget_card_name_$appWidgetId", "No Card")
            val cardType = secureStorage.getString("widget_card_type_$appWidgetId", "Tap to select")
            val cardColor = secureStorage.getInt("widget_card_color_$appWidgetId", 0xFF2196F3.toInt())

            // Create an Intent to launch CardEmulationActivity or configuration
            val intent = if (cardId == -1L) {
                // No card selected, open configuration
                Intent(context, WidgetConfigurationActivity::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            } else {
                // Card selected, open fullscreen card emulation
                Intent(context, CardEmulationActivity::class.java).apply {
                    putExtra("CARD_ID", cardId)
                    putExtra("WIDGET_ID", appWidgetId)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                           Intent.FLAG_ACTIVITY_NO_ANIMATION or
                           Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                }
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Build the widget layout
            val views = RemoteViews(context.packageName, layoutId).apply {
                setTextViewText(R.id.widget_card_name, cardName)
                setOnClickPendingIntent(R.id.widget_container, pendingIntent)

                // Set background color with transparency for better visual
                val transparentColor = (cardColor and 0x00FFFFFF) or 0xCC000000.toInt()
                setInt(R.id.widget_container, "setBackgroundColor", transparentColor)
            }

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        } catch (e: Exception) {
            // Handle error silently
            e.printStackTrace()
        }
    }

    private fun getLayoutForSize(width: Int, height: Int): Int {
        // Determine which layout to use based on widget size
        // Small: 1x1 (up to 110dp), Medium: 2x1, Large: 2x2
        return when {
            width < 180 && height < 180 -> R.layout.widget_card_small  // 1x1
            height < 180 -> R.layout.widget_card_medium  // 2x1
            else -> R.layout.widget_card_large  // 2x2
        }
    }
}
