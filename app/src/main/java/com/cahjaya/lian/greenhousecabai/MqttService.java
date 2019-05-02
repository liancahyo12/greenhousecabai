package com.cahjaya.lian.greenhousecabai;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import net.igenius.mqttservice.MQTTServiceCommand;
import net.igenius.mqttservice.MQTTServiceReceiver;


import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import helpers.DataHelper;
import helpers.DatabaseHelper;

import static helpers.NotifHelper.CHANNEL_3_ID;
import static helpers.NotifHelper.CHANNEL_2_ID;


public class MqttService extends MQTTServiceReceiver {
    private NotificationManagerCompat notificationManager;
    DataHelper data = new DataHelper();
    DatabaseHelper myDb;
    Integer s = 0, kr = 0, ktt = 0;
    static int i = 3;
    static boolean ok;
    private static final String TAG = "Receiver";
    Random r1 = new Random();
    Random r2 = new Random();
    Random r3 = new Random();
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
        notificationManager = NotificationManagerCompat.from(context);
        Log.e(TAG, "New message on " + topic + ":  " + new String(payload));
        if (topic.equals("sensor/suhu")) {
            data.setSuhu(Float.parseFloat(new String(payload)));
            s = (Integer.parseInt(new String(payload)));
        }
        if (topic.equals("sensor/hmdt")) {
            data.setHmdt(Float.parseFloat(new String(payload)));
            kr = (Integer.parseInt(new String(payload)));
        }
        if (topic.equals("sensor/ktanah")) {
            data.setKtanah(Float.parseFloat(new String(payload)));
            ktt = (Integer.parseInt(new String(payload)));
            masukdata(context);
        }
        if (topic.equals("sensor/lampu")){
            data.setLampu(Integer.parseInt(new String(payload)));
            masukdatac(context);
        }
        if   (topic.equals("sensor/kipas")){
            data.setKipas(Integer.parseInt(new String(payload)));
            masukdatac(context);
        }
        if (topic.equals("sensor/kran")){
            data.setKran(Integer.parseInt(new String(payload)));
            masukdatac(context);
        }
        ceksetdata();
        notif(context);

        //randomfakedata(context);

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
        Calendar c = Calendar.getInstance();
        int m = c.get(Calendar.MINUTE);
        if (m ==00 || m==01 || m==02 || m==59 || m==58) {
            DatabaseHelper db = new DatabaseHelper(context);
            Float yValue = data.getSuhu();
            Float zValue = data.getHmdt();
            Float zzValue = data.getKtanah();
            long xValue = new Date().getTime();
            boolean isInserted = db.insertData(yValue, zValue, zzValue, xValue);
            if (isInserted == true) {
                Log.e("Debug", "Data Masuk");
            }
        }
    }
    private void masukdatac(Context context){
        DatabaseHelper db = new DatabaseHelper(context);
        Integer lampu = data.getLampu();
        Integer kipas = data.getKipas();
        Integer kran = data.getKran();
        long xValue = new Date().getTime();
        boolean isInserted = db.insertDatac(lampu, kipas, kran, xValue);
        if(isInserted==true) {
            Log.e("Debug", "Control Data Masuk");
        }
    }
    private void notif(Context context){
        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_2_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(data.getTitle())
                .setContentText(data.getMessage())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setGroup("Data_Greenhouse_Cabai")
                .build();
        Notification summaryNotification = new NotificationCompat.Builder(context, CHANNEL_2_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(data.getMessage())
                        .setBigContentTitle(data.getTitle()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup("Data_Greenhouse_Cabai")
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true)
                .build();
        if (ok){
            notificationManager.notify(i++, notification);
            notificationManager.notify(2, summaryNotification);
        }
    }
    public void ceksetdata(){
        if(s == 0 && kr ==0 && ktt ==0){
            ok=false;
        }
        else if(s <= 23 && kr ==0 && ktt ==0){
            data.setTitle("Suhu terlalu rendah");
            data.setMessage("Suhu : "+data.getSuhu());
            ok = true;
        }else if(s >= 29 && kr ==0 && ktt ==0){
            data.setTitle("Suhu terlalu tinggi");
            data.setMessage("Suhu : "+data.getSuhu());
            ok = true;
        }else if(kr <= 59 && s == 0 && ktt == 0){
            data.setTitle("Kelembapan ruang terlalu rendah");
            data.setMessage("Kelembapan Ruang : "+data.getHmdt());
            ok = true;
        }else if(kr >= 81 && s == 0 && ktt == 0){
            data.setTitle("Kelembapan ruang terlalu tinggi");
            data.setMessage("Kelembapan Ruang : "+data.getHmdt());
            ok = true;
        }else if(ktt <= 59 && s == 0 && kr == 0){
            data.setTitle("Kelembapan tanah terlalu rendah");
            data.setMessage("Kelembapan Tanah : "+data.getKtanah());
            ok = true;
        }else if(ktt >= 81 && s == 0 && kr == 0){
            data.setTitle("Kelembapan tanah terlalu tinggi");
            data.setMessage("Kelembapan Tanah : "+data.getKtanah());
            ok = true;
        }else {
            ok=false;
        }
    }
    public void randomfakedata(Context context){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int sh = r1.nextInt(11);
        sh += 20;
        String shs = Integer.toString(sh);
        byte[] payload1 =  shs.getBytes();
        int sh1 = r2.nextInt(40);
        sh1 += 50;
        String shs1 = Integer.toString(sh1);
        byte[] payload2 =  shs1.getBytes();
        int sh2 = r3.nextInt(40);
        sh2 += 50;
        String shs2 = Integer.toString(sh2);
        byte[] payload3 =  shs2.getBytes();
        MQTTServiceCommand.publish(context, "sensor/suhu", payload1);
        MQTTServiceCommand.publish(context, "sensor/hmdt", payload2);
        MQTTServiceCommand.publish(context, "sensor/ktanah", payload3);
    }
}
