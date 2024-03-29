package com.example.liveaction_int;

import static android.content.ContentValues.TAG;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class Access_new extends AccessibilityService {
    private static final long COLLECTION_INTERVAL = 2 * 1000; // 1 minute
    private static final String NOTIFICATION_CHANNEL_ID = "MyChannelId";
    private static final int NOTIFICATION_ID = 12345;
    int s2 = 0;
    // for shared pref
    int uid_z;
    String dir = "";
    String Adid = "";/// for shared pref
    int executiondateInt;
    int dte;
    String abt = "";//// for shared pref
    String[] appslist = new String[]{}; // Event package Input Api///shared pref
    Boolean si = false;
    FileSender fs = new FileSender();
    Filewrite fw = new Filewrite();
    FileWriteRead frw = new FileWriteRead();
    String must_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    ArrayList<String> appl = new ArrayList<String>();///// for input Api package list
    int counter = 0;
    Integer prevMin = -1;
    private int previousSecond = -1;
    private ArrayList<String> installedApps; /// array list for inatalld  appltion

    @Override
    public void onCreate() {
        super.onCreate();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                    String adId = adInfo.getId();
                    Log.e("advertising id", adId);

                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("ADID", adId);

                    myEdit.apply();
                    // Use the advertising id
                } catch (IOException | GooglePlayServicesRepairableException |
                         GooglePlayServicesNotAvailableException exception) {
                    // Error handling if needed
                    Log.e("error1234", exception.toString());
                }
            }
        });
        // Create the notification channel
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notification Channel", NotificationManager.IMPORTANCE_HIGH);
        }
        NotificationManager notificationManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = getSystemService(NotificationManager.class);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification and set it to be ongoing
        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).setContentTitle("My App").setContentText("Running in the background").setSmallIcon(R.drawable.img).setOngoing(true).build();

        // Show the notification
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

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
        if (prevMin != c.get(Calendar.MINUTE)) {
            counter = 0;
            prevMin = c.get(Calendar.MINUTE);
        }
        int second = c.get(Calendar.SECOND);
        /////// input of shared pref - userid, dir, api endpoint//////
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        s2 = sh.getInt("UID", uid_z);
        dir = sh.getString("dir", "");
        abt = sh.getString("endpt", "");
        executiondateInt = sh.getInt("executiondate", 0);

        int a = 0;
        if (executiondateInt != c.get(Calendar.DATE)) {
            ////////trigger for usage data file creation /////////


            usedata(c);


        }


        // Only collect data for certain event types

///////////////////////////////data collection /////

        if (second != previousSecond) {
            previousSecond = second;
            Log.e("new_string_second", String.valueOf(previousSecond));
            AccessibilityNodeInfo source = event.getSource();


            if (source != null) {
                AccessibilityNodeInfo rowNode = AccessibilityNodeInfo.obtain(source);
                if (rowNode != null) {
                    /////////////////////
                    String str_ty = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + ":" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + ":" + c.get(Calendar.MILLISECOND);
                    String Pack_name = String.valueOf(rowNode.getPackageName());
                    String Event_type = String.valueOf(event.getEventType());
                    String event_str = "~NewEvent:event_info^" + Pack_name + "*" + Event_type + "^data^";
                    Log.e("new_string", event_str);
                    a = fw.writeFile(event_str, s2, c, dir, counter, true);
                    counter = a;
                    /////////////////////
                    int count = rowNode.getChildCount();
//
                    for (int i = 0; i < count; i++) {
                        AccessibilityNodeInfo completeNode = rowNode.getChild(i);
                        recur(completeNode, c, event);
                    }
//                }
                    str_ty = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + ":" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + ":" + c.get(Calendar.MILLISECOND);

                    String event_end = "^event_time^" + str_ty;
                    Log.e("new_string_end", event_end);
                    a = fw.writeFile(event_end, s2, c, dir, counter, false);
                    counter = a;
                }
            }
        }
        frw.RWFile(c, dir, s2);
        fs.sender(abt, dir, c, isCharging, net, s2);
    }

    ////Actual Recurtion class to obtain deepest branch/node of parent node///
    private void recur(AccessibilityNodeInfo completeNode, Calendar c, AccessibilityEvent event) {
        if (completeNode != null) {
            int cout = completeNode.getChildCount();
            try {
                if (cout == 0) {

                    String Text = String.valueOf(completeNode.getText());
                    String Content_Desc = String.valueOf(completeNode.getContentDescription());
                    String text = "";
                    if (Text != "null" && Text != "") {
                        text += "^text:" + Text;
                    }
                    if (Content_Desc != "null" && Content_Desc != "") {
                        text += "^text:" + Content_Desc;
                    }
                    if (text != "") {
                        Log.e("new_string..", text);
                        int a = fw.writeFile(text, s2, c, dir, counter, false);
                        counter = a;
                    }


                } else {


                    for (int i = 0; i < cout; i++) {
                        AccessibilityEvent ev = event;
                        AccessibilityNodeInfo completeNod = completeNode.getChild(i);
                        recur(completeNod, c, ev);
                    }
                }

            } catch (Exception e) {
                Log.e("exp", e.getMessage());

            }
        }

    }

    public void usedata(Calendar c) {
        installedApps = getInstalledApps();

        String joinedString = String.join(" ", installedApps);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String simOperatorName = telephonyManager.getSimOperatorName();
        String brand = Build.MANUFACTURER;
        String gt = Build.MODEL;
        String deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        String time_ev = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + ":" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + ":" + c.get(Calendar.MILLISECOND);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        Adid = sh.getString("ADID", "");
        String devicedata = "DeviceID\n" + deviceid + "\n" + "GoogleAdId\n" + Adid + "\n" + "TELECOM\n" + simOperatorName + "\n" + "PHONE_BRAND\n" + brand + "\n" + "MODEL_NAME\n" + gt + "\n" + "USAGE_STATS\n" + joinedString + "\nEVENT_TIME\n" + time_ev;
        Log.e("new_string_usdata", devicedata);
        fw.writedata(devicedata, c, dir, s2);
        dte = c.get(Calendar.DATE);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("executiondate", dte);


        myEdit.apply();
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
                String packageName = p.applicationInfo.packageName;
                long foregroundTime = 0;
                for (UsageStats usageStats : usageStatsList) {
                    if (usageStats.getPackageName().equals(packageName)) {
                        foregroundTime = usageStats.getTotalTimeInForeground();
                        break;
                    }
                }

                long foregroundTimeInMinutes = foregroundTime / 1000 / 60;


                String pack_time = packages + "*" + foregroundTimeInMinutes + ";";
                apps.add(pack_time);

            }
        }
        return apps;
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
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.notificationTimeout = 1000;
        info.packageNames = appl.toArray(appslist);
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS | AccessibilityServiceInfo.DEFAULT;
        this.setServiceInfo(info);

        Log.e(TAG, "onServiceConnected: ");


    }

}
