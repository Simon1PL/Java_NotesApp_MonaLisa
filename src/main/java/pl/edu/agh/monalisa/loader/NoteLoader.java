package pl.edu.agh.monalisa.loader;

import com.google.inject.Inject;
import pl.edu.agh.monalisa.model.AssignmentFile;
import pl.edu.agh.monalisa.model.Note;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NoteLoader {

    @Inject
    public NoteLoader() {
    }

    public void setupAssignmentFile(AssignmentFile file) {
        var notePath = Path.of(file.getPath().toString() + Note.NOTE_EXTENSION);
        File noteFile = notePath.toFile();
        if (noteFile.isDirectory())
            throw new IllegalStateException("Note file " + notePath.toString() + " is a directory.");


        try {
            if (!noteFile.exists()) {
                noteFile.createNewFile();
            }
            file.setNote(Files.readString(notePath));

        } catch (IOException e) {
            e.printStackTrace();
        }

        file.noteProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Files.write(notePath, newValue.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
