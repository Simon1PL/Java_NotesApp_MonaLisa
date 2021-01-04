package pl.edu.agh.monalisa.loader;

import com.google.inject.Guice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.edu.agh.monalisa.guice.MonaLisaModule;
import pl.edu.agh.monalisa.model.AssignmentFile;
import pl.edu.agh.monalisa.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NoteLoaderTest {

    private static final String ASSIGNMENT_FILE_NAME = "test_assignment.txt";
    private static final String NOTE_FILE_NAME = "test_assignment.txt.note";

    @Test
    public void shouldCreateNoteFile(@TempDir Path temp) {
        //given
        var student = mock(Student.class);
        when(student.getPath()).thenReturn(temp);
        var file = new AssignmentFile(ASSIGNMENT_FILE_NAME, student);

        var injector = Guice.createInjector(new MonaLisaModule());
        NoteLoader loader = injector.getInstance(NoteLoader.class);

        //when
        loader.setupAssignmentFile(file);

        //then
        assertTrue(temp.resolve(NOTE_FILE_NAME).toFile().exists());
    }

    @Test
    public void shouldReadNoteFile(@TempDir Path temp) throws IOException {
        //given
        var student = mock(Student.class);
        when(student.getPath()).thenReturn(temp);
        var file = new AssignmentFile(ASSIGNMENT_FILE_NAME, student);

        var injector = Guice.createInjector(new MonaLisaModule());
        NoteLoader loader = injector.getInstance(NoteLoader.class);
        var noteContent = "note content";
        Files.writeString(temp.resolve(Path.of(NOTE_FILE_NAME)), noteContent);

        //when
        loader.setupAssignmentFile(file);

        //then
        assertEquals(noteContent, file.noteProperty().get());

    }

    @Test
    public void shouldSaveToNoteFile(@TempDir Path temp) throws IOException {
        //given
        var student = mock(Student.class);
        when(student.getPath()).thenReturn(temp);
        var file = new AssignmentFile(ASSIGNMENT_FILE_NAME, student);

        var injector = Guice.createInjector(new MonaLisaModule());
        NoteLoader loader = injector.getInstance(NoteLoader.class);
        var noteContent = "note content";

        //when
        loader.setupAssignmentFile(file);
        file.noteProperty().set(noteContent);

        //then
        assertEquals(noteContent, Files.readString(temp.resolve(Path.of(NOTE_FILE_NAME))));
    }
}
