package project.users;

import project.enums.TeacherStatus;
import project.enums.UrgencyLevel;
import project.exceptions.NonResearchException;
import project.interfaces.Researcher;
import project.models.*;

import java.util.*;

public class Teacher extends Employee implements Researcher {
    private static final long serialVersionUID = 3L;

    private List<Course> courses;
    private List<Student> students;
    private TeacherStatus status;
    private int yearsOfExperience;
    private Map<Student, Integer> ratings;

    // Researcher өрістері
    private boolean isResearcher;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;
    private double hIndex;
    private List<UniversityJournal> subscriptions;

    public Teacher() {
        super();
        init();
    }

    public Teacher(String fullName, String email, String password,
                   int id, String position,
                   TeacherStatus status, int yearsOfExperience) {
        super(fullName, email, password, id, position);
        this.status = status;
        this.yearsOfExperience = yearsOfExperience;
        // PROFESSOR әрдайым researcher болады
        this.isResearcher = (status == TeacherStatus.PROFESSOR);
        init();
    }

    private void init() {
        this.courses = new ArrayList<>();
        this.students = new ArrayList<>();
        this.ratings = new HashMap<>();
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
    }

    public List<Course> viewCourses() {
        return new ArrayList<>(courses);
    }

    public void manageCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
            course.addInstructor(this);
            System.out.println("Course " + course.getName() +
                    " assigned to " + getFullName());
        }
    }

    public void addStudent(Student student) {
        if (student != null && !students.contains(student)) {
            students.add(student);
        }
    }

    public void putMarks(Student student, Course course,
                         double att1, double att2, double finalExam) {
        if (!students.contains(student)) {
            System.out.println("Student not found: " + student.getFullName());
            return;
        }
        if (!courses.contains(course)) {
            System.out.println("Course not found: " + course.getName());
            return;
        }
        Mark mark = new Mark(course, att1, att2, finalExam);
        student.addMark(course, mark);
        System.out.println("Mark assigned to " + student.getFullName() +
                " for " + course.getName());
    }

    public List<Student> viewStudents() {
        return new ArrayList<>(students);
    }

    public void sendComplaint(String complaint, Student student,
                              UrgencyLevel level) {
        if (!students.contains(student)) {
            System.out.println("Student not found.");
            return;
        }
        System.out.println("Complaint [" + level + "]: " + complaint +
                " — student: " + student.getFullName());
    }

    public void addRating(Student student, int rating) {
        ratings.put(student, rating);
    }

    public double calculateAverageRating() {
        return ratings.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    public TeacherStatus getStatus() { return status; }
    public void setStatus(TeacherStatus status) {
        this.status = status;
        // Professor болса автоматты researcher
        if (status == TeacherStatus.PROFESSOR) this.isResearcher = true;
    }

    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public boolean isProfessor() {
        return status == TeacherStatus.PROFESSOR;
    }

    public boolean isResearcher() { return isResearcher; }
    public void setResearcher(boolean researcher) {
        this.isResearcher = researcher;
    }

    @Override
    public double getHIndex() { return hIndex; }
    public void setHIndex(double hIndex) { this.hIndex = hIndex; }

    @Override
    public List<ResearchPaper> getPapers() {
        if (!isResearcher) {
            System.out.println(getFullName() + " is not a researcher.");
            return new ArrayList<>();
        }
        return new ArrayList<>(papers);
    }

    @Override
    public List<ResearchProject> getProjects() {
        if (!isResearcher) {
            System.out.println(getFullName() + " is not a researcher.");
            return new ArrayList<>();
        }
        return new ArrayList<>(projects);
    }

    public void addPaper(ResearchPaper paper) {
        if (!isResearcher) {
            System.out.println(getFullName() + " is not a researcher.");
            return;
        }
        if (!papers.contains(paper)) papers.add(paper);
    }

    @Override
    public void joinProject(ResearchProject project) throws NonResearchException {
        if (!isResearcher) {
            throw new NonResearchException(
                    getFullName() + " is not a researcher and cannot join a project!"
            );
        }
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
        if (!isResearcher || papers.isEmpty()) {
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
        if (!isResearcher) {
            System.out.println(getFullName() + " is not a researcher.");
            return;
        }
        if (!papers.contains(paper)) papers.add(paper);
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
        return super.toString() + ", status=" + status +
                ", experience=" + yearsOfExperience +
                ", isResearcher=" + isResearcher;
    }
}