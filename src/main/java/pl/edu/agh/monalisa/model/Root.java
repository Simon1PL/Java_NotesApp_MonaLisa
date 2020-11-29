package pl.edu.agh.monalisa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Root {
    private final ObservableList<Year> years;

    public Root(List<Year> years) {
        this.years = FXCollections.observableList(years);
    }

    public void addYear(Year year) {
        years.add(year);
    }

    public void removeYear(Year year) {
        years.remove(year);
    }

    public ObservableList<Year> getYears() {
        return years;
    }
}
