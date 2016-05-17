package com.fxp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fuxinpeng on 2016/5/17.
 */
public class SharedPreferenceUtil {
    private final String PREFS_NAME="chihuoACC";
    private final String PREFS_ACCID="accId";
    private final String PREFS_ACCEMAIL="accEmail";
    private final String PREFS_ACCPASSWORD="accPassword";
    private static SharedPreferenceUtil instance=null;
    private SharedPreferences prefs=null;
    public static synchronized SharedPreferenceUtil getInstance(Activity activity){
            if(null==instance){
                instance=new SharedPreferenceUtil(activity);
            }
        return instance;
    }
    private SharedPreferenceUtil(Activity activity){
        prefs = activity.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
    }
    public int getAccId(){
        if(null==prefs){
            return 0;
        }
        int accId = prefs.getInt(PREFS_ACCID, 0);
        return accId;
    }


}
