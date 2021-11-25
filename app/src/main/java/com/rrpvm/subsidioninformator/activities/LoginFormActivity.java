package com.rrpvm.subsidioninformator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.handlers.AuthorizationHandler;
import com.rrpvm.subsidioninformator.interfaces.Redirectable;
import com.rrpvm.subsidioninformator.objects.User;

import java.util.Locale;

//почти финальный класс
public class LoginFormActivity extends AppCompatActivity implements Redirectable {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        context = this;
       if(MainActivity.DEBUG_RECREATE) debugFillUsers();//!release only -> provides creating data and export to json                                      IF DATA EMPTY = UNCOMMENT THIS
        AuthorizationHandler authorizationHandler = AuthorizationHandler.getInstance();//хто був ответственный за авторизацию?
        authorizationHandler.importFromJSON(context);//take data of users(release)
        if (authorizationHandler.getUserSession().calculateSessionStatement()) {   //did we import current statement?(return true if current)
            redirect();
        }
        /*Android only*/
        singInBtn = findViewById(R.id.btn_signIn);
        loginForm = findViewById(R.id.login_form);
        passwordForm = findViewById(R.id.password_form);
        singInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginForm.getEditText().getText().toString().toLowerCase(Locale.ROOT).trim();
                String password = passwordForm.getEditText().getText().toString().trim();
                if (login.length() < 4 || password.length() < 4) {
                    loginForm.setError(getText(R.string.error_characters_too_small));
                    passwordForm.setError(getText(R.string.error_characters_too_small));
                    loginForm.setErrorEnabled(true);
                    passwordForm.setErrorEnabled(true);
                    return;
                }
                if (authorizationHandler.signIn(login, password)) {
                    passwordForm.getEditText().setText(new String());//reset
                    redirect();
                } else {
                    loginForm.setError(getText(R.string.error_data_incorrect));
                    passwordForm.setError(getText(R.string.error_data_incorrect));
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

    @Override
    public void redirect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(REDIRECT_DELAY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent redirectActivity = new Intent(LoginFormActivity.this, MainActivity.class);
                redirectActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(redirectActivity);
                finish();
            }
        }.start();

    }
    private void debugFillUsers() {
        AuthorizationHandler authorizationHandler = AuthorizationHandler.getInstance();//singleton
        authorizationHandler.getAuthorizationData().add(new User("qwerty", "qwerty", "Administator", User.UserType.C_ADMIN));
        authorizationHandler.getAuthorizationData().add(new User("jukov31", "zxcqwe", "ZxcKiller", User.UserType.C_USER));
        //try:
        authorizationHandler.getUserSession().importFromJSON(this);
        authorizationHandler.exportToJSON(this);//save data
    }

    //Android objects:
    private TextInputLayout loginForm;
    private TextInputLayout passwordForm;
    private Button singInBtn;
    private Context context;//sometimes needed
    //end section;
    public final static int REDIRECT_DELAY = 150;
}