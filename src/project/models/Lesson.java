package project.models;

import project.enums.Language;
import project.enums.LessonType;
import project.users.Student;
import project.users.Teacher;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lesson implements Serializable {
    private static final long serialVersionUID = 13L;

    private String name;
    private LessonType type;
    private Language language;
    private LocalDateTime dateTime;
    private List<Teacher> instructors;
    private List<Student> students;

    public Lesson() {
        this.instructors = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public Lesson(String name, LessonType type, Language language, LocalDateTime dateTime) {
        this.name = name;
        this.type = type;
        this.language = language;
        this.dateTime = dateTime;
        this.instructors = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public void addInstructor(Teacher teacher) {
        if (teacher != null && !instructors.contains(teacher))
            instructors.add(teacher);
    }

    public void registerStudent(Student student) {
        if (student != null && !students.contains(student))
            students.add(student);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(name, lesson.name) &&
                Objects.equals(dateTime, lesson.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dateTime);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LessonType getType() { return type; }
    public void setType(LessonType type) { this.type = type; }

    public Language getLanguage() { return language; }
    public void setLanguage(Language language) { this.language = language; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public List<Teacher> getInstructors() { return new ArrayList<>(instructors); }
    public List<Student> getStudents() { return new ArrayList<>(students); }

    @Override
    public String toString() {
        return "Lesson{" +
                "name='" + name + "'" +
                ", type=" + type +
                ", language=" + language +
                ", dateTime=" + dateTime +
                "}";
    }
}