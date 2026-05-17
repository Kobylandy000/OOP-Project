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

    // ==================== МЕТОДТАР ====================

    public void addGrade(Course course, double gpa) {
        // бұл бұрыннан бар, бірақ Mark сақталмайды
    }

    public void addMark(Course course, Mark mark) {
        courseMarks.put(course, mark);
    }

    public Mark getMark(Course course) {
        return courseMarks.get(course);
    }

    public double calculateGPA() {
        if (courseMarks.isEmpty()) return 0.0;
        double total = courseMarks.values().stream()
                .mapToDouble(Mark::getGpa)
                .sum();
        return total / courseMarks.size();
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