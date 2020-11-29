package pl.edu.agh.monalisa.model;

import java.io.IOException;

public abstract class File extends Package {
    public File(String name, Package parentDirectory) {
        super(name, parentDirectory);
    }

    @Override
    public void create() {
        java.io.File mainAppFile = new java.io.File(this.path);
        try {
            mainAppFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
