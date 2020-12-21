package pl.edu.agh.monalisa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Package extends FileOrPackage {
    private ObservableList<FileOrPackage> children = FXCollections.observableArrayList();

    public Package(String name, Path parentDirectoryPath, List<? extends FileOrPackage> children) {
        super(name, parentDirectoryPath);
        if (children != null) {
            this.setChildren(children);
        }
    }

    public ObservableList<? extends FileOrPackage> getChildren() {
        return this.children;
    }

    private void setChildren(List<? extends FileOrPackage> children) {
        this.children = FXCollections.observableList(children.stream().map(child -> (FileOrPackage) child).collect(Collectors.toList()));
    }

    public void addChild(FileOrPackage child) {
        children.add(child);
    }

    public void removeChild(Path path) {
        children.removeIf(child -> child.getPath().equals(path));
    }

    public void create() {
        java.io.File mainAppFile = this.getPath().toFile();
        mainAppFile.mkdir();
    }

    public void delete() {
        deleteRecursive(this.getPath().toFile());
    }

    private void deleteRecursive(java.io.File dir) {
        java.io.File[] files = dir.listFiles();
        if (files != null) {
            for (final java.io.File file : files) {
                deleteRecursive(file);
            }
        }
        dir.delete();
    }
}
