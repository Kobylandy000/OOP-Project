package project.users;

import java.util.ArrayList;
import java.util.List;

public class Admin extends Employee {
    private List<String> logFiles;
    public List<User> users;
    private static Admin instance;

    private Admin(String fullName, String email, String password, int id, String position) {
        super(fullName, email, password, id, position);
        this.logFiles = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin("System Admin", "admin@university.com", "admin123", 1, "Administrator");
        }
        return instance;
    }

    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            logFiles.add("User added: " + user.getFullName());
        }
    }

    public void removeUser(int userId) {
        User userToRemove = null;
        for (User user : users) {
            if (user.getId() == userId) {
                userToRemove = user;
                break;
            }
        }
        if (userToRemove != null) {
            users.remove(userToRemove);
            logFiles.add("User removed: " + userToRemove.getFullName());
        } else {
            logFiles.add("Failed to remove user with ID: " + userId + " (User not found)");
        }
    }

    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                logFiles.add("User updated: " + updatedUser.getFullName());
                return;
            }
        }
        logFiles.add("Failed to update user with ID: " + updatedUser.getId() + " (User not found)");
    }

    public List<String> viewLogs() {
        return new ArrayList<>(logFiles);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }
}