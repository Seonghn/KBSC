package com.example.kbkbkb;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerCommunicationActivity extends AppCompatActivity {
    private LinearLayout Lscreen;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.server_communication);//화면에 보여질 뷰 설정

        //view 연결 코드
        sc_tv1 = (TextView)findViewById(R.id.sc_tv);
        sc_btn1 = (Button)findViewById(R.id.server_send_btn);
        sc_account_et = (EditText)findViewById(R.id.account_et);
        sc_birth_et = (EditText)findViewById(R.id.birth_et);
        sc_passwd_et = (EditText)findViewById(R.id.passwd_et);

        //edittext 자연스러운 키보드 숨김 코드 (키보드가 아닌 바깥쪽 터치시 키보드가 사라짐)
        Lscreen = (LinearLayout)findViewById(R.id.server_communication_linear); //가장 상위 Linearlayout 가져옴

        Lscreen.setOnClickListener(new View.OnClickListener() { //상위 Linearlayout click 시 키보드 사라짐
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //키보드 관련 method
                if (getCurrentFocus() instanceof EditText) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0); //EditText가 아니면 키보드 숨김
                    getCurrentFocus().clearFocus();
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
                    Toast.makeText(ServerCommunicationActivity.this,"빈칸 없이 입력해주세요.",Toast.LENGTH_LONG).show();
                } else {
                    //edittext 초기화
                    sc_account_et.setText("");
                    sc_birth_et.setText("");
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
        dig = new AlertDialog.Builder(ServerCommunicationActivity.this).create();
        LayoutInflater factory = LayoutInflater.from(ServerCommunicationActivity.this);
        final View customView = factory.inflate(R.layout.loading, null);

        //Gif 이미지 처리부분
        final ImageView imgView = customView.findViewById(R.id.ivloading);
        Glide.with(ServerCommunicationActivity.this).load(R.raw.loading2).into(imgView);

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

        //edit text 입력검사 필요
        //비밀번호 보안 관련 작업 필요
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
