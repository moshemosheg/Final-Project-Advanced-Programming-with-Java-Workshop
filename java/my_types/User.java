package my_types;

public class User {

    private String email;
    private String firstName;
    private String lastName;
    private boolean admin;

    public User() {

    }

    public User(String email, String firstName, String lastName, boolean admin) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}
