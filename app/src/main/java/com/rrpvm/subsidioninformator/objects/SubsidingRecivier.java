package com.rrpvm.subsidioninformator.objects;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class SubsidingRecivier implements Serializable {
    public SubsidingRecivier(boolean male, String surname, String name, String patronymic, String region, String city, Date birthdate, String position,String itn,String passportId ,Subsidion subsidion, String image) {
        this.male = male;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.region = region;
        this.city = city;
        this.birthdate = birthdate;
        this.position = position;
        this.image = image;
        this.ITN = itn;
        this.passportId = passportId;
        this.subsidionData = subsidion;
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

    public String getITN() {
        return ITN;
    }

    public String getPassportId() {
        return passportId;
    }

    public boolean isMale() {
        return male;
    }
    public Subsidion getSubsidionData(){return this.subsidionData;}

    public String getPIB() {
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
    private String ITN;//ИНН
    private String passportId;//номер паспорта
    private Subsidion subsidionData;
    //RENDER DATA:
    private String image;//path for (icon of person)
}
