package com.rrpvm.subsidioninformator.handlers;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.rrpvm.subsidioninformator.adapters.RecivierItemAdapter;
import com.rrpvm.subsidioninformator.interfaces.Packable;
import com.rrpvm.subsidioninformator.objects.ComparatorDn;
import com.rrpvm.subsidioninformator.objects.ComparatorUp;
import com.rrpvm.subsidioninformator.objects.RecivierFilter;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;
import com.rrpvm.subsidioninformator.objects.Subsidion;
import com.rrpvm.subsidioninformator.utilities.JSONHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
//final release class
public class RecivierSubsidionHandler implements Packable {//time to Singleton->

    public static RecivierSubsidionHandler getInstance() {
        if (instance == null) instance = new RecivierSubsidionHandler();
        return instance;
    }

    public RecivierSubsidionHandler() {
        this.pureData = new ArrayList<>();
        this.dataList = new ArrayList<>();
        this.simpleFilter = new RecivierFilter();
        //debug mode: generate data of reciviers
        debugGenerateData();//if release -> overrides
    }
    public void bindDataToView(Context ctx, int item_list_resource_id) {
        this.context= ctx;
        adapter = new RecivierItemAdapter(ctx, item_list_resource_id, this.dataList);
    }
    public void bindContext(Context ctx){
        this.context = ctx;
    }
    public void sortData() {
        ArrayList<SubsidingRecivier> tmpdata = new ArrayList<>(this.dataList);
        if (this.aZSortMode) {//we love shit code
            tmpdata.sort(new ComparatorUp());
        } else {
            tmpdata.sort(new ComparatorDn());
        }
        this.setDataList(tmpdata);
        this.adapter.notifyDataSetChanged();
    }
    @Override
    public void importFromJSON(Context ctx){
        try {
            this.pureData = JSONHelper.importFromJSON(ctx, RECIVIERS_DATA_FILENAME, (new TypeToken<ArrayList<SubsidingRecivier>>() {
            }).getType());
            this.dataList.clear();
            this.dataList.addAll(this.pureData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void exportToJSON(Context ctx){
        JSONHelper.exportToJSON(ctx,this.pureData, RECIVIERS_DATA_FILENAME);
    }
    public static void setInstance(RecivierSubsidionHandler instance) {
        RecivierSubsidionHandler.instance = instance;
    }

    public RecivierItemAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecivierItemAdapter adapter) {
        this.adapter = adapter;
    }

    public ArrayList<SubsidingRecivier> getPureData() {
        return pureData;
    }

    public void setPureData(ArrayList<SubsidingRecivier> pureData) {
        this.pureData = pureData;
    }

    public ArrayList<SubsidingRecivier> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<SubsidingRecivier> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
    }

    public RecivierFilter getSimpleFilter() {
        return simpleFilter;
    }

    public void setSimpleFilter(RecivierFilter simpleFilter) {
        this.simpleFilter = simpleFilter;
    }

    public boolean isaZSortMode() {
        return aZSortMode;
    }

    public void setaZSortMode(boolean aZSortMode) {
        this.aZSortMode = aZSortMode;
    }

    private void debugGenerateData() {
        try {
            this.pureData.add(new SubsidingRecivier(true, "Якименко", "Микита", "Дмитрович", "Донецька", "Маріуполь", new Date(), "Naximova", "1449013711", "001842541", new Subsidion(true, 31423, 1900, 11400, "10.01.2019-10.01.2020", "10.01.2019-10.01.2020"), "wrong"));
            this.pureData.add(new SubsidingRecivier(false, "Абакумова", "Дар'я", "Геннадіївна", "Харківська", "Харків", new Date(), "wtf", "2449013711", "002842541", new Subsidion(true, 31425, 2900, 23500, "10.01.2019-10.01.2020", "10.01.2019-10.01.2020"), "wrong"));
            this.pureData.add(new SubsidingRecivier(true, "Ісіченко", "Руслан", "Максимович", "Донецька", "Маріуполь", new Date(), "Mitropolitska", "3449013711", "003842541", new Subsidion(true, 31429, 1500, 15400, "10.01.2019-10.01.2020", "10.01.2019-10.01.2020"), "wrong"));
            this.pureData.add(new SubsidingRecivier(true, "Теліцин", "Данило", "Віталійович", "Донецька", "Маріуполь", new Date(), "Tramvayna", "4449013711", "004842541", new Subsidion(true, 23168, 2100, 21800, "10.01.2019-10.01.2020", "10.01.2019-10.01.2020"), "wrong"));
            this.pureData.add(new SubsidingRecivier(false, "Краснощок", "Тамара", "Валеріївна", "Запорізька", "Запоріжжя", new Date(), "unknown", "5449013711", "005842541", new Subsidion(true, 15231, 3000, 31600, "10.01.2019-10.01.2020", "10.01.2019-10.01.2020"), "wrong"));
            this.dataList.addAll(pureData);
            this.exportToJSON(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filter() {
        dataList.clear();
        try {
            for (SubsidingRecivier recivier : pureData)// как происходит выборка: сначала, объект имеет право на добавление, но, если включена опция фильтра, а свойство объекта не подходит под него -> он нам не нужен
            {
                boolean shouldAdd = true;
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Ukraine"));//date не позволяет точно работать с месяцами и тд
                cal.setTime(recivier.getBirthdate());
                if (simpleFilter.getNameFilter().state == RecivierFilter.statement.WORK) {
                    String pib = recivier.getPIB().toLowerCase(Locale.ROOT);
                    if (!pib.contains(simpleFilter.getNameFilter().object)) shouldAdd = false;
                }
                if (true) { //нарушение общей концепции стейтмента(условие всегда должно проходить), но так удобней. Условие нужно лишь для поддержания внешнего вида кода
                    if (recivier.isMale() && !simpleFilter.getGenderFilter().object[0]) {//если ты мужчина + отключена выборка по мужчинам -> нет
                        shouldAdd = false;
                    }
                    if (!recivier.isMale() && !simpleFilter.getGenderFilter().object[1]) {//если ты женщина + отключена выборка по женщинам -> нет
                        shouldAdd = false;
                    }
                }
                if (simpleFilter.getCityFilter().state == RecivierFilter.statement.WORK) {
                    String[] arr = simpleFilter.getCityFilter().object.split(",");
                    boolean inSet = false;
                    for (String str : arr) {
                        if (recivier.getCity().toLowerCase(Locale.ROOT).contains(str.toLowerCase(Locale.ROOT)))
                            inSet = true;
                    }
                    if (!inSet) shouldAdd = false;
                }
                if (simpleFilter.getOblastFilter().state == RecivierFilter.statement.WORK) {
                    String[] arr = simpleFilter.getOblastFilter().object.split(",");
                    boolean inSet = false;
                    for (String str : arr) {
                        if (recivier.getRegion().toLowerCase(Locale.ROOT).contains(str.toLowerCase(Locale.ROOT))) {
                            inSet = true;
                            break;
                        }
                    }
                    if (!inSet) shouldAdd = false;
                }
                if (simpleFilter.getBirth_day().state == RecivierFilter.statement.WORK) {

                }
                if (simpleFilter.getBirth_month().state == RecivierFilter.statement.WORK) {
                    if (!simpleFilter.getBirth_month().object.isEmpty()) {
                        String[] arr = simpleFilter.getBirth_month().object.split(",");//array of integers presented by String
                        boolean inSet = false;
                        for (String str : arr)
                            if (cal.get(Calendar.MONTH) == Integer.parseInt(str)) {
                                inSet = true;
                                break;
                            }
                        if (!inSet) shouldAdd = false;
                    }
                }
                if (simpleFilter.getBirth_year().state == RecivierFilter.statement.WORK) {
                    if (cal.get(Calendar.YEAR) != simpleFilter.getBirth_year().object)
                        shouldAdd = false;
                }
                if (shouldAdd) dataList.add(recivier);//подходит по всем критериям
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    public final static String RECIVIERS_DATA_FILENAME = "ReciviersList.json";
    private static RecivierSubsidionHandler instance;
    private RecivierItemAdapter adapter;
    private ArrayList<SubsidingRecivier> pureData;//clear, non-filterd , full data
    private ArrayList<SubsidingRecivier> dataList;//filtered-data;
    private RecivierFilter simpleFilter;
    private boolean aZSortMode = true;
    private Context context=null;
}
