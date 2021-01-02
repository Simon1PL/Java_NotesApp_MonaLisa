package pl.edu.agh.monalisa;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.monalisa.guice.MonaLisaModule;
import pl.edu.agh.monalisa.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class MonaLisaApplication extends Application {

    public static void main(String[] args) {
        MonaLisaApplication.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Injector injector = Guice.createInjector(new MonaLisaModule());

        if (!injector.getInstance(Key.get(Path.class, Names.named("RootPath"))).toFile().exists()) {
            createExampleData(injector.getInstance(Key.get(Path.class, Names.named("RootPath"))));
        }

        var fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(injector::getInstance);

        fxmlLoader.setLocation(MonaLisaApplication.class.getResource("view/monalisa.fxml"));
        Parent parent = fxmlLoader.load();
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle("MonaLisa");
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("Stage is closing");
    }

    public static void createExampleData(Path appRootPath) {
        File mainAppFile = appRootPath.toFile();
        mainAppFile.mkdir();
        Year year1 = new Year("2018", mainAppFile.toPath());
        year1.create();
        Subject subject1 = new Subject("WDI", year1.getPath());
        subject1.create();
        Lab lab1 = new Lab("Lab1", subject1);
        lab1.create();
        Student student1 = new Student("Student1", lab1);
        student1.create();
        AssignmentFile assignmentFile1 = new AssignmentFile("main.py", student1);
        assignmentFile1.create();
        try {
            FileWriter fileWriter = new FileWriter(assignmentFile1.getPath().toFile());
            fileWriter.write("first line\nnext line");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
