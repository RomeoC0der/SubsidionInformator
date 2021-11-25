package com.rrpvm.subsidioninformator.objects;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.rrpvm.subsidioninformator.interfaces.Packable;
import com.rrpvm.subsidioninformator.utilities.JSONHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//final release of this class
public class UserSession implements Packable {
    public UserSession() {
        userSessionData = new UserSessionData();
    }

    public long getSessionStartTime() {
        return this.userSessionData.sessionStartTime;
    }

    public boolean isSessionCurrent() {
        return this.userSessionData.bSessionCurrent;
    }

    public String getUserName() {
        return this.userSessionData.userName;
    }

    public User.UserType getUserType() {
        return this.userSessionData.userType;
    }

    public void setUserType(User.UserType type) {
        this.userSessionData.userType = type;
    }

    public void setSessionStartTime(long newSessionStartTime) {
        this.userSessionData.sessionStartTime = newSessionStartTime;
    }

    public void setSessionStatement(boolean statement) {
        this.userSessionData.bSessionCurrent = statement;
    }

    public void setUserName(String userName) {
        this.userSessionData.userName = userName;
    }

    public boolean calculateSessionStatement() {
        Date date = java.util.Calendar.getInstance().getTime();
        this.userSessionData.bSessionCurrent = (date.getTime() - userSessionData.sessionStartTime) <= SESSION_DURATION;
        return this.userSessionData.bSessionCurrent;
    }

    @Override
    public void exportToJSON(Context ctx) {
        JSONHelper.exportToJSON(ctx, SESSION_FILENAME, this.userSessionData);
    }

    @Override
    public void importFromJSON(Context ctx) {
        this.userSessionData = JSONHelper.importFromJSON(ctx, SESSION_FILENAME, new TypeToken<UserSessionData>() {
        }, true);
        if (this.userSessionData == null) {
            this.userSessionData = new UserSessionData();
        }
    }

    public static final int SESSION_DURATION = 3600000;//*100->cast to mseconds : getTime return ms
    public static final String SESSION_FILENAME = "user_session.json";
    private UserSessionData userSessionData;
}

class UserSessionData {
    public UserSessionData(boolean bSessionCurrent, long sessionStartTime, String userName, User.UserType type) {
        this.bSessionCurrent = bSessionCurrent;
        this.sessionStartTime = sessionStartTime;
        this.userName = userName;
        this.userType = type;
    }

    public UserSessionData() {
        bSessionCurrent = false;
        sessionStartTime = -42;
        userName = new String();
        this.userType = User.UserType.C_USER;
    }

    public boolean bSessionCurrent;
    public long sessionStartTime;
    public String userName;
    public User.UserType userType;
}

