package project;

import project.enums.*;
import project.exceptions.InvalidSupervisorException;
import project.models.*;
import project.users.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the University Management System!");

        System.out.println("Please choose your preferred language:");
        for (Language lang : Language.values()) {
            System.out.println(lang.ordinal() + 1 + ". " + lang);
        }
        int languageChoice = scanner.nextInt();
        Language chosenLanguage = Language.values()[languageChoice - 1];
        System.out.println("You have chosen: " + chosenLanguage);

        Admin admin = Admin.getInstance();
        Teacher teacher = new Teacher("John Doe", "john.doe@university.com", "password", 2, "Professor", true, TeacherStatus.PROFESSOR, 10);
        Student student = new Student("Jane Smith", "jane.smith@university.com", "password", 3, 3.8, Faculty.SITE);
        Manager manager = new Manager("Mark Manager", "mark.manager@university.com", "password", 4, "Dean", ManagerType.DEANMANAGER);
        Course course = new Course("Computer Science", LessonType.LECTURE, chosenLanguage, CourseType.MAJOR, Faculty.SITE);

        admin.addUser(teacher);
        admin.addUser(student);
        admin.addUser(manager);
        System.out.println("Admin has added users: " + admin.getUsers());

        System.out.println("Admin has removed a user. Current users: " + admin.getUsers());

        manager.approveStudent(student);
        manager.addCourse(course, new Major("Software Engineering", List.of()), 2024);
        manager.assignCourseToTeacher(course, teacher);
        Report report = manager.generateReport();
        System.out.println(report);

        student.enrollCourse(course);
        teacher.students.add(student);

        teacher.putMarks(student, course, 95);
        student.rateTeacher(teacher, 5);
        teacher.setHIndex(3.0);

        student.enrollCourse(course);
        try {
            student.assignSupervisor(teacher);
            System.out.println("Student's supervisor: " + student.getSupervisor());
        } catch (InvalidSupervisorException e) {
            System.out.println("Error assigning supervisor: " + e.getMessage());
            System.out.println("Student's supervisor: " + student.getSupervisor());
        }

        News news = new News("Research Findings", "New AI techniques developed.", "Research");
        news.addComment("Great work!");
        System.out.println(news);

        TechSupporter techSupporter = new TechSupporter();
        Order order1 = new Order(1, "Fix projector", Status.PENDING);
        techSupporter.addOrder(order1);
        techSupporter.acceptOrder(order1);
        List<Order> acceptedOrders = techSupporter.viewOrder(Status.ACCEPTED);
        System.out.println("Accepted Orders: " + acceptedOrders);

        Employee sender = new Manager("Alice Manager", "alice.manager@university.com", "password", 5, "HR", ManagerType.OFFICEREGISTRATION);
        Employee recipient = new Teacher("Bob Teacher", "bob.teacher@university.com", "password", 6, "Assistant Professor", true, TeacherStatus.PRACTICIONER, 5);

        sender.sendMessage(recipient, "Hello Bob, please review the new guidelines.");
        sender.sendMessage(recipient, "Let me know if you need help.");
        recipient.sendMessage(sender, "Thank you, Alice. I'll check them out.");

        System.out.println("\nViewing Sent Messages for Alice:");
        sender.viewSentMessages().forEach(System.out::println);

        System.out.println("\nViewing Received Messages for Bob:");


        UniversityJournal journal = new UniversityJournal("Journal of Advanced Studies", "Global Academic Press");

        // Print initial state of the journal
        System.out.println("Journal Name: " + journal.getName());
        System.out.println("Publisher: " + journal.getPublisher());
        System.out.println("Initial Papers: " + journal.getPapers().size());

        // Create a ResearchPaper instance
        ResearchPaper paper1 = new ResearchPaper(
                "Advances in Quantum Computing",
                "Journal of Advanced Studies",
                LocalDate.of(2024, 12, 20),
                15
        );
        paper1.setDoi("10.1234/jas.2024.001");


        journal.addPaper(paper1);
        System.out.println("Added paper: " + paper1.getTitle());


        System.out.println("Papers in Journal: " + journal.getPapers().size());
        for (ResearchPaper paper : journal.getPapers()) {
            System.out.println(" - " + paper.getTitle());
        }


        journal.removePaper(paper1);
        System.out.println("Removed paper: " + paper1.getTitle());
        System.out.println("Papers in Journal after removal: " + journal.getPapers().size());



        scanner.close();
    }
}