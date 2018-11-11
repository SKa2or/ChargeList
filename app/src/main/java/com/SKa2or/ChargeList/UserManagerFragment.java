package com.SKa2or.ChargeList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.SKa2or.ChargeList.unit.User;

/**
 * Created by Shuo on 2016/3/30.
 * 用户管理界面
 */
public class UserManagerFragment extends Fragment {
    private ListView lv_users;
    private Button bt_addUser;
    private FragmentManager fragmentManager;
    private UsersAdapter usersAdapter;

    public UserManagerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragme_user_manager, null);
        fragmentManager = getFragmentManager();

        bt_addUser = (Button) view.findViewById(R.id.bt_addUser);
        lv_users = (ListView) view.findViewById(R.id.lv_users);
        usersAdapter = new UsersAdapter(getActivity(), R.layout.item_user_manager, User.getUsers(),this);

        bt_addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager.findFragmentById(R.id.fl_addUser) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.fl_addUser, new AddUserFragment())
                            .addToBackStack(null)
                            .commit();
                }else{
                    if (User.getUsers().size() >= 10) {
                        Toast.makeText(getActivity(), "You cannot add more users!", Toast.LENGTH_SHORT).show();
                    }
                    EditText et_userName = (EditText) container.findViewById(R.id.et_userName);
                    EditText et_initSum = (EditText) container.findViewById(R.id.et_initSum);

                    String name = et_userName.getText().toString();
                    String sum_s = et_initSum.getText().toString();
                    double sum;
                    if (name.equals("")) {
                        Toast.makeText(getActivity(), "Please enter user ID", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (sum_s.equals("")) {
                        sum=0;
                    }else{
                        sum = Double.parseDouble(sum_s);
                    }
                    User newUser = new User(name, sum);
                    Toast.makeText(getActivity(), "New User Added", Toast.LENGTH_SHORT).show();
                }
                refurbish();
            }
        });
        lv_users.setAdapter(usersAdapter);
        //短按item 选择当前用户 并刷新
        lv_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User.setCurrentUser(position);
                refurbish();
                Toast.makeText(getActivity(), "Switch to ："+User.getUsers().get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        //长按用户 可以删除用户
        lv_users.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Note");
                builder.setMessage("Do you want to delete this user?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean b = User.removeUser(position);
                        if (b) {
                            Toast.makeText(getActivity(), "Delete Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Delete Failure", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //有用户变动情况下的刷新
                        refurbish();
                    }
                });
                builder.setNegativeButton("CANCAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                builder.create().show();
                return false;
            }
        });
        return view;
    }
    //用户出现变更时刷新数据
    public void refurbish(){
        usersAdapter.notifyDataSetChanged();
        MainFragment.refurbish(true);
    }
}
