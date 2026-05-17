package project.storage;

import project.models.Course;
import project.models.News;
import project.models.ResearchPaper;
import project.models.ResearchProject;
import project.users.Admin;
import project.users.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Admin admin;
    private List<Course> courses;
    private List<ResearchPaper> researchPapers;
    private List<ResearchProject> researchProjects;
    private List<News> news;

    public AppData(Admin admin,
                   List<Course> courses,
                   List<ResearchPaper> researchPapers,
                   List<ResearchProject> researchProjects,
                   List<News> news) {
        this.admin = admin;
        this.courses = courses;
        this.researchPapers = researchPapers;
        this.researchProjects = researchProjects;
        this.news = news;
    }

    public Admin getAdmin() {
        return admin;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<ResearchPaper> getResearchPapers() {
        return researchPapers;
    }

    public List<ResearchProject> getResearchProjects() {
        return researchProjects;
    }

    public List<News> getNews() {
        return news;
    }

    public Course findCourseByName(String name) {
        for (Course course : courses) {
            if (course.getName().equalsIgnoreCase(name)) {
                return course;
            }
        }
        return null;
    }

    public User findUserByEmail(String email) {
        for (User user : admin.getUsers()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public void addResearchPaper(ResearchPaper paper) {
        researchPapers.add(paper);
    }

    public void addResearchProject(ResearchProject project) {
        researchProjects.add(project);
    }

    public void addNews(News item) {
        news.add(item);
    }

    public boolean isEmpty() {
        return admin == null || courses == null || courses.isEmpty();
    }

    public static AppData empty() {
        return new AppData(
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}