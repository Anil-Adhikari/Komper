package csci4540.ecu.komper.activities.creategrocerylist;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import csci4540.ecu.komper.R;

/**
 * Created by ANIL on 10/11/2017.
 */

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "Expiry Date";
    public static final String EXTRA_DATE = "com.csci4540.ecu.komper.activities.creategrocerylist.CreateGroceryListFragment.datepicker";

    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Date date = (Date)getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int oldYear = calendar.get(Calendar.YEAR);
        int oldMonth = calendar.get(Calendar.MONTH);
        int oldDay = calendar.get(Calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_datepicker, null);

        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(oldYear, oldMonth, oldDay, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Pick Expiry Date")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int newYear = mDatePicker.getYear();
                                int newMonth = mDatePicker.getMonth();
                                int newDay = mDatePicker.getDayOfMonth();

                                Date newDate = new GregorianCalendar(newYear, newMonth, newDay).getTime();
                                sendResult(Activity.RESULT_OK, newDate);

                            }
                        })
                .create();

    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
