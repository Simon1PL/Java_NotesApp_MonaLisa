package pl.edu.agh.monalisa.loader;

import com.google.inject.Guice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.edu.agh.monalisa.guice.MonaLisaModule;
import pl.edu.agh.monalisa.model.AssignmentFile;
import pl.edu.agh.monalisa.model.Package;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class FilesystemWatcherTest {

    @Test
    public void register_shouldSendCreatedEvent(@TempDir Path temp) throws IOException, InterruptedException {
        //given
        var injector = Guice.createInjector(new MonaLisaModule());
        FilesystemWatcher watcher = injector.getInstance(FilesystemWatcher.class);
        var pkg = mock(Package.class);
        when(pkg.getPath()).thenReturn(temp);
        var testFilePath = temp.resolve(Path.of("test"));

        //when
        var testObserver = watcher.register(pkg, FileType.DIRECTORY)
                .test();
        Thread.sleep(1000);
        Files.createDirectory(testFilePath);
        Thread.sleep(1000);

        //then
        testObserver
                .assertNoErrors()
                .assertValue(fileSystemEvent ->
                        fileSystemEvent.getKind() == FileSystemEvent.EventKind.CREATED
                                && fileSystemEvent.getTarget().equals(testFilePath)
                );
    }


    @Test
    public void register_shouldSendOnlyFilesWhenFilterEnabled(@TempDir Path temp) throws IOException, InterruptedException {
        //given
        var injector = Guice.createInjector(new MonaLisaModule());
        FilesystemWatcher watcher = injector.getInstance(FilesystemWatcher.class);
        var pkg = mock(Package.class);
        when(pkg.getPath()).thenReturn(temp);
        var testFilePath = temp.resolve(Path.of("test.txt"));

        //when
        var testObserver = watcher.register(pkg, FileType.FILE)
                .test();
        Thread.sleep(1000);
        Files.createDirectory(temp.resolve(Path.of("test")));
        Files.createFile(testFilePath);
        Thread.sleep(1000);

        //then
        testObserver
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(fileSystemEvent -> fileSystemEvent.getTarget().equals(testFilePath));
    }

    @Test
    public void register_shouldSkipNoteFiles(@TempDir Path temp) throws InterruptedException, IOException {
        //given
        var injector = Guice.createInjector(new MonaLisaModule());
        FilesystemWatcher watcher = injector.getInstance(FilesystemWatcher.class);
        var pkg = mock(Package.class);
        when(pkg.getPath()).thenReturn(temp);
        var testFilePath = temp.resolve(Path.of("test.txt.note"));

        //when
        var testObserver = watcher.register(pkg, FileType.FILE)
                .test();
        Thread.sleep(1000);
        Files.createFile(testFilePath);
        Thread.sleep(1000);

        //then
        testObserver
                .assertNoErrors()
                .assertEmpty();
    }

    @Test
    public void openAssignmentFile_shouldReadFileContents(@TempDir Path temp) throws InterruptedException, IOException {
        //given
        var injector = Guice.createInjector(new MonaLisaModule());
        FilesystemWatcher watcher = injector.getInstance(FilesystemWatcher.class);
        var assignment = mock(AssignmentFile.class);
        var testFilePath = temp.resolve(Path.of("test.txt"));
        when(assignment.getPath()).thenReturn(testFilePath);
        var fileContent = "test file content";
        Files.writeString(testFilePath, fileContent);

        //when
        var testObserver = watcher.openAssignmentFile(assignment)
                .test();
        Thread.sleep(1000);

        //then
        testObserver
                .assertNoErrors()
                .assertValue(fileContent);
    }

    @Test
    public void openAssignmentFile_shouldReadUpdatedFileContents(@TempDir Path temp) throws IOException, InterruptedException {
        //given
        var injector = Guice.createInjector(new MonaLisaModule());
        FilesystemWatcher watcher = injector.getInstance(FilesystemWatcher.class);
        var assignment = mock(AssignmentFile.class);
        var testFilePath = temp.resolve(Path.of("test.txt"));
        when(assignment.getPath()).thenReturn(testFilePath);
        var fileContent = "test file content";
        Files.writeString(testFilePath, "another content");

        //when
        var testObserver = watcher.openAssignmentFile(assignment)
                .test();
        Thread.sleep(1000);
        Files.writeString(testFilePath, fileContent);
        Thread.sleep(1000);

        //then
        testObserver
                .assertNoErrors()
                .assertValueAt(1, fileContent);
    }
}
