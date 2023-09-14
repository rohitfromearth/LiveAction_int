package com.ext.liveaction_int;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class Filewrite {
    Encrypti er;

    public int writeFile(String data_str,int userid, Calendar c, String dir,int counter, boolean isNewEvent) {
        String dte = String.valueOf(c.get(Calendar.DATE));
        String mnt = String.valueOf(c.get(Calendar.MONTH) + 1);
        String yer = String.valueOf(c.get(Calendar.YEAR));

        String min = String.valueOf(c.get(Calendar.MINUTE));
        String hr = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        er = new Encrypti();

        try {

            FileWriter wrt = new FileWriter(dir + File.separator + dte + "_" + mnt + "_" + yer + "_" + userid + "_" + hr + "_" + min +"_"+counter+ ".txt", true);

            File file = new File(dir + File.separator + dte + "_" + mnt + "_" + yer + "_" + userid + "_" + hr + "_" + min +"_"+counter+ ".txt");
            double fileSizeKB = (double) file.length() / 1024;
            System.out.println("File size in KB: " + fileSizeKB);
            if (isNewEvent && fileSizeKB > 20) {

                counter++;
                wrt = new FileWriter(dir + File.separator + dte + "_" + mnt + "_" + yer + "_" + userid + "_" + hr + "_" + min +"_"+counter+ ".txt", true);
            }

            wrt.append(data_str);
            wrt.close();

            return counter;

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("exp", String.valueOf(e));
        }
        return counter;
    }

    public void writedata(String devicedata, Calendar cal, String dir, int s2) {
        String dte = String.valueOf(cal.get(Calendar.DATE));
        String mnt = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String yer = String.valueOf(cal.get(Calendar.YEAR));

        String min = String.valueOf(cal.get(Calendar.MINUTE));
        String hr = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));

        try {

            FileWriter wrt = new FileWriter(dir + File.separator + dte + "_" + mnt + "_" + yer + "_" + s2 + "_" + hr + "_" + min + "US" + ".txt", true);
            wrt.append(devicedata);
            wrt.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("exp", String.valueOf(e));
        }
    }
}



