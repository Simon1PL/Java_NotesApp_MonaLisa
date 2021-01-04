package pl.edu.agh.monalisa.view;

import javafx.scene.control.ListView;
import pl.edu.agh.monalisa.model.AssignmentFile;

import java.util.stream.Collectors;


public class NoteList extends ListView<AssignmentFile> {

    public NoteList() {
        setCellFactory(param -> new NoteCell());
    }

    public void setSelectedFile(AssignmentFile file) {
        getItems().clear();

        var lab = file.getParent().getParent();
        var assignmentList = lab.getChildren()
                .stream()
                .flatMap(student -> student.getChildren().stream())
                .collect(Collectors.toList());
        getItems().addAll(assignmentList);
    }

}
