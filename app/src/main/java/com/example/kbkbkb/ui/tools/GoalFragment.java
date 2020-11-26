package com.example.kbkbkb.ui.tools;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.kbkbkb.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import me.relex.circleindicator.CircleIndicator;

public class GoalFragment extends Fragment {

    FragmentPagerAdapter adapterViewPager;
    PreferenceManager account_sp;
    public static ViewPager vpPager;
    private String serverip = "220.149.236.41";
    private int serverport = 8506;
    private Socket client;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    String Page="0";
    TextView t1,t2,t3,t4;
    private Context cont;

    private static int NUM_ITEMS = 1;

    //Fragment에서 Activity의 context를 가져오는 부분
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        cont = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_goal, container, false);

        vpPager = (ViewPager) root.findViewById(R.id.goal_vpPager);
        adapterViewPager = new GoalFragment.MyPagerAdapter(getFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = (CircleIndicator)root.findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);
        //CircleIndicator 설정
//        CircleIndicator indicator = (CircleIndicator) root.findViewById(R.id.consume_indicator);
//        indicator.setViewPager(vpPager);
        ImageButton add = root.findViewById(R.id.add);

        t1 = root.findViewById(R.id.r1);
        t2 = root.findViewById(R.id.r2);
        t3 = root.findViewById(R.id.r3);
        t4 = root.findViewById(R.id.r4);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionCodeTypeDialog octDialog  = new OptionCodeTypeDialog(getContext(), new CustomDialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        NUM_ITEMS++;
                        adapterViewPager = new GoalFragment.MyPagerAdapter(getFragmentManager());
                        vpPager.setAdapter(adapterViewPager);
                        t1.setText(account_sp.getCost(cont, Page+"1"));
                        t2.setText(account_sp.getRatio(cont, Page+"2"));
                        t3.setText(account_sp.getR1(cont, Page+"3"));
                        t4.setText(account_sp.getR2(cont, Page+"4"));
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
        return root;
    }
    public interface CustomDialogClickListener {
        void onPositiveClick();
        void onNegativeClick();
    }

    public class OptionCodeTypeDialog extends Dialog {

        private Context context;
        private CustomDialogClickListener customDialogClickListener;
        private EditText e1,e2,e3;
        private Button p1,p2,p3;

        public OptionCodeTypeDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener) {
            super(context);
            this.context = context;
            this.customDialogClickListener = customDialogClickListener;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.goal_pop1);

            p1 = findViewById(R.id.page1);
            p2 = findViewById(R.id.page2);
            p3 = findViewById(R.id.page3);

            p1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    p1.setSelected(true);
                    p2.setSelected(false);
                    p3.setSelected(false);
                    Page = "a";
                }
            });
            p2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(NUM_ITEMS==2){
                        p1.setSelected(false);
                        p2.setSelected(true);
                        p3.setSelected(false);
                        Page = "b";
                    } else{
                        Toast.makeText(getContext(), "1페이지가 비었습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            p3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(NUM_ITEMS==3) {
                        p1.setSelected(false);
                        p2.setSelected(false);
                        p3.setSelected(true);
                        Page = "c";
                    } else {
                        Toast.makeText(getContext(), "2페이지가 비었습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });

            e1 = findViewById(R.id.e1);
            e2 = findViewById(R.id.e2);
            e3 = findViewById(R.id.e3);

            Button yes = findViewById(R.id.yes);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialogClickListener.onPositiveClick();
                    if (e1.getText().toString().length() == 0 || e2.getText().toString().length() == 0 || e3.getText().toString().length() == 0) {
                        //만약 edittext가 어느 곳이든 빈 곳이라면
                        Toast.makeText(getContext(), "빈칸 없이 입력해주세요.", Toast.LENGTH_LONG).show();
                    } else{
                        if(!p1.isSelected() && !p2.isSelected() && !p3.isSelected()) {
                            Toast.makeText(getContext(), "페이지를 선택해주세요.", Toast.LENGTH_LONG).show();
                        } else{
                            Log.d("ch","df");
                            String send_text = e1.getText().toString()+"/"+e2.getText().toString()+"/"+e3.getText().toString();
                            e1.setText("");
                            e2.setText("");
                            e3.setText("");
                            Connect connect = new Connect();
                            connect.execute(send_text); //edittext에서 입력한 값을 전달
                            dismiss();
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
                client = new Socket(serverip, serverport); //socket 연결
                dataOutputStream = new DataOutputStream(client.getOutputStream()); //서버에게 정보를 보내기 위한 변수
                dataInputStream = new DataInputStream(client.getInputStream()); //서버에게 정보를 받기 위한 변수
                output_message = strings[0];
                dataOutputStream.writeUTF(output_message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (true) { //서버에게 입력받음
                try {
                    byte[] buf = new byte[1000];
                    int read_Byte = dataInputStream.read(buf);
                    input_message = new String(buf, 0, read_Byte);
                    if (!input_message.equals("stop")) {
                        Log.e("test","들어갔나요?");
                        publishProgress(input_message);
                        String[] res = input_message.split("/");

                        account_sp.getPreferences(cont);
                        account_sp.setCost(cont, Page+"1", res[0]);
                        account_sp.setRatio(cont, Page+"2", res[1]);
                        account_sp.setR1(cont, Page+"3", res[2]);
                        account_sp.setR2(cont, Page+"4", res[3]);
                        Log.d("page",account_sp.getCost(cont, Page+"1"));
                    } else {
                        break;
                    }
                    //Thread.sleep(1); //thread sleep을 빼면 중간에 한글 깨짐이 발생하였음 - 아마도 한번 thread의 쉬는 시간을 줘야 한글이 안깨지나봄.
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (java.lang.StringIndexOutOfBoundsException e) {
                } //catch (InterruptedException e) {
                //}
            }

            return null;
        }
    }
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        //fragment 개수 설정 변수

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

    //SharedPreference 클래스 정의
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