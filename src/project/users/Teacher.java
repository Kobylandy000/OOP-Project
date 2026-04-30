package project.users;

import project.enums.TeacherStatus;
import project.enums.UrgencyLevel;
import project.exceptions.NonResearchException;
import project.interfaces.Researcher;
import project.models.Course;
import project.models.News;
import project.models.ResearchPaper;
import project.models.ResearchProject;

import java.util.*;

public class Teacher extends Employee implements Researcher {
    private List<Course> mcourses;
    public List<Student> students;
    private boolean isProfessor;
    private TeacherStatus status;
    private UrgencyLevel level;
    private int yearsOfExperience;
    private Map<Student, Integer> ratings;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;
    private double hIndex;

    public Teacher() {
        super();
        this.mcourses = new ArrayList<>();
        this.students = new ArrayList<>();
        this.yearsOfExperience = 0;
        this.ratings = new HashMap<>();
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public Teacher(String fullName, String email, String password, int id, String position, boolean isProfessor, TeacherStatus status, int yearsOfExperience) {
        super(fullName, email, password, id, position);
        this.mcourses = new ArrayList<>();
        this.students = new ArrayList<>();
        this.isProfessor = isProfessor;
        this.status = status;
        this.yearsOfExperience = yearsOfExperience;
        this.ratings = new HashMap<>();
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public List<Course> getAssignedCourses() {
        return new ArrayList<>(mcourses);
    }

    public List<Course> viewCourses() {
        return new ArrayList<>(mcourses);
    }

    public void manageCourse(Course course) {
        if (!mcourses.contains(course)) {
            mcourses.add(course);
            System.out.println("Course " + course.getName() + " managed by teacher" + this.getFullName());
        } else {
            System.out.println("Course " + course.getName() + " is managed by the teacher." + this.getFullName());
        }
    }

    public void putMarks(Student student, Course course, double mark) {
        if (students.contains(student) && mcourses.contains(course)) {
            student.setMark(course, mark);
            System.out.println("Mark " + mark + " assigned to student " + student.getName() + " for course " + course.getName() + ".");
        } else {
            System.out.println("Student or course not found.");
        }
    }

    public void sendComplaint(String complaint, Student student, UrgencyLevel level) {
        if (students.contains(student)) {
            this.level = level;
            System.out.println("Complaint sent: " + complaint + " with urgency level " + level + " for student " + student.getName() + ".");
        } else {
            System.out.println("Student not found.");
        }
    }

    public List<Student> viewStudents(Course course) {
        List<Student> courseStudents = new ArrayList<>();
        for (Student student : students) {
            if (student.viewCourses().contains(course)) {
                courseStudents.add(student);
            }
        }
        return courseStudents;
    }

    public boolean isProfessor() {
        return isProfessor;
    }

    public void setProfessor(boolean isProfessor) {
        this.isProfessor = isProfessor;
    }

    public TeacherStatus getStatus() {
        return status;
    }

    public void setStatus(TeacherStatus status) {
        this.status = status;
    }

    public UrgencyLevel getLevel() {
        return level;
    }

    public void setLevel(UrgencyLevel level) {
        this.level = level;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public void addRating(Student student, int rating) {
        ratings.put(student, rating);
        System.out.println("Received rating " + rating + " from student " + student.getName() + ".");
    }

    public double calculateAverageRating() {
        return ratings.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    @Override
    public List<ResearchProject> getProjects() {
        if (isProfessor == true)
            return new ArrayList<>(projects);
        else
            return null;
    }

    @Override
    public List<ResearchPaper> getPapers() {
        if (isProfessor == true)
            return new ArrayList<>(papers);
        else
            return null;
    }



    public void setHIndex(double hIndex) {
        this.hIndex = hIndex;
    }

    @Override
    public double getHIndex(){
        return hIndex;
    }

    @Override
    public void joinProject(ResearchProject project) {
        if (isProfessor == true) {
            if (!projects.contains(project)) {
                projects.add(project);
                try {
                    project.addParticipant(this);
                } catch (NonResearchException e) {
                    System.out.println("Cannot join project: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        if (isProfessor == true) {
            List<ResearchPaper> sortedPapers = new ArrayList<>(papers);
            sortedPapers.sort(comparator);
            for (ResearchPaper paper : sortedPapers) {
                System.out.println("Title: " + paper.getTitle() + ", Citations: " + paper.getCitationsCount());
            }
        }
    }

    public void addPaper(ResearchPaper paper) {
        if (isProfessor == true) {
            if (!papers.contains(paper)) {
                papers.add(paper);
            }
        }
    }

    public void leaveProject(ResearchProject project) {
        if (isProfessor == true) {
            if (projects.contains(project)) {
                projects.remove(project);
                project.removeParticipant(this);
            }
        }
    }

    @Override
    public void publishPaper(ResearchPaper paper, News news) {
        if(isProfessor == true){
            papers.add(paper);
            news.announcePaper(paper);
        }
    }
}