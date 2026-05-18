package project.models;

public class Report {
    private int totalStudents;
    private int totalTeachers;
    private double averageGpa;
    private double maxGpa;
    private double minGpa;
    private int failingStudents;

    public Report() {}

    public int getTotalStudents() { return totalStudents; }
    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getTotalTeachers() { return totalTeachers; }
    public void setTotalTeachers(int totalTeachers) {
        this.totalTeachers = totalTeachers;
    }

    public double getAverageGpa() { return averageGpa; }
    public void setAverageGpa(double averageGpa) {
        this.averageGpa = averageGpa;
    }

    public double getMaxGpa() { return maxGpa; }
    public void setMaxGpa(double maxGpa) {
        this.maxGpa = maxGpa;
    }

    public double getMinGpa() { return minGpa; }
    public void setMinGpa(double minGpa) {
        this.minGpa = minGpa;
    }

    public int getFailingStudents() { return failingStudents; }
    public void setFailingStudents(int failingStudents) {
        this.failingStudents = failingStudents;
    }

    @Override
    public String toString() {
        return "=== REPORT ===\n" +
                "Total students   : " + totalStudents + "\n" +
                "Total teachers   : " + totalTeachers + "\n" +
                "Average GPA      : " + String.format("%.2f", averageGpa) + "\n" +
                "Highest GPA      : " + String.format("%.2f", maxGpa) + "\n" +
                "Lowest GPA       : " + String.format("%.2f", minGpa) + "\n" +
                "Failing students : " + failingStudents;
    }
}