package com.rrpvm.subsidioninformator.objects;

//final release class(25.11.2021 updated)
public class User {
    public enum UserType {
        C_USER(0), C_ADMIN(4);
        private final int id;

        UserType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public User(String l, String pass, String name, UserType userType) {
        this.login = l;
        this.password = pass;
        this.name = name;
        this.userType = userType;
    }

    public User(String login, String pass, UserType userType) {
        this.login = login;
        this.password = pass;
        this.name = new String();
        this.userType = userType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String login;
    private String password;
    private String name;
    private UserType userType;
}
