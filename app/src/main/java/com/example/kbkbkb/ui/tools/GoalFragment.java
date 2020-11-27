package com.example.kbkbkb.ui.tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.kbkbkb.R;
import com.example.kbkbkb.RegisterActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class GoalFragment extends Fragment {

    MyPagerAdapter adapterViewPager;
    PreferenceManager goal_sp;
    public static ViewPager vpPager;
    private String serverip = "220.149.236.152";
    private int serverport = 5505;
    private Socket client;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private Context cont;
    private EditText e1,e2,e3,e5;

    private static int NUM_ITEMS;
    private String[] array;

    //알림창 출력 변수
    private AlertDialog dig;


    //Fragment에서 Activity의 context를 가져오는 부분
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cont = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_goal, container, false);
        //NUM_ITEMS = account_sp.getPN(cont, "PN");
        vpPager = (ViewPager) root.findViewById(R.id.goal_vpPager);
        adapterViewPager = new MyPagerAdapter(getFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        //CircleIndicator 설정
//        CircleIndicator indicator = (CircleIndicator) root.findViewById(R.id.consume_indicator);
//        indicator.setViewPager(vpPager);
        ImageButton add = root.findViewById(R.id.add);

        goal_sp.getPreferences(cont);
        NUM_ITEMS = goal_sp.getPage(cont,"page_num");

        //저장되어있는 경우 추가함
        if(NUM_ITEMS > 0 && NUM_ITEMS <= 3) {
            for(int i=0; i<NUM_ITEMS; i++) {
                String msg = goal_sp.getServer(cont,NUM_ITEMS+"_descgoal");
                String check = msg.replace("'","");
                array = check.split("/");

                adapterViewPager.addItem(new GoalFragmentView(goal_sp.getGoal(cont,i+"_goal"), array[0], array[1], "1."+array[2]+" "+array[3]+"\n", "2."+array[4]+" "+array[5]+"\n",
                        "3."+array[6]+" "+array[7]+"\n", "4."+array[8]+" "+array[9]+"\n", "5."+array[10]+" "+array[11]+"\n","1."+array[12]+" "+array[13]+"\n",
                        "2."+array[14]+" "+array[15]+"\n", "3."+array[16]+" "+array[17]+"\n", "4."+array[18]+" "+array[19]+"\n", "5."+array[20]+" "+array[21]+"\n"));
            }
            vpPager.setAdapter(adapterViewPager);
        }

        //목표 추가버튼
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //알림창 띄움
                OptionCodeTypeDialog octDialog  = new OptionCodeTypeDialog(cont, new CustomDialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        //수정된 목표 개수로 ViewPager설정
                        if(adapterViewPager.getCount() < 3) {
                            goal_sp.setGoal(cont,adapterViewPager.getCount()+"_goal",e5.getText().toString());
                            NUM_ITEMS++;
                            goal_sp.setPage(cont, "page_num", NUM_ITEMS);
                        } else {
                            Toast.makeText(cont, "목표는 3개까지만 가능합니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onNegativeClick() {

                    }
                });
                octDialog.setCanceledOnTouchOutside(true);
                octDialog.setCancelable(true);
                octDialog.setTitle("목표 추가");
                octDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                octDialog.show();
            }
        });

        dig = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View customView = factory.inflate(R.layout.loading, null);

        //Gif 이미지 처리부분
        final ImageView imgView = customView.findViewById(R.id.ivloading);
        Glide.with(getActivity()).load(R.raw.loading).into(imgView);

        //바탕 투명하게 설정
        dig.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dig.setView(customView);

        return root;
    }
    public interface CustomDialogClickListener {
        void onPositiveClick();
        void onNegativeClick();
    }

    public class OptionCodeTypeDialog extends Dialog { //알림창 설명

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

            e1 = findViewById(R.id.e1);
            e2 = findViewById(R.id.e2);
            e3 = findViewById(R.id.e3);
            e5 = findViewById(R.id.e5);

            Button yes = findViewById(R.id.yes);
            //작성완료 버튼을 누르면
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialogClickListener.onPositiveClick();
                    if (e1.getText().toString().length() == 0 || e2.getText().toString().length() == 0 || e3.getText().toString().length() == 0 || e5.getText().toString().length() == 0) {
                        //만약 edittext가 어느 곳이든 빈 곳이라면
                        Toast.makeText(getContext(), "빈칸 없이 입력해주세요.", Toast.LENGTH_LONG).show();
                    } else{
                        String send_text = e1.getText().toString()+"/"+e2.getText().toString()+"/"+e3.getText().toString();

                        if(!send_text.isEmpty()) {
                            dig.show();
                            Connect connect = new Connect();
                            Log.e("connect check","연결되어 데이터를 보냅니다. : "+send_text);
                            connect.execute(send_text);
                            dismiss();
                            //로딩부분 구현 (PS. 안드로이드 정책상 8.0이상부터 앱은 사용자와 계속 상호작용해야함. 따라서, 바탕을 연속 클릭시 나가짐.

                        } else {
                            Toast.makeText(getContext(), "다시 보내주세요.", Toast.LENGTH_LONG).show();
                        }
                    }
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
    //네트워크 처리를 비동기 방식으로 처리
    private class Connect extends AsyncTask<String,String,Void> {
        private String output_message;
        private String input_message;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                client = new Socket(serverip,serverport); //socket 연결
                dataOutputStream = new DataOutputStream(client.getOutputStream()); //서버에게 정보를 보내기 위한 변수
                dataInputStream = new DataInputStream(client.getInputStream()); //서버에게 정보를 받기 위한 변수
                output_message = strings[0];
                dataOutputStream.writeUTF(output_message);
                Log.e("Connect Success","연결 성공");
            } catch (Exception e) {
                Log.e("Connect Loss","연결 실패");
                e.printStackTrace();
            }
            while(true) { //서버에게 입력받음
                try {
                    byte[] buf = new byte[1000];
                    int read_Byte = dataInputStream.read(buf);
                    input_message = new String(buf, 0, read_Byte);
                    if (!input_message.equals("stop")) {
                        Log.e("stop 아님","stop이 아닙니다.");
                        publishProgress(input_message);
                    } else {
                        Log.e("stop임","stop입니다.");
                        break;
                    }
                    //Thread.sleep(1); //thread sleep을 빼면 중간에 한글 깨짐이 발생하였음 - 아마도 한번 thread의 쉬는 시간을 줘야 한글이 안깨지나봄.
                } catch (IOException e) {
                } catch (java.lang.StringIndexOutOfBoundsException e) {
                } //catch (InterruptedException e) {
                //}
            }
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dig.dismiss();
            Log.e("input return","입력 다 받아 return");

            return null;
        }

        protected void onProgressUpdate(String... params){ //서버에서 받은 데이터를 textview로 보여줌
            //output += params[0];
            //sc_tv1.append("보낸 메세지: " + output_message + "\n받은 메세지: " + output);
            Log.d("CSOS",params[0]);

            goal_sp.setServer(cont,NUM_ITEMS+"_descgoal",params[0]);

            String check = params[0].replace("'","");
            array = check.split("/");

            adapterViewPager.addItem(new GoalFragmentView(e1.getText().toString(), array[0], array[1], "1."+array[2]+" "+array[3]+"원\n", "2."+array[4]+" "+array[5]+"원\n",
                    "3."+array[6]+" "+array[7]+"원\n", "4."+array[8]+" "+array[9]+"원\n", "5."+array[10]+" "+array[11]+"원\n","1."+array[12]+" "+array[13]+"\n",
                    "2."+array[14]+" "+array[15]+"\n", "3."+array[16]+" "+array[17]+"\n", "4."+array[18]+" "+array[19]+"\n", "5."+array[20]+" "+array[21]+"\n"));
            vpPager.setAdapter(adapterViewPager);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> items = new ArrayList<>();
        //fragment 개수 설정 변수

        //page어댑터 설정
        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        public void addItem(Fragment item) {
            items.add(item);
        }

        //page마다 어떤 프래그먼트가 들어가는지
        @Override
        public Fragment getItem(int position) {
//            switch(position) {
//                case 0:
//                    return new GoalFragment1();
//                case 1:
//                    return new GoalFragment2();
//                case 2:
//                    return new GoalFragment3();
////                case 3:
////                    return new ConsumeGrade4Fragment();
//                default:
//                    return null;
//            }
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }

    //SharedPreference 클래스 정의
    public static class PreferenceManager {

        private static SharedPreferences getPreferences(Context context) {
            return context.getSharedPreferences("preference",context.MODE_PRIVATE);
        }

        //페이지 번호
        public static void setPage(Context context, String key, int value) {
            SharedPreferences preferences = getPreferences(context); //context끼리 각각의 sharedPreference를 저장하고 있다.
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key,value); //키 값에 맞추어 String 값 삽입
            editor.commit();
        }

        //목표
        public static void setGoal(Context context, String key, String value) {
            SharedPreferences preferences = getPreferences(context); //context끼리 각각의 sharedPreference를 저장하고 있다.
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key,value); //키 값에 맞추어 String 값 삽입
            editor.commit();
        }

        //서버받는 정보
        public static void setServer(Context context, String key, String value) {
            SharedPreferences preferences = getPreferences(context); //context끼리 각각의 sharedPreference를 저장하고 있다.
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key,value); //키 값에 맞추어 String 값 삽입
            editor.commit();
        }

        public static Integer getPage(Context context, String key){
            SharedPreferences preferences = getPreferences(context);
            int value = preferences.getInt(key, 0);
            return value;
        }

        public static String getGoal(Context context, String key){
            SharedPreferences preferences = getPreferences(context);
            String value = preferences.getString(key, "");
            return value;
        }

        // 소비 금액 출력
        public static String getServer(Context context, String key) {
            SharedPreferences preferences = getPreferences(context);
            String value = preferences.getString(key, "");
            return value;
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