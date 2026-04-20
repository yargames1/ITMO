package org.example.rest.dto;

import org.example.entity.PointResult;

import java.time.LocalDateTime;

public class PointResultDto {
    private double x;
    private double y;
    private double r;
    private boolean hit;
    private LocalDateTime serverTime;
    private double processingTime;

    public PointResultDto(PointResult pr) {
        this.x = pr.getX();
        this.y = pr.getY();
        this.r = pr.getR();
        this.hit = pr.getHit();
        this.serverTime = pr.getServerTime();
        this.processingTime = pr.getProcessingTime();
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getR() { return r; }
    public boolean isHit() { return hit; }
    public LocalDateTime getServerTime() { return serverTime; }
    public double getProcessingTime() { return processingTime; }
}
