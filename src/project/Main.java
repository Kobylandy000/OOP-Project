package project;

import project.enums.*;
import project.exceptions.*;
import project.interfaces.Researcher;
import project.models.*;
import project.publisher.ResearchPaperPublisher;
import project.services.ResearchAnalytics;
import project.users.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import project.storage.AppData;
import project.storage.DataRepository;
import java.util.ArrayList;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static DataRepository repository = DataRepository.getInstance();
    private static AppData currentData;

    public static void main(String[] args) {
        // ==================== LOAD OR CREATE DATA ====================
        currentData = repository.load();

        if (currentData == null || currentData.isEmpty()) {
            currentData = createDefaultData();
            repository.save(currentData);
        }

        Admin admin = currentData.getAdmin();
        Admin.setInstance(admin);

        Course oop = currentData.findCourseByName("OOP");
        Course math = currentData.findCourseByName("Calculus");
        Course ds = currentData.findCourseByName("Data Structures");

        Teacher teacher1 = (Teacher) currentData.findUserByEmail("aibek@uni.kz");
        Teacher teacher2 = (Teacher) currentData.findUserByEmail("zarina@uni.kz");

        Student student1 = (Student) currentData.findUserByEmail("arman@uni.kz");
        Student student2 = (Student) currentData.findUserByEmail("dana@uni.kz");

        Manager manager = (Manager) currentData.findUserByEmail("gulnara@uni.kz");

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
        repository.save(currentData);
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

        // Tech Supporter
        TechSupporter techSupporter = new TechSupporter("Ivan Petrov", "ivan@uni.kz",
                "tech123", 401, "Tech Support Specialist");

        // Admin
        Admin admin = Admin.getInstance();

        admin.addUser(teacher1);
        admin.addUser(teacher2);
        admin.addUser(student1);
        admin.addUser(student2);
        admin.addUser(manager);
        admin.addUser(techSupporter);

        List<Course> courses = new ArrayList<>();
        courses.add(oop);
        courses.add(math);
        courses.add(ds);

        List<ResearchPaper> researchPapers = new ArrayList<>();
        List<ResearchProject> researchProjects = new ArrayList<>();
        List<News> news = new ArrayList<>();

        return new AppData(admin, courses, researchPapers, researchProjects, news);
    }

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

    private static void runMenu(project.users.User user, Course oop, Course math,
                                Course ds, Teacher teacher1, Teacher teacher2,
                                Student student1, Student student2, Manager manager) {
        if (user instanceof Student) {
            studentMenu((Student) user, oop, math, ds, teacher1);
        } else if (user instanceof Teacher) {
            teacherMenu((Teacher) user, oop, student1, student2);
        } else if (user instanceof Manager) {
            managerMenu((Manager) user, oop, math, ds, teacher1, teacher2, student1, student2);
        } else if (user instanceof Admin) {
            adminMenu((Admin) user);
        } else if (user instanceof TechSupporter) {
            techSupporterMenu((TechSupporter) user);
        }
    }

    // ==================== STUDENT MENU ====================
    private static void studentMenu(Student student, Course oop,
                                    Course math, Course ds, Teacher teacher) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- STUDENT MENU: " + student.getFullName() + " ---");
            System.out.println("1. Enroll in a course");
            System.out.println("2. View my courses");
            System.out.println("3. View grades");
            System.out.println("4. View transcript");
            System.out.println("5. Rate a teacher");
            System.out.println("6. Research activities");
            System.out.println("7. Subscribe to journal");
            System.out.println("8. Unsubscribe from journal");
            System.out.println("9. View my subscriptions");
            System.out.println("10. View all available courses");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n-- Available courses --");
                    List<Course> availableCourses = currentData.getCourses();
                    for (int i = 0; i < availableCourses.size(); i++) {
                        System.out.println((i + 1) + ". " + availableCourses.get(i));
                    }
                    System.out.print("Select a course: ");
                    try {
                        int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
                        if (idx >= 0 && idx < availableCourses.size()) {
                            student.enrollCourse(availableCourses.get(idx));
                            repository.save(currentData);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number.");
                    } catch (CreditLimitExceededException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case "2":
                    System.out.println("\n-- My courses --");
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
                    System.out.print("Enter teacher email: ");
                    String teacherEmail = scanner.nextLine().trim();
                    Teacher targetTeacher = (Teacher) currentData.findUserByEmail(teacherEmail);
                    if (targetTeacher == null) {
                        System.out.println("Teacher not found.");
                        break;
                    }
                    System.out.print("Enter rating (1-5): ");
                    try {
                        int rating = Integer.parseInt(scanner.nextLine().trim());
                        student.rateTeacher(targetTeacher, rating);
                        repository.save(currentData);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number.");
                    }
                    break;

                case "6":
                    researchDemoStudent(student);
                    repository.save(currentData);
                    break;

                case "7":
                    System.out.print("Enter journal name: ");
                    String journalName = scanner.nextLine().trim();
                    System.out.print("Enter publisher: ");
                    String publisher = scanner.nextLine().trim();
                    UniversityJournal journal = new UniversityJournal(journalName, publisher);
                    student.subscribe(journal);
                    repository.save(currentData);
                    break;

                case "8":
                    System.out.print("Enter journal name to unsubscribe: ");
                    String unsubJournal = scanner.nextLine().trim();
                    for (UniversityJournal j : student.getSubscriptions()) {
                        if (j.getName().equalsIgnoreCase(unsubJournal)) {
                            student.unsubscribe(j);
                            System.out.println("Unsubscribed from: " + unsubJournal);
                            break;
                        }
                    }
                    repository.save(currentData);
                    break;

                case "9":
                    System.out.println("\n-- My subscriptions --");
                    if (student.getSubscriptions().isEmpty()) {
                        System.out.println("No subscriptions.");
                    } else {
                        student.getSubscriptions().forEach(j -> 
                            System.out.println(j.getName() + " (" + j.getPublisher() + ")"));
                    }
                    break;

                case "10":
                    System.out.println("\n-- All available courses --");
                    currentData.getCourses().forEach(System.out::println);
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
        teacher.manageCourse(oop);
        teacher.addStudent(student1);
        teacher.addStudent(student2);

        boolean running = true;
        while (running) {
            System.out.println("\n--- TEACHER MENU: " + teacher.getFullName() + " ---");
            System.out.println("1. View my courses");
            System.out.println("2. View my students");
            System.out.println("3. Assign grades to student");
            System.out.println("4. View average rating");
            System.out.println("5. Research activities");
            System.out.println("6. Send complaint about student");
            System.out.println("7. View all courses");
            System.out.println("8. Publish research paper");
            System.out.println("9. Subscribe to journal");
            System.out.println("10. View my subscriptions");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n-- My courses --");
                    teacher.viewCourses().forEach(System.out::println);
                    break;

                case "2":
                    System.out.println("\n-- My students --");
                    teacher.viewStudents().forEach(System.out::println);
                    break;

                case "3":
                    System.out.println("\n-- Assign grades --");
                    List<Student> myStudents = teacher.viewStudents();
                    for (int i = 0; i < myStudents.size(); i++) {
                        System.out.println((i + 1) + ". " + myStudents.get(i).getFullName());
                    }
                    System.out.print("Select a student: ");
                    try {
                        int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
                        if (idx < 0 || idx >= myStudents.size()) {
                            System.out.println("Invalid student.");
                            break;
                        }
                        Student target = myStudents.get(idx);

                        System.out.print("Select course: ");
                        List<Course> myCourses = teacher.viewCourses();
                        for (int i = 0; i < myCourses.size(); i++) {
                            System.out.println((i + 1) + ". " + myCourses.get(i).getName());
                        }
                        int courseIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
                        if (courseIdx < 0 || courseIdx >= myCourses.size()) {
                            System.out.println("Invalid course.");
                            break;
                        }
                        Course targetCourse = myCourses.get(courseIdx);

                        System.out.print("First attestation (0-30): ");
                        double att1 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Second attestation (0-30): ");
                        double att2 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Final exam (0-40): ");
                        double fin = Double.parseDouble(scanner.nextLine().trim());

                        try {
                            teacher.putMarks(target, targetCourse, att1, att2, fin);
                            repository.save(currentData);
                        } catch (TooManyFailuresException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number entered.");
                    }
                    break;

                case "4":
                    System.out.printf("Average rating: %.1f%n", teacher.calculateAverageRating());
                    break;

                case "5":
                    researchDemoTeacher(teacher);
                    repository.save(currentData);
                    break;

                case "6":
                    System.out.print("Student email: ");
                    String studentEmail = scanner.nextLine().trim();
                    Student complaintStudent = (Student) currentData.findUserByEmail(studentEmail);
                    if (complaintStudent == null) {
                        System.out.println("Student not found.");
                        break;
                    }
                    System.out.print("Complaint text: ");
                    String complaintText = scanner.nextLine().trim();
                    System.out.print("Urgency (LOW/MEDIUM/HIGH): ");
                    try {
                        UrgencyLevel level = UrgencyLevel.valueOf(scanner.nextLine().trim().toUpperCase());
                        teacher.sendComplaint(complaintText, complaintStudent, level);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid urgency level.");
                    }
                    break;

                case "7":
                    System.out.println("\n-- All courses --");
                    currentData.getCourses().forEach(System.out::println);
                    break;

                case "8":
                    System.out.print("Paper title: ");
                    String paperTitle = scanner.nextLine().trim();
                    System.out.print("Journal: ");
                    String journal = scanner.nextLine().trim();
                    System.out.print("Pages: ");
                    int pages = Integer.parseInt(scanner.nextLine().trim());
                    ResearchPaper newPaper = new ResearchPaper(paperTitle, journal, LocalDate.now(), pages);
                    News news = new News("New paper published", paperTitle, "Research");
                    teacher.publishPaper(newPaper, news);
                    currentData.getResearchPapers().add(newPaper);
                    repository.save(currentData);
                    break;

                case "9":
                    System.out.print("Enter journal name: ");
                    String journalName = scanner.nextLine().trim();
                    System.out.print("Enter publisher: ");
                    String publisher = scanner.nextLine().trim();
                    UniversityJournal journalObj = new UniversityJournal(journalName, publisher);
                    teacher.subscribe(journalObj);
                    repository.save(currentData);
                    break;

                case "10":
                    System.out.println("\n-- My subscriptions --");
                    teacher.getSubscriptions().forEach(j -> 
                        System.out.println(j.getName() + " (" + j.getPublisher() + ")"));
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
            System.out.println("1. Approve student");
            System.out.println("2. Assign course to teacher");
            System.out.println("3. View students sorted by GPA");
            System.out.println("4. View students sorted by name");
            System.out.println("5. Generate report");
            System.out.println("6. Add news");
            System.out.println("7. View all news");
            System.out.println("8. Add new course");
            System.out.println("9. View pending requests");
            System.out.println("10. Approve request");
            System.out.println("11. View all students");
            System.out.println("12. View all teachers");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Student email to approve: ");
                    String studentEmail = scanner.nextLine().trim();
                    Student student = (Student) currentData.findUserByEmail(studentEmail);
                    if (student != null) {
                        manager.approveStudent(student);
                        repository.save(currentData);
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case "2":
                    System.out.print("Course name: ");
                    String courseName = scanner.nextLine().trim();
                    Course course = currentData.findCourseByName(courseName);
                    if (course == null) {
                        System.out.println("Course not found.");
                        break;
                    }
                    System.out.print("Teacher email: ");
                    String teacherEmail = scanner.nextLine().trim();
                    Teacher teacher = (Teacher) currentData.findUserByEmail(teacherEmail);
                    if (teacher != null) {
                        manager.assignCourseToTeacher(course, teacher);
                        repository.save(currentData);
                    } else {
                        System.out.println("Teacher not found.");
                    }
                    break;

                case "3":
                    System.out.println("\n-- Students sorted by GPA --");
                    manager.viewStudents(SortingCriteria.GPA)
                            .forEach(st -> System.out.println(st.getFullName() + " - GPA: " + st.getGpa()));
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
                    System.out.print("Topic: ");
                    String topic = scanner.nextLine().trim();
                    News news = new News(title, content, topic);
                    manager.addNews(news);
                    currentData.getNews().add(news);
                    repository.save(currentData);
                    break;

                case "7":
                    System.out.println("\n-- All news --");
                    for (News n : currentData.getNews()) {
                        System.out.println(n);
                    }
                    break;

                case "8":
                    System.out.print("Course name: ");
                    String newCourseName = scanner.nextLine().trim();
                    System.out.print("Lesson type (LECTURE/PRACTICE/OFFICEHOURS): ");
                    LessonType lessonType = LessonType.valueOf(scanner.nextLine().trim().toUpperCase());
                    System.out.print("Language (EN/KZ/RU): ");
                    Language lang = Language.valueOf(scanner.nextLine().trim().toUpperCase());
                    System.out.print("Course type (MAJOR/MINOR/ELECTIVE): ");
                    CourseType courseType = CourseType.valueOf(scanner.nextLine().trim().toUpperCase());
                    System.out.print("Faculty (SITE/OILGAS/IT): ");
                    Faculty faculty = Faculty.valueOf(scanner.nextLine().trim().toUpperCase());
                    System.out.print("Credits: ");
                    int credits = Integer.parseInt(scanner.nextLine().trim());
                    Course newCourse = new Course(newCourseName, lessonType, lang, courseType, faculty, credits);
                    currentData.getCourses().add(newCourse);
                    System.out.print("Assign to major? (yes/no): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                        System.out.print("Major name: ");
                        String majorName = scanner.nextLine().trim();
                        Major major = new Major(majorName);
                        System.out.print("Year: ");
                        int year = Integer.parseInt(scanner.nextLine().trim());
                        manager.addCourse(newCourse, major, year);
                    }
                    repository.save(currentData);
                    break;

                case "9":
                    System.out.println("\n-- Pending requests --");
                    manager.viewPendingRequests().forEach(System.out::println);
                    break;

                case "10":
                    System.out.print("Request description: ");
                    String reqDesc = scanner.nextLine().trim();
                    for (Request req : manager.viewPendingRequests()) {
                        if (req.getDescription().equalsIgnoreCase(reqDesc)) {
                            req.approve();
                            break;
                        }
                    }
                    repository.save(currentData);
                    break;

                case "11":
                    System.out.println("\n-- All students --");
                    manager.viewStudents(SortingCriteria.NAME).forEach(System.out::println);
                    break;

                case "12":
                    System.out.println("\n-- All teachers --");
                    manager.viewTeachers(SortingCriteria.NAME).forEach(System.out::println);
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
            System.out.println("3. Search for a user by ID");
            System.out.println("4. Remove a user");
            System.out.println("5. View logs");
            System.out.println("6. Add new user");
            System.out.println("7. Update user");
            System.out.println("8. View users by type");
            System.out.println("9. Save data");
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
                        project.users.User userById = admin.findUserById(id);
                        if (userById != null) {
                            System.out.println("Found: " + userById);
                        } else {
                            System.out.println("User not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                    break;

                case "4":
                    System.out.print("Enter ID: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        admin.removeUser(id);
                        repository.save(currentData);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                    break;

                case "5":
                    System.out.println("\n-- Logs --");
                    admin.viewLogs().forEach(System.out::println);
                    break;

                case "6":
                    System.out.print("User type (STUDENT/TEACHER/MANAGER/TECHSUPPORTER): ");
                    String type = scanner.nextLine().trim().toUpperCase();
                    System.out.print("Full name: ");
                    String fullName = scanner.nextLine().trim();
                    System.out.print("Email: ");
                    String email = scanner.nextLine().trim();
                    System.out.print("Password: ");
                    String password = scanner.nextLine().trim();
                    System.out.print("ID: ");
                    int id = Integer.parseInt(scanner.nextLine().trim());

                    project.users.User newUser = null;
                    switch (type) {
                        case "STUDENT":
                            System.out.print("GPA: ");
                            double gpa = Double.parseDouble(scanner.nextLine().trim());
                            System.out.print("Faculty (SITE/OILGAS/IT): ");
                            Faculty faculty = Faculty.valueOf(scanner.nextLine().trim().toUpperCase());
                            System.out.print("Year: ");
                            int year = Integer.parseInt(scanner.nextLine().trim());
                            newUser = new Student(fullName, email, password, id, gpa, faculty, year);
                            break;
                        case "TEACHER":
                            System.out.print("Position: ");
                            String position = scanner.nextLine().trim();
                            System.out.print("Status (LECTOR/PRACTICIONER/SENIOR_LECTOR/PROFESSOR): ");
                            TeacherStatus status = TeacherStatus.valueOf(scanner.nextLine().trim().toUpperCase());
                            System.out.print("Years of experience: ");
                            int exp = Integer.parseInt(scanner.nextLine().trim());
                            newUser = new Teacher(fullName, email, password, id, position, status, exp);
                            break;
                        case "MANAGER":
                            System.out.print("Position: ");
                            String mgrPosition = scanner.nextLine().trim();
                            System.out.print("Manager type (DEANMANAGER/OFFICEREGISTRATION/FINANCEMANAGER): ");
                            ManagerType mgrType = ManagerType.valueOf(scanner.nextLine().trim().toUpperCase());
                            newUser = new Manager(fullName, email, password, id, mgrPosition, mgrType);
                            break;
                        case "TECHSUPPORTER":
                            System.out.print("Position: ");
                            String techPosition = scanner.nextLine().trim();
                            newUser = new TechSupporter(fullName, email, password, id, techPosition);
                            break;
                        default:
                            System.out.println("Invalid user type.");
                    }
                    if (newUser != null) {
                        admin.addUser(newUser);
                        repository.save(currentData);
                        System.out.println("User added successfully.");
                    }
                    break;

                case "7":
                    System.out.print("Enter user ID to update: ");
                    int updateId = Integer.parseInt(scanner.nextLine().trim());
                    project.users.User toUpdate = admin.findUserById(updateId);
                    if (toUpdate == null) {
                        System.out.println("User not found.");
                        break;
                    }
                    System.out.print("New full name (press enter to keep): ");
                    String newName = scanner.nextLine().trim();
                    if (!newName.isEmpty()) toUpdate.setFullName(newName);
                    System.out.print("New email (press enter to keep): ");
                    String newEmail = scanner.nextLine().trim();
                    if (!newEmail.isEmpty()) toUpdate.setEmail(newEmail);
                    System.out.print("New password (press enter to keep): ");
                    String newPassword = scanner.nextLine().trim();
                    if (!newPassword.isEmpty()) toUpdate.setPassword(newPassword);
                    admin.updateUser(toUpdate);
                    repository.save(currentData);
                    break;

                case "8":
                    System.out.print("User type (Student/Teacher/Manager/TechSupporter): ");
                    String classType = scanner.nextLine().trim();
                    try {
                        Class<?> clazz = Class.forName("project.users." + classType);
                        List<project.users.User> filtered = admin.findUsersByType(clazz);
                        filtered.forEach(System.out::println);
                    } catch (ClassNotFoundException e) {
                        System.out.println("Invalid type.");
                    }
                    break;

                case "9":
                    repository.save(currentData);
                    System.out.println("Data saved successfully.");
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ==================== TECH SUPPORTER MENU ====================
    private static void techSupporterMenu(TechSupporter techSupporter) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- TECH SUPPORTER MENU: " + techSupporter.getFullName() + " ---");
            System.out.println("1. View all orders");
            System.out.println("2. View orders by status");
            System.out.println("3. Add new order");
            System.out.println("4. Accept order");
            System.out.println("5. Reject order");
            System.out.println("6. Complete order");
            System.out.println("7. Remove order");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n-- All orders --");
                    techSupporter.getAllOrders().forEach(System.out::println);
                    break;

                case "2":
                    System.out.print("Status (PENDING/ACCEPTED/REJECTED/COMPLETED): ");
                    try {
                        Status status = Status.valueOf(scanner.nextLine().trim().toUpperCase());
                        techSupporter.viewOrder(status).forEach(System.out::println);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid status.");
                    }
                    break;

                case "3":
                    System.out.print("Order ID: ");
                    int orderId = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Description: ");
                    String desc = scanner.nextLine().trim();
                    Order order = new Order(orderId, desc, Status.PENDING);
                    techSupporter.addOrder(order);
                    System.out.println("Order added.");
                    break;

                case "4":
                    System.out.print("Order ID to accept: ");
                    int acceptId = Integer.parseInt(scanner.nextLine().trim());
                    Order acceptOrder = findOrderById(techSupporter, acceptId);
                    if (acceptOrder != null) {
                        techSupporter.acceptOrder(acceptOrder);
                    }
                    break;

                case "5":
                    System.out.print("Order ID to reject: ");
                    int rejectId = Integer.parseInt(scanner.nextLine().trim());
                    Order rejectOrder = findOrderById(techSupporter, rejectId);
                    if (rejectOrder != null) {
                        techSupporter.rejectOrder(rejectOrder);
                    }
                    break;

                case "6":
                    System.out.print("Order ID to complete: ");
                    int completeId = Integer.parseInt(scanner.nextLine().trim());
                    Order completeOrder = findOrderById(techSupporter, completeId);
                    if (completeOrder != null) {
                        techSupporter.completeOrder(completeOrder);
                    }
                    break;

                case "7":
                    System.out.print("Order ID to remove: ");
                    int removeId = Integer.parseInt(scanner.nextLine().trim());
                    Order removeOrder = findOrderById(techSupporter, removeId);
                    if (removeOrder != null) {
                        techSupporter.removeOrder(removeOrder);
                        System.out.println("Order removed.");
                    }
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static Order findOrderById(TechSupporter techSupporter, int orderId) {
        for (Order o : techSupporter.getAllOrders()) {
            if (o.getOrderId() == orderId) {
                return o;
            }
        }
        System.out.println("Order not found.");
        return null;
    }

    // ==================== RESEARCH DEMO (STUDENT) ====================
    private static void researchDemoStudent(Student student) {
        System.out.println("\n--- RESEARCH DEMO ---");

        student.activateResearchProfile(4.0);

        ResearchPaper paper1 = new ResearchPaper("AI in Education",
                "IEEE Journal", LocalDate.of(2025, 5, 10), 12);
        paper1.setDoi("10.1109/AI.2025.001");
        paper1.setCitations(25);

        ResearchPaper paper2 = new ResearchPaper("Machine Learning Trends",
                "Springer", LocalDate.of(2025, 8, 20), 8);
        paper2.setDoi("10.1007/ML.2025.002");
        paper2.setCitations(10);

        student.addPaper(paper1);
        student.addPaper(paper2);
        currentData.getResearchPapers().add(paper1);
        currentData.getResearchPapers().add(paper2);

        ResearchProject project = new ResearchProject("AI Research 2026");
        try {
            student.joinProject(project);
            currentData.getResearchProjects().add(project);
            System.out.println("The student joined the project.");
        } catch (NonResearchException e) {
            System.out.println("Error: " + e.getMessage());
        }

        Student nonResearcher = new Student("Test Student", "test@uni.kz",
                "test", 999, 3.0, Faculty.IT, 1);
        System.out.println("\n-- A non-research student tries to join the project --");
        try {
            nonResearcher.joinProject(project);
        } catch (NonResearchException e) {
            System.out.println("Exception thrown: " + e.getMessage());
        }

        System.out.println("\n-- Sorted by citations --");
        student.printPapers(Comparator.comparingInt(ResearchPaper::getCitationsCount).reversed());

        System.out.println("\n-- Sorted by date --");
        student.printPapers(Comparator.comparing(ResearchPaper::getDatePublished));

        System.out.println("\n-- Sorted by page count --");
        student.printPapers(Comparator.comparingInt(ResearchPaper::getPages).reversed());

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
                    LocalDate.of(2025, 3, 12),
                    18
            );
            supervisorPaper.setDoi("10.1145/DS.2025.010");
            supervisorPaper.setCitations(40);
            supervisor.addPaper(supervisorPaper);
            currentData.getResearchPapers().add(supervisorPaper);

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
                    LocalDate.of(2025, 9, 5),
                    14
            );
            lecturerPaper.setDoi("10.1016/STE.2025.021");
            lecturerPaper.setCitations(60);
            lecturerResearcher.addPaper(lecturerPaper);
            currentData.getResearchPapers().add(lecturerPaper);

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
                    LocalDate.of(2025, 11, 15),
                    22
            );
            globalPaper.setDoi("10.1016/GAI.2025.099");
            globalPaper.setCitations(95);
            anotherSchoolProfessor.addPaper(globalPaper);

            List<List<Researcher>> allSchools = Arrays.asList(
                    schoolResearchers,
                    Arrays.asList(anotherSchoolProfessor)
            );

            ResearchAnalytics.findTopCitedResearcherOfYear(allSchools, 2025).ifPresent(topResearcher ->
                    System.out.println(
                            "Top cited researcher of 2025 among all schools: " +
                                    ResearchAnalytics.researcherName(topResearcher) +
                                    " (" + ResearchAnalytics.citationsForYear(topResearcher, 2025) + " citations)"
                    )
            );
        } catch (InvalidSupervisorException e) {
            System.out.println("Supervisor error: " + e.getMessage());
        }

        System.out.println("\n-- Observer: ResearchPaperPublisher --");
        ResearchPaperPublisher publisher = new ResearchPaperPublisher();
        publisher.subscribe(student);
        publisher.publishPaper(paper1);
    }

    // ==================== RESEARCH DEMO (TEACHER) ====================
    private static void researchDemoTeacher(Teacher teacher) {
        System.out.println("\n--- TEACHER RESEARCH DEMO ---");

        System.out.println("isProfessor: " + teacher.isProfessor());
        System.out.println("isResearcher: " + teacher.isResearcher());
        if (!teacher.isResearcher()) {
            teacher.activateResearchProfile(5.5);
            System.out.println("Research profile activated for " + teacher.getFullName());
        } else {
            teacher.setHIndex(5.5);
        }

        ResearchPaper paper = new ResearchPaper("Deep Learning Survey",
                "Nature", LocalDate.of(2025, 1, 15), 20);
        paper.setCitations(100);
        paper.setDoi("10.1038/DL.2025.001");
        currentData.getResearchPapers().add(paper);

        News news = new News("New Paper!", "Professor published a paper.", "Research");
        teacher.addPaper(paper);
        teacher.publishPaper(paper, news);
        currentData.getNews().add(news);

        ResearchProject project = new ResearchProject("Deep Learning Lab");
        try {
            teacher.joinProject(project);
            currentData.getResearchProjects().add(project);
        } catch (NonResearchException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n-- Papers sorted by citations --");
        teacher.printPapers(Comparator.comparingInt(ResearchPaper::getCitationsCount).reversed());
    }
}