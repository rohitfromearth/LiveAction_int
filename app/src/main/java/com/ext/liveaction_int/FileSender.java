package com.ext.liveaction_int;

import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class FileSender {
    int serverResponseCode = 0;
    String upLoadServerUri = null;
    public void sender(String urrl, String dir , Calendar c, boolean charging, NetworkInfo netInfo,int s2){
        String dte = String.valueOf(c.get(Calendar.DATE));
        String mnt = String.valueOf(c.get(Calendar.MONTH) + 1);
        String yer = String.valueOf(c.get(Calendar.YEAR));

        String min = String.valueOf(c.get(Calendar.MINUTE));
        String hr = String.valueOf(c.get(Calendar.HOUR_OF_DAY));

        if (charging){
            if (!(netInfo == null)) {
                File f = new File(dir);// originl
                try {
                    File[] file = f.listFiles();
                    for (int i = 0; i < file.length; i++) {
                        String uploadFileName = file[i].getName();
                        String fnm = dte + "_" + mnt + "_" + yer + "_" + s2 + "_" + hr + "_" + min ;
Log.e("filename-",fnm);
Log.e("filenam",uploadFileName);
                        if (!uploadFileName.contains(fnm)) {


                            //      uploadFile(dir + "/" + uploadFileName);
                            String file_absol = dir + File.separator + uploadFileName;
                            Log.e("filename", file_absol);
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                            StrictMode.setThreadPolicy(policy);
                            upLoadServerUri ="https://lifeactions.online/rawData/writeFile"; // release url
//                           String  upLoadServerUri = urrl + "/rawData/writeFile";


                            HttpURLConnection conn = null;
                            DataOutputStream dos = null;
                            String lineEnd = "\r\n";
                            String twoHyphens = "--";
                            String boundary = "*****";
                            int bytesRead, bytesAvailable, bufferSize;
                            byte[] buffer;
                            int maxBufferSize = 1024 * 1024;
                            File sourceFile = new File(file_absol);
String s2_s= String.valueOf(s2);
                            if (!sourceFile.isFile()) {

                            } else {
                                try {

//            CipherInputStream cp= new CipherInputStream(sourceFile);
                                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                                    URL url = new URL(upLoadServerUri);

                                    // Open a HTTP  connection to  the URL
                                    conn = (HttpURLConnection) url.openConnection();
                                    conn.setDoInput(true); // Allow Inputs
                                    conn.setDoOutput(true); // Allow Outputs
                                    conn.setUseCaches(false); // Don't use a Cached Copy
                                    conn.setRequestMethod("POST");
                                    conn.setRequestProperty("Connection", "Keep-Alive");
                                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                                    conn.setRequestProperty("file", file_absol);
                                    conn.setRequestProperty("ultra", "45");///new
                                    conn.setRequestProperty("userId", s2_s);//// userid


                                    dos = new DataOutputStream(conn.getOutputStream());
                                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                                    dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                                            + file_absol + "\"" + lineEnd);
                                    dos.writeBytes(lineEnd);

                                    // create a buffer of  maximum size
                                    bytesAvailable = fileInputStream.available();
                                    // create a buffer of  maximum size


                                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                    buffer = new byte[bufferSize];

                                    // read file and write it into form...
                                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                                    while (bytesRead > 0) {

                                        dos.write(buffer, 0, bufferSize);
                                        bytesAvailable = fileInputStream.available();
                                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                                    }

                                    // send multipart form data necesssary after file data...
                                    dos.writeBytes(lineEnd);
                                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                                    // Responses from the server (code and message)
                                    serverResponseCode = conn.getResponseCode();

                                    String serverResponseMessage = conn.getResponseMessage();

                                    Log.i("uploadFileress", "HTTP Response is : "
                                            + serverResponseMessage + ": " + serverResponseCode);


                                    //close the streams //
                                    fileInputStream.close();
                                    dos.flush();
                                    dos.close();
                                    if (serverResponseCode == 200) {
                                        Log.e("new_upload", uploadFileName+"tagger");
                                        sourceFile.delete();
                                    }


                                } catch (MalformedURLException ex) {


                                    ex.printStackTrace();


                                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                                } catch (Exception e) {


                                    e.printStackTrace();


                                    Log.e(" server Exception", "Exception : "
                                            + e.getMessage(), e);
                                }

                            }

                        }

                        // End else block

                    }
                } catch (Exception e) {
                    Log.e(" server", "Exception : "
                            + e.getMessage(), e);
                }
            }
        }
    }

}
