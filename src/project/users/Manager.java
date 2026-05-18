package project.users;

import project.enums.ManagerType;
import project.enums.SortingCriteria;
import project.models.*;

import java.util.*;

public class Manager extends Employee {
    private static final long serialVersionUID = 5L;

    private ManagerType managerType;
    private List<News> news;
    private List<Student> students;
    private List<Teacher> teachers;
    private List<Request> employeeRequests;

    public Manager() {
        super();
        init();
    }

    public Manager(String fullName, String email, String password,
                   int id, String position, ManagerType managerType) {
        super(fullName, email, password, id, position);
        this.managerType = managerType;
        init();
    }

    private void init() {
        this.news = new ArrayList<>();
        this.students = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.employeeRequests = new ArrayList<>();
    }

    // Студентті басқару методтары
    // Бекітілген студенттер ғана курсқа жазыла алады
    public void approveStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
            System.out.println("Student " + student.getFullName() + " approved.");
        } else {
            System.out.println("Student already approved.");
        }
    }

    public void addStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
        }
    }

    // Мұғалімдерді басқару методтары
    public void addTeacher(Teacher teacher) {
        if (!teachers.contains(teacher)) {
            teachers.add(teacher);
        }
    }

    // мұғалімге курс тағайындау
    public void assignCourseToTeacher(Course course, Teacher teacher) {
        teacher.manageCourse(course);
        System.out.println("Course " + course.getName() +
                " assigned to " + teacher.getFullName());
    }

    // курстарды басқаратын методтар
    public void addCourse(Course course, Major major, int year) {
        major.addCourseForYear(course, year);
        System.out.println("Course " + course.getName() +
                " added for year " + year);
    }

    // REPORT
    public Report generateReport() {
        Report report = new Report();

        // Жалпы статистика
        report.setTotalStudents(students.size());
        report.setTotalTeachers(teachers.size());

        // GPA статистикасы
        if (!students.isEmpty()) {
            double totalGpa = 0;
            double maxGpa = Double.MIN_VALUE;
            double minGpa = Double.MAX_VALUE;
            int failCount = 0;

            for (Student s : students) {
                totalGpa += s.getGpa();
                if (s.getGpa() > maxGpa) maxGpa = s.getGpa();
                if (s.getGpa() < minGpa) minGpa = s.getGpa();
                if (s.getGpa() < 2.0) failCount++;
            }

            report.setAverageGpa(totalGpa / students.size());
            report.setMaxGpa(maxGpa);
            report.setMinGpa(minGpa);
            report.setFailingStudents(failCount);
        }

        System.out.println(report);
        return report;
    }


    public List<Student> viewStudents(SortingCriteria criteria) {
        List<Student> sorted = new ArrayList<>(students);
        switch (criteria) {
            case GPA:
                Collections.sort(sorted); // Comparable пайдаланады
                break;
            case NAME:
                sorted.sort(Comparator.comparing(Student::getFullName));
                break;
        }
        return sorted;
    }

    public List<Teacher> viewTeachers(SortingCriteria criteria) {
        List<Teacher> sorted = new ArrayList<>(teachers);
        switch (criteria) {
            case NAME:
                sorted.sort(Comparator.comparing(Teacher::getFullName));
                break;
            case EXPERIENCE:
                sorted.sort(Comparator.comparingInt(
                        Teacher::getYearsOfExperience).reversed());
                break;
        }
        return sorted;
    }


    public void addNews(News newsItem) {
        news.add(newsItem);
        System.out.println("News added: " + newsItem.getTitle());
    }

    public void removeNews(News newsItem) {
        news.remove(newsItem);
    }

    public List<News> getNews() {
        return new ArrayList<>(news);
    }

    // REQUESTS
    public void addEmployeeRequest(Request request) {
        employeeRequests.add(request);
        System.out.println("Request added: " + request.getDescription());
    }

    public List<Request> viewPendingRequests() {
        List<Request> pending = new ArrayList<>();
        for (Request r : employeeRequests) {
            if (r.isPendingApproval()) pending.add(r);
        }
        return pending;
    }

    public ManagerType getManagerType() { return managerType; }
    public void setManagerType(ManagerType managerType) {
        this.managerType = managerType;
    }

    public List<Student> getStudents() { return new ArrayList<>(students); }
    public List<Teacher> getTeachers() { return new ArrayList<>(teachers); }

    @Override
    public String toString() {
        return super.toString() + ", managerType=" + managerType;
    }
}