package com.example.kbkbkb.ui.consumegrade;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.text.Edits;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kbkbkb.R;
import com.example.kbkbkb.ui.serverCom.ServerCommunicationFragment;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConsumeGrade3Fragment extends Fragment {

    //그래프 그리기 위한 변수
    private SQLiteDatabase database;
    private PieChart pieChart;
    private Context mContext;

    //문자열 개수 처리
    HashMap<String, Integer> hm = new HashMap<>();

    //SharedPreference 변수
    private PreferenceManager consume_sp;


    //SharedPreference 클래스 정의
    public static class PreferenceManager {

        private static SharedPreferences getPreferences(Context context) {
            return context.getSharedPreferences("preference",context.MODE_PRIVATE);
        }

        //첫번째 정보
        public static void setname(Context context, String key, String value) {
            SharedPreferences preferences = getPreferences(context); //context끼리 각각의 sharedPreference를 저장하고 있다.
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key,value); //키 값에 맞추어 String 값 삽입
            editor.commit();
        }

        public static void setvalue(Context context, String key, Integer value) {
            SharedPreferences preferences = getPreferences(context); //context끼리 각각의 sharedPreference를 저장하고 있다.
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key,value); //키 값에 맞추어 String 값 삽입
            editor.commit();
        }

        //저장된 정보 출력
        public static String getname(Context context, String key) {
            SharedPreferences preferences = getPreferences(context);
            String value = preferences.getString(key,"");
            return value;
        }

        public static int getvalue(Context context, String key) {
            SharedPreferences preferences = getPreferences(context);
            int value = preferences.getInt(key,0);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consume3, container, false);//fragment layout 연결

        boolean bResult = isCheckDatabase();    // DB가 있는지?
        if (!bResult) {  // DB가 없으면 복사
            //Log.d("check","database copy");

            copyDataBase();
        }

        Cursor c;

        openDatabase();
        c = database.rawQuery("select * from user", null);
        c.move(0);

        //저장된 정보를 이용하여 db를 확인할건지 정함
        consume_sp.getPreferences(mContext);

        if(consume_sp.getvalue(mContext,"value30") == 0) {//기본값이 0인데 0인 경우, 아직 db를 탐색하지 않은 것으로 봄
            for (int i = 0; i < 928; i++) {

                c.moveToNext();

                String date = c.getString(0);
                String send = c.getString(2);
                String money = c.getString(4);
                //돈 표시의 ,를 없앰
                String p_money = money.replace(",", "");

                //현재 날짜로 나눈 것이 아니기 때문에 수정 필요
                int subdate = Integer.parseInt(date.substring(5, 7));

                if (subdate == 12) {
                    if (hm.containsKey(send))
                        hm.put(send, hm.get(send) + Integer.parseInt(p_money));
                    else {
                        hm.put(send, Integer.parseInt(p_money));
                    }
                } else break;
            }

            Iterator it = sortByValue(hm).iterator();

            int cnt = 0;
            int guitar=0;

            while(it.hasNext()) {
                String t = (String)it.next();

                //사실 같아도 동작하나 혹시 몰라서 큰 값도 포함
                if (cnt >= 4) {
                    guitar += hm.get(t);
                } else {
                    consume_sp.setname(mContext,"name3"+cnt, t);
                    consume_sp.setvalue(mContext,"value3"+cnt, hm.get(t));
                    cnt++;
                }
            }
            consume_sp.setvalue(mContext,"value3"+4,guitar);
        }

        pieChart = (PieChart)view.findViewById(R.id.pie1);

        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.BLACK);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        /*Iterator it = sortByValue(hm).iterator();

        int cnt = 0;
        int guitar=0;

        while(it.hasNext()) {
            String t = (String) it.next();

            //사실 같아도 동작하나 혹시 몰라서 큰 값도 포함
            if (cnt >= 4) {
                guitar += hm.get(t);
            } else {
                yValues.add(new PieEntry(hm.get(t),t));
                cnt++;
            }
        }*/

        for(int i=0; i<4; i++) {
            yValues.add(new PieEntry(consume_sp.getvalue(mContext,"value3"+i),consume_sp.getname(mContext,"name3"+i)));
        }

        yValues.add(new PieEntry(consume_sp.getvalue(mContext,"value3"+4),"기타"));

        Description description = new Description();
        description.setText("이번달"); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(yValues,"");

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);

        return view;
    }
    public boolean isCheckDatabase() {
        String filePath = "/data/data/com.example.kbkbkb" + "/databases/" + "userinfo.db";
        File file = new File(filePath);
        return file.exists();
    }
    public void onAttach(Context context) {
        //그냥 getContext하면 정보를 못받는다.(null을 받는다) 이것 때문에 SharedPreference 관련 오류가 발생하였고 null이 아닌 Activity context정보를 받아야 해결이 가능하였다.
        super.onAttach(context);

        mContext = context;
    }

    public void copyDataBase() {

        AssetManager manager = mContext.getResources().getAssets();

        String folderPath = "/data/data/com.example.kbkbkb" + "/databases";
        String filePath = "/data/data/com.example.kbkbkb" + "/databases/" + "userinfo.db";
        File folder = new File(folderPath);
        File file = new File(filePath);
        FileOutputStream fileOut = null;
        BufferedOutputStream bufferOut = null;
        try {
            //InputStream inputStr = manager.open("db/" + "userinfo.db");
            InputStream inputStr = manager.open( "db/" + "userinfo.db");
            BufferedInputStream bufferStr = new BufferedInputStream(inputStr);
            if (folder.exists()) {
            } else {
                folder.mkdirs();
            }

            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fileOut = new FileOutputStream(file);
            bufferOut = new BufferedOutputStream(fileOut);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bufferStr.read(buffer, 0, 1024)) != -1) {
                bufferOut.write(buffer, 0, read);
            }
            bufferOut.flush();
            bufferOut.close();
            fileOut.close();
            bufferStr.close();
            inputStr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openDatabase() {
        database = getActivity().openOrCreateDatabase("userinfo.db", android.content.Context.MODE_PRIVATE, null);
        if (database != null) {
            Log.e("check", "데이터베이스가 존재합니다.");
        }
    }

    public static List sortByValue(final Map map) {

        List<String> list = new ArrayList();
        list.addAll(map.keySet());

        Collections.sort(list,new Comparator() {
            public int compare(Object o1,Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                return ((Comparable) v2).compareTo(v1);
            }
        });
        return list;
    }
}