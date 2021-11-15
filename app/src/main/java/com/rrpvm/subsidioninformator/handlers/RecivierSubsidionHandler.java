package com.rrpvm.subsidioninformator.handlers;

import android.content.Context;
import android.widget.TextView;

import com.rrpvm.subsidioninformator.adapters.RecivierItemAdapter;
import com.rrpvm.subsidioninformator.objects.RecivierFilter;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;
import com.rrpvm.subsidioninformator.utilities.JSONHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecivierSubsidionHandler {
    public final static String RECIVIERS_DATA_FILENAME = "ReciviersList.json";
    private RecivierItemAdapter adapter;
    private ArrayList<SubsidingRecivier>pure_data;//clear, non-filterd , full data
    private ArrayList<SubsidingRecivier>dataList;//filtered-data;
    private RecivierFilter r_filter;
    private boolean aZ_sortMode = true;
    public  void filter() {
        dataList.clear();
        try {
            for(SubsidingRecivier recivier: pure_data)// как происходит выборка: сначала, объект имеет право на добавление, но, если включена опция фильтра, а свойство объекта не подходит под него -> он нам не нужен
            {
                boolean shouldAdd = true;
                if(r_filter.getNameFilter().state == RecivierFilter.statement.WORK){
                    String pib = recivier.getSurname() +" "+ recivier.getName() +" "+recivier.getPatronymic();
                    pib = pib.toLowerCase(Locale.ROOT);
                    if(!pib.contains(r_filter.getNameFilter().object))shouldAdd = false;
                }
                if(r_filter.getGenderFilter().state == RecivierFilter.statement.WORK){
                    if(recivier.isMale() != r_filter.getGenderFilter().object)shouldAdd = false;
                }
                if(r_filter.getCityFilter().state == RecivierFilter.statement.WORK){
                    if(!recivier.getCity().contains(r_filter.getCityFilter().object))shouldAdd = false;
                }
                if(r_filter.getOblastFilter().state == RecivierFilter.statement.WORK){
                  //  if(!recivier.get().contains(r_filter.getCityFilter().object))shouldAdd = false;
                }
                if(r_filter.getBirth_day().state == RecivierFilter.statement.WORK){
                    //if(recivier.get)
                }
                if(r_filter.getBirth_month().state == RecivierFilter.statement.WORK){
            //
                }
                if(r_filter.getBirth_year().state == RecivierFilter.statement.WORK){
//
                }
                if(shouldAdd)dataList.add(recivier);//подходит по всем критериям
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
    public RecivierSubsidionHandler(Context ctx, int item_list_resource_id){//implement logotypes by serializing bitmap's?
        /*init objects*/
        pure_data = new ArrayList<>();
        dataList = new ArrayList<>();
        r_filter = new RecivierFilter();
        this.pure_data.add(new SubsidingRecivier(true,"Yakimenko", "Nikita","Dmitrievich","Donetska","Mariupol", new Date(), "Naximova","wrong"));
        this.pure_data.add(new SubsidingRecivier(true,"Isichenko", "Ruslan","Maksimovich","Donetska","Mariupol", new Date(), "Mitropolitska","wrong"));
        this.pure_data.add(new SubsidingRecivier(true,"Telytsin", "Danilo","Vitalievich","Donetska","Mariupol", new Date(),"Tramvayna","wrong"));
        this.pure_data.add(new SubsidingRecivier(false,"Krasnoshek", "Tamara","Valerievna","Zaporozhska","Zaporogie", new Date(),"unknown","wrong"));
        //pure_data = JSONHelper.importToJSON(ctx,RECIVIERS_DATA_FILENAME);
        this.dataList.addAll(this.pure_data);//copy all(no filter mode)
        adapter = new RecivierItemAdapter(ctx, item_list_resource_id, this.dataList);
    }
    public RecivierItemAdapter getAdapter(){
        return this.adapter;
    }
    public RecivierFilter getR_filter(){
        return this.r_filter;
    }
    public boolean getaZ_sortMode(){return this.aZ_sortMode;}
    public void setAZ_sortMode(boolean b){this.aZ_sortMode = b;}
    public  ArrayList<SubsidingRecivier> getDataList(){
        return this.dataList;
    }
    public   void setDataList(ArrayList<SubsidingRecivier> list){
       this.dataList.clear();
       this.dataList.addAll(list);//нельзя затирать ссылку.
    }

    public ArrayList<SubsidingRecivier> getPure_data() {
        return pure_data;
    }
}
