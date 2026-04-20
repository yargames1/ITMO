package org.example.rest.dto;


public class LoginRequest {

    private String login;
    private String passwd;

    public LoginRequest() {}

    public void setLogin(String login){
        this.login = login;
    }
    public String getLogin(){
        return login;
    }
    public void setPasswd(String passwd){
        this.passwd = passwd;
    }
    public String getPasswd(){
        return passwd;
    }
}
