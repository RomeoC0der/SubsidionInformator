package com.rrpvm.subsidioninformator.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.handlers.RecivierSubsidionHandler;
import com.rrpvm.subsidioninformator.interfaces.Redirectable;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;

public class EditRecivierDataActivity extends AppCompatActivity implements Redirectable {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recivier_data);
        context = this;
        initViews();
        Bundle extras = getIntent().getExtras();//get from intent bundle
        if (extras != null) {
            currentSubsidionRecivier = (SubsidingRecivier) extras.getSerializable("recivier_data");
        }
        fillViewWithData();
        setTitle(currentSubsidionRecivier.getPIB());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);//back button
        actionBar.setDisplayHomeAsUpEnabled(true);//display back button
        if (currentSubsidionRecivier.isMale()) {
            genderSwitch.check(R.id.edit_rg_male);
        } else genderSwitch.check(R.id.edit_rg_female);
        deleteActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialogFragment = new AlertDialog.Builder(context).setTitle("delete?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteRecivier();
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();
                dialogFragment.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_recivier_menu, menu);
        MenuItem sendSavesButton = menu.findItem(R.id.edit_save_button);
        sendSavesButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                saveChanges();
                return true;
            }
        });
        return true;
    }

    private void saveChanges() {
        RecivierSubsidionHandler recivierSubsidionHandler = RecivierSubsidionHandler.getInstance();//singleton в деле
        int position = RecivierSubsidionHandler.getInstance().getIdInPureData(currentSubsidionRecivier);
        if (position == recivierSubsidionHandler.getPureData().size()) return;//not found

        SubsidingRecivier foundedRecivier = recivierSubsidionHandler.getPureData().get(position);
        foundedRecivier.setSNP(textInputLayoutName.getEditText().getText().toString());
        foundedRecivier.setITN(editTIN.getEditText().getText().toString());
        foundedRecivier.getSubsidionData().setId(Integer.parseInt(editSubsidionID.getEditText().getText().toString()));
        foundedRecivier.setPassportId(editPassportID.getEditText().getText().toString());
        foundedRecivier.setRegion(editRegion.getEditText().getText().toString());
        foundedRecivier.setCity(editCity.getEditText().getText().toString());
        foundedRecivier.getSubsidionData().setJKP(Double.parseDouble(editJKPSize.getEditText().getText().toString()));
        foundedRecivier.getSubsidionData().setCGTP(Double.parseDouble(editCGTPSize.getEditText().getText().toString()));
        foundedRecivier.setMale(genderSwitch.getCheckedRadioButtonId() == R.id.edit_rg_male);
        recivierSubsidionHandler.getAdapter().notifyDataSetChanged();
        recivierSubsidionHandler.exportToJSON(this);//save todo:check if this shit->delete
        Toast.makeText(this, "успiх", Toast.LENGTH_LONG).show();
        redirect();
    }

    private void deleteRecivier() {
        RecivierSubsidionHandler recivierSubsidionHandler = RecivierSubsidionHandler.getInstance();//singleton в деле
        int position = RecivierSubsidionHandler.getInstance().getIdInPureData(currentSubsidionRecivier);
        if (position == recivierSubsidionHandler.getPureData().size()) return;//not found
        recivierSubsidionHandler.getPureData().remove(position);
        recivierSubsidionHandler.getDataList().remove(currentSubsidionRecivier);
        recivierSubsidionHandler.getAdapter().notifyDataSetChanged();
        Toast.makeText(context, "видалено", Toast.LENGTH_LONG).show();
        redirect();
    }
    private void initViews() {
        textInputLayoutName = (TextInputLayout) findViewById(R.id.edit_pib);
        editTIN = (TextInputLayout) findViewById(R.id.edit_TIN);
        editSubsidionID = (TextInputLayout) findViewById(R.id.edit_snID);
        editPassportID = (TextInputLayout) findViewById(R.id.edit_passID);
        editJKPSize = (TextInputLayout) findViewById(R.id.edit_JKPSize);
        editCGTPSize = (TextInputLayout) findViewById(R.id.edit_CGTPSize);
        genderSwitch = (RadioGroup) findViewById(R.id.edit_genderSwitch);
        editCity = (TextInputLayout) findViewById(R.id.edit_city);
        editRegion = (TextInputLayout) findViewById(R.id.edit_region);
        deleteActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonDeleteRecivier);
    }

    private void fillViewWithData() {
        textInputLayoutName.getEditText().setText(currentSubsidionRecivier.getPIB());
        editTIN.getEditText().setText(currentSubsidionRecivier.getITN());
        editPassportID.getEditText().setText(currentSubsidionRecivier.getPassportId());
        editCity.getEditText().setText(currentSubsidionRecivier.getCity());
        editRegion.getEditText().setText(currentSubsidionRecivier.getRegion());
        editSubsidionID.getEditText().setText(Integer.toString(currentSubsidionRecivier.getSubsidionData().getId()));
        editJKPSize.getEditText().setText(Double.toString(currentSubsidionRecivier.getSubsidionData().getJKP()));
        editCGTPSize.getEditText().setText(Double.toString(currentSubsidionRecivier.getSubsidionData().getCGTP()));
    }

    @Override
    public void redirect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish(); //->this works fine
                //  EditRecivierDataActivity.super.onBackPressed();//finish activity
            }
        }.start();
    }

    /*Android objects:*/
    private TextInputLayout textInputLayoutName;
    private TextInputLayout editRegion;
    private TextInputLayout editCity;
    private TextInputLayout editTIN;
    private TextInputLayout editPassportID;
    private TextInputLayout editSubsidionID;
    private TextInputLayout editJKPSize;
    private TextInputLayout editCGTPSize;
    private RadioGroup genderSwitch;//а чё, звучит мемно
    private FloatingActionButton deleteActionButton;
    /*section_end*/
    private SubsidingRecivier currentSubsidionRecivier;
    private Context context;


}