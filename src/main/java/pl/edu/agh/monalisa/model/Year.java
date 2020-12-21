package pl.edu.agh.monalisa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.List;

public class Year extends Package {
    private final ObservableList<Subject> subjects;

    public Year(String name, Path path) {
        super(name, path);
        this.subjects = FXCollections.observableArrayList();
    }

    public Year(String name, Path parentDirectoryPath, List<Subject> subjects) {
        super(name, parentDirectoryPath);
        this.subjects = FXCollections.observableList(subjects);
    }

    @Override
    public ObservableList<? extends Package> getChildrenPackages() {
        return getSubjects();
    }

    @Override
    public ObservableList<? extends GenericFile> getChildren() {
        return getSubjects();
    }

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    public ObservableList<Subject> getSubjects() {
        return subjects;
    }
}
