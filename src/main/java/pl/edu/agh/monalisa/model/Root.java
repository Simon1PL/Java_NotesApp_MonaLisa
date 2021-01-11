package pl.edu.agh.monalisa.model;

import java.nio.file.Path;
import java.util.List;

public class Root extends Package<Year> {

    public Root(String name, Path parent, List<Year> years) {
        super(name, parent, years);
    }

    public Root(String name, Path parent) {
        super(name, parent);
    }
}
