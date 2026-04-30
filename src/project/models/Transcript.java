package project.models;

import java.util.HashMap;
import java.util.Map;

public class Transcript {
    private Map<Course, Double> courseGrades;

    public Transcript() {
        this.courseGrades = new HashMap<>();
    }

    public void addGrade(Course course, double grade) {
        courseGrades.put(course, grade);
    }

    public double getGrade(Course course) {
        return courseGrades.getOrDefault(course, 0.0);
    }

    public Map<Course, Double> getAllGrades() {
        return new HashMap<>(courseGrades);
    }

    public double calculateGPA() {
        if (courseGrades.isEmpty()) {
            return 0.0;
        }
        double totalPoints = courseGrades.values().stream().mapToDouble(Double::doubleValue).sum();
        return totalPoints / courseGrades.size();
    }
}