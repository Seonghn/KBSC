package com.example.kbkbkb.ui.consumegrade;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kbkbkb.R;
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

public class ConsumeGrade4Fragment extends Fragment {

    //그래프 그리기 위한 변수
    private SQLiteDatabase database;
    private PieChart pieChart;
    private Context mContext;

    //문자열 개수 처리
    HashMap<String, Integer> hm = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consume4, container, false);//fragment layout 연결

        boolean bResult = isCheckDatabase();    // DB가 있는지?
        if (!bResult) {  // DB가 없으면 복사
            //Log.d("check","database copy");

            copyDataBase();
        }

        Cursor c;

        openDatabase();
        c = database.rawQuery("select * from user", null);
        c.move(0);

        for(int i=0; i<928; i++) {

            c.moveToNext();

            String send = c.getString(2);
            String money = c.getString(4);
            //돈 표시의 ,를 없앰
            String p_money = money.replace(",","");

            if(hm.containsKey(send))
                hm.put(send,hm.get(send)+Integer.parseInt(p_money));
            else{
                hm.put(send,Integer.parseInt(p_money));
            }
        }

        pieChart = (PieChart)view.findViewById(R.id.pie1);

        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        //pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.BLACK);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        Iterator it = sortByValue(hm).iterator();

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
        }

        yValues.add(new PieEntry(guitar,"기타"));

        Description description = new Description();
        description.setText("이번년도"); //라벨
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