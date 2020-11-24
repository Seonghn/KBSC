package com.example.kbkbkb.ui.consumegrade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.kbkbkb.R;

import me.relex.circleindicator.CircleIndicator;

public class ConsumeGradeFragment extends Fragment {

    private ConsumeGradeViewModel consumeGradeViewModel;
    FragmentPagerAdapter adapterViewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        consumeGradeViewModel =
                ViewModelProviders.of(this).get(ConsumeGradeViewModel.class);

        //findviewbyid를 위한 view 연결
        View root = inflater.inflate(R.layout.fragment_consumegrade, container, false);

        //viewpager 및 Adapter 설정
        ViewPager vpPager = (ViewPager)root.findViewById(R.id.consume_vpPager);
        adapterViewPager = new ConsumeGradeFragment.MyPagerAdapter(getFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        //CircleIndicator 설정
        CircleIndicator indicator = (CircleIndicator)root.findViewById(R.id.consume_indicator);
        indicator.setViewPager(vpPager);


        return root;
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
                    return new ConsumeGrade1Fragment();
                case 1:
                    return new ConsumeGrade2Fragment();
                case 2:
                    return new ConsumeGrade3Fragment();
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