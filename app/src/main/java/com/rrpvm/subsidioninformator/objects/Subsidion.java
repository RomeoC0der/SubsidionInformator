package com.rrpvm.subsidioninformator.objects;

import java.io.Serializable;

public class Subsidion implements Serializable {
    public boolean getStatement() {
        return statement;
    }

    public int getId() {
        return id;
    }

    public double getJKP() {
        return JKP;
    }

    public double getCGTP() {
        return CGTP;
    }

    public String getRecievRange() {
        return recievRange;
    }

    public void setStatement(boolean statement) {
        this.statement = statement;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJKP(double JKP) {
        this.JKP = JKP;
    }

    public void setCGTP(double CGTP) {
        this.CGTP = CGTP;
    }

    public void setRecievRange(String recievRange) {
        this.recievRange = recievRange;
    }

    public String getGotRange() {
        return gotRange;
    }

    public void setGotRange(String gotRange) {
        this.gotRange = gotRange;
    }

    public Subsidion() {
        this.statement = false;
        this.id = -1;
        this.JKP = -1.0;
        this.CGTP = -1.0;
        this.recievRange = null;
        this.gotRange = null;
    }

    public Subsidion(boolean statement, int id, double JKP, double CGTP, String recievRange, String getRange) {
        this.statement = statement;
        this.id = id;
        this.JKP = JKP;
        this.CGTP = CGTP;
        this.recievRange = recievRange;
        this.gotRange = getRange;
    }

    private boolean statement;//состояние субсидии
    private int id;//номер субсидии
    private double JKP;//Розмір Субсидії ЖКП За Місяць, грн.*
    private double CGTP;//Розмір Субсидії СГТП Річний, грн.
    private String recievRange;//Призначено За Період
    private String gotRange;//Призначено За Період
}