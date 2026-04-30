package project.models;

import project.users.Student;

import java.util.ArrayList;
import java.util.List;

public class Mark {
    private Course course;
    private double points;
    private double firstAttestation;
    private double secondAttestation;
    private double finalExam;
    private double total;
    private double gpa;
    private double digitalMark;
    private String literalMark;
    private List<Student> students;

    public Mark() {
        this.students = new ArrayList<>();
    }

    public Course getCourse() {
        return course;
    }

    public double getPoints() {
        return points;
    }

    public double getFirstAttestation() {
        return firstAttestation;
    }

    public double getSecondAttestation() {
        return secondAttestation;
    }

    public double getFinalExam() {
        return finalExam;
    }

    public double getTotal() {
        return total;
    }

    public double getGpa() {
        return gpa;
    }

    public double getDigitalMark() {
        return digitalMark;
    }

    public String getLiteralMark() {
        return literalMark;
    }
}