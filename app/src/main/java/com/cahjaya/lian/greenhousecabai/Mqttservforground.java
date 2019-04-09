package com.cahjaya.lian.greenhousecabai;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import helpers.DataHelper;
import helpers.DatabaseHelper;
import helpers.MqttHelper;

public class Mqttservforground extends Service {
    DatabaseHelper myDb;
    MqttHelper mqttHelper;
    SQLiteDatabase sqLiteDatabase;
    DataHelper data = new DataHelper();
    private List<Service> events;

    public Mqttservforground() {
    }
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";

    public class LocalBinder extends Binder {
        public Mqttservforground getService() {
            return Mqttservforground.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // do your jobs here
        myDb = new DatabaseHelper(this);
        sqLiteDatabase=myDb.getWritableDatabase();
        startForeground();
        new MqttService();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build());
    }
}
