package com.rrpvm.subsidioninformator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.rrpvm.subsidioninformator.R;

import java.util.Date;

public class LoaderActivity extends AppCompatActivity {
    private final int LOADER_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(LOADER_DELAY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(LoaderActivity.this, LoginFormActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}