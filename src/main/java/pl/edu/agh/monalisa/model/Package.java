package pl.edu.agh.monalisa.model;

import java.io.File;

public abstract class Package {
    String name;
    String path;

    public Package(String name, String parentDirectoryPath) {
        this.name = name;
        this.path = parentDirectoryPath + "\\" + this.name;
    }

    public Package(String name, Package parentDirectory) {
        this.name = name;
        this.path = parentDirectory.path + "\\" + this.name;
    }

    public void create() {
        File mainAppFile = new File(this.path);
        mainAppFile.mkdir();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    } // w setName trzeba zrobić aby nazwa była edytowana na dysku
}
