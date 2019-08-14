package com.cahjaya.lian.greenhousecabai;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import helpers.DatabaseHelper;
import helpers.MqttHelper;

public class KelFrag extends Fragment {
    View myView;
    MqttHelper mqttHelper;
    DatabaseHelper myDb;
    SQLiteDatabase sqLiteDatabase;
    GraphView graphViewsuhu;
    GraphView graphViewktanah;
    Button btnsuhuj, btnkeltj, harb, mingb, bulb, tahb,harb1, mingb1, bulb1, tahb1;
    ViewGroup tabsuhu, tabkelt, graf, shinn, kelinn, ii1,ii2,ii3,ii4,ii5,ii6,ii7,ii8;
    TextView legsj,legkj;
    long xktanah0,xktanahx;
    LineGraphSeries<DataPoint> seriessuhu=new LineGraphSeries<>(new DataPoint[0]);
    LineGraphSeries<DataPoint> seriesktanah=new LineGraphSeries<>(new DataPoint[0]);
    SimpleDateFormat sdfsuhu=new SimpleDateFormat(  "HH, dd", Locale.getDefault());
    SimpleDateFormat sdfktanah=new SimpleDateFormat(  "HH, dd", Locale.getDefault());
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View myView = inflater.inflate(R.layout.fragkel, container, false);
        myDb = new DatabaseHelper(getActivity());
        sqLiteDatabase=myDb.getWritableDatabase();
        graphViewsuhu=(GraphView) myView.findViewById(R.id.chartsuhu);
        graphViewktanah=(GraphView) myView.findViewById(R.id.chartktanah);

        tabsuhu=(ViewGroup) myView.findViewById(R.id.suhuruang);
        tabkelt=(ViewGroup) myView.findViewById(R.id.keltanah);
        graf=(ViewGroup) myView.findViewById(R.id.grafik);
        legsj=(TextView) myView.findViewById(R.id.legs);
        legkj=(TextView) myView.findViewById(R.id.legk);

        getkdata();
        chartkonf();
        resetktanah();
        final TabLayout tabLayout = myView.findViewById(R.id.tab_layoutk);
        tabLayout.addTab(tabLayout.newTab().setText("Harian"));
        tabLayout.addTab(tabLayout.newTab().setText("Mingguan"));
        tabLayout.addTab(tabLayout.newTab().setText("Bulanan"));
        tabLayout.addTab(tabLayout.newTab().setText("Tahunan"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0){
                    graphViewktanah.getGridLabelRenderer().setNumHorizontalLabels(9);
                    graphViewktanah.getViewport().setMinX(xktanah0-432);
                    legkj.setText("□ Kelembapan Tanah %   □ Waktu Jam, Tanggal");
                    sdfktanah=new SimpleDateFormat(  "HH, dd", Locale.getDefault());
                    resetktanah();
                }else if(tabLayout.getSelectedTabPosition() == 1){
                    graphViewktanah.getGridLabelRenderer().setNumHorizontalLabels(7);
                    graphViewktanah.getViewport().setMinX(xktanah0-6000);
                    legkj.setText("□ Kelembapan Tanah %  □ Waktu Tanggal, Bulan");
                    sdfktanah=new SimpleDateFormat(  "dd, MMM", Locale.getDefault());
                    resetktanah();
                }else if(tabLayout.getSelectedTabPosition() == 2){
                    graphViewktanah.getGridLabelRenderer().setNumHorizontalLabels(4);
                    graphViewktanah.getViewport().setMinX(xktanah0-25920);
                    legkj.setText("□ Kelembapan Tanah %   □ Waktu Tanggal, Bulan");
                    sdfsuhu=new SimpleDateFormat(  "dd, MMM", Locale.getDefault());
                    resetktanah();
                }else if(tabLayout.getSelectedTabPosition() == 3){
                    graphViewktanah.getGridLabelRenderer().setNumHorizontalLabels(7);
                    graphViewktanah.getViewport().setMinX(xktanah0-315360);
                    legkj.setText("□ Kelembapan Tanah %   □ Waktu Bulan, Tahun");
                    sdfktanah=new SimpleDateFormat(  "MMM, yyyy", Locale.getDefault());
                    resetktanah();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        return myView;
    }
    private DataPoint[] getDataPointktanah() {
        String[] columns={"DATE","KTANAH"};
        Cursor cursor=sqLiteDatabase.query("data_kondisi",columns,null,null,null,null,null, null);
        DataPoint[] dpktanah=new DataPoint[cursor.getCount()];

        for(int i=0;i<cursor.getCount();i++)
        {
            cursor.moveToNext();
            dpktanah[i]=new DataPoint(cursor.getLong(0)/100000,cursor.getInt(1));

        }
        cursor.close();
        return dpktanah;
    }
    public void chartkonf(){
        graphViewktanah.getViewport().setScalable(true);
        graphViewktanah.getGridLabelRenderer().setTextSize(24f);
        graphViewktanah.getViewport().setXAxisBoundsManual(true);
        graphViewktanah.getViewport().setMinX(xktanah0-432);
        graphViewktanah.getViewport().setMaxX(xktanah0);
        graphViewktanah.getViewport().setYAxisBoundsManual(true);
        graphViewktanah.getViewport().setMinY(1);
        graphViewktanah.getViewport().setMaxY(100);
        graphViewktanah.getGridLabelRenderer().setNumHorizontalLabels(9);
    }
    public void getkdata(){
        Cursor res = myDb.getLastData();
        if(res.getCount() == 0) {
            // show message
            //showMessage("Error","Nothing found");
            return;
        }
        while (res.moveToNext()) {
            xktanah0 = res.getLong(3) / 100000;
        }
        Cursor res0 = myDb.getFirstData();
        if(res0.getCount() == 0) {
            // show message
            //showMessage("Error","Nothing found");
            return;
        }
        while (res0.moveToNext()) {
            xktanahx = res0.getLong(3) / 100000;
            // Do somethi
        }
        res.close();
    }
    public void resetktanah(){
        graphViewktanah.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double valuektanah, boolean isValueXktanah){
                if(isValueXktanah){
                    return sdfktanah.format(new Date((long) valuektanah*100000));
                }else{
                    return super.formatLabel(valuektanah, isValueXktanah);
                }

            }
        });
        graphViewktanah.addSeries(seriesktanah);
        seriesktanah.resetData(getDataPointktanah());
    }
}