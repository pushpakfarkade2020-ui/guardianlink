package com.guardianlink;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class SOSWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {

        for (int id : ids) {

            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.widget_layout
            );

            // 👉 Intent to launch SOSActivity
            Intent intent = new Intent(context, SOSActivity.class);

            // ✅ Correct PendingIntent (Activity, NOT Broadcast)
            PendingIntent pi = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );

            // 👉 Attach click
            views.setOnClickPendingIntent(R.id.widgetBtn, pi);

            manager.updateAppWidget(id, views);
        }
    }
}