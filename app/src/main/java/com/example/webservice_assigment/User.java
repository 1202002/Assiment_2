package com.example.webservice_assigment;

public class User {

    private String  name ;
    private String pass ;

//     constractor section

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }


//      define Getter

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }


//    Define setter

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
