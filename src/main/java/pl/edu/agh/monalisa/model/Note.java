package pl.edu.agh.monalisa.model;

//TODO this is never used
public class Note {

    private int line;
    private String title;
    private String text;

    public Note(int line, String title, String text) {
        this.line = line;
        this.title = title;
        this.text = text;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
