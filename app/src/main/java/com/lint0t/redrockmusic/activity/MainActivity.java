package com.lint0t.redrockmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lint0t.redrockmusic.R;

public class MainActivity extends AppCompatActivity {

    private Button mbtn_login;
    private EditText medt_name,medt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mbtn_login = findViewById(R.id.btn_login);
        medt_name = findViewById(R.id.et_user_name);
        medt_password = findViewById(R.id.et_password);
        setListeners();

    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mbtn_login.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_login: {
                    if (!medt_name.getText().toString().equals("") && !medt_password.getText().toString().equals("") ) {
                        intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }else {
                        Toast.makeText(MainActivity.this,"请输入用户名或密码",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }
}
