package com.example.kbkbkb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RegisterActivity extends AppCompatActivity {

    EditText acc_num, bir_num, edit1, edit2, edit3, edit4;
    LinearLayout linearLayout;

    //서버 관련 변수
    private String serverip = "220.149.236.154";
    private int serverport = 8508;
    private Socket client;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private Connect connect = null;

    //알림창 출력 변수
    private AlertDialog dig;

    //회원가입 sp
    private PreferenceManager register_sp;

    //sp 설정
    public static class PreferenceManager {

        private static SharedPreferences getPreferences(Context context) {
            return context.getSharedPreferences("preference",context.MODE_PRIVATE);
        }

        //계좌 정보 저장
        public static void setPassword(Context context, String key, String value) {
            SharedPreferences preferences = getPreferences(context); //context끼리 각각의 sharedPreference를 저장하고 있다.
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key,value); //키 값에 맞추어 String 값 삽입
            editor.commit();
        }


        //저장된 계좌 정보 출력
        public static String getPassword(Context context, String key) {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acc_register);

        edit1 = (EditText) findViewById(R.id.pin1);
        edit2 = (EditText) findViewById(R.id.pin2);
        edit3 = (EditText) findViewById(R.id.pin3);
        edit4 = (EditText) findViewById(R.id.pin4);
        acc_num = (EditText) findViewById(R.id.acc_num);
        bir_num = (EditText) findViewById(R.id.bir_num);

        acc_num.requestFocus();

        acc_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (acc_num.length()==14){
                    linearLayout = findViewById(R.id.birthnumber);
                    LinearLayout.LayoutParams r_p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                    linearLayout.setLayoutParams(r_p);
                    bir_num.requestFocus();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bir_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (bir_num.length()==6){
                    linearLayout = findViewById(R.id.pinnumber);
                    LinearLayout.LayoutParams r_p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                    linearLayout.setLayoutParams(r_p);
                    edit1.requestFocus();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edit1.length()==1){  // edit1  값의 제한값을 6이라고 가정했을때
                    edit2.requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        edit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edit2.length()==1){  // edit1  값의 제한값을 6이라고 가정했을때
                    edit3.requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        edit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edit3.length()==1){  // edit1  값의 제한값을 6이라고 가정했을때
                    edit4.requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        edit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edit4.length()==1){  // edit4의 값이 다 들어오면
                    // 소켓 통신을 통해 edit text에 입력한 값들을 주고
                    String send_text = acc_num.getText().toString() + "/" + bir_num.getText().toString()+ "/" + edit1.getText().toString() + edit2.getText().toString() + edit3.getText().toString() + edit4.getText().toString();

                    //progress bar 실행
                    dig.show();
                    
                    //서버 통신
                    if(connect==null)
                        connect = new Connect();
                    Log.e("connect check","연결되어 데이터를 보냅니다. : "+send_text);
                    connect.execute(send_text);
                } else {
                    Log.e("edit4의 길이",Integer.toString(edit4.length()));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        //로딩부분 구현 (PS. 안드로이드 정책상 8.0이상부터 앱은 사용자와 계속 상호작용해야함. 따라서, 바탕을 연속 클릭시 나가짐.
        dig = new AlertDialog.Builder(this).create();
        LayoutInflater factory = LayoutInflater.from(this);
        final View customView = factory.inflate(R.layout.loading, null);

        //Gif 이미지 처리부분
        final ImageView imgView = customView.findViewById(R.id.ivloading);
        Glide.with(this).load(R.raw.loading).into(imgView);

        //바탕 투명하게 설정
        dig.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dig.setView(customView);
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
            Log.e("input return","입력 다 받아 return");
            return null;
        }

        protected void onProgressUpdate(String... params){ //서버에서 받은 데이터를 textview로 보여줌
            //output += params[0];
            //sc_tv1.append("보낸 메세지: " + output_message + "\n받은 메세지: " + output);
            Log.e("CSOS",params[0]); //params[0]이 받은 값

            if(params[0].equals("t")) { //실패한 경우
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"없는 정보입니다.\n 다시 작성해주세요.",Toast.LENGTH_LONG).show();
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
                startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                dig.dismiss();
            } else {  //성공한 경우
                register_sp.setPassword(getApplicationContext(),"password",edit1.getText().toString()+edit2.getText().toString()+edit3.getText().toString()+edit4.getText().toString());
                Toast.makeText(getApplicationContext(),"가입에 성공했습니다.\n 핀번호로 로그인해주세요.",Toast.LENGTH_LONG).show();
                finish();
                dig.dismiss();
                startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}