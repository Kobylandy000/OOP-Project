package project.publisher;

import project.interfaces.Researcher;
import project.models.ResearchPaper;

import java.util.ArrayList;
import java.util.List;

public class ResearchPaperPublisher {
    private List<Researcher> subscribers = new ArrayList<>();
    private List<ResearchPaper> papers = new ArrayList<>();

    public void subscribe(Researcher researcher) {
        if (!subscribers.contains(researcher)) {
            subscribers.add(researcher);
            System.out.println("Subscribed to publisher.");
        }
    }

    public void unsubscribe(Researcher researcher) {
        subscribers.remove(researcher);
        System.out.println("Unsubscribed from publisher.");
    }

    public void publishPaper(ResearchPaper paper) {
        papers.add(paper);
        notifySubscribers(paper);
    }

    private void notifySubscribers(ResearchPaper paper) {
        for (Researcher researcher : subscribers) {
            System.out.println("New paper published: \"" + paper.getTitle() + "\"");
        }
    }

    public List<ResearchPaper> getPapers() {
        return new ArrayList<>(papers);
    }
}