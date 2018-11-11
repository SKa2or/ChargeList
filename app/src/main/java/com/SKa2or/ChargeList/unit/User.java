package com.SKa2or.ChargeList.unit;

import android.content.ContentValues;
import android.database.Cursor;

import com.SKa2or.ChargeList.MainFragment;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shuo on 2016/3/27.
 * 用户
 */
public class User {
    private static LinkedList<User> users = new LinkedList<User>();
    private static User currentUser;
    private String name;
    //用户的所有收支记录
    private List<Record> records;
    //用户的金额总量
    private double amount;
    //对应数据库中的id字段 用来检索
    private long _id;

    /**
     * 创建用户
     * @param name 用户名
     * @param amount 初始资金
     */
    public User(String name, double amount) {
        this(name, amount, true);
    }

    public User(String name, double amount, boolean isCurrent) {
        this.name = name;
        this.amount = amount;
        this.records = new LinkedList<Record>();
        User.users.add(this);
        //将新建的User自动设为当前用户
        User.currentUser=this;
        ContentValues cv = new ContentValues();
        cv.put("name",name);
        cv.put("amount",amount);
        cv.put("isCurrent",false);
        this._id = MainFragment.getDb().insert(MyDatabaseHelper.TABLE_USERS_NAME, null, cv);
        cv.clear();
        if (isCurrent)
            this.setCurrentUser();
    }
    //用于从数据库读取用户 不会重复添加用户到数据库
    private User(int _id, String name, double amount, boolean isCurrent) {
        this._id=_id;
        this.name = name;
        this.amount = amount;
        this.records = new LinkedList<Record>();
        if (isCurrent) {
            this.setCurrentUser();
        }
        users.add(this);
    }
    public static User readUserInDatabase(Cursor cursor) {
        int _id = cursor.getInt(cursor.getColumnIndex("_id"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
        boolean isCurrent = cursor.getInt(cursor.getColumnIndex("isCurrent"))
                == 1 ? true : false;
        return new User(_id,name, amount, isCurrent);

    }

    public List<Record> getRecords() {
        return records;
    }

    /**
     * 添加一条记录
     * @param record 收支记录
     */
    public void addRecord(Record record){
        //支出情况在总金额减去支出 收入情况下在总金额中加上收入
        if (record.getRecordType() == Record.EXPEND) {
            amount -= record.getSum();
        } else {
            amount+=record.getSum();
        }
        records.add(0,record);
        Collections.sort(records);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount){
        this.amount = amount;
        //数据库上更改
        ContentValues contentValues = new ContentValues();
        contentValues.put("amount",amount);
        MainFragment.getDb().update(MyDatabaseHelper.TABLE_USERS_NAME,
                contentValues,"_id=?",new String[]{this.getId()+""});
    }

    public static LinkedList<User> getUsers() {
        return users;
    }

    //设置当前用户
    public void setCurrentUser() {
        User.currentUser = this;
        ContentValues contentValues = new ContentValues();
        contentValues.put("isCurrent",true);
        MainFragment.getDb().update(MyDatabaseHelper.TABLE_USERS_NAME, contentValues, "_id=?",
                new String[]{this._id + ""});
        contentValues.clear();
        contentValues.put("isCurrent",false);
        MainFragment.getDb().update(MyDatabaseHelper.TABLE_USERS_NAME, contentValues, "_id<>?",
                new String[]{this._id + ""});
    }

    //设置当前用户
    public static void setCurrentUser(int position) {
        User user = users.get(position);
        User.currentUser = user;
        user.setCurrentUser();
    }
    public static User getCurrentUser() {
        return User.currentUser;
    }
    //是否为当前用户
    public boolean isCurrentUser() {
        if (User.getCurrentUser().equals(this))
            return true;
        return false;
    }

    public String getName() {
        return name;
    }

    /**
     * 删除一条收支记录
     * @param position 删除的位置
     * @return 如果删除成功返回true 失败返回false
     */
    public boolean removeRecord(int position) {
        if (position >= records.size()) {
            return false;
        }
        Record removedRecord = records.remove(position);
        MainFragment.getDb().delete(MyDatabaseHelper.TABLE_Records_NAME,
                "_id=?", new String[]{removedRecord.getId()+""});
        if (removedRecord.getRecordType() == Record.EXPEND) {
            this.amount+=removedRecord.getSum();
        }else{
            this.amount-=removedRecord.getSum();
        }
        return true;
    }

    /**
     * 删除User
     * @param position 在Users中的位置
     * @return 成功返回true 失败返回false
     */
    public static boolean removeUser(int position) {
        //只剩一个用户时删除用户失败
        if (users.size()<=1) {
            return false;
        }

        if (User.users.size() == 0) {
            return false;
        }

        User removedUser = User.users.remove(position);
        //数据库上删除
        MainFragment.getDb().delete(MyDatabaseHelper.TABLE_USERS_NAME,
                "_id=?", new String[]{removedUser.getId() + ""});
        MainFragment.getDb().delete(MyDatabaseHelper.TABLE_Records_NAME,
                "userId=?", new String[]{removedUser.getId() + ""});
        //如果被删除用户为当前用户则将第一个用户设置为当前用户
        if (removedUser.isCurrentUser()) {
            User.users.get(0).setCurrentUser();
        }
        return true;
    }

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }
}
