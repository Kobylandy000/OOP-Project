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

public class Supervisor extends Employee implements Researcher {
    private static final long serialVersionUID = 8L;

    private double hIndex;
    private List<ResearchProject> projects;
    private List<ResearchPaper> papers;
    private List<UniversityJournal> subscriptions;

    public Supervisor() {
        super();
        init();
    }

    public Supervisor(String fullName, String email, String password,
                      int id, String position, double hIndex)
            throws InvalidSupervisorException {
        super(fullName, email, password, id, position);
        if (hIndex < 3) {
            throw new InvalidSupervisorException(
                    "Supervisor h-index is less than 3. Cannot assign as supervisor."
            );
        }
        this.hIndex = hIndex;
        init();
    }

    private void init() {
        this.projects = new ArrayList<>();
        this.papers = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
    }

    @Override
    public List<ResearchProject> getProjects() {
        return new ArrayList<>(projects);
    }

    @Override
    public List<ResearchPaper> getPapers() {
        return new ArrayList<>(papers);
    }

    @Override
    public double getHIndex() {
        return hIndex;
    }

    public void setHIndex(double hIndex) throws InvalidSupervisorException {
        if (hIndex < 3) {
            throw new InvalidSupervisorException(
                    "Supervisor h-index is less than 3. Cannot assign as supervisor."
            );
        }
        this.hIndex = hIndex;
    }

    public void addPaper(ResearchPaper paper) {
        if (!papers.contains(paper)) {
            papers.add(paper);
        }
    }

    @Override
    public void joinProject(ResearchProject project) throws NonResearchException {
        if (project == null) {
            throw new NonResearchException("Project cannot be null.");
        }
        if (!projects.contains(project)) {
            projects.add(project);
            project.addParticipant(this);
        }
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        if (papers.isEmpty()) {
            System.out.println("No papers found.");
            return;
        }
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort(comparator);
        sorted.forEach(p -> System.out.println(
                "Title: " + p.getTitle() +
                        ", Citations: " + p.getCitationsCount() +
                        ", Date: " + p.getDatePublished()
        ));
    }

    @Override
    public void publishPaper(ResearchPaper paper, News news) {
        if (!papers.contains(paper)) {
            papers.add(paper);
        }
        news.announcePaper(paper);
    }

    @Override
    public void subscribe(UniversityJournal journal) {
        if (!subscriptions.contains(journal)) {
            subscriptions.add(journal);
            System.out.println(getFullName() + " subscribed to " + journal.getName());
        }
    }

    @Override
    public void unsubscribe(UniversityJournal journal) {
        subscriptions.remove(journal);
    }

    @Override
    public List<UniversityJournal> getSubscriptions() {
        return new ArrayList<>(subscriptions);
    }

    @Override
    public String toString() {
        return super.toString() + ", hIndex=" + hIndex;
    }
}
