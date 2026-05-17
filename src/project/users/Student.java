package project.users;

import project.enums.Faculty;
import project.enums.StudentDegree;
import project.exceptions.InvalidSupervisorException;
import project.exceptions.NonResearchException;
import project.interfaces.Researcher;
import project.models.*;

import java.util.*;

public class Student extends User implements Comparable<Student>, Researcher {
    private static final long serialVersionUID = 4L;

    private StudentDegree degree;
    private Faculty faculty;
    private double gpa;
    private int year;
    private int failCount;

    private List<Course> courses;
    private Map<Course, Mark> marks;
    private Transcript transcript;

    // Researcher өрістері
    private boolean isResearcher;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;
    private double hIndex;
    private List<UniversityJournal> subscriptions;

    private Researcher supervisor;

    public Student() {
        super();
        init();
    }

    public Student(String fullName, String email, String password,
                   int id, double gpa, Faculty faculty, int year) {
        super(fullName, email, password, id);
        this.gpa = gpa;
        this.faculty = faculty;
        this.year = year;
        this.degree = StudentDegree.BACHELOR;
        init();
    }

    private void init() {
        this.courses = new ArrayList<>();
        this.marks = new HashMap<>();
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.transcript = new Transcript();
        this.failCount = 0;
    }

    // ==================== STUDENT МЕТОДТАРЫ ====================

    public void enrollCourse(Course course) {
        if (courses.contains(course)) {
            System.out.println("Бұл курсқа тіркелгенсіз.");
            return;
        }
        // credits тексерісі
        int totalCredits = courses.stream()
                .mapToInt(Course::getCredits)
                .sum();
        if (totalCredits + course.getCredits() > 21) {
            System.out.println("21 кредит шегінен асады!");
            return;
        }
        courses.add(course);
        course.registerStudent(this);
        System.out.println(getFullName() + " enrolled in " + course.getName());
    }

    void addMark(Course course, Mark mark) {
        if (mark.getTotal() < 50) {
            failCount++;
            if (failCount > 3) {
                System.out.println("Warning: " + getFullName() +
                        " failed more than 3 times!");
            }
        }
        marks.put(course, mark);
        transcript.addMark(course, mark); // ← өзгерді
        gpa = transcript.calculateGPA();  // ← автоматты жаңарады
    }

    public Map<Course, Mark> viewMarks() {
        return new HashMap<>(marks);
    }

    public List<Course> viewCourses() {
        return new ArrayList<>(courses);
    }

    public Transcript viewTranscript() {
        return transcript;
    }

    public void rateTeacher(Teacher teacher, int rating) {
        if (rating >= 1 && rating <= 5) {
            teacher.addRating(this, rating);
            System.out.println("You rated " + teacher.getFullName() +
                    ": " + rating + " stars.");
        } else {
            System.out.println("Rating must be between 1 and 5.");
        }
    }

    public void assignSupervisor(Researcher supervisor)
            throws InvalidSupervisorException {
        if (year != 4) {
            System.out.println("Only 4th year students can have a supervisor.");
            return;
        }
        if (supervisor.getHIndex() < 3) {
            throw new InvalidSupervisorException(
                    "Supervisor h-index is less than 3!");
        }
        this.supervisor = supervisor;
        System.out.println(getFullName() + " assigned supervisor: " + supervisor);
    }

    // ==================== RESEARCHER МЕТОДТАРЫ ====================

    @Override
    public List<ResearchProject> getProjects() {
        return new ArrayList<>(projects);
    }

    @Override
    public List<ResearchPaper> getPapers() {
        return new ArrayList<>(papers);
    }

    @Override
    public double getHIndex() { return hIndex; }
    public void setHIndex(double hIndex) { this.hIndex = hIndex; }

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

    public void addPaper(ResearchPaper paper) {
        if (!isResearcher) {
            System.out.println(getFullName() + " is not a researcher!");
            return;
        }
        if (!papers.contains(paper)) papers.add(paper);
    }

    @Override
    public void publishPaper(ResearchPaper paper, News news) {
        if (!isResearcher) {
            System.out.println(getFullName() + " is not a researcher!");
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

    // ==================== COMPARABLE ====================

    @Override
    public int compareTo(Student other) {
        return Double.compare(other.gpa, this.gpa); // GPA бойынша кемуі
    }

    // ==================== GETTERS / SETTERS ====================

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public StudentDegree getDegree() { return degree; }

    public boolean isResearcher() { return isResearcher; }
    public void setResearcher(boolean researcher) {
        this.isResearcher = researcher;
    }

    public Researcher getSupervisor() { return supervisor; }

    public int getFailCount() { return failCount; }

    @Override
    public String toString() {
        return super.toString() +
                ", faculty=" + faculty +
                ", gpa=" + gpa +
                ", year=" + year +
                ", degree=" + degree;
    }
}