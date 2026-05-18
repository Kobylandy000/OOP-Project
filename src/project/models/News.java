package project.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class News implements Serializable {
    private static final long serialVersionUID = 14L;

    private String title;
    private String content;
    private String topic;
    private boolean pinned;
    private LocalDateTime createdAt;
    private List<String> comments;

    public News() {
        this.comments = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public News(String title, String content, String topic) {
        this.title = title; // атауы
        this.content = content; // мазмұны
        this.topic = topic; // жаңалық категориясы ("Research", "General", "Event", "Sport")
        this.pinned = "Research".equalsIgnoreCase(topic); // маңыздылығы Researhcer болса ол маңызды болып табылады
        this.createdAt = LocalDateTime.now();
        this.comments = new ArrayList<>();
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public void announcePaper(ResearchPaper paper) { // announce paper - мақаланы хабарлау
        System.out.println("=== NEW PAPER ANNOUNCED ===");
        System.out.println("Title  : " + paper.getTitle());
        System.out.println("Journal: " + paper.getJournal());
        System.out.println("Date   : " + paper.getDatePublished());
    }

    @Override
    public boolean equals(Object o)     {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(title, news.title) &&
                Objects.equals(createdAt, news.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, createdAt);
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) {
        this.topic = topic;
        this.pinned = "Research".equalsIgnoreCase(topic);
    }

    public boolean isPinned() { return pinned; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<String> getComments() { return new ArrayList<>(comments); }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + "'" +
                ", topic='" + topic + "'" +
                ", pinned=" + pinned +
                ", createdAt=" + createdAt +
                "}";
    }
}