package project;

import project.enums.*;
import project.exceptions.*;
import project.interfaces.Researcher;
import project.models.*;
import project.publisher.ResearchPaperPublisher;
import project.services.ResearchAnalytics;
import project.users.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import project.storage.AppData;
import project.storage.DataRepository;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
// ==================== LOAD OR CREATE DATA ====================
        DataRepository repository = DataRepository.getInstance();

        AppData data = repository.load();

        if (data == null || data.isEmpty()) {
            data = createDefaultData();
            repository.save(data);
        }

        Admin admin = data.getAdmin();
        Admin.setInstance(admin);

        Course oop = data.findCourseByName("OOP");
        Course math = data.findCourseByName("Calculus");
        Course ds = data.findCourseByName("Data Structures");

        Teacher teacher1 = (Teacher) data.findUserByEmail("aibek@uni.kz");
        Teacher teacher2 = (Teacher) data.findUserByEmail("zarina@uni.kz");

        Student student1 = (Student) data.findUserByEmail("arman@uni.kz");
        Student student2 = (Student) data.findUserByEmail("dana@uni.kz");

        Manager manager = (Manager) data.findUserByEmail("gulnara@uni.kz");

        // ==================== AUTHENTICATION ====================

        System.out.println("\n========================================");
        System.out.println("       UNIVERSITY SYSTEM LOGIN");
        System.out.println("========================================");

        boolean loggedIn = false;
        int attempts = 0;

        while (!loggedIn && attempts < 3) {
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            // Search among all available users
            project.users.User authenticatedUser = authenticateUser(admin, email, password);
            if (authenticatedUser != null) {
                System.out.println("\nLogin successful! Welcome, "
                        + authenticatedUser.getFullName());
                loggedIn = true;
                runMenu(authenticatedUser, oop, math, ds, teacher1, teacher2,
                        student1, student2, manager);
            }

            if (!loggedIn) {
                attempts++;
                System.out.println("Invalid email or password. "
                        + (3 - attempts) + " attempt(s) remaining.");
            }
        }

        if (!loggedIn) {
            System.out.println("Unable to log into the system.");
        }
        repository.save(data);
        scanner.close();
    }

    private static AppData createDefaultData() {

        // Courses
        Course oop = new Course("OOP", LessonType.LECTURE, Language.EN,
                CourseType.MAJOR, Faculty.IT, 5);

        Course math = new Course("Calculus", LessonType.PRACTICE, Language.KZ,
                CourseType.MAJOR, Faculty.IT, 4);

        Course ds = new Course("Data Structures", LessonType.LECTURE, Language.EN,
                CourseType.MAJOR, Faculty.IT, 5);

        // Teachers
        Teacher teacher1 = new Teacher("Aibek Seitkali", "aibek@uni.kz",
                "teach123", 101, "Professor", TeacherStatus.PROFESSOR, 10);

        Teacher teacher2 = new Teacher("Zarina Bekova", "zarina@uni.kz",
                "teach456", 102, "Senior Lecturer", TeacherStatus.SENIOR_LECTOR, 5);

        // Students
        Student student1 = new Student("Arman Nurlanov", "arman@uni.kz",
                "stud123", 201, 3.5, Faculty.IT, 2);

        Student student2 = new Student("Dana Seitkali", "dana@uni.kz",
                "stud456", 202, 3.8, Faculty.IT, 4);

        // Manager
        Manager manager = new Manager("Gulnara Abdova", "gulnara@uni.kz",
                "man123", 301, "OR Manager", ManagerType.OFFICEREGISTRATION);

        // Admin
        Admin admin = Admin.getInstance();

        admin.addUser(teacher1);
        admin.addUser(teacher2);
        admin.addUser(student1);
        admin.addUser(student2);
        admin.addUser(manager);

        List<Course> courses = new ArrayList<>();
        courses.add(oop);
        courses.add(math);
        courses.add(ds);

        List<ResearchPaper> researchPapers = new ArrayList<>();
        List<ResearchProject> researchProjects = new ArrayList<>();
        List<News> news = new ArrayList<>();

        return new AppData(admin, courses, researchPapers, researchProjects, news);
    }

    // ==================== AUTHENTICATION ====================

    private static project.users.User authenticateUser(Admin admin, String email, String password) {
        if (admin.login(email, password)) {
            return admin;
        }

        for (project.users.User user : admin.getUsers()) {
            if (user.login(email, password)) {
                return user;
            }
        }

        return null;
    }

    // ==================== MENU DISPATCHER ====================

    private static void runMenu(project.users.User user, Course oop, Course math,
                                Course ds, Teacher teacher1, Teacher teacher2,
                                Student student1, Student student2, Manager manager) {

        if (user instanceof Student) {
            studentMenu((Student) user, oop, math, ds, teacher1);
        } else if (user instanceof Teacher) {
            teacherMenu((Teacher) user, oop, student1, student2);
        } else if (user instanceof Manager) {
            managerMenu((Manager) user, oop, math, ds, teacher1, teacher2,
                    student1, student2);
        } else if (user instanceof Admin) {
            adminMenu((Admin) user);
        }
    }

    // ==================== STUDENT MENU ====================

    private static void studentMenu(Student student, Course oop,
                                    Course math, Course ds, Teacher teacher) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- STUDENT MENU: " + student.getFullName() + " ---");
            System.out.println("1. Enroll in a course");
            System.out.println("2. View courses");
            System.out.println("3. View grades");
            System.out.println("4. View transcript");
            System.out.println("5. Rate a teacher");
            System.out.println("6. Research (joinProject, printPapers)");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n-- Available courses --");
                    System.out.println("1. " + oop);
                    System.out.println("2. " + math);
                    System.out.println("3. " + ds);
                    System.out.print("Select a course: ");
                    String c = scanner.nextLine().trim();
                    try {
                        if (c.equals("1")) student.enrollCourse(oop);
                        else if (c.equals("2")) student.enrollCourse(math);
                        else if (c.equals("3")) student.enrollCourse(ds);
                    } catch (CreditLimitExceededException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case "2":
                    System.out.println("\n-- Registered courses --");
                    if (student.viewCourses().isEmpty()) {
                        System.out.println("No courses found.");
                    } else {
                        student.viewCourses().forEach(System.out::println);
                    }
                    break;

                case "3":
                    System.out.println("\n-- Grades --");
                    if (student.viewMarks().isEmpty()) {
                        System.out.println("No grades found.");
                    } else {
                        student.viewMarks().forEach((course, mark) ->
                                System.out.println(course.getName() + ": " + mark));
                    }
                    break;

                case "4":
                    System.out.println("\n" + student.viewTranscript());
                    break;

                case "5":
                    System.out.print("Enter a rating (1-5): ");
                    try {
                        int rating = Integer.parseInt(scanner.nextLine().trim());
                        student.rateTeacher(teacher, rating);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number.");
                    }
                    break;

                case "6":
                    researchDemoStudent(student);
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ==================== TEACHER MENU ====================

    private static void teacherMenu(Teacher teacher, Course oop,
                                    Student student1, Student student2) {
        // Assign the course and students to the teacher in advance
        teacher.manageCourse(oop);
        teacher.addStudent(student1);
        teacher.addStudent(student2);

        boolean running = true;
        while (running) {
            System.out.println("\n--- TEACHER MENU: " + teacher.getFullName() + " ---");
            System.out.println("1. View courses");
            System.out.println("2. View students");
            System.out.println("3. Assign grades");
            System.out.println("4. View average rating");
            System.out.println("5. Research demo");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n-- Courses --");
                    teacher.viewCourses().forEach(System.out::println);
                    break;

                case "2":
                    System.out.println("\n-- Students --");
                    teacher.viewStudents().forEach(System.out::println);
                    break;

                case "3":
                    System.out.println("\n-- Assign grades --");
                    System.out.println("1. " + student1.getFullName());
                    System.out.println("2. " + student2.getFullName());
                    System.out.print("Select a student: ");
                    String s = scanner.nextLine().trim();
                    Student target = s.equals("1") ? student1 : student2;

                    try {
                        System.out.print("First attestation (0-30): ");
                        double att1 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Second attestation (0-30): ");
                        double att2 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Final exam (0-40): ");
                        double fin = Double.parseDouble(scanner.nextLine().trim());

                        // ===== TRY-CATCH for TooManyFailuresException =====
                        try {
                            teacher.putMarks(target, oop, att1, att2, fin);
                        } catch (TooManyFailuresException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        // =================================================

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number entered.");
                    }
                    break;

                case "4":
                    System.out.printf("Average rating: %.1f%n",
                            teacher.calculateAverageRating());
                    break;

                case "5":
                    researchDemoTeacher(teacher);
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ==================== MANAGER MENU ====================

    private static void managerMenu(Manager manager, Course oop, Course math,
                                    Course ds, Teacher teacher1, Teacher teacher2,
                                    Student student1, Student student2) {
        manager.addStudent(student1);
        manager.addStudent(student2);
        manager.addTeacher(teacher1);
        manager.addTeacher(teacher2);

        boolean running = true;
        while (running) {
            System.out.println("\n--- MANAGER MENU: " + manager.getFullName() + " ---");
            System.out.println("1. Approve students");
            System.out.println("2. Assign a course to a teacher");
            System.out.println("3. View students by GPA");
            System.out.println("4. View students by name");
            System.out.println("5. Generate report");
            System.out.println("6. Add news");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    manager.approveStudent(student1);
                    manager.approveStudent(student2);
                    break;

                case "2":
                    manager.assignCourseToTeacher(oop, teacher1);
                    manager.assignCourseToTeacher(math, teacher2);
                    break;

                case "3":
                    System.out.println("\n-- Students sorted by GPA --");
                    manager.viewStudents(SortingCriteria.GPA)
                            .forEach(st -> System.out.println(
                                    st.getFullName() + " - GPA: " + st.getGpa()));
                    break;

                case "4":
                    System.out.println("\n-- Students sorted by name --");
                    manager.viewStudents(SortingCriteria.NAME)
                            .forEach(st -> System.out.println(st.getFullName()));
                    break;

                case "5":
                    manager.generateReport();
                    break;

                case "6":
                    System.out.print("News title: ");
                    String title = scanner.nextLine().trim();
                    System.out.print("Content: ");
                    String content = scanner.nextLine().trim();
                    manager.addNews(new News(title, content, "General"));
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ==================== ADMIN MENU ====================

    private static void adminMenu(Admin admin) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View all users");
            System.out.println("2. Search for a user by name");
            System.out.println("3. Remove a user");
            System.out.println("4. View logs");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n-- All users --");
                    admin.getUsers().forEach(System.out::println);
                    break;

                case "2":
                    System.out.print("Name: ");
                    String name = scanner.nextLine().trim();
                    project.users.User found = admin.findUserByName(name);
                    if (found != null) {
                        System.out.println("Found: " + found);
                    } else {
                        System.out.println("User not found.");
                    }
                    break;

                case "3":
                    System.out.print("Enter ID: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        admin.removeUser(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                    break;

                case "4":
                    System.out.println("\n-- Logs --");
                    admin.viewLogs().forEach(System.out::println);
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ==================== RESEARCH DEMO (STUDENT) ====================

    private static void researchDemoStudent(Student student) {
        System.out.println("\n--- RESEARCH DEMO ---");

        // Make the student a researcher
        student.activateResearchProfile(4.0);

        // Create research papers (ALL DATES: January - May range, year 2026)
        ResearchPaper paper1 = new ResearchPaper("AI in Education",
                "IEEE Journal", LocalDate.of(2026, 2, 15), 12);  // February 15
        paper1.setDoi("10.1109/AI.2026.001");
        paper1.setCitations(25);

        ResearchPaper paper2 = new ResearchPaper("Machine Learning Trends",
                "Springer", LocalDate.of(2026, 4, 10), 8);       // April 10
        paper2.setDoi("10.1007/ML.2026.002");
        paper2.setCitations(10);

        student.addPaper(paper1);
        student.addPaper(paper2);

        // Project
        ResearchProject project = new ResearchProject("AI Research 2026");
        try {
            student.joinProject(project);
            System.out.println("The student joined the project.");
        } catch (NonResearchException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Student without researcher status
        Student nonResearcher = new Student("Test Student", "test@uni.kz",
                "test", 999, 3.0, Faculty.IT, 1);
        System.out.println("\n-- A non-research student tries to join the project --");
        try {
            nonResearcher.joinProject(project);
        } catch (NonResearchException e) {
            System.out.println("Exception thrown: " + e.getMessage());
        }

        // Sort papers
        System.out.println("\n-- Sorted by citations --");
        student.printPapers(Comparator.comparingInt(ResearchPaper::getCitationsCount).reversed());

        System.out.println("\n-- Sorted by date --");
        student.printPapers(Comparator.comparing(ResearchPaper::getDatePublished));

        System.out.println("\n-- Sorted by page count --");
        student.printPapers(Comparator.comparingInt(ResearchPaper::getPages).reversed());

        // Employee researcher and supervisor requirement
        try {
            Supervisor supervisor = new Supervisor(
                    "Dr. Omar Research",
                    "omar.research@uni.kz",
                    "super123",
                    401,
                    "Research Supervisor",
                    6.7
            );

            Student seniorStudent = new Student(
                    "Amina Research",
                    "amina@uni.kz",
                    "amin123",
                    555,
                    3.9,
                    Faculty.IT,
                    4
            );
            seniorStudent.activateResearchProfile(3.8);
            seniorStudent.assignSupervisor(supervisor);

            ResearchPaper supervisorPaper = new ResearchPaper(
                    "Distributed Systems for Campus Research",
                    "ACM Digital Library",
                    LocalDate.of(2026, 1, 20),  // January 20
                    18
            );
            supervisorPaper.setDoi("10.1145/DS.2026.010");
            supervisorPaper.setCitations(40);
            supervisor.addPaper(supervisorPaper);

            Teacher lecturerResearcher = new Teacher(
                    "Madina Ibrayeva",
                    "madina@uni.kz",
                    "lect123",
                    777,
                    "Senior Lecturer",
                    TeacherStatus.SENIOR_LECTOR,
                    7
            );
            lecturerResearcher.activateResearchProfile(4.5);

            ResearchPaper lecturerPaper = new ResearchPaper(
                    "Software Testing in Education",
                    "Elsevier",
                    LocalDate.of(2026, 3, 5),   // March 5
                    14
            );
            lecturerPaper.setDoi("10.1016/STE.2026.021");
            lecturerPaper.setCitations(60);
            lecturerResearcher.addPaper(lecturerPaper);

            List<Researcher> schoolResearchers = Arrays.asList(student, lecturerResearcher, supervisor);

            System.out.println("\n-- All university researchers' papers sorted by citations --");
            ResearchAnalytics.printUniversityPapers(
                    schoolResearchers,
                    Comparator.comparingInt(ResearchPaper::getCitationsCount).reversed()
            );

            ResearchAnalytics.findTopCitedResearcher(schoolResearchers).ifPresent(topResearcher ->
                    System.out.println(
                            "\nTop cited researcher of the school: " +
                                    ResearchAnalytics.researcherName(topResearcher) +
                                    " (" + ResearchAnalytics.totalCitations(topResearcher) + " citations)"
                    )
            );

            Teacher anotherSchoolProfessor = new Teacher(
                    "Professor Helen Grant",
                    "helen@other.edu",
                    "prof123",
                    888,
                    "Professor",
                    TeacherStatus.PROFESSOR,
                    12
            );

            ResearchPaper globalPaper = new ResearchPaper(
                    "Global AI Curriculum Review",
                    "ScienceDirect",
                    LocalDate.of(2026, 5, 1),   // May 1
                    22
            );
            globalPaper.setDoi("10.1016/GAI.2026.099");
            globalPaper.setCitations(95);
            anotherSchoolProfessor.addPaper(globalPaper);

            List<List<Researcher>> allSchools = Arrays.asList(
                    schoolResearchers,
                    Arrays.asList(anotherSchoolProfessor)
            );

            ResearchAnalytics.findTopCitedResearcherOfYear(allSchools, 2026).ifPresent(topResearcher ->
                    System.out.println(
                            "Top cited researcher of 2026 among all schools: " +
                                    ResearchAnalytics.researcherName(topResearcher) +
                                    " (" + ResearchAnalytics.citationsForYear(topResearcher, 2026) + " citations)"
                    )
            );
        } catch (InvalidSupervisorException e) {
            System.out.println("Supervisor error: " + e.getMessage());
        }

        // Publisher (Observer pattern)
        System.out.println("\n-- Observer: ResearchPaperPublisher --");
        ResearchPaperPublisher publisher = new ResearchPaperPublisher();
        publisher.subscribe(student);
        publisher.publishPaper(paper1);
    }

    // ==================== RESEARCH DEMO (TEACHER) ====================

    private static void researchDemoTeacher(Teacher teacher) {
        System.out.println("\n--- TEACHER RESEARCH DEMO ---");

        // Professors are researchers automatically; other teachers can activate a research profile
        System.out.println("isProfessor: " + teacher.isProfessor());
        System.out.println("isResearcher: " + teacher.isResearcher());
        if (!teacher.isResearcher()) {
            teacher.activateResearchProfile(5.5);
            System.out.println("Research profile activated for " + teacher.getFullName());
        } else {
            teacher.setHIndex(5.5);
        }

        ResearchPaper paper = new ResearchPaper("Deep Learning Survey",
                "Nature", LocalDate.of(2026, 2, 28), 20);  // February 28
        paper.setCitations(100);
        paper.setDoi("10.1038/DL.2026.001");

        News news = new News("New Paper!", "Professor published a paper.", "Research");
        teacher.addPaper(paper);
        teacher.publishPaper(paper, news);

        ResearchProject project = new ResearchProject("Deep Learning Lab");
        try {
            teacher.joinProject(project);
        } catch (NonResearchException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n-- Papers sorted by citations --");
        teacher.printPapers(Comparator.comparingInt(ResearchPaper::getCitationsCount).reversed());
    }
}