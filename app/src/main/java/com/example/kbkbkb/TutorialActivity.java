package com.example.kbkbkb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.kbkbkb.tutorial_fragment.FirstFragment;
import com.example.kbkbkb.tutorial_fragment.SecondFragment;
import com.example.kbkbkb.tutorial_fragment.ThirdFragment;

import me.relex.circleindicator.CircleIndicator;

public class TutorialActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);//layout은 tutorial.xml이 보이게

        //viewpager 및 Adapter 설정
        ViewPager vpPager = (ViewPager)findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        //CircleIndicator 설정
        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);

        Button b1 = (Button) findViewById(R.id.log);
        Button b2 = (Button) findViewById(R.id.reg);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
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
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                case 2:
                    return new ThirdFragment();
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
