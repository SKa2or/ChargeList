package com.SKa2or.ChargeList;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Date;


public class CalndarFragment extends Fragment {
    private DatePicker datePicker;
    private Button bt_ok;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calndar, container, false);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        datePicker.setMaxDate(new Date().getTime());
        datePicker.setMinDate(new Date().getTime()-(31*24*60*60*10000));
        Date date = AddRecordFragment.getDate();
        Log.d("TAG++", date.getYear() + " " + date.getMonth());

        datePicker.init(date.getYear(), date.getMonth(), date.getDay(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {}
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = datePicker.getYear();
                int monthOfYear = datePicker.getMonth();
                int dayOfMonth = datePicker.getDayOfMonth();
                AddRecordFragment.setDate(year,monthOfYear,dayOfMonth);
                CalndarFragment.this.onDestroyView();
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }

}
