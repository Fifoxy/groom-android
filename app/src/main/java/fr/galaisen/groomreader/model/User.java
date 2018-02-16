package fr.galaisen.groomreader.model;

public class User {
    private Integer id;
    private String email;
    private String first_name;
    private String last_name;
    private boolean is_staff;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public boolean isAdmin() {
        return is_staff;
    }

    public void setAdmin(boolean admin) {
        this.is_staff= admin;
    }
}
