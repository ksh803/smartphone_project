package kr.ac.project;

public class Memo {
    private String category;
    private String memo;
    private String title;
    private String timestamp;

    public Memo(String category, String memo, String timestamp) {
        this.category = category;
        this.memo = memo;
        this.title = title;
        this.timestamp = timestamp;
    }

    public String getCategory() {
        return category;
    }

    public String getMemo() {
        return memo;
    }

    public String getTitle() {
        return title;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
