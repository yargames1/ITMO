package org.example.lab2.model;

public class Validator {
    public Boolean isValid(double x, double y, double r){
        return !(r < 0 || r > 5 || x < -5 || x > 3 || y < -3 || y > 5);
    }
}
