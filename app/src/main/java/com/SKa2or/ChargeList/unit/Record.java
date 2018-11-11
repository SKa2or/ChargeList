package com.SKa2or.ChargeList.unit;

import android.content.ContentValues;
import android.database.Cursor;

import com.SKa2or.ChargeList.MainFragment;

import java.util.Date;

/**
 * Created by Shuo on 2016/3/27.
 * 收支记录
 */
public class Record implements Comparable<Record> {
    //收入
    public final static int INCOME = 1;
    //支出
    public final static int EXPEND = 0;
    //对该条收支记录的备注
    private String note;
    //支出类型 收入或支出
    private int recordType;
    //金额
    private double sum;
    //记录时间
    private Date date;
    //记录在数据库中的id
    private long _id;
    /**
     *
     * @param note 记录备注
     * @param recordType 收支类型
     * @param sum 金额
     * @param date 日期
     * @param user 创建该收支记录的用户
     */
    public Record(String note, int recordType, double sum,Date date,User user) {
        this.note = note;
        this.recordType = recordType;
        this.sum = sum;
        this.date = date;
        //数据库添加该记录
        ContentValues contentValues = new ContentValues();
        contentValues.put("note",note);
        contentValues.put("recordType",recordType);
        contentValues.put("sum",sum);
        contentValues.put("date",date.getTime());
        contentValues.put("userId",user.getId());
        this._id=MainFragment.getDb().insert(MyDatabaseHelper.TABLE_Records_NAME,
                null, contentValues);
    }

    public Record(Cursor cursor) {
        this._id = cursor.getInt(cursor.getColumnIndex("_id"));
        this.note = cursor.getString(cursor.getColumnIndex("note"));
        this.recordType = cursor.getInt(cursor.getColumnIndex("recordType"));
        this.sum = cursor.getDouble(cursor.getColumnIndex("sum"));
        this.date = new Date(cursor.getLong(cursor.getColumnIndex("date")));
    }

    public String getNote() {
        return note;
    }
    public int getRecordType() {
        return recordType;
    }
    public double getSum() {
        return sum;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }
    public void setSum(double sum) {
        this.sum = sum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return _id;
    }

    @Override
    public int compareTo(Record another) {
        return (int) -(this.getDate().getTime()-another.getDate().getTime());
    }
}
