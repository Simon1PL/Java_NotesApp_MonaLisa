package pl.edu.agh.monalisa.model;

import java.util.Collection;
import java.util.HashSet;

public class Student {
    private String name;
    private String surname;
    private Collection<Assigment> assigments = new HashSet<>();

    public Student(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void addAssigment(Assigment assigment) {
        this.assigments.add(assigment);
    }

    public Collection<Assigment> getLabs() {
        return assigments;
    }
}
