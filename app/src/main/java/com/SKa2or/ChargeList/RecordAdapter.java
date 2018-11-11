package com.SKa2or.ChargeList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.SKa2or.ChargeList.unit.Record;
import com.SKa2or.ChargeList.unit.User;

import java.text.SimpleDateFormat;

/**
 * Created by Shuo on 2016/3/27.
 *
 */
public class RecordAdapter extends ArrayAdapter<Record> {
    private int resource;
    private User user;

    public RecordAdapter(Context context, int resource) {
        super(context, R.layout.item_record,User.getCurrentUser().getRecords());
        this.resource = R.layout.item_record;
        this.user = User.getCurrentUser();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Record record = (Record) getItem(position);

        View view;
        if (convertView != null) {
            view = convertView;
        }else {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = LayoutInflater.from(getContext()).inflate(resource, null);
        }

        TextView tv_sum = (TextView) view.findViewById(R.id.tv_sum);
        TextView tv_note = (TextView) view.findViewById(R.id.tv_note);
        TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
        ImageView iv_recodeType = (ImageView) view.findViewById(R.id.iv_record_type_item);
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");

        tv_note.setText(record.getNote());
        tv_date.setText(sdf.format(record.getDate()));

        if (record.getRecordType() == Record.EXPEND) {
            iv_recodeType.setImageResource(R.drawable.red_minus);
            tv_sum.setTextColor(getContext().getResources().getColor(R.color.sumRed));
            tv_sum.setText("-"+record.getSum());
        } else {
            iv_recodeType.setImageResource(R.drawable.green_add);
            tv_sum.setTextColor(getContext().getResources().getColor(R.color.sumGreen));
            tv_sum.setText("+"+record.getSum());

        }
        return view;
    }
}
