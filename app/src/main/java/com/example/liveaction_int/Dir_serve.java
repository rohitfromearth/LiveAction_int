package com.example.liveaction_int;

import android.os.Environment;

import java.io.File;

public class Dir_serve {
    public void letshit(){


        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        // String dir =getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
//        String fldr_nme= dte+"-"+mnt+"-"+yer;
        String fldr_nme= "lifeaction";

        File f = new File(dir,fldr_nme);

        if (!f.exists()){
            f.mkdir();
        }
        String nfm = "lifeactionunencr";
        File r = new File(dir, nfm);
        if (!r.exists()){
            r.mkdir();
        }
    }
}
