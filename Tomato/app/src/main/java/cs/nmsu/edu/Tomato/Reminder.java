public class Reminder {
    private String title;
    private String description;
    private long time;

    public Reminder(String title, String description, long time) {
        this.title = title;
        this.description = description;
        this.time = time;
    }

    // Getters and setters for the Reminder properties

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
