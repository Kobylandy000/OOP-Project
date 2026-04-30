package project.interfaces;

import project.models.News;
import project.models.ResearchPaper;
import project.models.ResearchProject;

import java.util.Comparator;
import java.util.List;

public interface Researcher {
    List<ResearchProject> getProjects();
    List<ResearchPaper> getPapers();
    void joinProject(ResearchProject project);
    void printPapers(Comparator<ResearchPaper> comparator);
    void publishPaper(ResearchPaper paper, News news);
    double getHIndex();
}