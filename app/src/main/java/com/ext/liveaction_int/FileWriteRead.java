package com.ext.liveaction_int;

import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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

                if (!uploadFileName.equals(fnm) && !uploadFileName.equals(usdata) && !uploadFileName.startsWith("ecr")) {

                    File downloadFolder = new File(dir);
                    File dataFile = new File(downloadFolder, uploadFileName);
                    String filePath = dir + File.separator + uploadFileName;

                    String outfilePath = dir + File.separator + "ecr" + uploadFileName;
                    Boolean value = er.encryptFile(filePath, outfilePath);
                    if (value) {
                        dataFile.delete();
                    }
                }

            }
        } catch (Exception e) {

            Log.e("FileWriteRead", String.valueOf(e));
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
            Log.e("FileRead", String.valueOf(e));
            e.printStackTrace();
        }
        return content;
    }

}
