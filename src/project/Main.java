package project;

import project.enums.*;
import project.exceptions.*;
import project.factory.UserFactory;
import project.interfaces.Researcher;
import project.models.*;
import project.publisher.ResearchPaperPublisher;
import project.users.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // ==================== ДЕРЕКТЕР ДАЙЫНДАУ ====================

        // Курстар
        Course oop = new Course("OOP", LessonType.LECTURE, Language.EN,
                CourseType.MAJOR, Faculty.IT, 5);
        Course math = new Course("Calculus", LessonType.PRACTICE, Language.KZ,
                CourseType.MAJOR, Faculty.IT, 4);
        Course ds = new Course("Data Structures", LessonType.LECTURE, Language.EN,
                CourseType.MAJOR, Faculty.IT, 5);

        // Мұғалімдер
        Teacher teacher1 = new Teacher("Aibek Seitkali", "aibek@uni.kz",
                "teach123", 101, "Professor", TeacherStatus.PROFESSOR, 10);
        Teacher teacher2 = new Teacher("Zarina Bekova", "zarina@uni.kz",
                "teach456", 102, "Senior Lecturer", TeacherStatus.SENIOR_LECTOR, 5);

        // Студенттер
        Student student1 = new Student("Arman Nurlanов", "arman@uni.kz",
                "stud123", 201, 3.5, Faculty.IT, 2);
        Student student2 = new Student("Dana Seitkali", "dana@uni.kz",
                "stud456", 202, 3.8, Faculty.IT, 4);

        // Менеджер
        Manager manager = new Manager("Gulnara Abdova", "gulnara@uni.kz",
                "man123", 301, "OR Manager", ManagerType.OFFICEREGISTRATION);

        // Admin
        Admin admin = Admin.getInstance();

        // Барлығын admin-ге қос
        admin.addUser(teacher1);
        admin.addUser(teacher2);
        admin.addUser(student1);
        admin.addUser(student2);
        admin.addUser(manager);

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

            // Барлық userлерден іздеу
            for (project.users.User user : admin.getUsers()) {
                if (user.login(email, password)) {
                    System.out.println("\nСәтті кірдіңіз! Қош келдіңіз, "
                            + user.getFullName());
                    loggedIn = true;
                    runMenu(user, oop, math, ds, teacher1, teacher2,
                            student1, student2, manager);
                    break;
                }
            }

            if (!loggedIn) {
                attempts++;
                System.out.println("Қате email немесе пароль. "
                        + (3 - attempts) + " мүмкіндік қалды.");
            }
        }

        if (!loggedIn) {
            System.out.println("Жүйеге кіру мүмкін болмады.");
        }

        scanner.close();
    }

    // ==================== МЕНЮ ====================

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

    // ==================== STUDENT МЕНЮ ====================

    private static void studentMenu(Student student, Course oop,
                                    Course math, Course ds, Teacher teacher) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- STUDENT MENU: " + student.getFullName() + " ---");
            System.out.println("1. Курсқа тіркелу");
            System.out.println("2. Курстарды көру");
            System.out.println("3. Бағаларды көру");
            System.out.println("4. Транскрипт");
            System.out.println("5. Мұғалімге баға беру");
            System.out.println("6. Research (joinProject, printPapers)");
            System.out.println("0. Шығу");
            System.out.print("Таңдаңыз: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n-- Қолжетімді курстар --");
                    System.out.println("1. " + oop);
                    System.out.println("2. " + math);
                    System.out.println("3. " + ds);
                    System.out.print("Курс таңдаңыз: ");
                    String c = scanner.nextLine().trim();
                    if (c.equals("1")) student.enrollCourse(oop);
                    else if (c.equals("2")) student.enrollCourse(math);
                    else if (c.equals("3")) student.enrollCourse(ds);
                    break;

                case "2":
                    System.out.println("\n-- Тіркелген курстар --");
                    if (student.viewCourses().isEmpty()) {
                        System.out.println("Курс жоқ.");
                    } else {
                        student.viewCourses().forEach(System.out::println);
                    }
                    break;

                case "3":
                    System.out.println("\n-- Бағалар --");
                    if (student.viewMarks().isEmpty()) {
                        System.out.println("Баға жоқ.");
                    } else {
                        student.viewMarks().forEach((course, mark) ->
                                System.out.println(course.getName() + ": " + mark));
                    }
                    break;

                case "4":
                    System.out.println("\n" + student.viewTranscript());
                    break;

                case "5":
                    System.out.print("Рейтинг енгізіңіз (1-5): ");
                    try {
                        int rating = Integer.parseInt(scanner.nextLine().trim());
                        student.rateTeacher(teacher, rating);
                    } catch (NumberFormatException e) {
                        System.out.println("Қате сан.");
                    }
                    break;

                case "6":
                    researchDemoStudent(student);
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Қате таңдау.");
            }
        }
    }

    // ==================== TEACHER МЕНЮ ====================

    private static void teacherMenu(Teacher teacher, Course oop,
                                    Student student1, Student student2) {
        // Алдын ала мұғалімді курсқа тағайындау
        teacher.manageCourse(oop);
        teacher.addStudent(student1);
        teacher.addStudent(student2);

        boolean running = true;
        while (running) {
            System.out.println("\n--- TEACHER MENU: " + teacher.getFullName() + " ---");
            System.out.println("1. Курстарды көру");
            System.out.println("2. Студенттерді көру");
            System.out.println("3. Баға қою");
            System.out.println("4. Орташа рейтинг");
            System.out.println("5. Research demo");
            System.out.println("0. Шығу");
            System.out.print("Таңдаңыз: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n-- Курстар --");
                    teacher.viewCourses().forEach(System.out::println);
                    break;

                case "2":
                    System.out.println("\n-- Студенттер --");
                    teacher.viewStudents().forEach(System.out::println);
                    break;

                case "3":
                    System.out.println("\n-- Баға қою --");
                    System.out.println("1. " + student1.getFullName());
                    System.out.println("2. " + student2.getFullName());
                    System.out.print("Студент таңдаңыз: ");
                    String s = scanner.nextLine().trim();
                    Student target = s.equals("1") ? student1 : student2;

                    try {
                        System.out.print("1-аттестация (0-30): ");
                        double att1 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("2-аттестация (0-30): ");
                        double att2 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Финал (0-40): ");
                        double fin = Double.parseDouble(scanner.nextLine().trim());
                        teacher.putMarks(target, oop, att1, att2, fin);
                    } catch (NumberFormatException e) {
                        System.out.println("Қате сан енгізілді.");
                    }
                    break;

                case "4":
                    System.out.printf("Орташа рейтинг: %.1f%n",
                            teacher.calculateAverageRating());
                    break;

                case "5":
                    researchDemoTeacher(teacher);
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Қате таңдау.");
            }
        }
    }

    // ==================== MANAGER МЕНЮ ====================

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
            System.out.println("1. Студентті бекіту");
            System.out.println("2. Мұғалімге курс тағайындау");
            System.out.println("3. Студенттерді GPA бойынша көру");
            System.out.println("4. Студенттерді аты бойынша көру");
            System.out.println("5. Есеп жасау");
            System.out.println("6. Жаңалық қосу");
            System.out.println("0. Шығу");
            System.out.print("Таңдаңыз: ");

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
                    System.out.println("\n-- GPA бойынша --");
                    manager.viewStudents(SortingCriteria.GPA)
                            .forEach(st -> System.out.println(
                                    st.getFullName() + " — GPA: " + st.getGpa()));
                    break;

                case "4":
                    System.out.println("\n-- Аты бойынша --");
                    manager.viewStudents(SortingCriteria.NAME)
                            .forEach(st -> System.out.println(st.getFullName()));
                    break;

                case "5":
                    manager.generateReport();
                    break;

                case "6":
                    System.out.print("Жаңалық тақырыбы: ");
                    String title = scanner.nextLine().trim();
                    System.out.print("Мазмұны: ");
                    String content = scanner.nextLine().trim();
                    manager.addNews(new News(title, content, "General"));
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Қате таңдау.");
            }
        }
    }

    // ==================== ADMIN МЕНЮ ====================

    private static void adminMenu(Admin admin) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Барлық userлерді көру");
            System.out.println("2. User іздеу (аты бойынша)");
            System.out.println("3. User жою");
            System.out.println("4. Логтарды көру");
            System.out.println("0. Шығу");
            System.out.print("Таңдаңыз: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n-- Барлық userлер --");
                    admin.getUsers().forEach(System.out::println);
                    break;

                case "2":
                    System.out.print("Аты: ");
                    String name = scanner.nextLine().trim();
                    project.users.User found = admin.findUserByName(name);
                    if (found != null) System.out.println("Табылды: " + found);
                    break;

                case "3":
                    System.out.print("ID енгізіңіз: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        admin.removeUser(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Қате ID.");
                    }
                    break;

                case "4":
                    System.out.println("\n-- Логтар --");
                    admin.viewLogs().forEach(System.out::println);
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Қате таңдау.");
            }
        }
    }

    // ==================== RESEARCH DEMO (STUDENT) ====================

    private static void researchDemoStudent(Student student) {
        System.out.println("\n--- RESEARCH DEMO ---");

        // Студентті researcher ет
        student.setResearcher(true);
        student.setHIndex(4.0);

        // Мақала жасау
        ResearchPaper paper1 = new ResearchPaper("AI in Education",
                "IEEE Journal", LocalDate.of(2023, 5, 10), 12);
        paper1.setDoi("10.1109/AI.2023.001");
        paper1.setCitations(25);

        ResearchPaper paper2 = new ResearchPaper("Machine Learning Trends",
                "Springer", LocalDate.of(2022, 8, 20), 8);
        paper2.setDoi("10.1007/ML.2022.002");
        paper2.setCitations(10);

        student.addPaper(paper1);
        student.addPaper(paper2);

        // Project
        ResearchProject project = new ResearchProject("AI Research 2024");
        try {
            student.joinProject(project);
            System.out.println("Студент жобаға қосылды!");
        } catch (NonResearchException e) {
            System.out.println("Қате: " + e.getMessage());
        }

        // Researcher емес студент
        Student nonResearcher = new Student("Test Student", "test@uni.kz",
                "test", 999, 3.0, Faculty.IT, 1);
        System.out.println("\n-- Researcher емес студент joinProject жасаса --");
        try {
            nonResearcher.joinProject(project);
        } catch (NonResearchException e) {
            System.out.println("Exception ұсталды: " + e.getMessage());
        }

        // Мақалаларды сұрыптау
        System.out.println("\n-- Citations бойынша --");
        student.printPapers(Comparator.comparingInt(ResearchPaper::getCitationsCount).reversed());

        System.out.println("\n-- Күні бойынша --");
        student.printPapers(Comparator.comparing(ResearchPaper::getDatePublished));

        System.out.println("\n-- Беттер бойынша --");
        student.printPapers(Comparator.comparingInt(ResearchPaper::getPages).reversed());

        // Publisher (Observer pattern)
        System.out.println("\n-- Observer: ResearchPaperPublisher --");
        ResearchPaperPublisher publisher = new ResearchPaperPublisher();
        publisher.subscribe(student);
        publisher.publishPaper(paper1);
    }

    // ==================== RESEARCH DEMO (TEACHER) ====================

    private static void researchDemoTeacher(Teacher teacher) {
        System.out.println("\n--- RESEARCH DEMO (PROFESSOR) ---");

        // Professor автоматты researcher
        System.out.println("isProfessor: " + teacher.isProfessor());
        System.out.println("isResearcher: " + teacher.isResearcher());
        teacher.setHIndex(5.5);

        ResearchPaper paper = new ResearchPaper("Deep Learning Survey",
                "Nature", LocalDate.of(2024, 1, 15), 20);
        paper.setCitations(100);
        paper.setDoi("10.1038/DL.2024.001");

        News news = new News("New Paper!", "Professor published a paper.", "Research");
        teacher.addPaper(paper);
        teacher.publishPaper(paper, news);

        ResearchProject project = new ResearchProject("Deep Learning Lab");
        try {
            teacher.joinProject(project);
        } catch (NonResearchException e) {
            System.out.println("Қате: " + e.getMessage());
        }

        System.out.println("\n-- Мақалалар citations бойынша --");
        teacher.printPapers(Comparator.comparingInt(ResearchPaper::getCitationsCount).reversed());
    }
}