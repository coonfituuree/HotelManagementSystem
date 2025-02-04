package models;

public class Customer {
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String phone_number;
    private String created_at;

    public Customer() {
    }

    public Customer(String first_name, String last_name, String email, String password, String phone_number, String created_at) {
        setFirst_name(first_name);
        setLast_name(last_name);
        setEmail(email);
        setPassword(password);
        setPhone_number(phone_number);
        setCreated_at(created_at);
    }

    public Customer(int id, String first_name, String last_name, String email, String password, String phone_number, String created_at) {
        this(first_name, last_name, email, password, phone_number, created_at);
        this.id = id;
    }

    //Getter and Setter for value of DB customers

    public int getId() {
        return id;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
