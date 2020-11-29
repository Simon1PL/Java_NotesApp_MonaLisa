package pl.edu.agh.monalisa.model;

import java.util.Collection;
import java.util.HashSet;

public class Assigment extends Package {
    private String name;
    private Collection<Note> notes = new HashSet<>();
    private Collection<AssigmentFile> assigmentFiles = new HashSet<>();

    public Assigment(String name, Student student) {
        super(name, student);
        student.addAssigment(this);
    }

    public Collection<Note> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        this.notes.add(note);
    }

    public Collection<AssigmentFile> getAssigmentFiles() {
        return assigmentFiles;
    }

    public void addFile(AssigmentFile assigmentFile) {
        this.assigmentFiles.add(assigmentFile);
    }
}
