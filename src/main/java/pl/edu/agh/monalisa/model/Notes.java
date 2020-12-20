package pl.edu.agh.monalisa.model;

import java.util.Collection;
import java.util.HashSet;

public class Notes {
    private Collection<Note> notes = new HashSet<>();

    public Collection<Note> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        if (this.notes.stream().anyMatch(n -> n.getLine() == note.getLine())) {
            try {
                throw new Exception("Cant add more than 1 comment for the same line");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.notes.add(note);
    }

    public void removeNote(Note note) {
        Note removedNote = this.notes.stream()
                .filter(n -> n.getLine() == note.getLine())
                .findFirst().orElseThrow();
        this.notes.remove(removedNote);
    }

    public void editNote(Note note) {
        Note editedNote = this.notes.stream()
                .filter(n -> n.getLine() == note.getLine())
                .findFirst().orElseThrow();
        this.notes.remove(editedNote);
        this.notes.add(note);
    }
}
