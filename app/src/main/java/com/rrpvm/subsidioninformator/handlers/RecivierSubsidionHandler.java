package com.rrpvm.subsidioninformator.handlers;

import android.content.Context;
import android.widget.TextView;

import com.rrpvm.subsidioninformator.adapters.RecivierItemAdapter;
import com.rrpvm.subsidioninformator.objects.ComparatorDn;
import com.rrpvm.subsidioninformator.objects.ComparatorUp;
import com.rrpvm.subsidioninformator.objects.RecivierFilter;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;
import com.rrpvm.subsidioninformator.objects.Subsidion;
import com.rrpvm.subsidioninformator.utilities.JSONHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RecivierSubsidionHandler {
    public final static String RECIVIERS_DATA_FILENAME = "ReciviersList.json";
    private RecivierItemAdapter adapter;
    private ArrayList<SubsidingRecivier> pure_data;//clear, non-filterd , full data
    private ArrayList<SubsidingRecivier> dataList;//filtered-data;
    private RecivierFilter r_filter;
    private boolean aZ_sortMode = true;
    public void filter() {
        dataList.clear();
        try {
            for (SubsidingRecivier recivier : pure_data)// как происходит выборка: сначала, объект имеет право на добавление, но, если включена опция фильтра, а свойство объекта не подходит под него -> он нам не нужен
            {
                boolean shouldAdd = true;
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Ukraine"));//date не позволяет точно работать с месяцами и тд
                cal.setTime(recivier.getBirthdate());
                if (r_filter.getNameFilter().state == RecivierFilter.statement.WORK) {
                    String pib = recivier.getPIB().toLowerCase(Locale.ROOT);
                    if (!pib.contains(r_filter.getNameFilter().object)) shouldAdd = false;
                }
                if (true) { //нарушение общей концепции стейтмента(условие всегда должно проходить), но так удобней. Условие нужно лишь для поддержания внешнего вида кода
                    if (recivier.isMale() && !r_filter.getGenderFilter().object[0]) {//если ты мужчина + отключена выборка по мужчинам -> нет
                        shouldAdd = false;
                    }
                    if (!recivier.isMale() && !r_filter.getGenderFilter().object[1]) {//если ты женщина + отключена выборка по женщинам -> нет
                        shouldAdd = false;
                    }
                }
                if (r_filter.getCityFilter().state == RecivierFilter.statement.WORK) {
                    String[] arr = r_filter.getCityFilter().object.split(",");
                    boolean inSet = false;
                    for (String str : arr) {
                        if (recivier.getCity().toLowerCase(Locale.ROOT).contains(str.toLowerCase(Locale.ROOT)))
                            inSet = true;
                    }
                    if (!inSet) shouldAdd = false;
                }
                if (r_filter.getOblastFilter().state == RecivierFilter.statement.WORK) {
                    String[] arr = r_filter.getOblastFilter().object.split(",");
                    boolean inSet = false;
                    for (String str : arr) {
                        if (recivier.getRegion().toLowerCase(Locale.ROOT).contains(str.toLowerCase(Locale.ROOT))) {
                            inSet = true;
                            break;
                        }
                    }
                    if (!inSet) shouldAdd = false;
                }
                if (r_filter.getBirth_day().state == RecivierFilter.statement.WORK) {

                }
                if (r_filter.getBirth_month().state == RecivierFilter.statement.WORK) {
                    if (!r_filter.getBirth_month().object.isEmpty()) {
                        String[] arr = r_filter.getBirth_month().object.split(",");//array of integers presented by String
                        boolean inSet = false;
                        for (String str : arr)
                            if (cal.get(Calendar.MONTH) == Integer.parseInt(str)) {
                                inSet = true;
                                break;
                            }
                        if (!inSet) shouldAdd = false;
                    }
                }
                if (r_filter.getBirth_year().state == RecivierFilter.statement.WORK) {
                    if (cal.get(Calendar.YEAR) != r_filter.getBirth_year().object)
                        shouldAdd = false;
                }
                if (shouldAdd) dataList.add(recivier);//подходит по всем критериям
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
    public RecivierSubsidionHandler(Context ctx, int item_list_resource_id) {//implement logotypes by serializing bitmap's?
        /*init objects*/
        pure_data = new ArrayList<>();
        dataList = new ArrayList<>();
        r_filter = new RecivierFilter();
        this.pure_data.add(new SubsidingRecivier(true, "Якименко", "Микита", "Дмитрович", "Донецька", "Маріуполь", new Date(), "Naximova","1449013711","001842541", new Subsidion(true,31423,1900,11400,"10.01.2019-10.01.2020","10.01.2019-10.01.2020"),"wrong"));
        this.pure_data.add(new SubsidingRecivier(false, "Абакумова", "Дар'я", "Геннадіївна", "Харківська", "Харків", new Date(), "wtf","2449013711","002842541",new Subsidion(true,31425,2900,23500,"10.01.2019-10.01.2020","10.01.2019-10.01.2020"), "wrong"));
        this.pure_data.add(new SubsidingRecivier(true, "Ісіченко", "Руслан", "Максимович", "Донецька", "Маріуполь", new Date(), "Mitropolitska","3449013711","003842541",new Subsidion(true,31429,1500,15400,"10.01.2019-10.01.2020","10.01.2019-10.01.2020"), "wrong"));
        this.pure_data.add(new SubsidingRecivier(true, "Теліцин", "Данило", "Віталійович", "Донецька", "Маріуполь", new Date(), "Tramvayna", "4449013711","004842541",new Subsidion(true,23168,2100,21800,"10.01.2019-10.01.2020","10.01.2019-10.01.2020"),"wrong"));
        this.pure_data.add(new SubsidingRecivier(false, "Краснощок", "Тамара", "Валеріївна", "Запорізька", "Запоріжжя", new Date(), "unknown", "5449013711","005842541",new Subsidion(true,15231,3000,31600,"10.01.2019-10.01.2020","10.01.2019-10.01.2020"),"wrong"));
        //pure_data = JSONHelper.importToJSON(ctx,RECIVIERS_DATA_FILENAME);
        this.dataList.addAll(this.pure_data);//copy all(no filter mode)
        adapter = new RecivierItemAdapter(ctx, item_list_resource_id, this.dataList);
    }
    public void sortData(){
        ArrayList<SubsidingRecivier> _tmpdata = new ArrayList<>(this.getDataList());
        if (this.aZ_sortMode) {//we love shit code
            _tmpdata.sort(new ComparatorUp());
        } else {
            _tmpdata.sort(new ComparatorDn());
        }
        this.setDataList(_tmpdata);
        this.adapter.notifyDataSetChanged();
    }
    public RecivierItemAdapter getAdapter() {
        return this.adapter;
    }
    public RecivierFilter getR_filter() {
        return this.r_filter;
    }
    public boolean getaZ_sortMode() {
        return this.aZ_sortMode;
    }
    public void setAZ_sortMode(boolean b) {
        this.aZ_sortMode = b;
    }
    public ArrayList<SubsidingRecivier> getDataList() {
        return this.dataList;
    }
    public void setDataList(ArrayList<SubsidingRecivier> list) {
        this.dataList.clear();
        this.dataList.addAll(list);//нельзя затирать ссылку.
    }
    public ArrayList<SubsidingRecivier> getPure_data() {
        return pure_data;
    }
}
