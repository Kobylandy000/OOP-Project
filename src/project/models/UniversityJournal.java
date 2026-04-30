package project.models;

import java.util.ArrayList;
import java.util.List;

public class UniversityJournal {
    private String name;
    private String publisher;
    private List<ResearchPaper> papers;

    public UniversityJournal(String name, String publisher) {
        this.name = name;
        this.publisher = publisher;
        this.papers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<ResearchPaper> getPapers() {
        return new ArrayList<>(papers);
    }

    public void addPaper(ResearchPaper paper) {
        if (!papers.contains(paper)) {
            papers.add(paper);
        }
    }

    public void removePaper(ResearchPaper paper) {
        papers.remove(paper);
    }
}