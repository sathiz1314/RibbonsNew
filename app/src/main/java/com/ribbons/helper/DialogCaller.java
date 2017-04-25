package com.ribbons.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by User on 01-Mar-17.
 */

public class DialogCaller {

    public static void showDialog(Context context,String title,String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("Ok",onClickListener);
        dialog.show();
    }
}
