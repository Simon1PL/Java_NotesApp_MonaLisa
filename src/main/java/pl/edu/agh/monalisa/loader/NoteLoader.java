package pl.edu.agh.monalisa.loader;

import com.google.inject.Inject;
import pl.edu.agh.monalisa.model.AssignmentFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NoteLoader {

    @Inject
    public NoteLoader() {
    }

    public void setupAssignmentFile(AssignmentFile file) {
        var noteFile = Path.of(file.getPath().toString() + ".note");

        try {
            file.setNote(Files.readString(noteFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        file.noteProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Files.write(noteFile, newValue.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
