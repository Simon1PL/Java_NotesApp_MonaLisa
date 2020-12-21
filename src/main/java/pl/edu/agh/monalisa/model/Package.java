package pl.edu.agh.monalisa.model;

import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.List;

public abstract class Package extends GenericFile {


    public Package(String name, Path parentDirectory) {
        super(name, parentDirectory);
    }

    public Package(String name, Path parentDirectory, List<? extends GenericFile> children) {
        super(name, parentDirectory);
    }

    public abstract ObservableList<? extends Package> getChildrenPackages();

    public abstract ObservableList<? extends GenericFile> getChildren();
}
