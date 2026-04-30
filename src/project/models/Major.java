package project.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Major {
    private String name;
    private List<Course> courses;
    private Map<Integer, List<Course>> coursesByYear;

    public Major(String name, List<Course> courses) {
        this.name = name;
        this.courses = courses;
        this.coursesByYear = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourseForYear(Course course, int year) {
        if (!coursesByYear.containsKey(year)) {
            coursesByYear.put(year, new ArrayList<>());
        }
        coursesByYear.get(year).add(course);
    }
}