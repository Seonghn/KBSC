package com.example.kbkbkb.ui.tools;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kbkbkb.R;

public class GoalFragment1 extends Fragment {

    EditText editText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal1, container, false);//fragment layout 연결


        ImageButton select = view.findViewById(R.id.select1);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoalFragment.vpPager.setCurrentItem(2);
            }
        });


        editText = view.findViewById(R.id.tg_mon);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                GoalFragment.vpPager.setCurrentItem(2);
            }
        });

        return view;
    }
}
