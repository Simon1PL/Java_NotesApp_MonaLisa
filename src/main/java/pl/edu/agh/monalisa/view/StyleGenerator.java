package pl.edu.agh.monalisa.view;

import org.fxmisc.richtext.model.StyleSpans;

public interface StyleGenerator<S> {
    StyleSpans<S> computeStyles(String text);
}
