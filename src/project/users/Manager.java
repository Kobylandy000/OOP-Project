package project.users;

import project.enums.ManagerType;
import project.enums.SortingCriteria;
import project.models.*;

import java.util.*;
import java.util.stream.Collectors;

public class Manager extends Employee {
    private ManagerType managerType;
    private List<News> news;
    private List<Student> students;
    private List<Teacher> teachers;
    private List<Request> employeeRequests;

    public Manager() {
        super();
        this.news = new ArrayList<>();
        this.students = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.employeeRequests = new ArrayList<>();
    }

    public Manager(String fullName, String email, String password, int id, String position, ManagerType managerType) {
        super(fullName, email, password, id, position);
        this.managerType = managerType;
        this.news = new ArrayList<>();
        this.students = new ArrayList<>();
        this.employeeRequests = new ArrayList<>();
    }

    public List<Teacher> getTeachers() {
        return new ArrayList<>(teachers);
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }




    public void approveStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
            System.out.println("Student " + student.getFullName() + " approved.");
        } else {
            System.out.println("Student " + student.getFullName() + " is already approved.");
        }
    }

    public void addCourse(Course course, Major major, int year) {
        major.addCourseForYear(course, year);
        System.out.println("Course " + course.getName() + " added for Major: " + major.getName() + ", Year: " + year);
    }

    public void assignCourseToTeacher(Course course, Teacher teacher) {
        teacher.manageCourse(course);
        System.out.println("Course " + course.getName() + " assigned to Teacher: " + teacher.getFullName());
    }

    public Report generateReport() {
        Report report = new Report();

        double totalGpa = 0.0;
        for (Student student : students) {
            totalGpa += student.getGpa();
        }
        double averageGpa;
        if (students.isEmpty()) {
            averageGpa = 0.0;
        } else {
            averageGpa = totalGpa / students.size();
        }

        report.setAverageGpa(averageGpa);
        report.setTotalStudents(students.size());

        System.out.println("Report generated: Average GPA = " + averageGpa + ", Total Students = " + students.size());

        return report;
    }
    public List<?> viewInfo(SortingCriteria criteria, String type) {
        if ("students".equalsIgnoreCase(type)) {
            return sortStudents(criteria);
        } else if ("teachers".equalsIgnoreCase(type)) {
            return sortTeachers(criteria);
        }
        return new ArrayList<>();
    }

    private List<Student> sortStudents(SortingCriteria criteria) {
        List<Student> sortedStudents = new ArrayList<>(students);

        if (criteria == SortingCriteria.GPA) {
            sortedStudents.sort(new Comparator<Student>() {
                @Override
                public int compare(Student s1, Student s2) {
                    return Double.compare(s2.getGpa(), s1.getGpa());
                }
            });
        } else if (criteria == SortingCriteria.NAME) {
            sortedStudents.sort(new Comparator<Student>() {
                @Override
                public int compare(Student s1, Student s2) {
                    return s1.getFullName().compareTo(s2.getFullName());
                }
            });
        }

        return sortedStudents;
    }


    private List<Teacher> sortTeachers(SortingCriteria criteria) {
        List<Teacher> teachers = getTeachers();
        switch (criteria) {
            case NAME:
                teachers.sort(Comparator.comparing(Teacher::getFullName));
                break;
            case EXPERIENCE:
                teachers.sort(Comparator.comparingInt(Teacher::getYearsOfExperience).reversed());
                break;
        }
        return teachers;
    }


    public void prioritizeNews(String topic) {
        Collections.sort(news, (n1, n2) -> {
            if (n1.getTopic().equalsIgnoreCase(topic) && !n2.getTopic().equalsIgnoreCase(topic)) {
                return -1;
            } else if (!n1.getTopic().equalsIgnoreCase(topic) && n2.getTopic().equalsIgnoreCase(topic)) {
                return 1;
            }
            return 0;
        });
        System.out.println("News prioritized by topic: " + topic);
    }


    public List<Request> viewPendingRequests() {
        List<Request> pendingRequests = new ArrayList<>();
        for (Request request : employeeRequests) {
            if (request.isPendingApproval()) {
                pendingRequests.add(request);
            }
        }
        return pendingRequests;
    }

    public void addEmployeeRequest(Request request) {
        employeeRequests.add(request);
        System.out.println("Request added: " + request.getDescription());
    }
}
