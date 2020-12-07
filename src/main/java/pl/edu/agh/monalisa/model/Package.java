package pl.edu.agh.monalisa.model;

import javafx.collections.ObservableList;

import java.io.File;
import java.nio.file.Path;

public abstract class Package {
    private String name;
    private Path path;

    public Package(String name, Path parentDirectoryPath) {
        this.name = name;
        if (parentDirectoryPath == null)
            path = Path.of(this.name);
        else
            this.path = parentDirectoryPath.resolve(Path.of(this.name));
    }

    public Package(String name, Package parentDirectory) {
        this.name = name;
        this.path = parentDirectory.path.resolve(Path.of(this.name));
    }

    public void create() {
        File mainAppFile = this.path.toFile();
        mainAppFile.mkdir();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    } // w setName trzeba zrobić aby nazwa była edytowana na dysku

    public Path getPath() {
        return path;
    }

    public abstract ObservableList<? extends Package> getChildren();
}
