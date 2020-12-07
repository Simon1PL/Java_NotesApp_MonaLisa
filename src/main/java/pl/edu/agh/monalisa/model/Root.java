package pl.edu.agh.monalisa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.List;

public class Root extends Package {
    private final ObservableList<Year> years;

    public Root(String name, Path parent, List<Year> years) {
        super(name, parent);
        this.years = FXCollections.observableList(years);
    }

    public void addYear(Year year) {
        years.add(year);
    }

    @Override
    public ObservableList<? extends Package> getChildren() {
        return getYears();
    }

    public void removeYear(Year year) {
        years.remove(year);
    }

    public ObservableList<Year> getYears() {
        return years;
    }
}
