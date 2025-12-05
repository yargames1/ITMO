package org.example.beans;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultsBean implements Serializable {

    private List<PointResult> results;

    @PostConstruct
    public void init() {
        results = new ArrayList<>();
    }

    public void addResult(PointResult result) {
        results.add(0, result);
    }

    public List<PointResult> getResults() {
        return results;
    }
}