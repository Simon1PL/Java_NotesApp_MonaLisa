package pl.edu.agh.monalisa;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.monalisa.guice.MonaLisaModule;
import pl.edu.agh.monalisa.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MonaLisaApplication extends Application {
    public static void printFilesStructure(String rootPath) {
        try {
            Files.walk(Paths.get(rootPath))
                    .forEach(path -> System.out.println(path.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createExampleData(String mainAppFilePath) {
        File mainAppFile = new File(mainAppFilePath);
        mainAppFile.mkdir();
        Year year1 = new Year("2018", mainAppFile.toPath());
        year1.create();
        Subject subject1 = new Subject("WDI", year1.getPath());
        subject1.create();
        Lab lab1 = new Lab("Lab1", subject1);
        lab1.create();
        Student student1 = new Student("Student1", lab1.getPath());
        student1.create();
        AssignmentFile assignmentFile1 = new AssignmentFile("main.py", student1.getPath());
        assignmentFile1.create();
    }

    public static void main(String[] args) {
        String mainAppFilePath = System.getProperty("user.dir") + "\\MonaLisa";
        createExampleData(mainAppFilePath);
        printFilesStructure(mainAppFilePath);

        MonaLisaApplication.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Injector injector = Guice.createInjector(new MonaLisaModule());
        var fxmlLoader = new FXMLLoader();

        fxmlLoader.setControllerFactory(injector::getInstance);

        fxmlLoader.setLocation(MonaLisaApplication.class.getResource("view/monalisa.fxml"));
        Parent parent = fxmlLoader.load();
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle("MonaLisa");
        primaryStage.show();
    }
}
