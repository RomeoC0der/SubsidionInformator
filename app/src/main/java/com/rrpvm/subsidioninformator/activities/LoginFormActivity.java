package com.rrpvm.subsidioninformator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.handlers.AuthorizationHandler;
import com.rrpvm.subsidioninformator.objects.User;

import java.util.Locale;

public class LoginFormActivity extends AppCompatActivity {
    private AuthorizationHandler authorizationHandler;
    private TextInputLayout loginForm;
    private TextInputLayout passwordForm;
    private Context ctx;
    private Button singInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        authorizationHandler = new AuthorizationHandler();//хто був ответственный за авторизацию?
        //  authorizationHandler.getAuth_data().add(new User("klimenko391", "qwerty","waffel"));
        // authorizationHandler.getAuth_data().add(new User("jukov31", "zxcvbn","waffel"));
        //authorizationHandler.exportData(this);
        authorizationHandler.importData(this);//RELEASE MODE
        ctx = this;
        if (authorizationHandler.getSession().getSessionStatement()) {
            redirect();
        }
        singInBtn = findViewById(R.id.btn_signIn);
        loginForm = findViewById(R.id.login_form);
        passwordForm = findViewById(R.id.password_form);
        singInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginForm.getEditText().getText().toString().toLowerCase(Locale.ROOT).trim();
                String password = passwordForm.getEditText().getText().toString().trim();
                if (authorizationHandler.Authorize(login, password)) {
                    passwordForm.getEditText().setText("");//tak nado
                    //authorizationHandler.exportData(ctx);//update session file
                    redirect();
                } else {
                    // passwordForm.getEditText().setText("");//tak nado
                    loginForm.setError("login or password incorrect");
                    passwordForm.setError("login or password incorrect");
                    loginForm.setErrorEnabled(true);
                    passwordForm.setErrorEnabled(true);
                }
            }
        });
    }

    private void redirect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent redirectActivity = new Intent(LoginFormActivity.this, MainActivity.class);
                startActivity(redirectActivity);
                finish();
            }
        }.start();

    }
}