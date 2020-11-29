package pl.edu.agh.monalisa.model;

import java.util.Collection;
import java.util.HashSet;

public class Student extends Package {
    private String name;
    private String surname;
    private Collection<Assigment> assigments = new HashSet<>();

    public Student(String fileName, Lab lab) {
        super(fileName, lab);
        if (fileName.split(" ", 2).length == 2) {
            this.name = fileName.split(" ", 2)[0];
            this.surname = fileName.split(" ", 2)[1];
        }
        lab.addStudent(this);
    }

    public void addAssigment(Assigment assigment) {
        this.assigments.add(assigment);
    }

    public Collection<Assigment> getLabs() {
        return assigments;
    }
}
