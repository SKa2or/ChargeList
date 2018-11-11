package com.SKa2or.ChargeList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.SKa2or.ChargeList.unit.MyDatabaseHelper;
import com.SKa2or.ChargeList.unit.Record;
import com.SKa2or.ChargeList.unit.User;

import java.util.Collections;
import java.util.Date;


/**
 * Created by Shuo on 2016/3/27.
 *
 */
public class MainFragment extends Fragment {
    private ListView lv_recodes;
    private TextView tv_amount;
    private TextView tv_currentUserName;
    private Button bt_addRecord;
    private Button bt_selectUser;
    private ArrayAdapter<Record> adapter;
    private FragmentManager fragmentManager;
    private User currentUser;
    private static MainFragment thisFragment;
    private static SQLiteDatabase db;
    private static int OPERAND=0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //数据库初始化
        dataInit();
        thisFragment = MainFragment.this;
        currentUser = User.getCurrentUser();

        View view = inflater.inflate(R.layout.fragme_main,container,false);
        adapter = new RecordAdapter(getActivity(),  R.layout.item_record);

        lv_recodes = (ListView) view.findViewById(R.id.lv_recodes);
        lv_recodes.setAdapter(adapter);
        tv_amount = (TextView) view.findViewById(R.id.tv_amount);
        tv_currentUserName = (TextView) view.findViewById(R.id.tv_currentUserName);
        bt_addRecord = (Button) view.findViewById(R.id.bt_addRecord);
        bt_selectUser = (Button) view.findViewById(R.id.bt_selectUser);
        fragmentManager = getFragmentManager();

        tv_currentUserName.setText(currentUser.getName());
        tv_amount.setText("Remaining Balance : "+currentUser.getAmount());
        // 点击进入添加收支记录界面 AddRecordFragment
        bt_addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction()
                        .add(R.id.fragme_main, new AddRecordFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        //进入用户管理界面
        bt_selectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .add(R.id.fragme_main,new UserManagerFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        //设置对listview 每一个条目长按的监听 长按删除
        lv_recodes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Notes");
                builder.setCancelable(false);
                builder.setMessage("Do you want to delete this record?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (currentUser.removeRecord(position)) {
                            Toast.makeText(getActivity(), "Delete Success", Toast.LENGTH_SHORT).show();
                            refurbish();
                        } else {
                            Toast.makeText(getActivity(), "Delete Failure", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                return false;
            }
        });
        return view;
    }

    /**
     * 添加测试数据
     * @param size 添加记录条数
     */
    public User testAddData(int size) {
        User testUser = new User("TestUser", 1000);
        for(int i=0;i<size;i++) {
            Record record = new Record("#"+i,i%2,1000+i*2,new Date(),User.getCurrentUser());
            testUser.addRecord(record);
        }
        return testUser;
    }


    /**
     * 刷新
     * @param isChangUser 是否更改用户
     */
    public static void refurbish(boolean isChangUser) {
        if (isChangUser == true) {
            thisFragment.currentUser=User.getCurrentUser();
            thisFragment.adapter = new RecordAdapter(thisFragment.getActivity(),R.layout.item_record);
            thisFragment.lv_recodes.setAdapter(thisFragment.adapter);
        }
        thisFragment.adapter.notifyDataSetChanged();

        thisFragment.tv_amount.setText("Remaining Balance : "+thisFragment.currentUser.getAmount());
        thisFragment.tv_currentUserName.setText(thisFragment.currentUser.getName());
    }

    public static void refurbish() {
        thisFragment.adapter.notifyDataSetChanged();

        thisFragment.tv_amount.setText("Remaining Balance : "+thisFragment.currentUser.getAmount());
        thisFragment.tv_currentUserName.setText(thisFragment.currentUser.getName());
    }

    /**
     * 获取数据库
     * @return 数据库
     */
    public static SQLiteDatabase getDb() {
        return db;
    }

    /**
     * 初始化数据 从数据库读取用户 和 记录 信息
     */
    public void dataInit() {
        if (OPERAND!=0)
            return;
        //获取数据库
        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(getActivity());
        db = myDatabaseHelper.getReadableDatabase();

        Cursor userCursor = db.query(MyDatabaseHelper.TABLE_USERS_NAME,
                new String[]{"_id", "name", "amount", "isCurrent"},
                null, null, null, null, null);
        if (userCursor.moveToFirst()) {
            do {
                User user = User.readUserInDatabase(userCursor);
                Cursor recordCursor = db.query(MyDatabaseHelper.TABLE_Records_NAME,
                        new String[]{"_id", "note", "recordType", "sum", "date", "userId"},
                        "userId=?", new String[]{user.getId() + ""}, null, null, null);
                if (recordCursor.moveToFirst()) {
                    do {
                        user.addRecord(new Record(recordCursor));
                    } while (recordCursor.moveToNext());
                }
                Collections.sort(user.getRecords());
            } while (userCursor.moveToNext());
        } else {
            //数据库没有用户的情况下 自动新建默认用户
            new User("SKa2or", 0);
        }
        OPERAND++;
    }
}
