package com.example.liveaction_int;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class Filewrite {
    Encrypti er;

    public void writeFile(String data_str,String userid,Calendar c ,String dir){
        String dte = String.valueOf(c.get(Calendar.DATE));
        String mnt = String.valueOf(c.get(Calendar.MONTH) + 1);
        String yer = String.valueOf(c.get(Calendar.YEAR));

        String min = String.valueOf(c.get(Calendar.MINUTE));
        String hr = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        er = new Encrypti();
        String encryptedString = er.enrycpp(data_str);
//        Log.e("file_data",file_dt);
        try {

            FileWriter wrt = new FileWriter(dir + File.separator + dte + "_" + mnt + "_" + yer + "_" + userid+ "_" + hr + "_" + min + ".txt", true);
            Log.e("1234567", encryptedString);

            wrt.append(data_str);
            wrt.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("exp", String.valueOf(e));
        }


    }
    public void writedata(String devicedata, Calendar cal,String dir,String s2) {
        String dte = String.valueOf(cal.get(Calendar.DATE));
        String mnt = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String yer = String.valueOf(cal.get(Calendar.YEAR));

        String min = String.valueOf(cal.get(Calendar.MINUTE));
        String hr = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));


        try {

            FileWriter wrt = new FileWriter(dir + File.separator + dte + "_" + mnt + "_" + yer + "_" + s2+ "_" + hr + "_" + min +"US"+".txt", true);
//            Log.e("1234567", encryptedString);

            wrt.append(devicedata);
            wrt.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("exp", String.valueOf(e));
        }
    }
}



