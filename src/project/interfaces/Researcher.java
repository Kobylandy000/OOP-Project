package project.interfaces;

import project.exceptions.NonResearchException;
import project.models.News;
import project.models.ResearchPaper;
import project.models.ResearchProject;
import project.models.UniversityJournal;

import java.util.Comparator;
import java.util.List;
import java.io.Serializable;

public interface Researcher extends Serializable {

    // Research методтары
    List<ResearchProject> getProjects();
    List<ResearchPaper> getPapers();
    void joinProject(ResearchProject project) throws NonResearchException;
    void printPapers(Comparator<ResearchPaper> comparator);
    void publishPaper(ResearchPaper paper, News news);
    double getHIndex();

    // Journal subscription методтары
    void subscribe(UniversityJournal journal);
    void unsubscribe(UniversityJournal journal);
    List<UniversityJournal> getSubscriptions();
}