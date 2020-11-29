package pl.edu.agh.monalisa.controller;

import com.google.inject.Inject;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import pl.edu.agh.monalisa.model.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MonaLisaController {
    private final Loader loader;
    private Root model;

    @FXML
    private Label infoText;

    @Inject
    public MonaLisaController(Loader loader) {
        this.loader = loader;
    }


    @FXML
    public void initialize() {
        model = loader.loadModel(Path.of("MonaLisa"));

        for (Year year : model.getYears()) {
            for (Subject subject : year.getSubjects()) {
                for (Lab lab : subject.getLabs()) {
                    for (Student student : lab.getStudents()) {
                        student.getAssignments().addListener((ListChangeListener<AssignmentFile>) c -> updateVisualization());
                    }
                    lab.getStudents().addListener((ListChangeListener<Student>) c -> updateVisualization());
                }
                subject.getLabs().addListener((ListChangeListener<Lab>) c -> updateVisualization());
            }
            year.getSubjects().addListener((ListChangeListener<Subject>) c -> updateVisualization());

        }

        updateVisualization();
    }

    private void updateVisualization() {
        infoText.setText(getVisualizationString());
    }

    private String getVisualizationString() {
        StringBuilder sb = new StringBuilder();
        for (Year year : model.getYears()) {
            sb.append("\n>").append(year.getName());

            for (Subject subject : year.getSubjects()) {
                sb.append("\n\t>").append(subject.getName());
                for (Lab lab : subject.getLabs()) {
                    sb.append("\n\t\t>").append(lab.getName());
                    for (Student student : lab.getStudents()) {
                        sb.append("\n\t\t\t>").append(student.getName());
                        for (AssignmentFile assignment : student.getAssignments()) {
                            sb.append("\n\t\t\t\t>").append(assignment.getName());
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

}
