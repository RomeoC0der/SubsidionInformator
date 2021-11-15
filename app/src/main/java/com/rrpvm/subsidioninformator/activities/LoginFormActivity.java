package com.rrpvm.subsidioninformator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
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
          authorizationHandler.getAuth_data().add(new User("klimenko391", "qwerty","waffel"));
         authorizationHandler.getAuth_data().add(new User("jukov31", "zxcvbn","waffel"));
        authorizationHandler.exportData(this);
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
                if (login.length() < 4 || password.length() < 4) {
                    loginForm.setError("login must be longer than 4 symbols");//заранее
                    passwordForm.setError("password must be longer than 4 symbols");//заранее
                    loginForm.setErrorEnabled(true);
                    passwordForm.setErrorEnabled(true);
                    return;
                }
                if (authorizationHandler.Authorize(login, password)) {
                    passwordForm.getEditText().setText("");//tak nado
                    //authorizationHandler.exportData(ctx);//update session file
                    redirect();
                } else {
                    loginForm.setError("login or password incorrect");//заранее
                    passwordForm.setError("login or password incorrect");//заранее
                    loginForm.setErrorEnabled(true);
                    passwordForm.setErrorEnabled(true);
                }
            }
        });
        loginForm.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginForm.setErrorEnabled(false);
            }
        });
        loginForm.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginForm.setErrorEnabled(false);
                passwordForm.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passwordForm.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordForm.setErrorEnabled(false);
            }
        });
        passwordForm.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginForm.setErrorEnabled(false);
                passwordForm.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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