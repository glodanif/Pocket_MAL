package com.g.pocketmal.ui.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import androidx.appcompat.view.ContextThemeWrapper;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ClearButtonDatePickerDialog extends DatePickerDialog {

    private OnDateSetListener listener;

    public ClearButtonDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, 0, callBack, year, monthOfYear, dayOfMonth);
        listener = callBack;
        //FIXME: how to set Unknown Date through the API?
        //setButton(BUTTON_NEUTRAL, context.getString(R.string.remove), this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (listener != null && which == BUTTON_NEUTRAL) {
            listener.onDateSet(null, 0, 0, 0);
        } else {
            super.onClick(dialog, which);
        }
    }

    public static void show(Context context, long date, OnDateSetListener dateSetListener) {

        Calendar calendar = Calendar.getInstance();

        if (date != 0) {
            calendar.setTime(new Date(date));
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (isBrokenSamsungDevice(context)) {
            context = new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog);
        }

        new ClearButtonDatePickerDialog(context, dateSetListener, year, month, day).show();
    }

    private static boolean isBrokenSamsungDevice(Context context) {
        return (Build.MANUFACTURER.equalsIgnoreCase("samsung") &&
                (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP ||
                        Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1)) &&
                !context.getResources().getConfiguration().locale.equals(Locale.ENGLISH);
    }
}
