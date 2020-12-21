package pl.edu.agh.monalisa.loader;

import java.io.File;

public enum FileType {
    FILE,
    DIRECTORY;

    public static FileType fromFile(File file) {
        if (file.isDirectory()) return DIRECTORY;
        else return FILE;
    }
}
