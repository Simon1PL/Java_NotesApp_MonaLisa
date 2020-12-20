package pl.edu.agh.monalisa.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public abstract class GenericFile extends FileOrPackage {
    public GenericFile(String name, Path parentDirectory) {
        super(name, parentDirectory);
    }

    public void create() {
        File file = this.getPath().toFile();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        this.getPath().toFile().delete();
    }
}
