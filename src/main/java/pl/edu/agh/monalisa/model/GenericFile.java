package pl.edu.agh.monalisa.model;

import java.io.File;
import java.io.IOException;

public abstract class GenericFile extends Package {
    public GenericFile(String name, Package parentDirectory) {
        super(name, parentDirectory);
    }

    @Override
    public void create() {
        File file = this.path.toFile();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
