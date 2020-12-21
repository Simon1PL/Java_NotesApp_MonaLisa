package pl.edu.agh.monalisa.model;

import java.nio.file.Path;
import java.util.List;

public class Root extends Package {

    public Root(String name, Path parent, List<Year> years) {
        super(name, parent, years);
    }

    public void addYear(Year year) {
        this.addChild(year);
    }
}
