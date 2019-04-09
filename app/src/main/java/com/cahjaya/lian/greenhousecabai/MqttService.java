package com.cahjaya.lian.greenhousecabai;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import net.igenius.mqttservice.MQTTServiceReceiver;


import java.util.Date;

import helpers.DataHelper;
import helpers.DatabaseHelper;



public class MqttService extends MQTTServiceReceiver {
    DataHelper data = new DataHelper();
    private static final String TAG = "Receiver";
    @Override
    public void onPublishSuccessful(Context context, String requestId, String topic) {

    }

    @Override
    public void onSubscriptionSuccessful(Context context, String requestId, String topic) {

    }

    @Override
    public void onSubscriptionError(Context context, String requestId, String topic, Exception exception) {

    }

    @Override
    public void onMessageArrived(Context context, String topic, byte[] payload){

        Log.e(TAG, "New message on " + topic + ":  " + new String(payload));
        if (topic.equals("sensor/suhu")) {
            data.setSuhu(Float.parseFloat(new String(payload)));
        }
        if (topic.equals("sensor/hmdt")) {
            data.setHmdt(Float.parseFloat(new String(payload)));
        }
        if (topic.equals("sensor/ktanah")) {
            data.setKtanah(Float.parseFloat(new String(payload)));
        }
        masukdata(context);
    }

    @Override
    public void onConnectionSuccessful(Context context, String requestId) {
        Log.e(TAG, "Connected!");
    }

    @Override
    public void onException(Context context, String requestId, Exception exception) {
        Log.e(TAG, requestId + " exception");
    }

    @Override
    public void onConnectionStatus(Context context, boolean connected) {

    }
    private void masukdata(Context context){
        DatabaseHelper db = new DatabaseHelper(context);
        Float yValue = data.getSuhu();
        Float zValue = data.getHmdt();
        Float zzValue = data.getKtanah();
        long xValue = new Date().getTime();
        boolean isInserted = db.insertData(yValue, zValue, zzValue, xValue);
        if(isInserted==true) {
            Log.e("Debug", "Data Masuk");
        }
    }

}
