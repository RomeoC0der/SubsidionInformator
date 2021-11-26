package com.rrpvm.subsidioninformator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.interfaces.Redirectable;

import java.lang.ref.WeakReference;
import java.util.Date;

public class LoaderActivity extends AppCompatActivity implements Redirectable {
    private final int LOADER_DELAY = 1000;
    private final int EXTERNAL_STORAGE_READ_PERMISSION_CODE = 23;
    private final int EXTERNAL_STORAGE_WRITE_PERMISSION_CODE = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        if (!checkIfAlreadyGrantedPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_READ_PERMISSION_CODE);
        } else redirect();
    }

    @Override
    public void redirect() {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case EXTERNAL_STORAGE_READ_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted read", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case EXTERNAL_STORAGE_WRITE_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted write", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied to write your External storage", Toast.LENGTH_SHORT).show();
                }
                redirect();
                break;
            }
        }
    }

    private boolean checkIfAlreadyGrantedPermission(final String permissionName) {
        int result = ContextCompat.checkSelfPermission(this, permissionName);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}