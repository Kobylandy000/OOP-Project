package project.users;

import java.io.Serializable;
import java.util.Objects;

public abstract class User implements Serializable {
    private String fullName;
    private String email;
    private String password;
    private int id;

    private static final long serialVersionUID = 1L;

    public User() {}

    public User(String fullName, String email, String password, int id) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    @Override
    public String toString() {
        return fullName + " (" + email + ")";
    }
}