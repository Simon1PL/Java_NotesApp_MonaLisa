package pl.edu.agh.monalisa.model;

import java.util.Set;

public class Root {
    private final Set<Year> years;

    public Root(Set<Year> years) {
        this.years = years;
    }

    public void addYear(Year year) {
        years.add(year);
    }

    public void removeYear(Year year) {
        years.remove(year);
    }

    public Set<Year> getYears() {
        return years;
    }
}
