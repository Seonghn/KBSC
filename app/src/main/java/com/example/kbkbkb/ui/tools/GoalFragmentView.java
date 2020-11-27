package com.example.kbkbkb.ui.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kbkbkb.R;

public class GoalFragmentView extends Fragment {
    private TextView target_tv, goaltime_tv, goalconsume_tv, gold1_tv, gold2_tv, gold3_tv, gold4_tv, gold5_tv,
            frequency1_tv, frequency2_tv, frequency3_tv, frequency4_tv, frequency5_tv;

    String target, goaltime, goalconsume, gold1, gold2, gold3, gold4, gold5, frequency1, frequency2, frequency3, frequency4, frequency5;

    GoalFragmentView(String target, String goaltime, String goalconsume, String gold1, String gold2, String gold3, String gold4, String gold5,
                     String frequency1, String frequency2, String frequency3, String frequency4, String frequency5) {
        this.target = target;
        this.goaltime = goaltime;
        this.goalconsume = goalconsume;
        this.gold1 = gold1;
        this.gold2 = gold2;
        this.gold3 = gold3;
        this.gold4 = gold4;
        this.gold5 = gold5;
        this.frequency1 = frequency1;
        this.frequency2 = frequency2;
        this.frequency3 = frequency3;
        this.frequency4 = frequency4;
        this.frequency5 = frequency5;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal1, container, false);//fragment layout 연결

        target_tv = view.findViewById(R.id.target);
        goaltime_tv = view.findViewById(R.id.tm);
        goalconsume_tv = view.findViewById(R.id.r1);
        gold1_tv = view.findViewById(R.id.fl1);
        gold2_tv = view.findViewById(R.id.fl2);
        gold3_tv = view.findViewById(R.id.fl3);
        gold4_tv = view.findViewById(R.id.fl4);
        gold5_tv = view.findViewById(R.id.fl5);
        frequency1_tv = view.findViewById(R.id.fr1);
        frequency2_tv = view.findViewById(R.id.fr2);
        frequency3_tv = view.findViewById(R.id.fr3);
        frequency4_tv = view.findViewById(R.id.fr4);
        frequency5_tv = view.findViewById(R.id.fr5);

        target_tv.setText(target);
        goaltime_tv.setText(goaltime);
        goalconsume_tv.setText(goalconsume);
        gold1_tv.setText(gold1);
        gold2_tv.setText(gold2);
        gold3_tv.setText(gold3);
        gold4_tv.setText(gold4);
        gold5_tv.setText(gold5);
        frequency1_tv.setText(frequency1);
        frequency2_tv.setText(frequency2);
        frequency3_tv.setText(frequency3);
        frequency4_tv.setText(frequency4);
        frequency5_tv.setText(frequency5);

        return view;
    }
}
