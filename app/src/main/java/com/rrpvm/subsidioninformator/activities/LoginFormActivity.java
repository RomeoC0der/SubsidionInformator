package com.rrpvm.subsidioninformator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.handlers.AuthorizationHandler;
import com.rrpvm.subsidioninformator.objects.User;

import java.util.Locale;

public class LoginFormActivity extends AppCompatActivity {
    private AuthorizationHandler authorizationHandler;
    private EditText loginForm;
    private EditText passwordForm;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        authorizationHandler = new AuthorizationHandler();
        authorizationHandler.getAuth_data().add(new User("klimenko391", "qwerty","waffel"));
        authorizationHandler.getAuth_data().add(new User("jukov31", "zxcvbn","waffel"));
        authorizationHandler.exportData(this);
        ctx = this;
        authorizationHandler.importData(ctx);
        if(authorizationHandler.getSession().getSessionStatement()){
            redirect();
        }
        Button loginButton = findViewById(R.id.btn_login);
        loginForm = findViewById(R.id.login_form);
        passwordForm = findViewById(R.id.password_form);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginForm.getText().toString().toLowerCase(Locale.ROOT).trim();
                String password = passwordForm.getText().toString().trim();
                if(authorizationHandler.Authorize(login, password)){
                    passwordForm.setText("");//tak nado
                    //authorizationHandler.exportData(ctx);//update session file
                    redirect();
                }
                else{
                    passwordForm.setText("");
                    loginForm.setBackground(getResources().getDrawable(R.drawable.custom_edit_text));
                    passwordForm.setBackground(getResources().getDrawable(R.drawable.custom_edit_text));
                //    passwordForm.setBackground();
                }
            }
        });
    }
    private void redirect(){
    new Thread(){
        @Override
        public void run(){
            try{
                Thread.sleep(50);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            Intent redirectActivity = new Intent(LoginFormActivity.this, MainActivity.class);
            startActivity(redirectActivity);
            finish();
        }
    }.start();

    }
}