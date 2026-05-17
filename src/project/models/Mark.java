package project.models;

import java.io.Serializable;
import java.util.Objects;

public class Mark implements Serializable {
    private static final long serialVersionUID = 10L;

    private Course course;
    private double firstAttestation;
    private double secondAttestation;
    private double finalExam;
    private double total;
    private double gpa;
    private String literalMark;

    public Mark() {}

    public Mark(Course course, double firstAttestation,
                double secondAttestation, double finalExam) {
        this.course = course;
        this.firstAttestation = firstAttestation;
        this.secondAttestation = secondAttestation;
        this.finalExam = finalExam;
        calculateTotal();
    }

    // ==================== ЕСЕПТЕУ ====================

    private void calculateTotal() {
        this.total = firstAttestation + secondAttestation + finalExam;
        this.gpa = calculateGpa(total);
        this.literalMark = calculateLiteralMark(total);
    }

    private double calculateGpa(double total) {
        if (total >= 95) return 4.0;
        else if (total >= 90) return 3.67;
        else if (total >= 85) return 3.33;
        else if (total >= 80) return 3.0;
        else if (total >= 75) return 2.67;
        else if (total >= 70) return 2.33;
        else if (total >= 65) return 2.0;
        else if (total >= 60) return 1.67;
        else if (total >= 55) return 1.33;
        else if (total >= 50) return 1.0;
        else return 0.0;
    }

    private String calculateLiteralMark(double total) {
        if (total >= 95) return "A";
        else if (total >= 90) return "A-";
        else if (total >= 85) return "B+";
        else if (total >= 80) return "B";
        else if (total >= 75) return "B-";
        else if (total >= 70) return "C+";
        else if (total >= 65) return "C";
        else if (total >= 60) return "C-";
        else if (total >= 55) return "D+";
        else if (total >= 50) return "D";
        else return "F";
    }

    // ==================== EQUALS / HASHCODE ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mark mark = (Mark) o;
        return Objects.equals(course, mark.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course);
    }

    // ==================== GETTERS / SETTERS ====================

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public double getFirstAttestation() { return firstAttestation; }
    public void setFirstAttestation(double firstAttestation) {
        this.firstAttestation = firstAttestation;
        calculateTotal();
    }

    public double getSecondAttestation() { return secondAttestation; }
    public void setSecondAttestation(double secondAttestation) {
        this.secondAttestation = secondAttestation;
        calculateTotal();
    }

    public double getFinalExam() { return finalExam; }
    public void setFinalExam(double finalExam) {
        this.finalExam = finalExam;
        calculateTotal();
    }

    public double getTotal() { return total; }
    public double getGpa() { return gpa; }
    public String getLiteralMark() { return literalMark; }

    @Override
    public String toString() {
        return "Mark{" +
                "course=" + (course != null ? course.getName() : "N/A") +
                ", att1=" + firstAttestation +
                ", att2=" + secondAttestation +
                ", final=" + finalExam +
                ", total=" + total +
                ", grade=" + literalMark +
                ", gpa=" + gpa +
                "}";
    }
}