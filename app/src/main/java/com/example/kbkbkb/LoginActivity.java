package com.example.kbkbkb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edit1,edit2,edit3,edit4;

    //custom keyboard 관련 변수
    Button btn[] = new Button[12];
    int cnt = 0;
    int randnum[] = new int[10];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        edit1 = (EditText) findViewById(R.id.pin1);
        edit2 = (EditText) findViewById(R.id.pin2);
        edit3 = (EditText) findViewById(R.id.pin3);
        edit4 = (EditText) findViewById(R.id.pin4);

        //강제로 키보드 내리는 메소드
        edit1.setFocusableInTouchMode(false);
        edit2.setFocusableInTouchMode(false);
        edit3.setFocusableInTouchMode(false);
        edit4.setFocusableInTouchMode(false);

        /*edit1.requestFocus();
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
                if(edit4.length()==1){  // edit1  값의 제한값을 6이라고 가정했을때
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        }); */

        edit4.addTextChangedListener(new TextWatcher() {
             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if (edit4.length() == 1) {  // edit1  값의 제한값을 6이라고 가정했을때
                     startActivity(new Intent(getApplicationContext(), MainActivity.class));
                 }
             }

             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

             @Override
             public void afterTextChanged(Editable s) {
             }
         });

        //custom keyboard
        btn[0] = (Button)findViewById(R.id.btn1);
        btn[1] = (Button)findViewById(R.id.btn2);
        btn[2] = (Button)findViewById(R.id.btn3);
        btn[3] = (Button)findViewById(R.id.btn4);
        btn[4] = (Button)findViewById(R.id.btn5);
        btn[5] = (Button)findViewById(R.id.btn6);
        btn[6] = (Button)findViewById(R.id.btn7);
        btn[7] = (Button)findViewById(R.id.btn8);
        btn[8] = (Button)findViewById(R.id.btn9);
        btn[9] = (Button)findViewById(R.id.btn10);
        btn[10] = (Button)findViewById(R.id.btn11);
        btn[11] = (Button)findViewById(R.id.btn12);

        Randnum();

        btn[0].setText(Integer.toString(randnum[0]));
        btn[1].setText(Integer.toString(randnum[1]));
        btn[2].setText(Integer.toString(randnum[2]));
        btn[3].setText(Integer.toString(randnum[3]));
        btn[4].setText(Integer.toString(randnum[4]));
        btn[5].setText(Integer.toString(randnum[5]));
        btn[6].setText(Integer.toString(randnum[6]));
        btn[7].setText(Integer.toString(randnum[7]));
        btn[8].setText(Integer.toString(randnum[8]));
        btn[10].setText(Integer.toString(randnum[9]));

        for(int i=0; i<12; i++) {
            btn[i].setOnClickListener(this);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                addtoarray(Integer.toString(randnum[0]));
                break;
            case R.id.btn2:
                addtoarray(Integer.toString(randnum[1]));
                break;
            case R.id.btn3:
                addtoarray(Integer.toString(randnum[2]));
                break;
            case R.id.btn4:
                addtoarray(Integer.toString(randnum[3]));
                break;
            case R.id.btn5:
                addtoarray(Integer.toString(randnum[4]));
                break;
            case R.id.btn6:
                addtoarray(Integer.toString(randnum[5]));
                break;
            case R.id.btn7:
                addtoarray(Integer.toString(randnum[6]));
                break;
            case R.id.btn8:
                addtoarray(Integer.toString(randnum[7]));
                break;
            case R.id.btn9:
                addtoarray(Integer.toString(randnum[8]));
                break;
            case R.id.btn11:
                addtoarray(Integer.toString(randnum[9]));
                break;
            case R.id.btn12:
                backarray();
                break;
        }
    }

    //keyboard 입력값 저장
    public void addtoarray(String numbers) {
        if(cnt == 0) {
            edit1.setText(numbers);
        } else if(cnt == 1) {
            edit2.setText(numbers);
        } else if(cnt == 2) {
            edit3.setText(numbers);
        } else {
            edit4.setText(numbers);
        }

        cnt++;
    }

    //del 키 알고리즘
    public void backarray() {
        if(cnt==0) {

        } else if(cnt==1) {
            cnt--;
            edit1.setText(null);
        } else if(cnt==2) {
            cnt--;
            edit2.setText(null);
        } else if(cnt==3) {
            cnt--;
            edit3.setText(null);
        }
    }

    //랜덤 수 생성
    public void Randnum(){
        Random r = new Random();
        for(int i=0; i<10; i++) {
            randnum[i] = r.nextInt(10);
            for(int j=0;j<i;j++) {
                if(randnum[i]==randnum[j]) i--;
            }
        }
    }
}
