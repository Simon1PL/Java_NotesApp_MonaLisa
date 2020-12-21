package pl.edu.agh.monalisa.view;

import javafx.application.Platform;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.model.Paragraph;
import org.reactfx.collection.ListModification;

import java.util.function.Consumer;

public class VisibleParagraphStyler<PS, SEG, S> implements Consumer<ListModification<? extends Paragraph<PS, SEG, S>>> {
    private final GenericStyledArea<PS, SEG, S> area;
    private final StyleGenerator<S> styleGenerator;
    private int prevParagraph, prevTextLength;

    public VisibleParagraphStyler(GenericStyledArea<PS, SEG, S> area, StyleGenerator<S> styleGenerator) {
        this.styleGenerator = styleGenerator;
        this.area = area;
    }

    @Override
    public void accept(ListModification<? extends Paragraph<PS, SEG, S>> listModification) {
        if (listModification.getAddedSize() > 0) {
            int paragraph = Math.min(area.firstVisibleParToAllParIndex() + listModification.getFrom(), area.getParagraphs().size() - 1);
            String text = area.getText(paragraph, 0, paragraph, area.getParagraphLength(paragraph));

            if (paragraph != prevParagraph || text.length() != prevTextLength) {
                int startPos = area.getAbsolutePosition(paragraph, 0);
                Platform.runLater(() -> area.setStyleSpans(startPos, styleGenerator.computeStyles(text)));
                prevTextLength = text.length();
                prevParagraph = paragraph;
            }
        }
    }
}
