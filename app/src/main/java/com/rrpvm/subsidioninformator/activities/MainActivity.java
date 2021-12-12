package com.rrpvm.subsidioninformator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.databinding.ActivityMainBinding;
import com.rrpvm.subsidioninformator.handlers.AuthorizationHandler;
import com.rrpvm.subsidioninformator.objects.RecivierFilter;
import com.rrpvm.subsidioninformator.handlers.RecivierSubsidionHandler;
import com.rrpvm.subsidioninformator.objects.User;

import java.io.FileInputStream;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private void configSideMenu() {
        setSupportActionBar(toolbar); // устанавливаем тулбар как экшн бар
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawerLayoutMenu, this.toolbar, 0, 0);//вспомогательная кнопка для навбара
        drawerLayoutMenu.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //end binding
        //you can remove these:
        this.addRecivierButton = binding.mainFabAddRecivier;
        this.subsidionRecivierList = binding.receivirsList;
        this.drawerLayoutMenu = binding.drawerLayout;
        this.toolbar = binding.toolbar;
        //stop delete
        this.setupDataJSON();
        RecivierSubsidionHandler recivierSubsidionHandler = RecivierSubsidionHandler.getInstance();
        recivierSubsidionHandler.bindDataToView(this, R.layout.subsidion_recivier_item);//create adapter for listview
        this.subsidionRecivierList.setAdapter(recivierSubsidionHandler.getAdapter());//set adapter
        configSideMenu();//sidebar setup
        NavigationView view = binding.navigationView;
        this.setupNavigationViewItems(view.getMenu());
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return onNavigationViewItemSelected(item);
            }
        });
        this.addRecivierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddRecivierButtonClick();
            }
        });
        if (AuthorizationHandler.getInstance().getUserSession().getUserType() == User.UserType.C_USER) {
            this.addRecivierButton.setVisibility(View.INVISIBLE);
        }
        recivierSubsidionHandler.sortData();
    }

    /*nav_header_menu method*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header_menu, menu);
        RecivierSubsidionHandler recivierSubsidionHandler = RecivierSubsidionHandler.getInstance();
        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty())
                    recivierSubsidionHandler.getSimpleFilter().getStringFilter().state = RecivierFilter.statement.CLEAR;
                else
                    recivierSubsidionHandler.getSimpleFilter().getStringFilter().state = RecivierFilter.statement.WORK;

                recivierSubsidionHandler.getSimpleFilter().getStringFilter().object = s.toLowerCase(Locale.ROOT);//update the filter
                recivierSubsidionHandler.filter();//do filtering
                // updateCounters();
                return true;
            }
        });
        return true;
    }

    /*nav_header_menu method*/
    @Override
    public boolean onOptionsItemSelected(MenuItem menu_item) {
        RecivierSubsidionHandler recivierSubsidionHandler = RecivierSubsidionHandler.getInstance();
        int id = menu_item.getItemId();
        switch (id) {
            case R.id.app_bar_sort: {
                recivierSubsidionHandler.setaZSortMode(!recivierSubsidionHandler.isaZSortMode());
                recivierSubsidionHandler.sortData();
                break;
            }
            default: {
                break;
            }
        }
        return super.onOptionsItemSelected(menu_item);
    }

    private void setupNavigationViewItems(Menu menu) {
        RecivierSubsidionHandler recivierSubsidionHandler = RecivierSubsidionHandler.getInstance();
        MenuItem city_selector = menu.findItem(R.id.menu_city_selector);
        MenuItem region_selector = menu.findItem(R.id.menu_region_selector);
        MenuItem year_selector = menu.findItem(R.id.menu_birthdate_fill_year);
        MenuItem month_selector = menu.findItem(R.id.menu_birthdate_fill_month);
        TextInputLayout til_city_selector = (TextInputLayout) city_selector.getActionView();
        TextInputLayout til_region_selector = (TextInputLayout) region_selector.getActionView();
        TextInputLayout til_year_selector = (TextInputLayout) year_selector.getActionView();
        TextInputLayout til_month_selector = (TextInputLayout) month_selector.getActionView();
        AutoCompleteTextView view_month_selector = til_month_selector.findViewById(R.id.menu_birthdate_month_autocomplete);
        til_region_selector.setHint(R.string.hint_region_name);
        til_city_selector.setHint(R.string.hint_city_name);
        til_year_selector.setHint(R.string.navigation_select_year);
        view_month_selector.setHint(R.string.navigation_select_month);
        til_year_selector.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.nav_menu_month_select, android.R.layout.simple_dropdown_item_1line);
        view_month_selector.setAdapter(adapter);//set data to spinner
        til_city_selector.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String _str = charSequence.toString().trim();
                if (_str.length() > 0) {
                    recivierSubsidionHandler.getSimpleFilter().getCityFilter().object = _str;
                    recivierSubsidionHandler.getSimpleFilter().getCityFilter().state = RecivierFilter.statement.WORK;
                } else
                    recivierSubsidionHandler.getSimpleFilter().getCityFilter().state = RecivierFilter.statement.CLEAR;
                recivierSubsidionHandler.filter();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        til_region_selector.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String _str = charSequence.toString().trim();
                if (_str.length() > 0) {
                    recivierSubsidionHandler.getSimpleFilter().getOblastFilter().object = _str;
                    recivierSubsidionHandler.getSimpleFilter().getOblastFilter().state = RecivierFilter.statement.WORK;
                } else
                    recivierSubsidionHandler.getSimpleFilter().getOblastFilter().state = RecivierFilter.statement.CLEAR;
                recivierSubsidionHandler.filter();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        til_year_selector.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String _str = charSequence.toString().trim();
                if (_str.length() > 9) {
                    _str = _str.substring(0, 9);
                    til_year_selector.getEditText().setText(_str);
                }
                if (_str.length() > 0) {
                    recivierSubsidionHandler.getSimpleFilter().getBirth_year().object = Integer.parseInt(_str);
                    recivierSubsidionHandler.getSimpleFilter().getBirth_year().state = RecivierFilter.statement.WORK;
                } else
                    recivierSubsidionHandler.getSimpleFilter().getBirth_year().state = RecivierFilter.statement.CLEAR;
                recivierSubsidionHandler.filter();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        view_month_selector.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String[] data = charSequence.toString().trim().toLowerCase(Locale.ROOT).split(",");
                String[] allMonths = getResources().getStringArray(R.array.nav_menu_month_select);
                String result = new String();
                if (data.length > 0) {
                    for (String substr : data) {
                        for (int x = 1; x < allMonths.length; x++) {
                            if (allMonths[x].contains(substr)) {
                                result += x - 1 + ",";
                            }
                        }
                    }
                    recivierSubsidionHandler.getSimpleFilter().getBirth_month().object = result;
                    recivierSubsidionHandler.getSimpleFilter().getBirth_month().state = RecivierFilter.statement.WORK;
                } else
                    recivierSubsidionHandler.getSimpleFilter().getBirth_month().state = RecivierFilter.statement.CLEAR;
                recivierSubsidionHandler.filter();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private boolean onNavigationViewItemSelected(@NonNull MenuItem item) {
        RecivierSubsidionHandler recivierSubsidionHandler = RecivierSubsidionHandler.getInstance();
        switch (item.getItemId()) {
            case R.id.menu_switch_men: {
                item.setChecked(!item.isChecked());
                recivierSubsidionHandler.getSimpleFilter().getGenderFilter().object[0] = item.isChecked();
                break;
            }
            case R.id.menu_switch_women: {
                item.setChecked(!item.isChecked());
                recivierSubsidionHandler.getSimpleFilter().getGenderFilter().object[1] = item.isChecked();
                break;
            }
            case R.id.logout_button: {
                AuthorizationHandler.getInstance().logOut();
                Intent loginActivityI = new Intent(MainActivity.this, LoginFormActivity.class);
                startActivity(loginActivityI);
                finish();
                break;
            }
            default:
                break;
        }
        recivierSubsidionHandler.filter();
        return true;
    }

    private void setupDataJSON() {
        RecivierSubsidionHandler recivierSubsidionHandler = RecivierSubsidionHandler.getInstance();
        try {
            FileInputStream fis = openFileInput(RecivierSubsidionHandler.RECIVIERS_DATA_FILENAME);
            fis.close();
        } catch (Exception e) {//если файла не было - создаем с данными
            recivierSubsidionHandler.bindContext(this);
            recivierSubsidionHandler.debugGenerateData();
            recivierSubsidionHandler.exportToJSON(this);
        }
        recivierSubsidionHandler.importFromJSON(this);
    }

    private void onAddRecivierButtonClick() {
        Intent activity_message = new Intent(MainActivity.this, EditRecivierDataActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EditRecivierDataActivity.bundleArgumentMode, EditRecivierDataActivity.EDIT_MODE.CREATE_NEW_USER.getValue());
        activity_message.putExtras(bundle);
        startActivity(activity_message);
        //dont finish this activity!! (we dont create new MainActivity in EditRecivierDataActivity, we only finish them);
    }

    //app_objects_end
    //android_objects_start
    private DrawerLayout drawerLayoutMenu;//get's from binding
    private Toolbar toolbar;//get's from binding
    private ListView subsidionRecivierList;//get's from binding
    private SearchView searchView;//get's from binding
    private FloatingActionButton addRecivierButton;//get's from binding

    private ActivityMainBinding binding;//gradle->viewBinding
    //android_objects_end
}
