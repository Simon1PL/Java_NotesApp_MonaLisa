package pl.edu.agh.monalisa.model;

import io.reactivex.rxjava3.disposables.Disposable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AssignmentFile extends RegularFile {
//    private String text;
    private Disposable fileContentListener;
    private final StringProperty note;
    private final Student parent;

    public AssignmentFile(String name, Student parent) {
        super(name, parent.getPath());
        this.parent = parent;
        this.note = new SimpleStringProperty();
//        this.loadTextFromFile();
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

//    public void loadTextFromFile() {
//        try {
//            this.text = this.getPath().toFile().exists() ? Files.readString(this.getPath()) : "";
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
