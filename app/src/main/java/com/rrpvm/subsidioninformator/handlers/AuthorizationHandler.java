package com.rrpvm.subsidioninformator.handlers;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.rrpvm.subsidioninformator.interfaces.Packable;
import com.rrpvm.subsidioninformator.objects.SimpleCryptography;
import com.rrpvm.subsidioninformator.objects.User;
import com.rrpvm.subsidioninformator.objects.UserSession;
import com.rrpvm.subsidioninformator.utilities.JSONHelper;

import java.util.ArrayList;
import java.util.Date;

//final release class
public class AuthorizationHandler implements Packable { //time to Singleton
    public static AuthorizationHandler getInstance() {//->Singleton
        if (instance == null) instance = new AuthorizationHandler();
        return instance;
    }

    public AuthorizationHandler() {
        this.userSession = new UserSession();
        this.authorizationData = new ArrayList<>();
        this.simpleCryptography = new SimpleCryptography();
    }
    @Override
    public void importFromJSON(Context ctx) {
        try {
            this.lastCtx = ctx;
            authorizationData = JSONHelper.importFromJSON(ctx, DATA_FILENAME, (new TypeToken<ArrayList<User>>() {
            }).getType());
            userSession.importFromJSON(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void exportToJSON(Context ctx) {
        try {
            this.lastCtx = ctx;
            JSONHelper.exportToJSON(ctx, authorizationData, DATA_FILENAME);
            userSession.exportToJSON(ctx);//могут возникнуть проблемы, когда нет даты и ты делаешь первый экспорт. Действия : создаем дату, комментируем экспорт
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserSession getUserSession() {
        return this.userSession;
    }

    public ArrayList<User> getAuthorizationData() {
        return this.authorizationData;
    }

    public boolean signIn(String login, String pass) {
        if (this.authorizationData == null || this.authorizationData.isEmpty()) return false;
        for (User user : authorizationData) {
            /*NORMAL DATABASE  LINE if (user.getLogin().trim().equals(login) && user.getPassword().equals(simpleCryptography.encryp(pass))) {*/
            if (user.getLogin().trim().equals(login) && simpleCryptography.encrypt(user.getPassword()).equals(simpleCryptography.encrypt(pass))) {
                this.userSession.setSessionStatement(true);
                this.userSession.setSessionStartTime(new Date().getTime());
                this.userSession.setUserName(user.getName());
                this.userSession.setUserType(user.getUserType());
                if (lastCtx != null) {
                    this.userSession.exportToJSON(lastCtx);//save
                }
                return true;
            }
        }
        return false;
    }

    public void logOut(){
        userSession.setSessionStatement(false);
        userSession.setSessionStartTime(0);
        userSession.exportToJSON(lastCtx);
    }

    public static final String DATA_FILENAME = "Users.json";
    private static AuthorizationHandler instance;//->singleton
    private ArrayList<User> authorizationData;//gets data from db(json) and provides all data of all users
    private UserSession userSession;//store data of logged user
    private Context lastCtx;//gson dependencies
    private SimpleCryptography simpleCryptography;//for encryption passwords
}
