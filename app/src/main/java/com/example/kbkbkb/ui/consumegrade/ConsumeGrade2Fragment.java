package com.example.kbkbkb.ui.consumegrade;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.broooapps.graphview.CurveGraphView;
import com.example.kbkbkb.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class ConsumeGrade2Fragment extends Fragment{

    //그래프 그리기 위한 변수
    private SQLiteDatabase database;
    private CurveGraphView curveGraphView;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consume2, container, false);//fragment layout 연결

        boolean bResult = isCheckDatabase();    // DB가 있는지?
        if (!bResult) {  // DB가 없으면 복사
            Log.d("check","database copy");
            copyDataBase();
        }

        Cursor c;

        openDatabase();
        c = database.rawQuery("select * from user", null);

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
        AssetManager manager = mContext.getAssets();
        String folderPath = "/data/data/com.example.kbkbkb" + "/databases";
        String filePath = "/data/data/com.example.kbkbkb" + "/databases/" + "userinfo.db";
        File folder = new File(folderPath);
        File file = new File(filePath);
        FileOutputStream fileOut = null;
        BufferedOutputStream bufferOut = null;
        try {
            InputStream inputStr = manager.open("db/" + "userinfo.db");
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
            Log.e("Error : ", e.getMessage());
        }
    }
    public void openDatabase() {
        database = getActivity().openOrCreateDatabase("userinfo.db", android.content.Context.MODE_PRIVATE, null);
        if (database != null) {
            Log.e("check", "데이터베이스가 존재합니다.");
        }
    }

    public abstract void onCreate(SQLiteDatabase db);

    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}