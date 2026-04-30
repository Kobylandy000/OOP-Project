package project.models;

import project.enums.Language;
import project.enums.LessonType;
import project.users.Student;
import project.users.Teacher;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private String name;
    private List<Teacher> instructors;
    private List<Student> students;
    private LessonType type;
    private Language language;

    public Lesson() {
        this.instructors = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public void addInstructor(Teacher teacher) {
        if (teacher != null && !instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }


    public void registerStudent(Student student) {
        if (student != null && !students.contains(student)) {
            students.add(student);
        }
    }

    public String getLessonInfo() {
        return "Lesson: " + name + ", Type: " + type + ", Language: " + language;
    }
}
