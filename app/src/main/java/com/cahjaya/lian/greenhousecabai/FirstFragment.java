package com.cahjaya.lian.greenhousecabai;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import net.igenius.mqttservice.MQTTServiceCommand;
import net.igenius.mqttservice.MQTTServiceReceiver;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import helpers.DataHelper;
import helpers.DatabaseHelper;
import helpers.MqttHelper;


/**
 * Created by Lian CahJaya on 28/03/2019.
 */

public class FirstFragment extends Fragment {
    DatabaseHelper myDb;
    Button btnviewAll;
    MqttHelper mqttHelper;
    SQLiteDatabase sqLiteDatabase;
    DataHelper data = new DataHelper();
    SimpleDateFormat sdf=new SimpleDateFormat(  "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    TextView tt;
    TextView th;
    TextView kt;
    String lp,ki, kr;
    ImageView lampui, kipasi, krani;
    Switch lampu, kipas, kran;
    View myView;
    int count = 0;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.firstlayout, container, false);
        myDb = new DatabaseHelper(getActivity());
        sqLiteDatabase=myDb.getWritableDatabase();
        tt = (TextView) myView.findViewById(R.id.tt);
        th = (TextView) myView.findViewById(R.id.th);
        kt = (TextView) myView.findViewById(R.id.kt);
        lampu = (Switch) myView.findViewById(R.id.lampu);
        kipas = (Switch) myView.findViewById(R.id.kipas);
        kran = (Switch) myView.findViewById(R.id.kran);
        btnviewAll = (Button)myView.findViewById(R.id.button_viewAll);
        lampui = (ImageView) myView.findViewById(R.id.lampui);
        kipasi = (ImageView) myView.findViewById(R.id.kipasi);
        krani = (ImageView) myView.findViewById(R.id.krani);
        viewAll();
        lampu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(lampu.isChecked()){
                    byte[] payload1 = "1".getBytes();
                    MQTTServiceCommand.publish(getActivity(), "sensor/lampu", payload1);
                    lampui.setImageResource(R.drawable.lampu_on1);
                    lampu.setChecked(true);
                }else if(!lampu.isChecked()) {
                    byte[] payload1 = "0".getBytes();
                    MQTTServiceCommand.publish(getActivity(), "sensor/lampu", payload1);
                    lampui.setImageResource(R.drawable.lampu_off1);
                    lampu.setChecked(false);
                }

            }
        });
        kipas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(kipas.isChecked()){
                    byte[] payload1 = "1".getBytes();
                    MQTTServiceCommand.publish(getActivity(), "sensor/kipas", payload1);
                    kipasi.setImageResource(R.drawable.fan_on1);
                    kipas.setChecked(true);
                }else if(!kipas.isChecked()) {
                    byte[] payload1 = "0".getBytes();
                    MQTTServiceCommand.publish(getActivity(), "sensor/kipas", payload1);
                    kipasi.setImageResource(R.drawable.fan_off1);
                    kipas.setChecked(false);
                }

            }
        });
        kran.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(kran.isChecked()){
                    byte[] payload1 = "1".getBytes();
                    MQTTServiceCommand.publish(getActivity(), "sensor/kran", payload1);
                    krani.setImageResource(R.drawable.kran_on1);
                    kran.setChecked(true);
                }else if(!kran.isChecked()) {
                    byte[] payload1 = "0".getBytes();
                    MQTTServiceCommand.publish(getActivity(), "sensor/kran", payload1);
                    krani.setImageResource(R.drawable.kran_off1);
                    kran.setChecked(false);
                }

            }
        });
        return myView;

    }
    private MQTTServiceReceiver receiver = new MQTTServiceReceiver() {
        @Override
        public void onSubscriptionSuccessful(Context context,
                                             String requestId, String topic) {

        }

        @Override
        public void onSubscriptionError(Context context, String requestId,
                                        String topic, Exception exception) {
            // called when a subscription is not successful.
            // This usually happens when the broker does not give permissions
            // for the requested topic
        }

        @Override
        public void onPublishSuccessful(Context context, String requestId, String topic) {
            // called when a subscription is successful
        }

        @Override
        public void onMessageArrived(Context context, String topic,
                                     byte[] payload) {
            if (topic.equals("sensor/suhu")){
                tt.setText(new String(payload));
            }
            if (topic.equals("sensor/hmdt")){
                th.setText(new String(payload));
            }
            if (topic.equals("sensor/ktanah")){
                kt.setText(new String(payload));
            }
            if (topic.equals("sensor/lampu")){
                lp = new String(payload);
                if(lp.equals("1")) {
                    lampu.setChecked(true);
                    lampui.setImageResource(R.drawable.lampu_on1);
                }else if(lp.equals("0")){
                    lampu.setChecked(false);
                    lampui.setImageResource(R.drawable.lampu_off1);
                }
            }
            if (topic.equals("sensor/kipas")){
                ki = new String(payload);
                if(ki.equals("1")) {
                    kipas.setChecked(true);
                    kipasi.setImageResource(R.drawable.fan_on1);
                }else if(ki.equals("0")){
                    kipas.setChecked(false);
                    kipasi.setImageResource(R.drawable.fan_off1);
                }
            }
            if (topic.equals("sensor/kran")){
                kr = new String(payload);
                if(kr.equals("1")) {
                    kran.setChecked(true);
                    krani.setImageResource(R.drawable.kran_on1);
                }else if(kr.equals("0")){
                    kran.setChecked(false);
                    krani.setImageResource(R.drawable.kran_off1);
                }
            }
            // called when a new message arrives on any topic
        }

        @Override
        public void onConnectionSuccessful(Context context, String requestId) {
            // called when the connection is successful
        }

        @Override
        public void onException(Context context, String requestId,
                                Exception exception) {
            // called when an error happens
        }

        @Override
        public void onConnectionStatus(Context context, boolean connected) {
            // called when connection status is requested or changes
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        receiver.register(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        receiver.unregister(getActivity());
    }
    public void viewAll() {
        btnviewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            //buffer.append("Id :"+ res.getString(0)+"\n");
                            buffer.append("Suhu :"+ res.getString(1)+"\n");
                            buffer.append("Humudity :"+ res.getString(2)+"\n");
                            buffer.append("Kelembapan Tanah :"+ res.getString(3)+"\n");
                            buffer.append("Waktu :"+ sdf.format(new Date((long)res.getLong(4)))+"\n\n");
                        }

                        // Show all data
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}
