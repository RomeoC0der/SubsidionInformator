package com.rrpvm.subsidioninformator.objects;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.rrpvm.subsidioninformator.utilities.JSONHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserSession   {
    public static final int SESSION_DURATION =3600000;//*100->cast to mseconds : getTime return ms
    public static final String SESSION_FILENAME = "user_session.json";
    private boolean bSessionCurrent;
    private long sessionStartTime;

    public boolean getSessionStatement() {
        Date date = java.util.Calendar.getInstance().getTime();
        bSessionCurrent = (date.getTime() - sessionStartTime) <= SESSION_DURATION;
        System.out.println(date.getTime());
        System.out.println(sessionStartTime);
        System.out.println(date.getTime() -sessionStartTime);
        return bSessionCurrent;
    }
    public void setSessionStatement(boolean bValue) {
        bSessionCurrent = bValue;
    }
    public void update() {
    }
    public void exportData(Context ctx) {
        UserSessionData ussd = new UserSessionData(bSessionCurrent, sessionStartTime);
        ArrayList<UserSessionData> kostil = new ArrayList<>();
        kostil.add(ussd);
        JSONHelper.exportToJSON(ctx, kostil, SESSION_FILENAME);
    }
    public void importData(Context ctx) {
      ArrayList<UserSessionData> data = new ArrayList<>();
      data = JSONHelper.importFromJSON(ctx, SESSION_FILENAME,(new TypeToken<List<UserSessionData>>() {}).getType());
      if(data == null || data.isEmpty())
      {
        //  System.out.println("why?");
          return;
      }
      bSessionCurrent = data.get(0).getSessionCurrent();//костыль
      sessionStartTime = data.get(0).getSessionStartTime();
    }
    public long getSessionStartTime() {
        return sessionStartTime;
    }
    public void setSessionStartTime(long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }
}

class UserSessionData {
    private boolean bSessionCurrent;
    private long sessionStartTime;

    public boolean getSessionCurrent() {
        return bSessionCurrent;
    }
    public long getsSessionStartTime() {
        return sessionStartTime;
    }
    public void setbSessionCurrent(boolean bSessionCurrent) {
        this.bSessionCurrent = bSessionCurrent;
    }
    public long getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }
    public UserSessionData(boolean bSessionCurrent, long sessionStartTime) {
        this.bSessionCurrent = bSessionCurrent;
        this.sessionStartTime = sessionStartTime;
    }
    public  UserSessionData(){

    }
}

