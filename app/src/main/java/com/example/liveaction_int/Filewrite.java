package com.example.liveaction_int;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class Filewrite {
    public void writeFile(String data_str,String userid,Calendar c ,String dir){
        String dte = String.valueOf(c.get(Calendar.DATE));
        String mnt = String.valueOf(c.get(Calendar.MONTH) + 1);
        String yer = String.valueOf(c.get(Calendar.YEAR));

        String min = String.valueOf(c.get(Calendar.MINUTE));
        String hr = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        try {

            FileWriter wrt = new FileWriter(dir + File.separator + dte + "_" + mnt + "_" + yer + "_" + userid+ "_" + hr + "_" + min + ".txt", true);
            Log.e("1234567", data_str);

            wrt.append(data_str);
            wrt.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("exp", String.valueOf(e));
        }


    }


}
