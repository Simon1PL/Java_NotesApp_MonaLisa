package pl.edu.agh.monalisa.loader;

import java.io.File;

public interface ItemChangeListener {
    void run(File changedFile);
}
