package com.rrpvm.subsidioninformator.objects;

public class User {
    private String login;
    private String password;
    public String name;

    public User(String l, String pass, String name) {
        this.login = l;
        this.password = pass;
        this.name = name;
    }

    public User(String login, String pass) {
        this.login = login;
        this.password = pass;
        this.name = " ";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
