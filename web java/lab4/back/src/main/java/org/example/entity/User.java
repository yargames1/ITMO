package org.example.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String passwdHash;
    @Column(nullable = false)
    private String salt;

    @OneToMany(mappedBy = "user")
    private List<PointResult> results = new ArrayList<>();

    // Конструктор по умолчанию (обязателен для JPA)
    public User() {}

    // Геттеры и сеттеры
    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() { return login; }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSalt() { return salt; }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPasswdHash() { return passwdHash; }

    public void setPasswdHash(String passwdHash) {
        this.passwdHash = passwdHash;
    }

    public List<PointResult> getResults() { return results; }

    public void setResults(List<PointResult> results) {
        this.results = results;
    }
}


