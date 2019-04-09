package com.cahjaya.lian.greenhousecabai;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import net.igenius.mqttservice.MQTTServiceReceiver;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import helpers.DatabaseHelper;
import helpers.MqttHelper;

public class Tab1Fragment extends Fragment {
    View myView;
    MqttHelper mqttHelper;
    DatabaseHelper myDb;
    SQLiteDatabase sqLiteDatabase;
    GraphView graphViewsuhu;
    GraphView graphViewhmdt;
    GraphView graphViewktanah;
    LineGraphSeries<DataPoint> seriessuhu=new LineGraphSeries<>(new DataPoint[0]);
    LineGraphSeries<DataPoint> serieshmdt=new LineGraphSeries<>(new DataPoint[0]);
    LineGraphSeries<DataPoint> seriesktanah=new LineGraphSeries<>(new DataPoint[0]);
    SimpleDateFormat sdfsuhu=new SimpleDateFormat(  "HH:mm:ss", Locale.getDefault());
    SimpleDateFormat sdfhmdt=new SimpleDateFormat(  "HH:mm:ss", Locale.getDefault());
    SimpleDateFormat sdfktanah=new SimpleDateFormat(  "HH:mm:ss", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_one, container, false);
        myDb = new DatabaseHelper(getActivity());
        sqLiteDatabase=myDb.getWritableDatabase();
        graphViewsuhu=(GraphView) myView.findViewById(R.id.chartsuhu);
        graphViewhmdt=(GraphView) myView.findViewById(R.id.charthmdt);
        graphViewktanah=(GraphView) myView.findViewById(R.id.chartktanah);
        graphViewsuhu.getViewport().setScalable(true);
        graphViewhmdt.getViewport().setScalable(true);
        graphViewktanah.getViewport().setScalable(true);
        graphViewsuhu.getViewport().setXAxisBoundsManual(true);
        graphViewsuhu.getViewport().setMinX(1);
        graphViewsuhu.getViewport().setMaxX(1000);
        graphViewhmdt.getViewport().setXAxisBoundsManual(true);
        graphViewhmdt.getViewport().setMinX(4);
        graphViewhmdt.getViewport().setMaxX(80);
        graphViewktanah.getViewport().setYAxisBoundsManual(true);
        graphViewktanah.getViewport().setMinY(4);
        graphViewktanah.getViewport().setMaxY(80);
        graphViewsuhu.getViewport().setYAxisBoundsManual(true);
        graphViewsuhu.getViewport().setMinY(1);
        graphViewsuhu.getViewport().setMaxY(100);
        graphViewhmdt.getViewport().setYAxisBoundsManual(true);
        graphViewhmdt.getViewport().setMinY(1);
        graphViewhmdt.getViewport().setMaxY(100);
        graphViewktanah.getViewport().setYAxisBoundsManual(true);
        graphViewktanah.getViewport().setMinY(1);
        graphViewktanah.getViewport().setMaxY(100);
        graphViewsuhu.addSeries(seriessuhu);
        graphViewhmdt.addSeries(serieshmdt);
        graphViewktanah.addSeries(seriesktanah);

        graphViewsuhu.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double valuesuhu, boolean isValueXsuhu){
                if(isValueXsuhu){
                    return sdfsuhu.format(new Date((long) valuesuhu));
                }else{
                    return super.formatLabel(valuesuhu, isValueXsuhu);
                }

            }
        });
        graphViewhmdt.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double valuehmdt, boolean isValueXhmdt){
                if(isValueXhmdt){
                    return sdfhmdt.format(new Date((long) valuehmdt));
                }else{
                    return super.formatLabel(valuehmdt, isValueXhmdt);
                }

            }
        });
        graphViewktanah.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double valuektanah, boolean isValueXktanah){
                if(isValueXktanah){
                    return sdfktanah.format(new Date((long) valuektanah));
                }else{
                    return super.formatLabel(valuektanah, isValueXktanah);
                }

            }
        });
        seriessuhu.resetData(getDataPointsuhu());
        serieshmdt.resetData(getDataPointhmdt());
        seriesktanah.resetData(getDataPointktanah());
        return myView;
    }
    private MQTTServiceReceiver receiver = new MQTTServiceReceiver() {
        @Override
        public void onSubscriptionSuccessful(Context context,
                                             String requestId, String topic) {
            // called when a message has been successfully published
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
                seriessuhu.resetData(getDataPointsuhu());
                graphViewsuhu.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                    @Override
                    public String formatLabel(double valuesuhu, boolean isValueXsuhu){
                        if(isValueXsuhu){
                            return sdfsuhu.format(new Date((long)valuesuhu));
                        }else{
                            return super.formatLabel(valuesuhu, isValueXsuhu);
                        }

                    }
                });
            }
            if (topic.equals("sensor/hmdt")){
                serieshmdt.resetData(getDataPointhmdt());
                graphViewhmdt.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                    @Override
                    public String formatLabel(double valuehmdt, boolean isValueXhmdt){
                        if(isValueXhmdt){
                            return sdfhmdt.format(new Date((long)valuehmdt));
                        }else{
                            return super.formatLabel(valuehmdt, isValueXhmdt);
                        }

                    }
                });
            }
            if (topic.equals("sensor/ktanah")){
                seriesktanah.resetData(getDataPointktanah());
                graphViewktanah.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                    @Override
                    public String formatLabel(double valuektanah, boolean isValueXktanah){
                        if(isValueXktanah){
                            return sdfktanah.format(new Date((long)valuektanah));
                        }else{
                            return super.formatLabel(valuektanah, isValueXktanah);
                        }

                    }
                });
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

    private DataPoint[] getDataPointsuhu() {
        String[] columns={"DATE","SUHU"};
        Cursor cursor=sqLiteDatabase.query("ghc_table",columns,null,null,null,null,null, null);
        DataPoint[] dpsuhu=new DataPoint[cursor.getCount()];

        for(int i=0;i<cursor.getCount();i++)
        {
            cursor.moveToNext();
            dpsuhu[i]=new DataPoint(cursor.getLong(0),cursor.getInt(1));
        }
        return dpsuhu;
    }
    private DataPoint[] getDataPointhmdt() {
        String[] columns={"DATE","HMDT"};
        Cursor cursor=sqLiteDatabase.query("ghc_table",columns,null,null,null,null,null, null);
        DataPoint[] dphmdt=new DataPoint[cursor.getCount()];

        for(int i=0;i<cursor.getCount();i++)
        {
            cursor.moveToNext();
            dphmdt[i]=new DataPoint(cursor.getLong(0),cursor.getInt(1));
        }
        return dphmdt;
    }
    private DataPoint[] getDataPointktanah() {
        String[] columns={"DATE","KTANAH"};
        Cursor cursor=sqLiteDatabase.query("ghc_table",columns,null,null,null,null,null, null);
        DataPoint[] dpktanah=new DataPoint[cursor.getCount()];

        for(int i=0;i<cursor.getCount();i++)
        {
            cursor.moveToNext();
            dpktanah[i]=new DataPoint(cursor.getLong(0),cursor.getInt(1));
        }
        return dpktanah;
    }
}