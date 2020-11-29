package pl.edu.agh.monalisa;

import com.google.gson.GsonBuilder;
import pl.edu.agh.monalisa.model.*;

import java.util.LinkedList;
import java.util.List;

public class MonaLisaApplication {
    public static void main(String[] args) {
        List<Year> years = new LinkedList<>();

        Year year1 = new Year("2018");
        Subject subject1 = new Subject("WDI");
        Lab lab1 = new Lab("Lab1");
        Student student1 = new Student("Student1", "");
        Assigment assigment1 = new Assigment("zad1");
        AssigmentFile assigmentFile1 = new AssigmentFile("main.py");
        Note note1 = new Note();

        years.add(year1);
        years.stream().filter(year -> year.getName().equals("2018")).findFirst().ifPresent(year -> year.addSubject(subject1));
        subject1.addLab(lab1);
        lab1.addStudent(student1);
        student1.addAssigment(assigment1);
        assigment1.addFile(assigmentFile1);
        assigment1.addNote(note1);

        year1.setName("2020");
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(years));
    }
}
