package project.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Major implements Serializable {
    private static final long serialVersionUID = 15L;

    private String name;
    private List<Course> courses;
    private Map<Integer, List<Course>> coursesByYear;

    public Major() {
        this.courses = new ArrayList<>();
        this.coursesByYear = new HashMap<>();
    }

    public Major(String name) {
        this.name = name;
        this.courses = new ArrayList<>();
        this.coursesByYear = new HashMap<>();
    }

    public void addCourseForYear(Course course, int year) {
        coursesByYear.computeIfAbsent(year, k -> new ArrayList<>()).add(course);
        if (!courses.contains(course)) courses.add(course);
    }

    public List<Course> getCoursesByYear(int year) {
        return new ArrayList<>(coursesByYear.getOrDefault(year, new ArrayList<>()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Major major = (Major) o;
        return Objects.equals(name, major.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Course> getCourses() { return new ArrayList<>(courses); }

    @Override
    public String toString() {
        return "Major{name='" + name + "', courses=" + courses.size() + "}";
    }
}