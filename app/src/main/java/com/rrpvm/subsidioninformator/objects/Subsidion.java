package com.rrpvm.subsidioninformator.objects;

import java.util.Date;

public class Subsidion {
    public boolean getStatement(){
        return statement;
    }
    public int getId(){
        return  id;
    }
    public double getJKP(){
        return  JKP;
    }
    public  double getCGTP(){
        return  CGTP;
    }
    public Date getStartDate(){
        return  startDate;
    }
    public Date getLastRecieveDate(){
        return  lastRecieveDate;
    }
    public void setStatement(boolean statement)
    {
        this.statement=statement;
    }
    public void setId(int id)
    {
        this.id=id;
    }
    public void setJKP(double JKP)
    {
        this.JKP=JKP;
    }
    public void setCGTP(double CGTP)
    {
        this.CGTP=CGTP;
    }
    public void setStartDate(Date startDate){
        this.startDate = startDate;
    }
    public  void setLastRecieveDate(Date lastRecieveDate)
    {
        this.lastRecieveDate = lastRecieveDate;
    }
    public Subsidion(){
        this.statement=false;
        this.id=-1;
        this.JKP=-1.0;
        this.CGTP=-1.0;
        this.startDate=null;
        this.lastRecieveDate=null;
    }
    public Subsidion(boolean statement, int id, double JKP, double CGTP, Date startDate, Date lastRecieveDate)
    {
        this.statement = statement;
        this.id = id;
        this.JKP = JKP;
        this.CGTP = CGTP;
        this.startDate = startDate;
        this.lastRecieveDate = lastRecieveDate;
    }
    private boolean statement;//состояние субсидии
    private int id;//номер субсидии
    private double JKP;//Розмір Субсидії ЖКП За Місяць, грн.*
    private double CGTP;//Розмір Субсидії СГТП Річний, грн.
    private Date startDate;//Призначено За Період
    private Date lastRecieveDate;//Нараховано За Період
}