package project.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Transcript implements Serializable {
    private static final long serialVersionUID = 11L;

    private Map<Course, Mark> courseMarks;

    public Transcript() {
        this.courseMarks = new HashMap<>();
    }

    public void addMark(Course course, Mark mark) {
        courseMarks.put(course, mark);
    }

    public Mark getMark(Course course) {
        return courseMarks.get(course);
    }

    public double calculateGPA() {
        if (courseMarks.isEmpty()) {
            return 0.0;
        }

        double totalGradePoints = 0.0;
        int totalCredits = 0;

        for (Map.Entry<Course, Mark> entry : courseMarks.entrySet()) {
            Course course = entry.getKey();
            Mark mark = entry.getValue();
            totalGradePoints += mark.getGpa() * course.getCredits();
            totalCredits += course.getCredits();
        }

        return totalCredits == 0 ? 0.0 : totalGradePoints / totalCredits;
    }

    
    public Map<Course, Mark> getAllMarks() {
        return new HashMap<>(courseMarks);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TRANSCRIPT ===\n");
        for (Map.Entry<Course, Mark> entry : courseMarks.entrySet()) {
            sb.append(entry.getKey().getName())
                    .append(" — ")
                    .append(entry.getValue().getLiteralMark())
                    .append(" (GPA: ")
                    .append(entry.getValue().getGpa())
                    .append(")\n");
        }
        sb.append("Overall GPA: ").append(String.format("%.2f", calculateGPA()));
        return sb.toString();
    }
}