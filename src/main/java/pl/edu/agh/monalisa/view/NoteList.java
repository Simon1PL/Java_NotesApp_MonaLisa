package pl.edu.agh.monalisa.view;

import javafx.scene.control.ListView;
import pl.edu.agh.monalisa.model.AssignmentFile;

import java.util.function.Consumer;
import java.util.stream.Collectors;


public class NoteList extends ListView<AssignmentFile> {

    private Consumer<AssignmentFile> showCallback;

    public NoteList() {
        setCellFactory(param -> new NoteCell(showCallback));
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

    public void setOnShowClicked(Consumer<AssignmentFile> callback) {
        this.showCallback = callback;
    }

}
