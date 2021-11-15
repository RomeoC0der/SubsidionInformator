package com.rrpvm.subsidioninformator.objects;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import java.util.Date;

public class SubsidingRecivier {

  /*  public SubsidingRecivier(){
        this.name = null;
        this.surname = null;
        this.patronymic = null;
        this.street = null;
        this.city = null;
        this.homeNumber = 0;
        this.subsidion = null;
    }
    public SubsidingRecivier(String name, String surname, String patronymic, String city, String street, String passportNumber, int TIN, int homeNumber, Subsidion subsidion){
        name.trim();
        surname.trim();
        patronymic.trim();
        city.trim();
        street.trim();
        passportNumber.trim();
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.city = city;
        this.street = street;
        this.homeNumber = homeNumber;
        this.subsidion = subsidion;
        this.passportNumber = passportNumber;
        this.TIN = TIN;
    }
    */
   /* public boolean contains(RecivierFilter filter){
        String pib = this.name.concat(this.surname.concat(this.patronymic));
        if(filter.)
    }*/
    public SubsidingRecivier(boolean male, String surname, String name, String patronymic, String region, String city, Date birthdate, String position,String image){
        this.male = male;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.region = region;
        this.city = city;
        this.birthdate = birthdate;
        this.position = position;
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getPatronymic() {
        return patronymic;
    }
    public String getRegion() {
        return region;
    }
    public String getPosition() {
        return position;
    }
    public String getCity() {
        return city;
    }
    public Date getBirthdate() {
        return birthdate;
    }
    public String getImage() {
        return image;
    }
    public boolean isMale() {
        return male;
    }

    public String getPIB(){
        return surname + " " + name + " " + patronymic;
    }
    //general data:
    private String name;
    private String surname;
    private String patronymic;
    private String region;
    private String city;
    private String position;
    private Date birthdate;
    private boolean male;
    //RENDER DATA:
    private String image;//path for (icon of person)

/* private String city;
    private String street;
    private String passportNumber;
    private int TIN;//Идентификационный номер налогоплательщика
    private int homeNumber;
    private Subsidion subsidion;*/
}
