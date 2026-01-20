package com.hotel.model;

public class Guest {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public Guest() {}

    public Guest(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public Guest(int id, String firstName, String lastName, String email, String phone) {
        this(firstName, lastName, email, phone);
        this.id = id;
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return id; }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}