package project.models;

public class Report {
    private double averageGpa;
    private int totalStudents;

    public double getAverageGpa() {
        return averageGpa;
    }

    public void setAverageGpa(double averageGpa) {
        this.averageGpa = averageGpa;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    @Override
    public String toString() {
        return "Report [Average GPA=" + averageGpa + ", Total Students=" + totalStudents + "]";
    }
}