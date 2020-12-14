package pl.edu.agh.monalisa.model;

import com.google.gson.Gson;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import pl.edu.agh.monalisa.constants.AvailableExtensionsEnum;
import pl.edu.agh.monalisa.constants.AvailableExtensionsEnum.AvailableExtensions;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AssignmentFile extends GenericFile {
    private AvailableExtensions extension;
    private Notes notes = new Notes();//not used currently
    private String text;
    private Path notesPath;
    private Disposable fileContentListener;
    private final StringProperty note;
    private final Student parent;

    public AssignmentFile(String name, Student parent) {
        super(name, parent.getPath());
        this.parent = parent;
        this.extension = AvailableExtensionsEnum.getExtension(name);
        this.loadTextFromFile();
        this.notesPath = this.getPath().getParent().resolve(this.getName().replace(".", "") + "notes.json");
        this.note = new SimpleStringProperty();
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

    public void loadTextFromFile() {
        try {
            this.text = this.getPath().toFile().exists() ? Files.readString(this.getPath()) : "";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<? extends Package> getChildren() {
        return null;
    }

    public void addNote(Note note) {
        this.notes.addNote(note);
        this.saveNotes();
    }

    public void removeNote(Note note) {
        this.notes.removeNote(note);
        this.saveNotes();
    }

    public void editNote(Note note) {
        this.notes.editNote(note);
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

    public Disposable getFileContentListener() {
        return fileContentListener;
    }

    public void setFileContentListener(Disposable fileContentListener) {
        this.fileContentListener = fileContentListener;
    }

    public StringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }

    public Student getParent() {
        return parent;
    }
}
