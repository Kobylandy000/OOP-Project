package project.users;

import project.exceptions.InvalidSupervisorException;
import project.exceptions.NonResearchException;
import project.interfaces.Researcher;
import project.models.News;
import project.models.ResearchPaper;
import project.models.ResearchProject;
import project.models.UniversityJournal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Supervisor implements Researcher {
    private double hIndex;
    private List<ResearchProject> projects = new ArrayList<>();
    private List<ResearchPaper> papers = new ArrayList<>();
    private List<UniversityJournal> subscriptions = new ArrayList<>();

    public Supervisor(double hIndex) throws InvalidSupervisorException {
        if (hIndex < 3) {
            throw new InvalidSupervisorException(
                    "Supervisor h-index is less than 3. Cannot assign as supervisor."
            );
        }
        this.hIndex = hIndex;
    }

    @Override
    public List<ResearchProject> getProjects() { return new ArrayList<>(projects); }

    @Override
    public List<ResearchPaper> getPapers() { return new ArrayList<>(papers); }

    @Override
    public double getHIndex() { return hIndex; }

    @Override
    public void joinProject(ResearchProject project) throws NonResearchException {
        if (project == null) throw new NonResearchException("Project cannot be null.");
        if (!projects.contains(project)) {
            projects.add(project);
            project.addParticipant(this);
        }
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort(comparator);
        sorted.forEach(p -> System.out.println(p.getTitle()));
    }

    @Override
    public void publishPaper(ResearchPaper paper, News news) {
        if (!papers.contains(paper)) papers.add(paper);
        news.announcePaper(paper);
    }

    @Override
    public void subscribe(UniversityJournal journal) {
        if (!subscriptions.contains(journal)) subscriptions.add(journal);
    }

    @Override
    public void unsubscribe(UniversityJournal journal) {
        subscriptions.remove(journal);
    }

    @Override
    public List<UniversityJournal> getSubscriptions() { return new ArrayList<>(subscriptions); }

    @Override
    public String toString() { return "Supervisor{hIndex=" + hIndex + "}"; }
}