package com.rrpvm.subsidioninformator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.objects.ComparatorDn;
import com.rrpvm.subsidioninformator.objects.ComparatorUp;
import com.rrpvm.subsidioninformator.objects.RecivierFilter;
import com.rrpvm.subsidioninformator.handlers.RecivierSubsidionHandler;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //app_objects_start
    private RecivierSubsidionHandler recivierSubsidionHandler;
    //app_objects_end
    //android_objects_start
    private DrawerLayout drawerLayoutMenu;
    private Toolbar toolbar;
    private ListView subsidionRecivierList;
    private SearchView searchView;
    //android_objects_end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recivierSubsidionHandler = new RecivierSubsidionHandler(this, R.layout.subsidion_recivier_item);//bind by ctx +  layout of item in list
        this.drawerLayoutMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.subsidionRecivierList = (ListView) findViewById(R.id.receivirs_list);
        //end_init;
        /* side_menu_init*/
        setSupportActionBar(toolbar); // устанавливаем тулбар как экшн бар
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawerLayoutMenu, this.toolbar, 0, 0);//вспомогательная кнопка для навбара
        drawerLayoutMenu.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_switch_all_genders:{
                        recivierSubsidionHandler.getR_filter().getGenderFilter().state= RecivierFilter.statement.CLEAR;
                        break;
                    }
                    case R.id.menu_switch_only_men:{
                        recivierSubsidionHandler.getR_filter().getGenderFilter().state= RecivierFilter.statement.WORK;
                        recivierSubsidionHandler.getR_filter().getGenderFilter().object= true;
                        break;
                    }
                    case R.id.menu_switch_only_women:{
                        recivierSubsidionHandler.getR_filter().getGenderFilter().state= RecivierFilter.statement.WORK;
                        recivierSubsidionHandler.getR_filter().getGenderFilter().object= false;
                        break;
                    }
                    default:
                        break;
                }
                recivierSubsidionHandler.filter();
                TextView menu_counter_current_elements = findViewById(R.id.menu_counter_current);
                TextView menu_counter_all_elements = findViewById(R.id.menu_counter_all);
                menu_counter_all_elements.setText("all elements: " + recivierSubsidionHandler.getPure_data().size());
                menu_counter_current_elements.setText("displayed elements: " + recivierSubsidionHandler.getDataList().size());
                return true;
            }
        });
        this.subsidionRecivierList.setAdapter(recivierSubsidionHandler.getAdapter()); //set data of list
    }
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
                    recivierSubsidionHandler.getR_filter().getNameFilter().state = RecivierFilter.statement.CLEAR;
                else
                    recivierSubsidionHandler.getR_filter().getNameFilter().state = RecivierFilter.statement.WORK;

                recivierSubsidionHandler.getR_filter().getNameFilter().object = s.toLowerCase(Locale.ROOT);//update the filter
                recivierSubsidionHandler.filter();//do filtering
                return true;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menu_item) {
        int id = menu_item.getItemId();
        switch (id) {
            case R.id.app_bar_sort: {
                this.recivierSubsidionHandler.setAZ_sortMode(!this.recivierSubsidionHandler.getaZ_sortMode());
                ArrayList<SubsidingRecivier> _tmpdata = new ArrayList<>(this.recivierSubsidionHandler.getDataList());
                if (this.recivierSubsidionHandler.getaZ_sortMode()) {//we love shit code
                    _tmpdata.sort(new ComparatorUp());
                } else {
                    this.recivierSubsidionHandler.getDataList().sort(new ComparatorDn());
                    _tmpdata.sort(new ComparatorDn());
                }
                this.recivierSubsidionHandler.setDataList(_tmpdata);
                this.recivierSubsidionHandler.getAdapter().notifyDataSetChanged();
                break;
            }
            default: {
                break;
            }
        }
        return super.onOptionsItemSelected(menu_item);
    }
}