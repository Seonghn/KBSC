package com.example.kbkbkb.ui.tools;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.kbkbkb.R;

public class GoalFragment extends Fragment {

    EditText whatmonth;
    FragmentPagerAdapter adapterViewPager;
    public static ViewPager vpPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_goal, container, false);

        vpPager = (ViewPager) root.findViewById(R.id.goal_vpPager);
        adapterViewPager = new GoalFragment.MyPagerAdapter(getFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        //CircleIndicator 설정
//        CircleIndicator indicator = (CircleIndicator) root.findViewById(R.id.consume_indicator);
//        indicator.setViewPager(vpPager);
        ImageButton add = root.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionCodeTypeDialog octDialog  = new OptionCodeTypeDialog(getContext(), new CustomDialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        Log.d("TEST", "OK click");
                    }

                    @Override
                    public void onNegativeClick() {
                        Log.d("TEST", "no click");
                    }
                });
                octDialog.setCanceledOnTouchOutside(true);
                octDialog.setCancelable(true);
                octDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                octDialog.show();
            }
        });
        return root;
    }
    public interface CustomDialogClickListener {
        void onPositiveClick();
        void onNegativeClick();
    }

    public class OptionCodeTypeDialog extends Dialog {

        private Context context;
        private CustomDialogClickListener customDialogClickListener;

        public OptionCodeTypeDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener) {
            super(context);
            this.context = context;
            this.customDialogClickListener = customDialogClickListener;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.goal_pop1);

            Button yes = findViewById(R.id.yes);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialogClickListener.onPositiveClick();
                    dismiss();
                }
            });
            Button no = findViewById(R.id.no);
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialogClickListener.onNegativeClick();
                    dismiss();
                }
            });
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        //fragment 개수 설정 변수
        private static int NUM_ITEMS = 3;

        //page어댑터 설정
        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        //page마다 어떤 프래그먼트가 들어가는지
        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new GoalFragment1();
                case 1:
                    return new GoalFragment2();
                case 2:
                    return new GoalFragment3();
//                case 3:
//                    return new ConsumeGrade4Fragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }


}