package pl.edu.agh.monalisa.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pl.edu.agh.monalisa.model.AssignmentFile;

import java.io.IOException;
import java.util.function.Consumer;

public class NoteCell extends ListCell<AssignmentFile> {

    @FXML
    private VBox vbox;
    @FXML
    private Button showButton;
    @FXML
    private Button copyButton;
    @FXML
    private Label titleLabel;
    @FXML
    private TextArea noteText;

    private final Consumer<AssignmentFile> showCallback;

    public NoteCell(Consumer<AssignmentFile> showCallback) {
        this.showCallback = showCallback;
    }

    @Override
    protected void updateItem(AssignmentFile item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            loadFXML();
            initShowButton(item);
            initCopyButton();
            updateTitleLabel(item);
            updateNoteText(item);
            setGraphic(vbox);
        } else {
            setGraphic(new Pane());
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

    private void initShowButton(AssignmentFile assignmentFile) {
        showButton.setOnAction(event -> showCallback.accept(assignmentFile));
    }

    private void initCopyButton() {
        copyButton.setOnAction(event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(noteText.getText());
            clipboard.setContent(content);
        });
    }

    private void updateTitleLabel(AssignmentFile file) {
        titleLabel.setText(file.getParent().getName() + ", " + file.getName());
    }

    private void updateNoteText(AssignmentFile file) {
        noteText.textProperty().unbind();
        noteText.textProperty().bind(file.noteProperty());
    }

}
