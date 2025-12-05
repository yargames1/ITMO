package org.example.beans;


import java.io.Serializable;

public class NavigationBean implements Serializable {

    public String toMain() {
        // Переход на main.xhtml (создавать позже)
        return "main.xhtml?faces-redirect=true";
    }

    public String toIndex(){
        return "index.xhtml?faces-redirect=true";
    }
}