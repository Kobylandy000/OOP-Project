package project.models;

import project.enums.CourseType;
import project.enums.Faculty;
import project.enums.Language;
import project.enums.LessonType;
import project.users.Student;
import project.users.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements Serializable {
    private static final long serialVersionUID = 9L;

    private String name;
    private LessonType lessonType;
    private Language language;
    private CourseType courseType;
    private Faculty faculty;
    private int credits;
    private List<Teacher> instructors;
    private List<Student> students;

    public Course() {
        this.instructors = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public Course(String name, LessonType lessonType, Language language,
                  CourseType courseType, Faculty faculty, int credits) {
        this.name = name;
        this.lessonType = lessonType;
        this.language = language;
        this.courseType = courseType;
        this.faculty = faculty;
        this.credits = credits;
        this.instructors = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public void addInstructor(Teacher teacher) {
        if (teacher != null && !instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }

    public void removeInstructor(Teacher teacher) {
        instructors.remove(teacher);
    }

    public void registerStudent(Student student) {
        if (student != null && !students.contains(student)) {
            students.add(student);
        }
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public boolean isFreeElectiveFor(Faculty studentFaculty) {
        return this.courseType == CourseType.ELECTIVE &&
                this.faculty != studentFaculty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(name, course.name) &&
                Objects.equals(faculty, course.faculty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, faculty);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public List<Teacher> getInstructors() {
        return new ArrayList<>(instructors);
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + "'" +
                ", lessonType=" + lessonType +
                ", credits=" + credits +
                ", faculty=" + faculty +
                "}";
    }
}