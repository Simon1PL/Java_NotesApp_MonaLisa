package pl.edu.agh.monalisa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class Subject extends Package {
    private ObservableList<Lab> labs;

    public Subject(String name, Path parent) {
        super(name, parent);
        this.labs = FXCollections.observableArrayList();
    }

    public Subject(String name, Path parent, List<Lab> labs) {
        super(name, parent);
        this.labs = FXCollections.observableList(labs);
    }

    public void addLab(Lab subject) {
        this.labs.add(subject);
    }

    public ObservableList<Lab> getLabs() {
        return labs;
    }
}
