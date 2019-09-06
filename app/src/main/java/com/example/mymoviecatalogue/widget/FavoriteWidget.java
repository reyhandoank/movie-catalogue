package com.example.mymoviecatalogue.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.mymoviecatalogue.R;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteWidget extends AppWidgetProvider {


    public static String TOAST_ACTION = "com.example.mymoviecatalogue.TOAST_ACTION";
    public static String EXTRA_ITEM = "com.example.mymoviecatalogue.EXTRA_ITEM";
    public static String UPDATE_WIDGET = "com.example.mymoviecatalogue.UPDATE_WIDGET";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        remoteViews.setRemoteAdapter(R.id.stack, intent);
        remoteViews.setEmptyView(R.id.stack, R.id.empty_view);

        Intent toastIntent = new Intent(context, FavoriteWidget.class);
        toastIntent.setAction(TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.stack, toastPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                String index = intent.getStringExtra(EXTRA_ITEM);
                Toast.makeText(context, index, Toast.LENGTH_SHORT).show();
            }

            if (intent.getAction().equals(UPDATE_WIDGET)) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] id = appWidgetManager.getAppWidgetIds(new ComponentName(context, FavoriteWidget.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.stack);
            }
        }
        super.onReceive(context, intent);
    }
}

