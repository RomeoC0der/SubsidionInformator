package com.rrpvm.subsidioninformator.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.fragments.EditElementDialog;
import com.rrpvm.subsidioninformator.handlers.RecivierSubsidionHandler;
import com.rrpvm.subsidioninformator.interfaces.Redirectable;
import com.rrpvm.subsidioninformator.objects.BitmapWrapper;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;
import com.rrpvm.subsidioninformator.utilities.Utilities;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditRecivierDataActivity extends AppCompatActivity implements Redirectable {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recivier_data);
        context = this;
        initViews();
        bindListeners();
        Bundle extras = null;
        try {
            extras = getIntent().getExtras();//get from intent bundle
        } catch (NullPointerException e) {
            redirect();
        }
        this.editMode = extras.getInt(EditRecivierDataActivity.bundleArgumentMode);
        if (this.editMode == EDIT_MODE.EDIT_EXIST_USER.getValue()) {
            currentSubsidionRecivier = (SubsidingRecivier) extras.getSerializable("recivier_data");
            fillViewWithData();
            setTitle(currentSubsidionRecivier.getPIB());
            if (currentSubsidionRecivier.isMale()) {
                genderSwitch.check(R.id.edit_rg_male);
            } else genderSwitch.check(R.id.edit_rg_female);
        } else {
            setTitle(getString(R.string.title_create_mode));
            deleteActionButton.setVisibility(View.INVISIBLE);
            currentSubsidionRecivier = new SubsidingRecivier();
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);//back button
        actionBar.setDisplayHomeAsUpEnabled(true);//display back button
        extras.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_recivier_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                redirect();
                return true;
            }
            case R.id.edit_save_button: {
                saveChanges();
                return true;
            }
        }
        return true;
    }

    private ArrayList<TextInputLayout> inputValidation() {
        ArrayList<TextInputLayout> invalid_list = new ArrayList<>();
        if (textInputLayoutName.getEditText().getText().toString().trim().isEmpty())
            invalid_list.add(textInputLayoutName);
        if (editRegion.getEditText().getText().toString().trim().isEmpty())
            invalid_list.add(editRegion);
        if (editCity.getEditText().getText().toString().trim().isEmpty())
            invalid_list.add(editCity);
        if (editTIN.getEditText().getText().toString().trim().isEmpty())
            invalid_list.add(editTIN);
        if (editPassportID.getEditText().getText().toString().trim().isEmpty())
            invalid_list.add(editPassportID);
        if (editSubsidionID.getEditText().getText().toString().trim().isEmpty())
            invalid_list.add(editSubsidionID);
        if (editBirthdate.getEditText().getText().toString().trim().isEmpty())
            invalid_list.add(editBirthdate);
        if (editJKPSize.getEditText().getText().toString().trim().isEmpty())
            invalid_list.add(editJKPSize);
        if (editCGTPSize.getEditText().getText().toString().trim().isEmpty())
            invalid_list.add(editCGTPSize);
        if (editArrived.getEditText().getText().toString().trim().isEmpty())
            invalid_list.add(editArrived);
        if (editTaken.getEditText().getText().toString().trim().isEmpty())
            invalid_list.add(editTaken);
        if (editPosition.getEditText().getText().toString().trim().isEmpty())
            invalid_list.add(editPosition);
        return invalid_list;
    }

    private void saveChanges() {
        ArrayList<TextInputLayout> invalids = inputValidation();
        if (!invalids.isEmpty()) {
            EditElementDialog dialog = new EditElementDialog();
            dialog.show(getFragmentManager(), "custom");
            for (TextInputLayout textInputLayout : invalids) {
                textInputLayout.setError(getString(R.string.error_save_changes_failure));
                textInputLayout.setErrorEnabled(true);
                textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        textInputLayout.setErrorEnabled(false);
                        textInputLayout.getEditText().removeTextChangedListener(this);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                if (!textInputLayout.equals(editArrived) && !textInputLayout.equals(editTaken) && !textInputLayout.equals(editBirthdate))//на них уже есть свои листенеры
                    textInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            textInputLayout.setErrorEnabled(false);
                        }
                    });
            }
            return;
        }
        SubsidingRecivier foundedRecivier = null;
        RecivierSubsidionHandler recivierSubsidionHandler = RecivierSubsidionHandler.getInstance();//singleton в деле
        if (this.editMode == EDIT_MODE.EDIT_EXIST_USER.getValue()) {
            int position = RecivierSubsidionHandler.getInstance().getIdInPureData(currentSubsidionRecivier);
            if (position == recivierSubsidionHandler.getPureData().size()) return;//not found
            foundedRecivier = recivierSubsidionHandler.getPureData().get(position);
        } else {
            foundedRecivier = currentSubsidionRecivier;
        }
        try {
            //<SetDataSelection>
            foundedRecivier.setSNP(textInputLayoutName.getEditText().getText().toString());
            foundedRecivier.setITN(editTIN.getEditText().getText().toString());
            foundedRecivier.setPassportId(editPassportID.getEditText().getText().toString());
            foundedRecivier.setRegion(editRegion.getEditText().getText().toString());
            foundedRecivier.setCity(editCity.getEditText().getText().toString());
            foundedRecivier.setPosition(editPosition.getEditText().getText().toString());
            foundedRecivier.setMale(genderSwitch.getCheckedRadioButtonId() == R.id.edit_rg_male);
            foundedRecivier.setBirthdate(genericDateFormat.parse(editBirthdate.getEditText().getText().toString()));
            //<SetSubsidionDataSelection>
            foundedRecivier.getSubsidionData().setId(Integer.parseInt(editSubsidionID.getEditText().getText().toString()));
            foundedRecivier.getSubsidionData().setJKP(Double.parseDouble(editJKPSize.getEditText().getText().toString()));
            foundedRecivier.getSubsidionData().setCGTP(Double.parseDouble(editCGTPSize.getEditText().getText().toString()));
            foundedRecivier.getSubsidionData().setRecievRange(editArrived.getEditText().getText().toString());
            foundedRecivier.getSubsidionData().setGotRange(editTaken.getEditText().getText().toString());
            BitmapWrapper newBitmap = currentSubsidionRecivier.getImage();
            if (foundedRecivier.getImage() == null) {
                foundedRecivier.setImage(newBitmap);
            } else foundedRecivier.getImage().setBitmap(newBitmap.getBitmap());
        } catch (Exception parseExecption) {
            parseExecption.printStackTrace();
            return;
        }
        if (this.editMode == EDIT_MODE.CREATE_NEW_USER.getValue()) {
            recivierSubsidionHandler.getPureData().add(foundedRecivier);
            recivierSubsidionHandler.filter();
        }
        recivierSubsidionHandler.getAdapter().notifyDataSetChanged();
        recivierSubsidionHandler.exportToJSON(this);
        Toast.makeText(this, getText(R.string.toast_success), Toast.LENGTH_LONG).show();
        redirect();
    }

    private void deleteRecivier() {
        RecivierSubsidionHandler recivierSubsidionHandler = RecivierSubsidionHandler.getInstance();//singleton в деле
        int position = RecivierSubsidionHandler.getInstance().getIdInPureData(currentSubsidionRecivier);
        if (position == recivierSubsidionHandler.getPureData().size()) return;//not found
        recivierSubsidionHandler.getPureData().remove(position);
        recivierSubsidionHandler.getDataList().remove(currentSubsidionRecivier);
        recivierSubsidionHandler.getAdapter().notifyDataSetChanged();
        Toast.makeText(context, getText(R.string.toast_deleted), Toast.LENGTH_LONG).show();
        recivierSubsidionHandler.exportToJSON(this);
        redirect();
    }

    private void initViews() {
        textInputLayoutName = (TextInputLayout) findViewById(R.id.edit_pib);
        editTIN = (TextInputLayout) findViewById(R.id.edit_TIN);
        editSubsidionID = (TextInputLayout) findViewById(R.id.edit_snID);
        editPassportID = (TextInputLayout) findViewById(R.id.edit_passID);
        editJKPSize = (TextInputLayout) findViewById(R.id.edit_JKPSize);
        editCGTPSize = (TextInputLayout) findViewById(R.id.edit_CGTPSize);
        editBirthdate = (TextInputLayout) findViewById(R.id.edit_birthdate);
        genderSwitch = (RadioGroup) findViewById(R.id.edit_genderSwitch);
        editCity = (TextInputLayout) findViewById(R.id.edit_city);
        editRegion = (TextInputLayout) findViewById(R.id.edit_region);
        editArrived = (TextInputLayout) findViewById(R.id.edit_arrived);
        editTaken = (TextInputLayout) findViewById(R.id.edit_taken);
        editPosition = (TextInputLayout) findViewById(R.id.edit_street);
        deleteActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonDeleteRecivier);
        imagePreview = (ImageView) findViewById(R.id.edit_img_preview);
        buttonSetImage = (MaterialButton) findViewById(R.id.edit_img_select_button);
    }

    private void fillViewWithData() {
        editPassportID.getEditText().setText(currentSubsidionRecivier.getPassportId());
        editTIN.getEditText().setText(currentSubsidionRecivier.getITN());
        textInputLayoutName.getEditText().setText(currentSubsidionRecivier.getPIB());
        editRegion.getEditText().setText(currentSubsidionRecivier.getRegion());
        editCity.getEditText().setText(currentSubsidionRecivier.getCity());
        editPosition.getEditText().setText(currentSubsidionRecivier.getPosition());
        editBirthdate.getEditText().setText(genericDateFormat.format(currentSubsidionRecivier.getBirthdate()));

        editArrived.getEditText().setText(currentSubsidionRecivier.getSubsidionData().getRecievRange());
        editTaken.getEditText().setText(currentSubsidionRecivier.getSubsidionData().getGotRange());
        editSubsidionID.getEditText().setText(Integer.toString(currentSubsidionRecivier.getSubsidionData().getId()));
        editJKPSize.getEditText().setText(Double.toString(currentSubsidionRecivier.getSubsidionData().getJKP()));
        editCGTPSize.getEditText().setText(Double.toString(currentSubsidionRecivier.getSubsidionData().getCGTP()));
        Drawable imgToPresent = null;
        Bitmap bitmap = currentSubsidionRecivier.getImage().getBitmap();
        try {
            imagePreview.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            imagePreview.setImageResource(currentSubsidionRecivier.isMale() ? R.drawable.default_man_icon_foreground : R.drawable.default_women_icon_foreground);//default icon if we cant find normal image
        }
    }

    private void bindListeners() {

        editArrived.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker picker = MaterialDatePicker.Builder.dateRangePicker().setInputMode(MaterialDatePicker.INPUT_MODE_TEXT).setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).
                        setTitleText(getString(R.string.datepicker_range_take)).build();
                picker.show(getSupportFragmentManager(), editArrived.toString());
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Pair<Long, Long> pair = (Pair<Long, Long>) selection;
                        editArrived.getEditText().setText(String.format("%s - %s", genericDateFormat.format(pair.first), genericDateFormat.format(pair.second)));
                    }
                });
                if (editArrived.isErrorEnabled()) editArrived.setErrorEnabled(false);
            }
        });
        editTaken.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker picker = MaterialDatePicker.Builder.dateRangePicker().setInputMode(MaterialDatePicker.INPUT_MODE_TEXT).setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).
                        setTitleText(getString(R.string.datepicker_range_take)).build();
                picker.show(getSupportFragmentManager(), editArrived.toString());
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Pair<Long, Long> pair = (Pair<Long, Long>) selection;
                        editTaken.getEditText().setText(String.format("%s - %s", genericDateFormat.format(pair.first), genericDateFormat.format(pair.second)));
                    }
                });
                if (editArrived.isErrorEnabled()) editArrived.setErrorEnabled(false);
            }
        });
        deleteActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialogFragment = new AlertDialog.Builder(context).setTitle(getText(R.string.dialog_warning_title)).setPositiveButton(getText(R.string.dialog_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteRecivier();
                    }
                }).setNegativeButton(getText(R.string.dialog_denied), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setMessage("are you sure to delete this data?").create();
                dialogFragment.show();
            }
        });
        buttonSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, Pick_image);

            }
        });
        editBirthdate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker picker = MaterialDatePicker.Builder.datePicker().setInputMode(MaterialDatePicker.INPUT_MODE_TEXT).setSelection(MaterialDatePicker.thisMonthInUtcMilliseconds()).
                        setTitleText(getString(R.string.datepicker_range_take)).build();
                picker.show(getSupportFragmentManager(), editArrived.toString());
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Long value = (Long) selection;
                        editBirthdate.getEditText().setText(genericDateFormat.format(new Date(value)));
                    }
                });
                if (editArrived.isErrorEnabled()) editArrived.setErrorEnabled(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        currentSubsidionRecivier.setImage(new BitmapWrapper(selectedImage));
                        currentSubsidionRecivier.getImage().normalize();
                        imagePreview.setImageBitmap(currentSubsidionRecivier.getImage().getBitmap());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
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
                finish(); //->this works fine, мы не создаем новый объект мейн активити
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
    private TextInputLayout editBirthdate;
    private TextInputLayout editJKPSize;
    private TextInputLayout editCGTPSize;
    private TextInputLayout editArrived;
    private TextInputLayout editTaken;
    private TextInputLayout editPosition;//street
    private RadioGroup genderSwitch;//а чё, звучит мемно
    private FloatingActionButton deleteActionButton;
    private ImageView imagePreview;
    private MaterialButton buttonSetImage;
    /*section_end*/
    private SubsidingRecivier currentSubsidionRecivier;
    private Context context;
    private int editMode = 0;
    public static SimpleDateFormat genericDateFormat = new SimpleDateFormat("yyyy.MM.dd");

    public enum EDIT_MODE {
        CREATE_NEW_USER(0), EDIT_EXIST_USER(1);
        int mode;

        EDIT_MODE(int x) {
            this.mode = x;
        }

        public int getValue() {
            return mode;
        }
    }

    public static String bundleArgumentMode = "ARG_MODE";
    public final int Pick_image = 1;
}