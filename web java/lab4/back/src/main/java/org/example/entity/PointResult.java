package org.example.entity;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "point_results")
public class PointResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Double x;
    @Column(nullable = false)
    private Double y;
    @Column(nullable = false)
    private Double r;
    @Column(nullable = false)
    private Boolean hit;

    @Column(nullable = false)
    private LocalDateTime  serverTime;

    @Column(nullable = false)
    private Double processingTime; // в миллисекундах

    // Конструктор по умолчанию (обязателен для JPA)
    public PointResult() {}

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

    public LocalDateTime   getServerTime() {
        return serverTime;
    }

    public void setServerTime(LocalDateTime   serverTime) {
        this.serverTime = serverTime;
    }

    public Double getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Double processingTime) {
        this.processingTime = processingTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
