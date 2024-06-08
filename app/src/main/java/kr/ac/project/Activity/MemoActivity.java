package kr.ac.project.Activity;

public class MemoActivity {
    private long id; // Changed from int to long
    private String title;
    private String text;
    private long timestamp;

    public MemoActivity(long id, String title, String text, long timestamp) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Getter and Setter methods
    public long getId() { // Changed from int to long
        return id;
    }

    public void setId(long id) { // Changed from int to long
        this.id = id;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
