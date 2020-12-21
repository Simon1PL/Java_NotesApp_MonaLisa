package pl.edu.agh.monalisa.model;

import java.nio.file.Path;

public abstract class FileOrPackage {
    private String name;
    private Path path;

    public FileOrPackage(String name, Path parentDirectoryPath) {
        this.name = name;
        if (parentDirectoryPath == null) {
            path = Path.of(this.name);
        } else {
            this.path = parentDirectoryPath.resolve(Path.of(this.name));
        }
    }

    public abstract void create();

    public abstract void delete();

    public void setName(String name) {
        java.io.File oldFile = this.getPath().toFile();
        java.io.File newFile = new java.io.File(oldFile.getParentFile(), name);
        oldFile.renameTo(newFile);
        this.name = newFile.getName();
        this.path = newFile.toPath();
    }

    public String getName() {
        return name;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
