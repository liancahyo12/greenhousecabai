package com.cahjaya.lian.greenhousecabai;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import helpers.DatabaseHelper;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import static android.support.v4.content.FileProvider.getUriForFile;

/**
 * Created by Lian CahJaya on 28/11/2017.
 */

public class ThirdFragment extends Fragment {
    Button backup;
    Button restore;
    Button simpan, aboutt;
    Switch serv;
    View myView;
    ViewGroup bcc,rss,exx;
    protected Cursor cursor;
    DatabaseHelper dbHelper;
    private static final String TAG = "Receiver";

    SimpleDateFormat sdf=new SimpleDateFormat(  "yyyy-MM-dd HH:mm:ss", Locale.getDefault());


    private String  csvFile = "DataGreenhouseCabai.xls";

    private File directoryex = new File("/mnt/sdcard/GreenhouseCabai/excel/");
    private File directorych = new File("/data/data/com.cahjaya.lian.greenhousecabai/files/excel/");
    private File directorydb = new File("/mnt/sdcard/GreenhouseCabai/data/");
    private static String DB_PATH = "/data/data/com.cahjaya.lian.greenhousecabai/databases/";
    private static String DB_NAME = "ghc.db";
    private static String DB_NAME1 = "ghc.db-wal";
    private static String DB_NAME2 = "ghc.db-shm";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.thirdlayout, container, false);

        backup = (Button)myView.findViewById(R.id.backup);
        aboutt=(Button)myView.findViewById(R.id.aboutt);
        bcc=(ViewGroup)myView.findViewById(R.id.bcg);
        rss=(ViewGroup)myView.findViewById(R.id.rsg);
        //bcc.setVisibility(View.GONE);
        //rss.setVisibility(View.GONE);
        aboutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),AboutActivity.class);
                startActivity(i);

            }
        });
        backup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch (which){

                        case DialogInterface.BUTTON_POSITIVE:
                            if (!directorydb.isDirectory()) {
                                directorydb.mkdirs();
                            }
                            try {
                                File file = new File(DB_PATH + DB_NAME);
                                File file1 = new File(DB_PATH + DB_NAME+"-wal");
                                File file2 = new File(DB_PATH + DB_NAME+"-shm");//Uri.toString());
                                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {

                                    //FileInputStream myInput = FileInputStream(file); // myContext.getAssets().open(DB_NAME);
                                    FileInputStream myInput;
                                    FileInputStream myInput1;
                                    FileInputStream myInput2;

                                    myInput = new FileInputStream(file);
                                    myInput1 = new FileInputStream(file1);
                                    myInput2 = new FileInputStream(file2);

                                    final File newFile = new File("/mnt/sdcard/GreenhouseCabai/data/");
                                    newFile.mkdir();
                                    // Path to the just created empty db
                                    String outFileName = "/mnt/sdcard/GreenhouseCabai/data/ghc.db";
                                    String outFileName1 = "/mnt/sdcard/GreenhouseCabai/data/ghc.db-wal";
                                    String outFileName2 = "/mnt/sdcard/GreenhouseCabai/data/ghc.db-shm";// DB_PATH + DB_NAME;

                                    //Open the empty db as the output stream
                                    OutputStream myOutput = new FileOutputStream(outFileName);
                                    OutputStream myOutput1 = new FileOutputStream(outFileName1);
                                    OutputStream myOutput2 = new FileOutputStream(outFileName2);

                                    //transfer bytes from the inputfile to the outputfile
                                    byte[] buffer = new byte[1024];
                                    int length;
                                    while ((length = myInput.read(buffer))>0){
                                        myOutput.write(buffer, 0, length);
                                    }
                                    while ((length = myInput1.read(buffer))>0){
                                        myOutput1.write(buffer, 0, length);
                                    }
                                    while ((length = myInput2.read(buffer))>0){
                                        myOutput2.write(buffer, 0, length);
                                    }


                                    //Close the streams
                                    myOutput.flush();
                                    myOutput.close();
                                    myInput.close();
                                    myOutput1.flush();
                                    myOutput1.close();
                                    myInput1.close();
                                    myOutput2.flush();
                                    myOutput2.close();
                                    myInput2.close();
                                }else{
                                    FileInputStream myInput;

                                    myInput = new FileInputStream(file);

                                    final File newFile = new File("/mnt/sdcard/GreenhouseCabai/data");
                                    newFile.mkdir();
                                    // Path to the just created empty db
                                    String outFileName = "/mnt/sdcard/GreenhouseCabai/data/ghc.db";// DB_PATH + DB_NAME;

                                    //Open the empty db as the output stream
                                    OutputStream myOutput = new FileOutputStream(outFileName);

                                    //transfer bytes from the inputfile to the outputfile
                                    byte[] buffer = new byte[1024];
                                    int length;
                                    while ((length = myInput.read(buffer))>0){
                                        myOutput.write(buffer, 0, length);
                                    }


                                    //Close the streams
                                    myOutput.flush();
                                    myOutput.close();
                                    myInput.close();
                                }
                                Toast.makeText(getActivity().getApplicationContext(), "Berhasil tersimpan di sdcard/GreenhouseCabai/data/", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Benarkah anda ingin backup database?").setPositiveButton(Html.fromHtml("<font color='#008577'>Ya</font>"), dialogClickListener)
                    .setNegativeButton(Html.fromHtml("<font color='#008577'>Tidak</font>"), dialogClickListener).show();
            // TODO Auto-generated method stub

        }
        });
        restore = (Button)myView.findViewById(R.id.restore);
        restore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                try {

                                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
                                        File file = new File("/mnt/sdcard/GreenhouseCabai/data/ghc.db");
                                        File file1 = new File("/mnt/sdcard/GreenhouseCabai/data/ghc.db-wal");
                                        File file2 = new File("/mnt/sdcard/GreenhouseCabai/data/ghc.db-shm");//Uri.toString());
                                        //FileInputStream myInput = FileInputStream(file); // myContext.getAssets().open(DB_NAME);
                                        FileInputStream myInput;
                                        FileInputStream myInput1;
                                        FileInputStream myInput2;

                                        myInput = new FileInputStream(file);
                                        myInput1 = new FileInputStream(file1);
                                        myInput2 = new FileInputStream(file2);


                                        // Path to the just created empty db
                                        String outFileName = DB_PATH + DB_NAME;
                                        String outFileName1 = DB_PATH + DB_NAME1;
                                        String outFileName2 = DB_PATH + DB_NAME2;

                                        //Open the empty db as the output stream
                                        OutputStream myOutput = new FileOutputStream(outFileName);
                                        OutputStream myOutput1 = new FileOutputStream(outFileName1);
                                        OutputStream myOutput2 = new FileOutputStream(outFileName2);

                                        //transfer bytes from the inputfile to the outputfile
                                        byte[] buffer = new byte[1024];
                                        int length;
                                        while ((length = myInput.read(buffer))>0){
                                            myOutput.write(buffer, 0, length);
                                        }
                                        while ((length = myInput1.read(buffer))>0){
                                            myOutput1.write(buffer, 0, length);
                                        }
                                        while ((length = myInput2.read(buffer))>0){
                                            myOutput2.write(buffer, 0, length);
                                        }

                                        //Close the streams
                                        myOutput.flush();
                                        myOutput.close();
                                        myInput.close();
                                        myOutput1.flush();
                                        myOutput1.close();
                                        myInput1.close();
                                        myOutput2.flush();
                                        myOutput2.close();
                                        myInput2.close();
                                    }else{
                                        File file = new File("/mnt/sdcard/GreenhouseCabai/data/ghc.db");
                                        //FileInputStream myInput = FileInputStream(file); // myContext.getAssets().open(DB_NAME);
                                        FileInputStream myInput;

                                        myInput = new FileInputStream(file);


                                        // Path to the just created empty db
                                        String outFileName = DB_PATH + DB_NAME;

                                        //Open the empty db as the output stream
                                        OutputStream myOutput = new FileOutputStream(outFileName);

                                        //transfer bytes from the inputfile to the outputfile
                                        byte[] buffer = new byte[1024];
                                        int length;
                                        while ((length = myInput.read(buffer))>0){
                                            myOutput.write(buffer, 0, length);
                                        }

                                        //Close the streams
                                        myOutput.flush();
                                        myOutput.close();
                                        myInput.close();
                                    }

                                    Toast.makeText(getActivity().getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
                                    //finish();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Belum ada cadangan data, ", Toast.LENGTH_LONG).show();
                                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
                                        Toast.makeText(getActivity().getApplicationContext(), "pastikan file ghc.db, ghc.db-wal, ghc.db-shm berada di /sdcard/GreenhouseCabai/data/, ", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }else {
                                        // TODO Auto-generated catch block
                                        Toast.makeText(getActivity().getApplicationContext(), "pastikan file ghc.db berada di /sdcard/GreenhouseCabai/data/", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Benarkah anda ingin memulihkan database?").setPositiveButton(Html.fromHtml("<font color='#008577'>Ya</font>"), dialogClickListener)
                        .setNegativeButton(Html.fromHtml("<font color='#008577'>Tidak</font>"), dialogClickListener).show();
                // TODO Auto-generated method stub

            }
        });
        simpan = (Button)myView.findViewById(R.id.excel);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //create directory if not exist
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                directorych.mkdirs();
                                if (!directoryex.isDirectory()) {
                                    directoryex.mkdirs();


                                }
                                try {
                                    dbHelper = new DatabaseHelper(getActivity());
                                    final Cursor cursor = dbHelper.getAllData();
                                    //file path
                                    File file2 = new File(directorych, csvFile);
                                    File file = new File(directoryex, csvFile);
                                    WorkbookSettings wbSettings = new WorkbookSettings();
                                    wbSettings.setLocale(new Locale("en", "EN"));
                                    WritableWorkbook workbook;
                                    WritableWorkbook workbook2;
                                    workbook = Workbook.createWorkbook(file, wbSettings);
                                    workbook2 = Workbook.createWorkbook(file2, wbSettings);
                                    //Excel sheet name. 0 represents first sheet
                                    WritableSheet sheet = workbook.createSheet("Data Greenhouse", 0);
                                    WritableSheet sheet2 = workbook2.createSheet("Data Greenhouse", 0);
                                    // column and row
                                    sheet.addCell(new Label(0, 0, "ID"));
                                    sheet.addCell(new Label(1, 0, "Suhu"));
                                    sheet.addCell(new Label(2, 0, "Kelembapan Tanah"));
                                    sheet.addCell(new Label(3, 0, "Waktu"));
                                    sheet2.addCell(new Label(0, 0, "ID"));
                                    sheet2.addCell(new Label(1, 0, "Suhu"));
                                    sheet2.addCell(new Label(2, 0, "Kelembapan Tanah"));
                                    sheet2.addCell(new Label(3, 0, "Waktu"));

                                    if (cursor.moveToFirst()) {
                                        do {
                                            String id = cursor.getString(cursor.getColumnIndex("ID"));
                                            String suhu = cursor.getString(cursor.getColumnIndex("SUHU"));
                                            String ktanah = cursor.getString(cursor.getColumnIndex("KTANAH"));
                                            Long date = cursor.getLong(cursor.getColumnIndex("DATE"));

                                            int i = cursor.getPosition() + 1;
                                            sheet.addCell(new Label(0, i, id));
                                            sheet.addCell(new Label(1, i, suhu));
                                            sheet.addCell(new Label(2, i, ktanah));
                                            sheet.addCell(new Label(3, i, sdf.format(new Date((long)date))));
                                            sheet2.addCell(new Label(0, i, id));
                                            sheet2.addCell(new Label(1, i, suhu));
                                            sheet2.addCell(new Label(2, i, ktanah));
                                            sheet2.addCell(new Label(3, i, sdf.format(new Date((long)date))));
                                        } while (cursor.moveToNext());
                                    }

                                    //closing cursor
                                    cursor.close();
                                    workbook.write();
                                    workbook.close();
                                    workbook2.write();
                                    workbook2.close();
                                    Toast.makeText(getActivity().getApplication(),
                                            "Data telah berhasil dieksport ke excel di /sdcard/GreenhouseCabai/DataGreenhouseCabai.xls", Toast.LENGTH_SHORT).show();
                                    viewExcel();


                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Benarkah anda ingin menyimpan data dalam excel?").setPositiveButton(Html.fromHtml("<font color='#008577'>Ya</font>"), dialogClickListener)
                        .setNegativeButton(Html.fromHtml("<font color='#008577'>Tidak</font>"), dialogClickListener).show();

            }
        });

        return myView;
    }
    public void viewExcel()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        openXLS("/mnt/sdcard/GreenhouseCabai/excel/");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Buka file excel sekarang?").setPositiveButton(Html.fromHtml("<font color='#008577'>Ya</font>"), dialogClickListener)
                .setNegativeButton(Html.fromHtml("<font color='#008577'>Tidak</font>"), dialogClickListener).show();

    }

    private void openXLS(final String path) {
        File imagePath = new File(getActivity().getFilesDir(), "excel");
        File newFile = new File(imagePath, "DataGreenhouseCabai.xls");
        File file = new File(path);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", newFile);
        } else {
            uri = Uri.fromFile(newFile);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "Application not found", Toast.LENGTH_SHORT).show();
        }
    }
}
