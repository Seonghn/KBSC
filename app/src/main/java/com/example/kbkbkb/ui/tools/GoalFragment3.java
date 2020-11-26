package com.example.kbkbkb.ui.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kbkbkb.R;

public class GoalFragment3 extends Fragment {

    PreferenceManager account_sp;
    private TextView r1,r2,fl1,fl2,fl3,fl4,fl5,fr1,fr2,fr3,fr4,fr5,target,tm;
    private Context cont;

    //Fragment에서 Activity의 context를 가져오는 부분
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        cont = context;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal3, container, false);//fragment layout 연결

        r1 = view.findViewById(R.id.r1);
        r2 = view.findViewById(R.id.r2);
        fl1 = view.findViewById(R.id.fl1);
        fl2 = view.findViewById(R.id.fl2);
        fl3 = view.findViewById(R.id.fl3);
        fl4 = view.findViewById(R.id.fl4);
        fl5 = view.findViewById(R.id.fl5);
        fr1 = view.findViewById(R.id.fr1);
        fr2 = view.findViewById(R.id.fr2);
        fr3 = view.findViewById(R.id.fr3);
        fr4 = view.findViewById(R.id.fr4);
        fr5 = view.findViewById(R.id.fr5);
        target = view.findViewById(R.id.target);
        tm = view.findViewById(R.id.tm);
        String p = "c";
        account_sp.getPreferences(cont);

        try{
            r1.setText(account_sp.getCost(cont, p+"1"));
            r2.setText(account_sp.getCost(cont, p+"2"));
            fl1.setText(account_sp.getCost(cont, p+"3"));
            fl2.setText(account_sp.getCost(cont, p+"4"));
            String[] res = account_sp.getTarget(cont, p+"5").split("/");
            tm.setText(res[0]);
            target.setText(res[1]);
        } catch (Exception e){
            r1.setText("-");
            r2.setText("-");
            tm.setText("-");
            target.setText("-");
        }
        return view;
    }

    public static class PreferenceManager {

        private static SharedPreferences getPreferences(Context context) {
            return context.getSharedPreferences("preference",context.MODE_PRIVATE);
        }

        //소비 금액
        public static void setCost(Context context, String key, String value) {
            SharedPreferences preferences = getPreferences(context); //context끼리 각각의 sharedPreference를 저장하고 있다.
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key,value); //키 값에 맞추어 String 값 삽입
            editor.commit();
        }

        //비율
        public static void setRatio(Context context, String key, String value) {
            SharedPreferences preferences = getPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key,value);
            editor.commit();
        }

        //1번결과
        public static void setR1(Context context, String key, String value) {
            SharedPreferences preferences = getPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key,value);
            editor.commit();
        }

        //2번결과
        public static void setR2(Context context, String key, String value) {
            SharedPreferences preferences = getPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key,value);
            editor.commit();
        }

        // 소비 금액 출력
        public static String getCost(Context context, String key) {
            SharedPreferences preferences = getPreferences(context);
            String value = preferences.getString(key," ");
            return value;
        }

        //비율 출력
        public static String getRatio(Context context, String key) {
            SharedPreferences preferences = getPreferences(context);
            String value = preferences.getString(key," ");
            return value;
        }

        //1번결과 출력
        public static String getR1(Context context, String key) {
            SharedPreferences preferences = getPreferences(context);
            String value = preferences.getString(key," ");
            return value;
        }

        //2번결과 출력
        public static String getR2(Context context, String key) {
            SharedPreferences preferences = getPreferences(context);
            String value = preferences.getString(key,"");
            return value;
        }

        public static void setTarget(Context context, String key, String value) {
            SharedPreferences preferences = getPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key,value);
            editor.commit();
        }

        // 소비 금액 출력
        public static String getTarget(Context context, String key) {
            SharedPreferences preferences = getPreferences(context);
            String value = preferences.getString(key, "");
            return value;
        }

        //키 삭제
        public static void removeKey(Context context,String key) {
            SharedPreferences preferences = getPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(key);
            editor.commit();
        }

        //전체 삭제
        public static void clear(Context context) {
            SharedPreferences preferences = getPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
        }
    }

}
