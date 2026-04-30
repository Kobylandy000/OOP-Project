package project.users;

import project.enums.Faculty;
import project.exceptions.InvalidSupervisorException;
import project.interfaces.Researcher;
import project.models.Course;
import project.models.ResearchPaper;
import project.models.Transcript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User {
    private List<Course> courses;
    private Map<Course, Double> marks;
    private Transcript transcript;
    private boolean isResearcher;
    private List<ResearchPaper> researchPapers = new ArrayList<>();
    private Faculty faculty;
    private double hIndex;

    private double gpa;

    public Student(String fullName, String email, String password, int id, double gpa, Faculty faculty) {
        super(fullName, email, password, id);
        this.gpa = gpa;
        this.faculty = faculty;
        this.courses = new ArrayList<>();
        this.marks = new HashMap<>();
    }

    public void enrollCourse(Course course) {
        courses.add(course);
    }

    public List<Course> getFreeElectives(List<Course> allCourses) {
        List<Course> freeElectives = new ArrayList<>();
        for (Course course : allCourses) {
            if (course.isFreeElectiveFor(this.faculty)) {
                freeElectives.add(course);
            }
        }
        return freeElectives;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setHIndex(double hIndex) {
        this.hIndex = hIndex;
    }

    @Override
    public double getHIndex(){
        return hIndex;
    }


    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public List<Course> viewCourses() {
        return courses;
    }

    public void registerForCourse(Course course) {
        courses.add(course);
    }

    public Map<Course, Double> viewMarks() {
        return marks;
    }

    public Transcript viewTranscript() {
        return transcript;
    }

    public void rateTeacher(Teacher teacher, int rating) {
        if (rating >= 1 && rating <= 5) {
            teacher.addRating(this, rating);
            System.out.println("You rated teacher " + teacher.getFullName() + " with " + rating + " stars.");
        } else {
            System.out.println("Invalid rating. Please provide a rating between 1 and 5.");
        }
    }
    public void setMark(Course course, double mark) {
        if (marks != null) {
            marks.put(course, mark);
            System.out.println("Mark " + mark + " set for course " + course.getName() + ".");
        } else {
            System.out.println("Marks map is not initialized.");
        }
    }

    public String getName() {
        return getFullName();
    }

    private Researcher supervisor;

    public void assignSupervisor(Researcher supervisor) throws InvalidSupervisorException {
        if (supervisor.getHIndex() < 3) {
            throw new InvalidSupervisorException(
                    "Supervisor h-index is less than 3. Cannot assign as supervisor."
            );
        }
        this.supervisor = supervisor;
        System.out.println(supervisor + " is now your supervisor.");
    }

    public Researcher getSupervisor() {
        return supervisor;
    }
}