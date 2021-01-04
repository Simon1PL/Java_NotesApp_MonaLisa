package pl.edu.agh.monalisa.loader;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.edu.agh.monalisa.guice.MonaLisaModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoaderTest {


    void setupTmp(Path temp) throws IOException {
        temp.toFile().mkdir();
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
        Injector injector = Guice.createInjector(new MonaLisaModule());
        var loader = injector.getInstance(Loader.class);


        //when
        var model = loader.loadModel(temp);
        var year = model.getChildren().get(0);
        var subject = year.getChildren().get(0);
        var lab = subject.getChildren().get(0);
        var student = lab.getChildren().get(0);

        //then
        assertEquals(year.getName(), "2018");
        assertEquals(subject.getName(), "WDI");
        assertEquals(lab.getName(), "Lab1");
        assertEquals(student.getName(), "Student1");

        assertEquals(student.getChildren().get(0).getName(), "main.py");
    }

}