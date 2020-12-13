package pl.edu.agh.monalisa.model;

import com.google.gson.Gson;
import javafx.collections.ObservableList;
import pl.edu.agh.monalisa.constants.AvailableExtensionsEnum;
import pl.edu.agh.monalisa.constants.AvailableExtensionsEnum.AvailableExtensions;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;

public class AssignmentFile extends GenericFile {
    private AvailableExtensions extension;
    private Notes notes = new Notes();
    private String text;
    private Path notesPath;

    public AssignmentFile(String name, Path parent) {
        super(name, parent);
        this.extension = AvailableExtensionsEnum.getExtension(name);
        try {
            this.text = this.getPath().toFile().exists() ? Files.readString(this.getPath()) : "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.notesPath = this.getPath().getParent().resolve(this.getName().replace(".", "") + "notes.json");
        try {
            if (this.notesPath.toFile().exists()) {
                this.notes = new Gson().fromJson(Files.readString(this.notesPath), Notes.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getText() {
        return text;
    }

    @Override
    public ObservableList<? extends Package> getChildren() {
        return null;
    }

    public void addNote(Note note) {
        Collection<Note> notesTmp = this.notes.getNotes();
        if (notesTmp.stream().anyMatch(n -> n.getLine() == note.getLine())) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notesTmp.add(note);
        this.notes.setNotes(notesTmp);
        this.saveNotes();
    }

    public void removeNote(Note note) {
        Collection<Note> notesTmp = this.notes.getNotes();
        Note removedNote = notesTmp.stream().filter(n -> n.getLine() == note.getLine()).findFirst().get();
        notesTmp.remove(removedNote);
        this.notes.setNotes(notesTmp);
        this.saveNotes();
    }

    public void editNote(Note note) {
        Collection<Note> notesTmp = this.notes.getNotes();
        Note editedNote = notesTmp.stream().filter(n -> n.getLine() == note.getLine()).findFirst().get();
        notesTmp.remove(editedNote);
        notesTmp.add(note);
        this.notes.setNotes(notesTmp);
        this.saveNotes();
    }

    public void saveNotes() {
        try {
            if (!this.notesPath.toFile().exists()) {
                this.notesPath.toFile().createNewFile();
            }
            FileWriter fileWriter = new FileWriter(this.notesPath.toFile());
            fileWriter.write(new Gson().toJson(this.notes));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
