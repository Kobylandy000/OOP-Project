package project.users;

import project.enums.Faculty;
import project.enums.StudentDegree;
import project.exceptions.CreditLimitExceededException;
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

    // Researcher fields
    private boolean isResearcher;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;
    private double hIndex;
    private List<UniversityJournal> subscriptions;

    private Researcher supervisor; // ғылыми жетекші 4 курс студенті үшін

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

    public void enrollCourse(Course course) throws CreditLimitExceededException {
        if (courses.contains(course)) {
            System.out.println("You are already enrolled in this course.");
            return;
        }
        // Check credits limit
        int totalCredits = courses.stream()
                .mapToInt(Course::getCredits)
                .sum();
        if (totalCredits + course.getCredits() > 21) {
            throw new CreditLimitExceededException(
                    "Cannot enroll: " + getFullName() +
                            " would exceed 21 credit limit! Current: " + totalCredits +
                            ", Requested: " + course.getCredits()
            );
        }
        courses.add(course);
        course.registerStudent(this);
        System.out.println(getFullName() + " enrolled in " + course.getName());
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
            throws InvalidSupervisorException { // 4 курс студентіне ғылыми жетекші тағайындау
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

    public void setHIndex(double hIndex) {
        if (!isResearcher) {
            System.out.println(getFullName() + " is not a researcher!");
            return;
        }
        this.hIndex = hIndex;
    }

    public void activateResearchProfile(double hIndex) {
        this.isResearcher = true;
        this.hIndex = hIndex;
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
        if (paper == null) {
            System.out.println("Cannot add null paper.");
            return;
        }
        if (!papers.contains(paper)) {
            papers.add(paper);
        }
    }

    @Override
    public void publishPaper(ResearchPaper paper, News news) {
        if (!isResearcher) {
            System.out.println(getFullName() + " is not a researcher!");
            return;
        }
        if (paper == null) {
            System.out.println("Cannot publish null paper.");
            return;
        }
        if (!papers.contains(paper)) {
            papers.add(paper);
        }
        if (news != null) {
            news.announcePaper(paper); // мақаланы хабарлау
        } else {
            System.out.println("Paper \"" + paper.getTitle() + "\" published, but no news announcement was created.");
        }
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
    public int compareTo(Student other) {
        return Double.compare(other.gpa, this.gpa); // Descending by GPA
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public StudentDegree getDegree() {
        return degree;
    }

    public boolean isResearcher() {
        return isResearcher;
    }

    public Researcher getSupervisor() {
        return supervisor;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    // Teacher баға қою үшін керек getter'лар
    public Map<Course, Mark> getMarks() {
        return marks;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", faculty=" + faculty +
                ", gpa=" + gpa +
                ", year=" + year +
                ", degree=" + degree;
    }
}