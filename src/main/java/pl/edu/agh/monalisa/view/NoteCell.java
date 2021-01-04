package pl.edu.agh.monalisa.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import pl.edu.agh.monalisa.model.AssignmentFile;

import java.io.IOException;

public class NoteCell extends ListCell<AssignmentFile> {

    @FXML
    private VBox vbox;
    @FXML
    private Button showButton;
    @FXML
    private Button copyButton;
    @FXML
    private Label titleLabel;

    @Override
    protected void updateItem(AssignmentFile item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            loadFXML();
            initShowButton();
            initCopyButton();
            updateTitleLabel();
            updateNoteText();
            setGraphic(vbox);
        }
    }

    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("noteListItem.fxml"));
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initShowButton() {

    }

    private void initCopyButton() {

    }

    private void updateTitleLabel() {

    }

    private void updateNoteText() {

    }


}
