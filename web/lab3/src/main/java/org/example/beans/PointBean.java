package org.example.beans;

import java.io.Serializable;
import java.util.Date;

public class PointBean implements Serializable {

    private Double x;
    private Double y;
    private Double r;

    private ResultsBean resultsBean;

    public void submit() {
        System.out.println("submit() called, resultsBean = " + resultsBean);
        System.out.printf("x: %s y: %s r: %s%n", x, y, r);
        long startTime = System.nanoTime();

        // Проверка попадания в область
        boolean hit = checkHit(x, y, r);

        // Создание результата
        PointResult result = new PointResult();
        result.setX(x);
        result.setY(y);
        result.setR(r);
        result.setHit(hit);
        result.setServerTime(new Date());
        result.setProcessingTime((System.nanoTime() - startTime)/ 1_000_000.0);

        // Сохранение через application-scoped bean пока не реализуем
        resultsBean.addResult(result);
    }

    private boolean checkHit(double x, double y, double r) {
        // Треугольник
        if (x <= 0 && y <= 0 && y >= -x - r) return true;

        // Прямоугольник
        if (x >= 0 && y >= 0 && x <= r && y <= r/2) return true;

        // Четверть круга
        if (x >= 0 && y <= 0 && (x*x + y*y <= r*r/4)) return true;

        return false;
    }

    // геттеры/сеттеры + setter для resultsBean

    public Double getX() {
        return x;
    }
    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }
    public void setY(Double y) {
        this.y = y;
    }

    public Double getR() {
        return r;
    }
    public void setR(Double r) {
        this.r = r;
    }

    public void setResultsBean(ResultsBean resultsBean) {
        this.resultsBean = resultsBean;
    }

}