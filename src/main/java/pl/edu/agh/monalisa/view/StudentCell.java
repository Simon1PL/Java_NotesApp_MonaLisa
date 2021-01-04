package pl.edu.agh.monalisa.view;

import javafx.scene.control.ListCell;
import pl.edu.agh.monalisa.model.Student;

public class StudentCell extends ListCell<Student> {

    @Override
    protected void updateItem(Student item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            setText(item.getName());
        } else {
            setText("");
        }
    }
}
