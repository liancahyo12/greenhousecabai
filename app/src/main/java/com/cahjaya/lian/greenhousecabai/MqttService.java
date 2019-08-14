package com.cahjaya.lian.greenhousecabai;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import net.igenius.mqttservice.MQTTServiceReceiver;


import java.util.Calendar;
import java.util.Date;

import helpers.DataHelper;
import helpers.DatabaseHelper;

import static helpers.NotifHelper.CHANNEL_2_ID;


public class MqttService extends MQTTServiceReceiver {
    private NotificationManagerCompat notificationManager;
    DataHelper data = new DataHelper();
    Integer s = 0, ktt = 0, lp = 0, ks = 0, pp = 0;
    static int i = 3;
    static boolean ok ,yes;
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
        notificationManager = NotificationManagerCompat.from(context);
        Log.e(TAG, "New message on " + topic + ":  " + new String(payload));
        yes = false;
        ok = false;
        if (topic.equals("sensor/suhu")) {
            data.setSuhu(Integer.parseInt(new String(payload)));
            s = (Integer.parseInt(new String(payload)));
            if(s <= 23){
                data.setTitle("Suhu terlalu rendah");
                data.setMessage("Suhu : "+data.getSuhu());
                ok = true;
            }else if(s >= 29){
                data.setTitle("Suhu terlalu tinggi");
                data.setMessage("Suhu : "+data.getSuhu());
                ok = true;
            }
        }
        if (topic.equals("sensor/ktanah")) {
            data.setKtanah(Integer.parseInt(new String(payload)));
            ktt = (Integer.parseInt(new String(payload)));
            if(ktt <= 59){
                data.setTitle("Kelembapan tanah terlalu rendah");
                data.setMessage("Kelembapan Tanah : "+data.getKtanah());
                ok = true;
            }else if(ktt >= 81){
                data.setTitle("Kelembapan tanah terlalu tinggi");
                data.setMessage("Kelembapan Tanah : "+data.getKtanah());
                ok = true;
            }
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
        if (topic.equals("status/lampu")){
            lp = (Integer.parseInt(new String(payload)));
            if(lp == 1) {
                data.setTitle("Status Lampu");
                data.setMessage("Lampu Menyala");
                ok = true;
            }
            if(lp ==0){
                data.setTitle("Status Lampu");
                data.setMessage("Lampu Mati");
                ok = true;
            }
        }
        if   (topic.equals("status/kipas")){
            ks = (Integer.parseInt(new String(payload)));
            if(ks == 1) {
                data.setTitle("Status Kipas");
                data.setMessage("Kipas Menyala");
                ok = true;
            }
            if(ks ==0){
                data.setTitle("Status Kipas");
                data.setMessage("Kipas Mati");
                ok = true;
            }
        }
        if (topic.equals("status/kran")){
            pp = (Integer.parseInt(new String(payload)));
            if(pp == 1) {
                data.setTitle("Status Pompa");
                data.setMessage("Pompa Menyala");
                ok = true;
            }
            if(pp ==0){
                data.setTitle("Status Pompa");
                data.setMessage("Pompa Mati");
                ok = true;
            }
        }
        createNotification(data.getMessage(),context);
        //notif(context);
        //notifakuator(context);
        //createNotification2(data.getMessage(),context);

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
        if(connected){
            Toast.makeText(context.getApplicationContext(), "Greenhouse Cabai Tersambung ke server", Toast.LENGTH_LONG).show();
        }else if(!connected){
            Toast.makeText(context.getApplicationContext(), "Greenhouse Cabai terputus dari server", Toast.LENGTH_LONG).show();
            Toast.makeText(context.getApplicationContext(), "Greenhouse Cabai menyambungkan kembali ke server ......", Toast.LENGTH_LONG).show();
        }


    }
    private void masukdata(Context context){
        Calendar c = Calendar.getInstance();
        int m = c.get(Calendar.MINUTE);
        if (m < 05 || m > 55) {
            DatabaseHelper db = new DatabaseHelper(context);
            int yValue = data.getSuhu();
            int zzValue = data.getKtanah();
            long xValue = new Date().getTime();
            boolean isInserted = db.insertData(yValue, zzValue, xValue);
            if (isInserted == true) {
                Log.e("Debug", "Data Masuk");
            }
            db.close();
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
        db.close();
    }
    private void notif(Context context){
        Intent notificationIntent = new Intent(context, MainActivity.class);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
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
                .setSound(uri)
                .setAutoCancel(true)
                .build();
        Notification summaryNotification = new NotificationCompat.Builder(context, CHANNEL_2_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(data.getMessage())
                        .setBigContentTitle(data.getTitle()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup("Data_Greenhouse_Cabai")
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setContentIntent(pendingIntent)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .build();
        if (ok){
            notificationManager.notify(i++, notification);
            notificationManager.notify(2, summaryNotification);
        }
    }
    private NotificationManager notifManager;
    public void createNotification(String aMessage, Context context) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String id = CHANNEL_2_ID; // default_channel_id
        String title = "GreenhouseCabai"; // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id);
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200});
                notifManager.createNotificationChannel(mChannel);
            }
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(data.getTitle())                            // required
                    .setSmallIcon(R.mipmap.ic_launcher)   // required
                    .setContentText(aMessage) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setSound(uri);
            if (ok) {
                Notification notification = builder.build();
                notifManager.notify(i++, notification);;
            }
        }
        else {
            notif(context);
        }

    }
}
