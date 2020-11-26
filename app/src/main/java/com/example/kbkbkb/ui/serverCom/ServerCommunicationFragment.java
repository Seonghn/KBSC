package com.example.kbkbkb.ui.serverCom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.ui.AppBarConfiguration;

import com.bumptech.glide.Glide;
import com.example.kbkbkb.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerCommunicationFragment extends Fragment {

    private LinearLayout Lscreen;

    //상단 바 관련 변수
    private AppBarConfiguration mAppBarConfiguration;

    //서버 관련 변수
    private String serverip = "220.149.236.154";
    private int serverport = 8508;
    private Socket client;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    //계좌 정보 출력 변수
    private TextView sc_tv1;
    private Button sc_btn1;
    private EditText sc_account_et, sc_birth_et, sc_passwd_et;
    private String output;

    //알림창 출력 변수
    private AlertDialog dig;

    //SharedPrefernce를 이용한 계좌 저장 변수
    private PreferenceManager account_sp;
    private CheckBox account_cb;
    private String sp_ackey = "ac_kbkbkb";
    private String sp_btkey = "bt_kbkbkb";
    private String sp_cbkey = "cb_kbkbkb";
    private Context cont; //계속 불리면 연산량이 많을 것 같아 변수로 저장


    //SharedPreference 클래스 정의
    public static class PreferenceManager {

        private static SharedPreferences getPreferences(Context context) {
            return context.getSharedPreferences("preference",context.MODE_PRIVATE);
        }

       //계좌 정보 저장
        public static void setAccountInfo(Context context, String key, String value) {
            SharedPreferences preferences = getPreferences(context); //context끼리 각각의 sharedPreference를 저장하고 있다.
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key,value); //키 값에 맞추어 String 값 삽입
            editor.commit();
        }

        //생년월일 정보 저장
        public static void setBirth(Context context, String key, Integer value) {
            SharedPreferences preferences = getPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key,value);
            editor.commit();
        }

        //체크박스 체크여부 정보 저장
        public static void setCheckBox(Context context, String key, Boolean value) {
            SharedPreferences preferences = getPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key,value);
            editor.commit();
        }


        //저장된 계좌 정보 출력
        public static String getAccountInfo(Context context, String key) {
            SharedPreferences preferences = getPreferences(context);
            String value = preferences.getString(key,"");
            return value;
        }

        //저장된 생년월일 정보 출력
        public static Integer getBirth(Context context, String key) {
            SharedPreferences preferences = getPreferences(context);
            Integer value = preferences.getInt(key,0);
            return value;
        }

        //저장된 체크박스 여부 출력
        public static Boolean getCheckBox(Context context, String key) {
            SharedPreferences preferences = getPreferences(context);
            return preferences.getBoolean(key,false);
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

    public ServerCommunicationFragment() {
    }

    @Override
    public void onAttach(Context context) {
        //그냥 getContext하면 정보를 못받는다.(null을 받는다) 이것 때문에 SharedPreference 관련 오류가 발생하였고 null이 아닌 Activity context정보를 받아야 해결이 가능하였다.
        super.onAttach(context);
        cont = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ServerCommunicationModel serverCommunicationModel = ViewModelProviders.of(this).get(ServerCommunicationModel.class);
        View root = inflater.inflate(R.layout.fragment_servercom, container, false);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        //view 연결 코드
        sc_tv1 = (TextView)getView().findViewById(R.id.sc_tv);
        sc_btn1 = (Button)getView().findViewById(R.id.server_send_btn);
        sc_account_et = (EditText)getView().findViewById(R.id.account_et);
        sc_birth_et = (EditText)getView().findViewById(R.id.birth_et);
        sc_passwd_et = (EditText)getView().findViewById(R.id.passwd_et);
        account_cb = (CheckBox) getView().findViewById(R.id.account_cb);

        //edittext 자연스러운 키보드 숨김 코드 (키보드가 아닌 바깥쪽 터치시 키보드가 사라짐)
        Lscreen = (LinearLayout)getView().findViewById(R.id.server_communication_linear); //가장 상위 Linearlayout 가져옴

        Lscreen.setOnClickListener(new View.OnClickListener() { //상위 Linearlayout click 시 키보드 사라짐
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); //키보드 관련 method
                if (getActivity().getCurrentFocus() instanceof EditText) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0); //EditText가 아니면 키보드 숨김
                    getActivity().getCurrentFocus().clearFocus();
                }
            }
        });

        sc_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edittext값 가져오기
                String send_text = sc_account_et.getText().toString()+"/"+sc_birth_et.getText().toString()+"/"+sc_passwd_et.getText().toString();

                if(sc_account_et.getText().toString().length() == 0 || sc_birth_et.getText().toString().length() == 0 || sc_passwd_et.getText().toString().length() == 0) {
                    //만약 edittext가 어느 곳이든 빈 곳이라면
                    Toast.makeText(getActivity(),"빈칸 없이 입력해주세요.",Toast.LENGTH_LONG).show();
                } else {
                    //edittext 초기화
                    sc_passwd_et.setText("");

                    //알람 띄우기
                    dig.show();

                    //서버 통신
                    Connect connect = new Connect();
                    connect.execute(send_text); //edittext에서 입력한 값을 전달
                }
            }
        });

        //로딩부분 구현 (PS. 안드로이드 정책상 8.0이상부터 앱은 사용자와 계속 상호작용해야함. 따라서, 바탕을 연속 클릭시 나가짐.
        dig = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View customView = factory.inflate(R.layout.loading, null);

        //Gif 이미지 처리부분
        final ImageView imgView = customView.findViewById(R.id.ivloading);
        Glide.with(getActivity()).load(R.raw.loading).into(imgView);

        //바탕 투명하게 설정
        dig.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dig.setView(customView);
        //button 표시 - 나중에 지울 부분
        /*dig.setButton(DialogInterface.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });*/


        /*Sharedpreference 정보를 토대로 계좌 정보 저장 및 출력*/
        account_sp.getPreferences(cont);

        //checkbox가 체크되는 순간 callback되어 불러진다.
        account_cb.setOnClickListener(new CheckBox.OnClickListener() { //매개변수가 자동완성이 안되서 놀람.
            @Override
            public void onClick(View v) {
                if(account_cb.isChecked()) { //checkbox가 체크가 되는 순간 edittext에 있는 정보 저장, 체크가 되는 순간 말고도 나가는 순간에도 정보를 저장하고 있어야함, 버튼 클릭시에도 저장해야함
                    account_sp.setAccountInfo(cont, sp_ackey, sc_account_et.getText().toString());
                    //빈칸인 경우, number format이 아니기 때문에 오류가 발생하여 그냥 넘기도록 한다.
                    try {
                        account_sp.setBirth(cont, sp_btkey, Integer.parseInt(sc_birth_et.getText().toString()));
                    } catch (Exception e) {

                    }
                    account_sp.setCheckBox(cont, sp_cbkey, true);
                } else { //check가 안된 경우
                    //false로 저장
                    account_sp.setCheckBox(cont, sp_cbkey, false);
                }
            }
        });

        //먼저 checkbox의 표시여부를 받아온다.

        Boolean checkbox_check = PreferenceManager.getCheckBox(getContext(), sp_cbkey);

        //checkbox 표시되는 것에 따라
        if(checkbox_check) { //checkbox가 true라고 저장되어 있다면
            //체크박스를 체크
            account_cb.setChecked(true);

            //나머지 계좌 및 생년월일 정보를 가져오고
            String account_number = account_sp.getAccountInfo(cont,sp_ackey);
            Integer birth_number = account_sp.getBirth(cont,sp_btkey);

            //저장된 정보로 설정
            sc_account_et.setText(account_number);
            sc_birth_et.setText(birth_number.toString());
        }


        //비밀번호 보안 관련 작업 필요

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStop() {
        //fragment가 없어질 때도 저장
        super.onStop();
        if(account_cb.isChecked()) {
            account_sp.removeKey(cont, sp_ackey);
            account_sp.setAccountInfo(cont, sp_ackey, sc_account_et.getText().toString());
            try {
                account_sp.setBirth(cont, sp_btkey, Integer.parseInt(sc_birth_et.getText().toString()));
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onDestroyView() {
        //fragment가 view에서 해제되는 순간에도 저장
        if(account_cb.isChecked()) {
            account_sp.removeKey(cont, sp_ackey);
            account_sp.setAccountInfo(cont, sp_ackey, sc_account_et.getText().toString());
            try {
                account_sp.setBirth(cont, sp_btkey, Integer.parseInt(sc_birth_et.getText().toString()));
            } catch (Exception e) {

            }
        }
        super.onDestroyView();
    }

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
            } catch (Exception e) {
                e.printStackTrace();
            }
            while(true) { //서버에게 입력받음
                try {
                    byte[] buf = new byte[1000];
                    int read_Byte = dataInputStream.read(buf);
                    input_message = new String(buf, 0, read_Byte);
                    if (!input_message.equals("stop")) {
                        publishProgress(input_message);
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

        protected void onProgressUpdate(String... params){ //서버에서 받은 데이터를 textview로 보여줌
            //output += params[0];
            //sc_tv1.append("보낸 메세지: " + output_message + "\n받은 메세지: " + output);
            Log.d("CSOS",params[0]);
            sc_tv1.append(params[0]);
            dig.dismiss();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}