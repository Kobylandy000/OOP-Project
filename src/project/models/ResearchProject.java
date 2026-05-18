package project.models;

import project.exceptions.NonResearchException;
import project.interfaces.Researcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResearchProject implements Serializable {
    private static final long serialVersionUID = 8L;

    private String topic;
    private List<ResearchPaper> publishedPapers;
    private List<Researcher> participants;

    public ResearchProject() {
        this.publishedPapers = new ArrayList<>();
        this.participants = new ArrayList<>();
    }

    public ResearchProject(String topic) {
        this();
        this.topic = topic;
    }

    public void publishPaper(ResearchPaper paper) {
        if (!publishedPapers.contains(paper)) {
            publishedPapers.add(paper);
            System.out.println("Paper published: " + paper.getTitle());
        }
    }

    public void addParticipant(Researcher researcher) throws NonResearchException {
        if (researcher == null) {
            throw new NonResearchException("Participant cannot be null.");
        }
        if (!participants.contains(researcher)) {
            participants.add(researcher);
            System.out.println("Researcher added: " + researcher);
        }
    }

    public void removeParticipant(Researcher researcher) {
        if (participants.remove(researcher)) {
            System.out.println("Researcher removed: " + researcher);
        }
    }

    public boolean isParticipant(Researcher researcher) {
        return participants.contains(researcher);
    }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public List<ResearchPaper> getPublishedPapers() {
        return new ArrayList<>(publishedPapers);
    }

    public List<Researcher> getParticipants() {
        return new ArrayList<>(participants);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchProject that = (ResearchProject) o;
        return Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic);
    }

    @Override
    public String toString() {
        return "ResearchProject{" +
                "topic='" + topic + "'" +
                ", papers=" + publishedPapers.size() +
                ", participants=" + participants.size() +
                "}";
    }
}