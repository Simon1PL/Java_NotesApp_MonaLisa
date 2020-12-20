package pl.edu.agh.monalisa.loader;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import pl.edu.agh.monalisa.guice.MonaLisaModule;
import pl.edu.agh.monalisa.model.Lab;
import pl.edu.agh.monalisa.model.Subject;
import pl.edu.agh.monalisa.model.Year;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class LoaderTest {

    private static final Path tmpPath = Path.of("testTmp");

    @BeforeEach
    void setupTmp() throws IOException {
        tmpPath.toFile().mkdir();
        var year = tmpPath.resolve("2018");
        year.toFile().mkdir();

        var subject = year.resolve("WDI");
        subject.toFile().mkdir();

        var lab = subject.resolve("Lab1");
        lab.toFile().mkdir();

        var student = lab.resolve("Student1");
        student.toFile().mkdir();

        student.resolve("main.py").toFile().createNewFile();
    }

    @AfterEach
    void deleteTmp() throws IOException {
        Files.walk(tmpPath)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }


    @Test
    public void initialModelLoads() {
        Injector injector = Guice.createInjector(new MonaLisaModule());
        var loader = injector.getInstance(Loader.class);

        var model = loader.loadModel(tmpPath);

        var year = model.getChildren().get(0);
        assertEquals(year.getName(), "2018");


        var subject = ((Year)year).getChildren().get(0);
        assertEquals(subject.getName(), "WDI");


        var lab = ((Subject)subject).getChildren().get(0);
        assertEquals(lab.getName(), "Lab1");

        var student = ((Lab)lab).getStudents().get(0);
        assertEquals(student.getName(), "Student1");

        assertEquals(student.getChildren().get(0).getName(), "main.py");
    }

    public void modelUpdatedOnDirectoryCreated(){
        Injector injector = Guice.createInjector(new MonaLisaModule());
        var loader = injector.getInstance(Loader.class);

        var model = loader.loadModel(tmpPath);

        model.getPath().resolve("2019").toFile().mkdir();

        assertEquals(2, model.getChildren().size());
    }

    public void modelUpdatesOnFileDeleted(){
        Injector injector = Guice.createInjector(new MonaLisaModule());
        var loader = injector.getInstance(Loader.class);

        var model = loader.loadModel(tmpPath);
    }

    public void modelNotUpdatedOnIncorrectFolderStructure(){
        Injector injector = Guice.createInjector(new MonaLisaModule());
        var loader = injector.getInstance(Loader.class);

        var model = loader.loadModel(tmpPath);
    }
}