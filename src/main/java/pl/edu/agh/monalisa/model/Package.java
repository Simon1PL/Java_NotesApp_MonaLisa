package pl.edu.agh.monalisa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.List;

public abstract class Package<T extends GenericFile> extends GenericFile {

    protected final ObservableList<T> children;

    public Package(String name, Path parentDirectory) {
        super(name, parentDirectory);
        children = FXCollections.observableArrayList();
    }

    public Package(String name, Path parentDirectory, List<T> children) {
        //TODO change to this
        super(name, parentDirectory);
        this.children = FXCollections.observableList(children);
    }

    public ObservableList<T> getChildren() {
        return children;
    }

    public void addChild(T child) {
        children.add(child);
    }
}
