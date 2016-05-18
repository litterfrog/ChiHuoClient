package com.fxp.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fuxinpeng on 2016/5/17.
 */
public class SharedPreferenceManager {
    private final String PREFS_NAME="chihuoACC";
    private final String PREFS_ACCID="accId";
    private final String PREFS_ACCEMAIL="accEmail";
    private final String PREFS_ACCPASSWORD="accPassword";
    private static SharedPreferenceManager instance=null;
    private SharedPreferences prefs=null;
    public static synchronized SharedPreferenceManager getInstance(Activity activity){
            if(null==instance){
                instance=new SharedPreferenceManager(activity);
            }
        return instance;
    }
    private SharedPreferenceManager(Activity activity){
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
    public boolean checkLoginStatus(){
        if(0!=this.getAccId()){
            return true;
        }
        return false;
    }


}
