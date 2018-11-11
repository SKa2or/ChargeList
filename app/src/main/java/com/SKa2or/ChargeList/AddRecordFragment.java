package com.SKa2or.ChargeList;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SKa2or.ChargeList.unit.Record;
import com.SKa2or.ChargeList.unit.User;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shuo on 2016/3/28.
 * 添加收支记录Fragment
 */
public class AddRecordFragment extends Fragment {
    private User user;
    private EditText et_note;
    private EditText et_sum;
    private Button bt_addRecord;
    private AppCompatRadioButton rb_income;
    private AppCompatRadioButton rb_expend;
    private LinearLayout ll_calendar;
    private LinearLayout ll_clock;
    private TextView tv_clock;
    private TextView tv_calendar;
    private MainFragment mainFragment;
    private static AddRecordFragment thisFragment;
    //记录时间
    private static Date date;
    private View view;


    public AddRecordFragment() {
        this.user = User.getCurrentUser();
        thisFragment = this;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragme_add_record, null);
        init();

        return view;
    }

    //对各组件初始化
    private void init() {
        et_note = (EditText) view.findViewById(R.id.et_note);
        et_sum = (EditText) view.findViewById(R.id.et_sum);
        bt_addRecord = (Button) view.findViewById(R.id.bt_addRecord);
        rb_expend = (AppCompatRadioButton) view.findViewById(R.id.rb_expend);
        rb_income = (AppCompatRadioButton) view.findViewById(R.id.rb_income);
        ll_calendar = (LinearLayout) view.findViewById(R.id.ll_calendar);
        ll_clock = (LinearLayout) view.findViewById(R.id.ll_clock);
        tv_clock = (TextView) view.findViewById(R.id.tv_clock);
        tv_calendar = (TextView) view.findViewById(R.id.tv_calendar);
        date = new Date();

        tv_calendar.setText(new SimpleDateFormat("yy/MM/dd").format(date));
        tv_clock.setText(new SimpleDateFormat("hh:mm").format(date));

        //日历选择 启动CalendarFragment
        ll_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .add(R.id.fragme_main,new CalndarFragment())
                        .addToBackStack(null)
                        .commit();

            }
        });
        //时间选择
        ll_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .add(R.id.fragme_main,new ClockFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        //添加记录
        bt_addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sum = et_sum.getText().toString();
                String note = et_note.getText().toString();

                if (sum.equals("")) {
                    Toast.makeText(getActivity(), "Please enter amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                int recordType = rb_income.isChecked() ? Record.INCOME : Record.EXPEND;
                Record record = new Record(note, recordType, Double.parseDouble(sum), date,User.getCurrentUser());
                user.addRecord(record);
                Toast.makeText(getActivity(), "Remaining Balance : "+user.getAmount(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        MainFragment.refurbish();
    }

    public static Date getDate() {
        return date;
    }

    public static void setDate(int year, int month, int day) {
        date.setYear(year);
        date.setMonth(month);
        date.setDate(day);

        thisFragment.tv_calendar.setText(new SimpleDateFormat("yy/MM/dd").format(date));
    }

    public static void setTime(int hour, int minute) {
        date.setMinutes(minute);
        date.setHours(hour);

        thisFragment.tv_clock.setText(new SimpleDateFormat("HH:mm").format(date));
    }
}
