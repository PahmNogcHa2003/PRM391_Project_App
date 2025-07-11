package com.example.prm391_project_apprestaurants.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
public class UtilHelper {
    public static AlertDialog createDialog(Context context, String title, String message,
                                           CharSequence negativeBtn, CharSequence positiveBtn,
                                           DialogInterface.OnClickListener posListener, DialogInterface.OnClickListener negListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtn, posListener);
        builder.setNegativeButton(negativeBtn, negListener);
        return builder.create();
    }
}
