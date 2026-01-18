package com.hotel.model;

public class Room {
    private int id;
    private String roomNumber;
    private String type;
    private double pricePerNight;
    private boolean isAvailable;

    public Room (int id,String roomNumber,String type,double pricePerNight,boolean isAvailable){
        this.id=id;
        this.roomNumber=roomNumber;
        this.type=type;
        this.pricePerNight=pricePerNight;
        this.isAvailable=isAvailable;
    }

    public int getId() {return id;}
    public String getRoomNumber(){return roomNumber;}
    public String getType(){return type;}
    public double getPricePerNight(){return pricePerNight;}
    public void setAvailable(boolean available){isAvailable=available;}
    public boolean isAvailable(){return isAvailable;}
}
