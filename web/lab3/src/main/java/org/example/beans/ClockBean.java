package org.example.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named("clockBean")
@RequestScoped
public class ClockBean implements Serializable {

    public String getCurrentTime() {

        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}