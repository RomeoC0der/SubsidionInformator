package com.rrpvm.subsidioninformator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.handlers.AuthorizationHandler;
import com.rrpvm.subsidioninformator.objects.RecivierFilter;
import com.rrpvm.subsidioninformator.handlers.RecivierSubsidionHandler;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private void configSideMenu() {
        this.drawerLayoutMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // устанавливаем тулбар как экшн бар
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawerLayoutMenu, this.toolbar, 0, 0);//вспомогательная кнопка для навбара
        drawerLayoutMenu.addDrawerListener(toggle);
        toggle.syncState();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.subsidionRecivierList = (ListView) findViewById(R.id.receivirs_list);
        this.recivierSubsidionHandler = RecivierSubsidionHandler.getInstance();
        //   this.recivierSubsidionHandler.bindContext(this);//debug
        //   this.recivierSubsidionHandler.exportToJSON(this);//debug
        this.recivierSubsidionHandler.importFromJSON(this);
        this.recivierSubsidionHandler.bindDataToView(this, R.layout.subsidion_recivier_item);//create adapter for listview
        this.subsidionRecivierList.setAdapter(recivierSubsidionHandler.getAdapter()); //bind
        configSideMenu();//sidebar
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        TextView header_status = (TextView) (view.getHeaderView(0)).findViewById(R.id.nav_header_status);
        header_status.setText(AuthorizationHandler.getInstance().getUserSession().getUserName());//в меню устанавливаем имя пользователя (RELEASE MODE)
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_switch_men: {
                        if (item.isChecked()) item.setChecked(false);
                        else item.setChecked(true);
                        recivierSubsidionHandler.getSimpleFilter().getGenderFilter().object[0] = item.isChecked();
                        break;
                    }
                    case R.id.menu_switch_women: {
                        if (item.isChecked()) item.setChecked(false);
                        else item.setChecked(true);
                        recivierSubsidionHandler.getSimpleFilter().getGenderFilter().object[1] = item.isChecked();
                        break;
                    }
                    default:
                        break;
                }
                recivierSubsidionHandler.filter();
                // updateCounters();
                return true;
            }
        });
        Menu nav_menu = view.getMenu();
        this.setupNavigationViewItems(nav_menu);
        this.recivierSubsidionHandler.sortData();
    }

    /*nav_header_menu method*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty())
                    recivierSubsidionHandler.getSimpleFilter().getNameFilter().state = RecivierFilter.statement.CLEAR;
                else
                    recivierSubsidionHandler.getSimpleFilter().getNameFilter().state = RecivierFilter.statement.WORK;

                recivierSubsidionHandler.getSimpleFilter().getNameFilter().object = s.toLowerCase(Locale.ROOT);//update the filter
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
        int id = menu_item.getItemId();
        switch (id) {
            case R.id.app_bar_sort: {
                this.recivierSubsidionHandler.setaZSortMode(!this.recivierSubsidionHandler.isaZSortMode());
                this.recivierSubsidionHandler.sortData();
                break;
            }
            default: {
                break;
            }
        }
        return super.onOptionsItemSelected(menu_item);
    }

    private void setupNavigationViewItems(Menu menu) {
        MenuItem city_selector = menu.findItem(R.id.menu_city_selector);
        MenuItem region_selector = menu.findItem(R.id.menu_region_selector);
        MenuItem year_selector = menu.findItem(R.id.menu_birthdate_fill_year);
        MenuItem month_selector = menu.findItem(R.id.menu_birthdate_fill_month);
        TextInputLayout til_city_selector = (TextInputLayout) city_selector.getActionView();
        TextInputLayout til_region_selector = (TextInputLayout) region_selector.getActionView();
        TextInputLayout til_year_selector = (TextInputLayout) year_selector.getActionView();
        TextInputLayout til_month_selector = (TextInputLayout) month_selector.getActionView();
        AutoCompleteTextView view_month_selector = til_month_selector.findViewById(R.id.menu_birthdate_month_autocomplete);
        til_region_selector.setHint(R.string.nav_menu_region_hint);
        til_city_selector.setHint(R.string.nav_menu_city_hint);
        til_year_selector.setHint(R.string.nav_menu_yearHint);
        view_month_selector.setHint(R.string.nav_menu_monthHint);
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
                //   updateCounters();
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
                //  updateCounters();
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
                // updateCounters();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        view_month_selector.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override /*ITS IMPLEMENTATION 3, on my mind - the best*/
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String[] data = charSequence.toString().trim().toLowerCase(Locale.ROOT).split(",");
                String[] allMonths = getResources().getStringArray(R.array.nav_menu_month_select);
                String result = new String();
                if (data.length > 0) {
                    for (String substr : data) {
                        for (int x = 1; x < allMonths.length; x++) {
                            if (allMonths[x].contains(substr)) {
                                result += Integer.toString(x - 1) + ",";
                            }
                        }
                    }
                    recivierSubsidionHandler.getSimpleFilter().getBirth_month().object = result;
                    recivierSubsidionHandler.getSimpleFilter().getBirth_month().state = RecivierFilter.statement.WORK;
                } else
                    recivierSubsidionHandler.getSimpleFilter().getBirth_month().state = RecivierFilter.statement.CLEAR;
                recivierSubsidionHandler.filter();
                // updateCounters();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void updateCounters() { /*DEBUG ONLY*/
        TextView header_status = findViewById(R.id.nav_header_status);
        if (header_status != null) {
            header_status.setText("displayed:" + recivierSubsidionHandler.getDataList().size() + "/" + recivierSubsidionHandler.getPureData().size());
        }
    }

    //app_objects_start
    private RecivierSubsidionHandler recivierSubsidionHandler;//but it's stil Singleton(we store only the link)
    //app_objects_end
    //android_objects_start
    private DrawerLayout drawerLayoutMenu;
    private Toolbar toolbar;
    private ListView subsidionRecivierList;
    private SearchView searchView;

    //android_objects_end
}
