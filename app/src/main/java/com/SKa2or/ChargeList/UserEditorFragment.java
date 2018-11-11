package com.SKa2or.ChargeList;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.SKa2or.ChargeList.unit.User;


public class UserEditorFragment extends Fragment {
    private EditText et_amount;
    private Button bt_save;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_editor, container, false);
        et_amount = (EditText) view.findViewById(R.id.et_amount);
        bt_save = (Button) view.findViewById(R.id.bt_save);
        et_amount.setText(User.getCurrentUser().getAmount()+"");
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount = Double.parseDouble(et_amount.getText().toString());
                User.getCurrentUser().setAmount(amount);
                MainFragment.refurbish();
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }

}
