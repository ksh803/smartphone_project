package kr.ac.project;

public class Memo {
    private String title;
    private String memo;
    private String timestamp;

    public Memo(String title, String memo, String timestamp) {
        this.title = title;
        this.memo = memo;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getMemo() {
        return memo;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
