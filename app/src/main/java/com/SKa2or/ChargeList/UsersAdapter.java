package com.SKa2or.ChargeList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.SKa2or.ChargeList.unit.User;

import java.util.List;

/**
 * Created by Shuo on 2016/3/30.
 */
public class UsersAdapter extends ArrayAdapter<User> {
    private Context context;
    private int resource;
    private Fragment fragment;

    public UsersAdapter(Context context, int resource, List<User> objects,Fragment fragment) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.fragment=fragment;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        User user = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, null);
        TextView tv_userName = (TextView) view.findViewById(R.id.tv_user_name);
        ImageView iv_isCurrentUser = (ImageView) view.findViewById(R.id.iv_isCurrentUser);
        ImageButton ib_userEditor = (ImageButton) view.findViewById(R.id.ib_userEditor);
        //编辑用户
        ib_userEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = fragment.getFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragme_main,new UserEditorFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        //设置用户名到item
        tv_userName.setText(user.getName());
        //如果是当前用户 前面的勾显示 否则隐藏
        if (user.isCurrentUser()) {
            iv_isCurrentUser.setVisibility(View.VISIBLE);
        } else {
            iv_isCurrentUser.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
