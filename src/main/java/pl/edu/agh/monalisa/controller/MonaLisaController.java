package pl.edu.agh.monalisa.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import pl.edu.agh.monalisa.loader.FilesystemWatcher;
import pl.edu.agh.monalisa.model.*;
import pl.edu.agh.monalisa.loader.Loader;
import pl.edu.agh.monalisa.model.Package;
import pl.edu.agh.monalisa.view.StudentCell;
import pl.edu.agh.monalisa.view.VisibleParagraphStyler;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonaLisaController {
    @Inject
    @Named("RootPath")
    private Path rootPath;

    private final Loader loader;
    private final FilesystemWatcher watcher;
    private Root model;
    private AssignmentFile selectedFile;
    private int notesAmount = 1; //Do wywalenia potem

    @FXML
    private TreeView<Package> fileTree;

    @FXML
    private CodeArea fileView;

    @FXML
    private Button addNoteButton;

    @FXML
    private HBox container;

    @FXML
    private TextArea noteView;

    @FXML
    private ListView<Student> studentListView;

    private static final String[] KEYWORDS = new String[]{
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while", "def"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"   // for whole text processing (text blocks)
            + "|" + "/\\*[^\\v]*" + "|" + "^\\h*\\*([^\\v]*|/)";  // for visible paragraph processing (line by line)

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    @Inject
    public MonaLisaController(Loader loader, FilesystemWatcher watcher) {
        this.loader = loader;
        this.watcher = watcher;
    }

    @FXML
    public void initialize() {
        model = loader.loadModel(this.rootPath);
        fileTree.setRoot(new TreeItem<>(model));
        fileTree.setShowRoot(false);

        for (Year year : model.getYears()) {
            TreeItem<Package> yearItem = addTreeItem(fileTree.getRoot(), year);
            addListener(yearItem, year);
            for (Subject subject : year.getSubjects()) {
                var subjectItem = addTreeItem(yearItem, subject);
                addListener(subjectItem, subject);
                for (Lab lab : subject.getLabs()) {
                    var labItem = addTreeItem(subjectItem, lab);
                    addListener(labItem, lab);
                    for (Student student : lab.getStudents()) {
                        var studentItem = addTreeItem(labItem, student);
                        addListener(studentItem, student);

                        for (AssignmentFile assignmentFile : student.getAssignments())
                            addTreeItem(studentItem, assignmentFile);
                    }
                }
            }

        }
        fileView.setParagraphGraphicFactory(LineNumberFactory.get(fileView));
        fileView.getVisibleParagraphs().addModificationObserver(
                new VisibleParagraphStyler<>(fileView, this::computeHighlighting));
        fileView.getStylesheets().add(MonaLisaController.class.getResource("syntaxHighlighting.css").toExternalForm());


        fileTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getValue() instanceof AssignmentFile) {
                if (this.selectedFile != null) {
                    watcher.closeAssignmentFile(this.selectedFile);
                    this.selectedFile.noteProperty().unbindBidirectional(noteView.textProperty());
                    noteView.clear();
                }
                this.selectedFile = (AssignmentFile) newValue.getValue();
                var disposable = watcher.openAssignmentFile(this.selectedFile).subscribe(this.fileView::replaceText);
                this.selectedFile.setFileContentListener(disposable);

                noteView.setText(this.selectedFile.noteProperty().getValue());
                this.selectedFile.noteProperty().bindBidirectional(noteView.textProperty());

                studentListView.setItems(this.selectedFile.getParent().getParent().getStudents());
            }
        });

//        addNoteButton.setOnAction((actionEvent) -> {
//            if (this.selectedFile != null) this.selectedFile.addNote(new Note(this.notesAmount++, "title", "NOTE"));
//        });

        studentListView.setCellFactory(param -> new StudentCell());
    }

    private void updateTree(TreeItem<Package> parent, ListChangeListener.Change<? extends Package> change) {
        while (change.next())
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(pkg -> {
                    var newTreeItem = addTreeItem(parent, pkg);
                    if (pkg.getChildren() != null)
                        addListener(newTreeItem, pkg);
                });
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(pkg -> {
                    parent.getChildren().removeIf(treeItem -> treeItem.getValue().equals(pkg));
                });
            }
    }

    private void addListener(TreeItem<Package> parent, Package pkg) {
        pkg.getChildren().addListener((ListChangeListener<Package>) c -> updateTree(parent, c));
    }

    private TreeItem<Package> addTreeItem(TreeItem<Package> parent, Package item) {
        var newItem = new TreeItem<>(item);
        parent.getChildren().add(newItem);
        return newItem;
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
