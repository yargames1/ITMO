package org.example.beans;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "point_results")
public class PointResult implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "point_seq")
    @SequenceGenerator(name = "point_seq", sequenceName = "point_sequence", allocationSize = 1)
    private Long id;

    private Double x;
    private Double y;
    private Double r;
    private Boolean hit; // попадание

    @Temporal(TemporalType.TIMESTAMP)
    private Date serverTime;

    private Double processingTime; // в миллисекундах

    // Конструктор по умолчанию (обязателен для JPA)
    public PointResult() {}

    // Конструктор со всеми полями (опционально)
    public PointResult(Double x, Double y, Double r, Boolean hit, Date serverTime, Double processingTime) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.serverTime = serverTime;
        this.processingTime = processingTime;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getHit() {
        return hit;
    }

    public void setHit(Boolean hit) {
        this.hit = hit;
    }

    public Date getServerTime() {
        return serverTime;
    }

    public void setServerTime(Date serverTime) {
        this.serverTime = serverTime;
    }

    public Double getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Double processingTime) {
        this.processingTime = processingTime;
    }
}