package project.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UniversityJournal implements Serializable {
    private static final long serialVersionUID = 18L;

    private String name;
    private String publisher;
    private List<ResearchPaper> papers;

    public UniversityJournal() {
        this.papers = new ArrayList<>();
    }

    public UniversityJournal(String name, String publisher) {
        this.name = name;
        this.publisher = publisher;
        this.papers = new ArrayList<>();
    }

    public void addPaper(ResearchPaper paper) {
        if (!papers.contains(paper)) papers.add(paper);
    }

    public void removePaper(ResearchPaper paper) {
        papers.remove(paper);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniversityJournal that = (UniversityJournal) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(publisher, that.publisher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, publisher);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public List<ResearchPaper> getPapers() { return new ArrayList<>(papers); }

    @Override
    public String toString() {
        return "UniversityJournal{" +
                "name='" + name + "'" +
                ", publisher='" + publisher + "'" +
                ", papers=" + papers.size() +
                "}";
    }
}