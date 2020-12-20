package pl.edu.agh.monalisa.model;

import java.nio.file.Path;
import java.util.List;

public class Year extends Package {

    public Year(String name, Path parent) {
        this(name, parent, null);
    }

    public Year(String name, Path parent, List<Subject> subjects) {
        super(name, parent, subjects);
    }

    public void addSubject(Subject subject) {
        this.addChild(subject);
    }

    /*public ObservableList<Subject> getSubjects() {
        return FXCollections.observableList(this.getChildren().filtered(child -> child instanceof Subject));
        return FXCollections.observableList(this.getChildren().stream().filter(child -> child instanceof Subject).map(child -> (Subject)child).collect(Collectors.toList()));
    }*/
}
