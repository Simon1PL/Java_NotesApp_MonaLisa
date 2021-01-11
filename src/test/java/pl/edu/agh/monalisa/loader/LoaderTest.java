package pl.edu.agh.monalisa.loader;

import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

public class LoaderTest {

    void setupTmp(Path temp) throws IOException {
        var year = temp.resolve("2018");
        year.toFile().mkdir();

        var subject = year.resolve("WDI");
        subject.toFile().mkdir();

        var lab = subject.resolve("Lab1");
        lab.toFile().mkdir();

        var student = lab.resolve("Student1");
        student.toFile().mkdir();

        student.resolve("main.py").toFile().createNewFile();
    }

    @Test
    public void initialModelLoads(@TempDir Path temp) throws IOException {
        //given
        setupTmp(temp);
        var watcher = mock(FilesystemWatcher.class);
        when(watcher.register(any(), any())).thenReturn(mock(Observable.class));
        var loader = new Loader(watcher, mock(NoteLoader.class));

        //when
        var model = loader.loadModel(temp);

        //then
        verify(watcher, timeout(100).atLeastOnce()).register(model, FileType.DIRECTORY);
    }

}