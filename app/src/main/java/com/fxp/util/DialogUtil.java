package com.fxp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by fuxinpeng on 2016/5/17.
 */
public class DialogUtil {
    private static DialogInterface.OnClickListener dialogButtonListener=new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            dialog.dismiss();
        }
    };
    public static void dialogWithOneButton(Activity activity,String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", dialogButtonListener);
        builder.create().show();
    }
    public static void dialogWithOneButton(Activity activity,String message,DialogInterface.OnClickListener dialogButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", dialogButtonListener);
        builder.create().show();
    }

}
