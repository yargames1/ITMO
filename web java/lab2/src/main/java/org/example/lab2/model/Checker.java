package org.example.lab2.model;

public class Checker {
    public boolean isHit(double x, double y, double r){
        return (x <= 0 && y <= 0 && x >= -r && y >= -r) ||        // прямоугольник
                (x >= 0 && y <= 0 && y >= (x - r / 2)) ||          // треугольник
                (x <= 0 && y >= 0 && (x * x + y * y <= r * r));    // четверть круга
    }
}
