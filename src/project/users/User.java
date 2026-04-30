package project.users;

import project.enums.Format;
import project.exceptions.NonResearchException;
import project.interfaces.Researcher;
import project.models.News;
import project.models.ResearchPaper;
import project.models.ResearchProject;
import project.models.UniversityJournal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class User implements Researcher {
    private String fullName;
    private String email;
    private String password;
    private int id;
    private Format format;
    private List<UniversityJournal> subscriptions;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;
    private double hIndex;

    public User() {
        this.subscriptions = new ArrayList<>();
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public User(String fullName, String email, String password, int id) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.id = id;
        this.subscriptions = new ArrayList<>();
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<UniversityJournal> getSubscriptions() {
        return new ArrayList<>(subscriptions);
    }

    public void setSubscriptions(List<UniversityJournal> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void subscribe(UniversityJournal journal) {
        if (!subscriptions.contains(journal)) {
            subscriptions.add(journal);
        }
    }

    public void unsubscribe(UniversityJournal journal) {
        subscriptions.remove(journal);
    }

    public void notifySubscription(ResearchPaper paper) {
        System.out.println("Notification for " + fullName + ": New paper published - "
                + paper.getTitle() + " in " + paper.getJournal());
    }


    @Override
    public List<ResearchProject> getProjects() {
        return new ArrayList<>(projects);
    }

    @Override
    public List<ResearchPaper> getPapers() {
        return new ArrayList<>(papers);
    }

    public void setHIndex(double hIndex) {
        this.hIndex = hIndex;
    }

    @Override
    public double getHIndex() {
        return hIndex;
    }

    @Override
    public void joinProject(ResearchProject project) {
        if (!projects.contains(project)) {
            projects.add(project);
            try {
                project.addParticipant(this);
            } catch (NonResearchException e) {
                System.out.println("Cannot join project: " + e.getMessage());
            }
        }
    }


    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sortedPapers = new ArrayList<>(papers);
        sortedPapers.sort(comparator);
        for (ResearchPaper paper : sortedPapers) {
            System.out.println("Title: " + paper.getTitle() + ", Citations: " + paper.getCitationsCount()); // Use getCitationsCount
        }
    }

    public void addPaper(ResearchPaper paper) {
        if (!papers.contains(paper)) {
            papers.add(paper);
        }
    }

    public void leaveProject(ResearchProject project) {
        if (projects.contains(project)) {
            projects.remove(project);
            project.removeParticipant(this);
        }
    }

    public void publishPaper(ResearchPaper paper, News news) {
        papers.add(paper);
        news.announcePaper(paper);
    }

    @Override public String toString() {
        return fullName + " (" + email + ")";
    }
}