package com.hotel.model;

public class Guest {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public Guest(){}

    public Guest(String firstName, String lastName,String email,String phone){
        firstName=this.firstName;
        lastName=this.lastName;
        email=this.email;
        phone=this.phone;
    }
    public void setId(int id){this.id=id;}

    public Guest(int id,String firstName, String lastName,String email,String phone){
        id=this.id;
        firstName=this.firstName;
        lastName=this.lastName;
        email=this.email;
        phone=this.phone;
    }

    public int getId(){return id;}
    public String getFirstName(){return firstName;}
    public String getLastName(){return lastName;}
    public String getEmail(){return email;}
    public String getPhone(){return phone;}
}
