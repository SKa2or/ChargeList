package com.SKa2or.ChargeList;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Date;


public class ClockFragment extends Fragment {
    private TimePicker timePicker;
    private Button bt_ok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clock, container, false);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        timePicker.setIs24HourView(false);
        Date date = AddRecordFragment.getDate();
        int hours = date.getHours();
        int minutes = date.getMinutes();
        timePicker.setCurrentHour(hours);
        timePicker.setCurrentMinute(minutes);


        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hourOfDay = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                AddRecordFragment.setTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }

}
