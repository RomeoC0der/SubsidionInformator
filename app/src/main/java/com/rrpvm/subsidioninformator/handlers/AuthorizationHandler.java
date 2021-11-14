package com.rrpvm.subsidioninformator.handlers;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.rrpvm.subsidioninformator.objects.User;
import com.rrpvm.subsidioninformator.objects.UserSession;
import com.rrpvm.subsidioninformator.utilities.JSONHelper;

import java.util.ArrayList;
import java.util.Date;

public class AuthorizationHandler {
    public static final String OUTPUT_FILENAME = "Users.json";
    public static final int XOR_KEY = 45;
    private ArrayList<User> auth_data;
    private UserSession session;
    private Context lastCtx;

    //encryption
    private String encrypt(String password)
    {
        String enc_string = new String();
        for(char c : password.toCharArray()){
                c = (char)(c^XOR_KEY);
            enc_string += c;
        }
        return enc_string;
    }
    //decryption (none)
    public boolean Authorize(String login, String pass) {
        if(auth_data==null)return false;
        for (User user : auth_data) {
           // if (login.equals(user.getLogin()) && encrypt(pass).equals(user.getPassword())) {
            if (login.equals(user.getLogin()) && encrypt(pass).equals(encrypt(user.getPassword()))) {
                session = new UserSession();
                session.setSessionStatement(true);
                session.setSessionStartTime(new Date().getTime());
                if (lastCtx != null) {
                    session.exportData(lastCtx);
                }
                return true;
            }
        }
        return false;
    }
    public void exportData(Context ctx) {
        lastCtx = ctx;
        JSONHelper.exportToJSON(ctx, auth_data, OUTPUT_FILENAME);
        //session.exportData(ctx);
    }
    public void importData(Context ctx) {
        lastCtx = ctx;
        auth_data = JSONHelper.importFromJSON(ctx, OUTPUT_FILENAME, (new TypeToken<ArrayList<User>>() {}).getType());
        session.importData(ctx);
    }
    public UserSession getSession() {
        return session;
    }
    public AuthorizationHandler() {
        session = new UserSession();
        this.auth_data = new ArrayList<>();
    }
    public ArrayList<User> getAuth_data() {
        return auth_data;
    }
}
