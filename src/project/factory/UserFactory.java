package project.factory;

import project.enums.Faculty;
import project.enums.ManagerType;
import project.enums.TeacherStatus;
import project.users.Manager;
import project.users.Student;
import project.users.Teacher;
import project.users.User;

public class UserFactory {

    public static User getUser(String type, String fullName, String email, String password, int id,
                               String position, double gpa, Faculty faculty, boolean isProfessor,
                               TeacherStatus status, int yearsOfExperience, ManagerType managerType) {
        switch (type) {
            case "STUDENT":
                return new Student(fullName, email, password, id, gpa, faculty);

            case "TEACHER":
                return new Teacher(fullName, email, password, id, position, isProfessor, status, yearsOfExperience);

            case "MANAGER":
                return new Manager(fullName, email, password, id, position, managerType);

            default:
                throw new IllegalArgumentException("Invalid user type: " + type + "Type in upper case");
        }
    }
}