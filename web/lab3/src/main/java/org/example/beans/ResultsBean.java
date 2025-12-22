package org.example.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class ResultsBean implements Serializable {

    @Inject
    private PointResultRepository repository;

    private List<PointResult> results;

    @PostConstruct
    public void init() {
        try {
            results = repository.findAll();
        } catch (Exception e) {
            results = new ArrayList<>();
        }
    }

    public void addResult(PointResult result) {
        PointResult saved = repository.save(result);
        results.add(0, saved);
    }

    public void clearResults() {
        repository.deleteAll();
        results.clear();
    }

    public List<PointResult> getResults() {
        return results;
    }
}