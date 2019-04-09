package com.cahjaya.lian.greenhousecabai;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import helpers.DatabaseHelper;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by Lian CahJaya on 28/11/2017.
 */

public class ThirdFragment extends Fragment {
    Button backup;
    Button restore;
    Button simpan;
    Switch serv;
    View myView;
    protected Cursor cursor;
    DatabaseHelper dbHelper;
    private static final String TAG = "Receiver";

    SimpleDateFormat sdf=new SimpleDateFormat(  "yyyy-MM-dd HH:mm:ss", Locale.getDefault());


    private File sd = Environment.getExternalStorageDirectory();
    private String  csvFile = "myData.xls";

    private File directory = new File(sd.getAbsolutePath());
    private static String DB_PATH = "/data/data/com.cahjaya.lian.greenhousecabai/databases/";
    private static String DB_NAME = "ghc.db";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.thirdlayout, container, false);
        dbHelper = new DatabaseHelper(getActivity());
        final Cursor cursor = dbHelper.getAllData();
        backup = (Button)myView.findViewById(R.id.backup);
        backup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch (which){

                        case DialogInterface.BUTTON_POSITIVE:
                            try {
                                File file = new File(DB_PATH + DB_NAME); //Uri.toString());
                                //FileInputStream myInput = FileInputStream(file); // myContext.getAssets().open(DB_NAME);
                                FileInputStream myInput;

                                myInput = new FileInputStream(file);
                                String intStorageDirectory = getActivity().getFilesDir().toString();
                                File folder = new File(intStorageDirectory, "Greenhouse Cabai");
                                folder.mkdirs();
                                // Path to the just created empty db
                                String outFileName = "/sdcard/ghc.db"; // DB_PATH + DB_NAME;

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
                                Toast.makeText(getActivity().getApplicationContext(), "Berhasil tersimpan di sdcard/Greenhouse Cabai/ghc.db", Toast.LENGTH_LONG).show();
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
            builder.setMessage("Benarkah anda ingin backup database?").setPositiveButton("Ya", dialogClickListener)
                    .setNegativeButton("Tidak", dialogClickListener).show();
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
                                    File file = new File("/sdcard/ghc.db"); //Uri.toString());
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
                                    Toast.makeText(getActivity().getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
                                    //finish();
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
                builder.setMessage("Benarkah anda ingin memulihkan database?").setPositiveButton("Ya", dialogClickListener)
                        .setNegativeButton("Tidak", dialogClickListener).show();
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
                                if (!directory.isDirectory()) {
                                    directory.mkdirs();
                                }
                                try {

                                    //file path
                                    File file = new File(directory, csvFile);
                                    WorkbookSettings wbSettings = new WorkbookSettings();
                                    wbSettings.setLocale(new Locale("en", "EN"));
                                    WritableWorkbook workbook;
                                    workbook = Workbook.createWorkbook(file, wbSettings);
                                    //Excel sheet name. 0 represents first sheet
                                    WritableSheet sheet = workbook.createSheet("Data Greenhouse", 0);
                                    // column and row
                                    sheet.addCell(new Label(0, 0, "ID"));
                                    sheet.addCell(new Label(1, 0, "Suhu"));
                                    sheet.addCell(new Label(2, 0, "Kelembapan Ruang"));
                                    sheet.addCell(new Label(3, 0, "Kelembapan Tanah"));
                                    sheet.addCell(new Label(4, 0, "Tanggal"));

                                    if (cursor.moveToFirst()) {
                                        do {
                                            String id = cursor.getString(cursor.getColumnIndex("ID"));
                                            String suhu = cursor.getString(cursor.getColumnIndex("SUHU"));
                                            String hmdt = cursor.getString(cursor.getColumnIndex("HMDT"));
                                            String ktanah = cursor.getString(cursor.getColumnIndex("KTANAH"));
                                            Long date = cursor.getLong(cursor.getColumnIndex("DATE"));

                                            int i = cursor.getPosition() + 1;
                                            sheet.addCell(new Label(0, i, id));
                                            sheet.addCell(new Label(1, i, suhu));
                                            sheet.addCell(new Label(2, i, hmdt));
                                            sheet.addCell(new Label(3, i, ktanah));
                                            sheet.addCell(new Label(4, i, sdf.format(new Date((long)date))));
                                        } while (cursor.moveToNext());
                                    }

                                    //closing cursor
                                    cursor.close();
                                    workbook.write();
                                    workbook.close();
                                    Toast.makeText(getActivity().getApplication(),
                                            "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
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
                builder.setMessage("Benarkah anda ingin menyimpan data dalam excel?").setPositiveButton("Ya", dialogClickListener)
                        .setNegativeButton("Tidak", dialogClickListener).show();

            }
        });

        return myView;
    }
}
