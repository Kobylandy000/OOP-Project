package project.users;

import project.exceptions.InvalidSupervisorException;
import project.interfaces.Researcher;
import project.models.News;
import project.models.ResearchPaper;
import project.models.ResearchProject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Supervisor implements Researcher {
    private double hIndex;
    private List<ResearchProject> projects = new ArrayList<>();
    private List<ResearchPaper> papers = new ArrayList<>();

    public Supervisor(double hIndex) throws InvalidSupervisorException {
        if (hIndex < 3) {
            throw new InvalidSupervisorException("Supervisor h-index is less than 3. Cannot assign as supervisor.");
        }
        this.hIndex = hIndex;
    }


    @Override
    public List<ResearchProject> getProjects() {
        return projects;
    }

    @Override
    public List<ResearchPaper> getPapers() {
        return papers;
    }

    @Override
    public double getHIndex() {
        return hIndex;
    }

    @Override
    public void joinProject(ResearchProject project) {
        projects.add(project);
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        papers.sort(comparator);
        for (ResearchPaper paper : papers) {
            System.out.println(paper.getTitle());
        }
    }

    public void publishPaper(ResearchPaper paper, News news) {
        papers.add(paper);
        news.announcePaper(paper);
    }
}