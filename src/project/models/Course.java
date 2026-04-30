package project.models;

import project.enums.CourseType;
import project.enums.Faculty;
import project.enums.Language;
import project.enums.LessonType;
import project.users.Student;
import project.users.Teacher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Course {
    private String name;
    private LessonType type;
    private List<Teacher> instructors;
    private Faculty faculty;
    private Language language;
    private List<Student> students;
    private CourseType courseType;

    public Course(String name, LessonType type, Language language, CourseType courseType, Faculty faculty) {
        this.instructors = new ArrayList<>();
        this.students = new ArrayList<>();
        this.name = name;
        this.type = type;
        this.language = language;
        this.courseType = courseType;
        this.faculty = faculty;
    }

    public void addInstructor(Teacher teacher) {
        instructors.add(teacher);
    }

    public void registerStudent(Student student) {
        students.add(student);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public Set<Teacher> getInstructors() {
        return new HashSet<>(instructors);
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public boolean isFreeElectiveFor(Faculty studentFaculty) {
        return this.courseType == CourseType.MAJOR && this.faculty != studentFaculty;
    }
}
