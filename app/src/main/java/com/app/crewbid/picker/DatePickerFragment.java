package com.app.crewbid.picker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.app.crewbid.utility.Utility;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener, OnClickListener {

    private int[] fromDate = new int[3];
    private int[] compareDate = new int[3];
    private int resourceId = 0;
    private boolean positiveClick = false;
    private DateChooserInterface dateChooserInterface;

    public static interface DateChooserInterface {
        public void onDateSet(DatePicker view, int year, int month, int day,
                              int resourceId);

        public void onDateSet(DatePicker view, int year, int month, int day,
                              int resourceId, boolean fromClick);
    }

    public DatePickerFragment() {
        // TODO Auto-generated constructor stub
        positiveClick = false;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        int year = 0, month = 0, day = 0;
        if (fromDate[0] == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            year = fromDate[2];
            month = fromDate[1];
            day = fromDate[0];
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, compareDate[0]);
        calendar.set(Calendar.MONTH, compareDate[1]);
        calendar.set(Calendar.YEAR, compareDate[2]);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this,
                year, month, day);
//		dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.getDatePicker().setSpinnersShown(true);
        dialog.getDatePicker().setCalendarViewShown(false);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Done", this);
        dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", this);
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Utility.log(getTag(), "day?" + day + "&month?" + month + "&year?"
                + year);
        if (dateChooserInterface != null && positiveClick) {
            dateChooserInterface.onDateSet(view, year, month, day, resourceId);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // TODO Auto-generated method stub
        super.onDismiss(dialog);
        Utility.log(getTag(), "onDismiss");
    }

    public int[] getFromDate() {
        return fromDate;
    }

    public void setFromDate(int[] fromDate) {
        this.fromDate = fromDate;
    }

    public DateChooserInterface getDateChooserInterface() {
        return dateChooserInterface;
    }

    public void setDateChooserInterface(
            DateChooserInterface dateChooserInterface) {
        this.dateChooserInterface = dateChooserInterface;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
        if (which == DatePickerDialog.BUTTON_POSITIVE) {
            Utility.log(getTag(), "Positive");
            DatePickerDialog datePickerDialog = (DatePickerDialog) this
                    .getDialog();
            DatePicker datePicker = datePickerDialog.getDatePicker();
            positiveClick = true;
            if (dateChooserInterface != null && positiveClick) {
                dateChooserInterface.onDateSet(null, datePicker.getYear(),
                        datePicker.getMonth(), datePicker.getDayOfMonth(),
                        resourceId);

                    dateChooserInterface.onDateSet(null, datePicker.getYear(),
                            datePicker.getMonth(), datePicker.getDayOfMonth(),
                            resourceId, true);

            }
        } else if (which == DatePickerDialog.BUTTON_NEGATIVE) {
            Utility.log(getTag(), "Negative");
            positiveClick = false;
        }
    }

    public int[] getCompareDate() {
        return compareDate;
    }

    public void setCompareDate(int[] compareDate) {
        this.compareDate = compareDate;
    }
}