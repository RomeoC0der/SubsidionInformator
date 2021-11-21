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
    //Android objects:
    private TextInputLayout loginForm;
    private TextInputLayout passwordForm;
    private Button singInBtn;
    private Context context;//sometimes needed
    //end section;
    public final static int REDIRECT_DELAY = 150;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        context = this;
       // debugFillUsers();//!release only -> provides creating data and export to json                                      IF DATA EMPTY = UNCOMMENT THIS
        AuthorizationHandler authorizationHandler = AuthorizationHandler.getInstance();//хто був ответственный за авторизацию?
        authorizationHandler.importFromJSON(context);//take data of users(release)
        if(authorizationHandler.getUserSession().calculateSessionStatement()){   //did we import current statement?(return true if current)
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
                    loginForm.setError("login must be longer than 4 symbols");//todo: string resource
                    passwordForm.setError("password must be longer than 4 symbols");//todo:string resource
                    loginForm.setErrorEnabled(true);
                    passwordForm.setErrorEnabled(true);
                    return;
                }
                if (authorizationHandler.signIn(login, password)) {
                    passwordForm.getEditText().setText("");//reset
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
                startActivity(redirectActivity);
                finish();
            }
        }.start();

    }
    private void debugFillUsers(){
        AuthorizationHandler authorizationHandler = AuthorizationHandler.getInstance();//singleton
        authorizationHandler.getAuthorizationData().add(new User("klimenko391", "qwerty", "Sancho13"));
        authorizationHandler.getAuthorizationData().add(new User("jukov31", "zxcvbn", "ZxcKiller"));
        //try:
        authorizationHandler.getUserSession().importFromJSON(this);
        authorizationHandler.exportToJSON(this);//save data
    }
}