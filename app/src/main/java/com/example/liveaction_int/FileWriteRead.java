package com.example.liveaction_int;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class FileWriteRead {

    Encrypti er;

    public void RWFile(Calendar c, String dir, int s2) {
        er = new Encrypti();
        String dte = String.valueOf(c.get(Calendar.DATE));
        String mnt = String.valueOf(c.get(Calendar.MONTH) + 1);
        String yer = String.valueOf(c.get(Calendar.YEAR));

        String min = String.valueOf(c.get(Calendar.MINUTE));
        String hr = String.valueOf(c.get(Calendar.HOUR_OF_DAY));


        File f = new File(dir);// originl
        try {
            File[] file = f.listFiles();

            for (int i = 0; i < file.length; i++) {
                String uploadFileName = file[i].getName();
                String usdata = dte + "_" + mnt + "_" + yer + "_" + s2 + "_" + hr + "_" + min + "US" + ".txt";
                String fnm = dte + "_" + mnt + "_" + yer + "_" + s2 + "_" + hr + "_" + min + ".txt";
                //    20_12_2022_62_157.txt

                if (!uploadFileName.equals(fnm) && !uploadFileName.equals(usdata) && !uploadFileName.startsWith("ecr")) {

//                        if (!uploadFileName.equals(fnm||usdata)) {
//                    String file_absol = dir + File.separator + uploadFileName;
                    File downloadFolder = new File(dir);

                    //////new encrypt implementation 28- april///
                    File dataFile = new File(downloadFolder, uploadFileName);
                    String filePath = dir + File.separator + uploadFileName;
                    String outfilePath= dir + File.separator + "ecr" + uploadFileName;
                  Boolean value = er.encryptFile(filePath, outfilePath);
                    if (value){
                        dataFile.delete();
                    }
///////////////////////////////////////////
//                    File dataFile = new File(downloadFolder, uploadFileName);
//                    String dassh = readFile(dataFile);
//                    Log.e("marer", dassh);
//                    if (dassh != null) {
//                        String enrcyp = er.enrycpp(dassh);
//                        try {
//                            FileWriter wrt = new FileWriter(dir + File.separator + "ecr" + uploadFileName, true);
//                            wrt.append(enrcyp);
//                            wrt.close();
//                            dataFile.delete();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            Log.e("WRFIlelogfr", String.valueOf(e));
//                        }
//                    }

                }

            }
        } catch (Exception e) {

            Log.e("WRFIlelog", String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    public String readFile(File dataFile) {
        String content = "";
        try (FileInputStream fis = new FileInputStream(dataFile)) {
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                content += line;
            }
            br.close();
            isr.close();
        } catch (IOException e) {
            Log.e("WRFIlelogre", String.valueOf(e));
            e.printStackTrace();
        }
        return content;
    }

}
