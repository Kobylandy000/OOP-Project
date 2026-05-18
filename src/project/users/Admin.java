package project.users;

import java.util.ArrayList;
import java.util.List;

public class Admin extends Employee {
    private static final long serialVersionUID = 6L;

    private static volatile Admin instance;

    private List<String> logFiles; // әр юзердің қосылып, өшірілгені, өзгергені туралы ақпарат
    private List<User> users;

    private Admin() {
        super("System Admin", "admin@university.com", "admin123", 1, "Administrator");
        this.logFiles = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public static Admin getInstance() {
        if (instance == null) { // Объект БАР МА? (Жылдам тексеру)
            synchronized (Admin.class) { // ҚҰЛЫПТАУ (бір уақытта бір адам кіреді)
                if (instance == null) { // 	Қайта тексеру (БАСҚА адам жасап қоймады ма?)
                    instance = new Admin(); // ЖАЛҒЫЗ объектті құру
                }
            }
        }
        return instance; // Объектті қайтару
    }
    public static void setInstance(Admin loadedAdmin) {
        if (loadedAdmin != null) {
            instance = loadedAdmin;
        }
    }

    private Object readResolve() {
        instance = this;
        return this;
    }

    public void addUser(User user) {
        if (user == null) return;
        if (!users.contains(user)) {
            users.add(user);
            log("User added: " + user.getFullName() + " [" + user.getClass().getSimpleName() + "]");
        } else {
            log("User already exists: " + user.getFullName());
        }
    }

    public void removeUser(int userId) {
        User target = findUserById(userId);
        if (target != null) {
            users.remove(target);
            log("User removed: " + target.getFullName());
        } else {
            log("Failed to remove — user not found. ID: " + userId);
        }
    }

    public void updateUser(User updatedUser) {
        if (updatedUser == null) return;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                log("User updated: " + updatedUser.getFullName());
                return;
            }
        }
        log("Failed to update — user not found. ID: " + updatedUser.getId());
    }


    // іздейтін методтар
    public User findUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId) return user;
        }
        log("User not found. ID: " + userId);
        return null;
    }

    public User findUserByName(String fullName) {
        for (User user : users) {
            if (user.getFullName().equalsIgnoreCase(fullName)) return user;
        }
        log("User not found: " + fullName);
        return null;
    }

    public List<User> findUsersByType(Class<?> type) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (type.isInstance(user)) result.add(user);
        }
        return result;
    }

    private void log(String message) {
        String entry = "[" + java.time.LocalDateTime.now() + "] " + message;
        logFiles.add(entry);
        System.out.println(entry);
    }

    public List<String> viewLogs() {
        return new ArrayList<>(logFiles);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public String toString() {
        return "Admin{" + getFullName() + ", users=" + users.size() + "}";
    }
}