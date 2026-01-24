package com.hotel.util;

import com.hotel.model.Guest;
import com.hotel.model.Reservation;
import com.hotel.model.Room;
import com.hotel.services.*;
import com.hotel.repositories.PostgresRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final ReservationService resService;
    private final PaymentService paymentService;
    private final RoomAvailabilityService availabilityService;
    private final PostgresRepository repo;
    private final Scanner scanner;

    public ConsoleUI(ReservationService resService,
                     PaymentService paymentService,
                     RoomAvailabilityService availabilityService,
                     PostgresRepository repo){
        this.resService=resService;
        this.paymentService=paymentService;
        this.availabilityService=availabilityService;
        this.repo=repo;
        this.scanner= new Scanner(System.in);
    }

    public void start(){
        System.out.println("Hotel System Started");

        while(true){
            printMenu();
            int choice=readInt();

            if (choice==0){
                System.out.println("Quitting System");
                break;
            }
            try {
                handleChoice(choice);
            } catch (Exception e){
                System.out.println("Error:"+e.getMessage());
            }
        }
    }

    public void printMenu(){
        System.out.println("\n ======MENU======");
        System.out.println("1.Search available rooms(by dates)");
        System.out.println("2.Make reservation");
        System.out.println("3.Pay for reservation");
        System.out.println("4.Check status of reservation");
        System.out.println("5.Cancel reservation");
        System.out.println("0.Quit system");
        System.out.println("You choice: ");
    }

    public int readInt(){
        try{
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e){
            return -1;
        }
    }

    private void handleChoice(int choice){
        switch (choice){
            case 1:
                System.out.println("Date of check in: ");
                LocalDate checkIn=LocalDate.parse(scanner.nextLine());
                System.out.println("Date of check out: ");
                LocalDate checkOut=LocalDate.parse(scanner.nextLine());

                List<Room> rooms= availabilityService.getAvailableRooms(checkIn,checkOut);
                if (rooms.isEmpty()){
                    System.out.println("No available rooms");
                } else{
                    System.out.println("Available rooms: ");
                    for (Room r: rooms){
                        System.out.println("ID:"+r.getId()+" Number: "+r.getRoomNumber()+" Type of room: "+r.getType()+" Price: "+r.getPricePerNight());
                    }
                } break;
            case 2:
                System.out.println("\n====Reservation of room====");
                System.out.println("Your choice of room: ");
                int roomId=readInt();

                if (!availabilityService.isRoomAvailable(roomId)){
                    System.out.println("Room is taken");
                    break;
                }
                System.out.println("Write your information:");
                System.out.println("Name: ");
                String fName=scanner.nextLine();
                System.out.println("Last name: ");
                String lName=scanner.nextLine();
                System.out.println("Email: ");
                String email=scanner.nextLine();
                System.out.println("Phone number:");
                String phone=scanner.nextLine();

                Guest newGuest=new Guest(0, fName, lName, email,phone);
                Guest savedGuest=repo.saveGuest(newGuest);

                if (savedGuest==null){
                    System.out.println("Error");
                    break;
                }
                System.out.println("Successfully registered guest. Guest ID: "+ savedGuest.getId());

                System.out.println("How long do plan to stay?");
                int days=readInt();
                LocalDate check_In=LocalDate.now();
                LocalDate check_Out=check_In.plusDays(days);

                int bookingId=resService.createReservation(savedGuest.getId(),roomId,check_In,check_Out);

                System.out.println("Reservation made successfully");
                System.out.println("Reservation number: "+bookingId+" for guest: "+ savedGuest.getFirstName());
                break;
            case 3:
                System.out.println("ID of your reservation: ");
                int payId=readInt();
                if (paymentService.payReservation(payId)){
                    System.out.println("Successfully paid");
                } else {
                    System.out.println("Already paid");
                } break;
            case 4:
                System.out.println("Id of reservation: ");
                int resId=readInt();
                Reservation res=repo.getReservationById(resId);
                if (res!=null){
                    System.out.println("===Status of reservation===");
                    System.out.println("Guest: "+res.getGuest().getFirstName());
                    System.out.println("Status: "+(res.isPaid()?"PAID":"NOT PAID"));
                } else {
                    System.out.println("Not found");
                }
                break;
            case 5:
                System.out.println("Write Id of your reservation: ");
                int idCancel=readInt();
                resService.cancelReservation(idCancel);
                System.out.println("Reservation cancelled");
                break;
        }
    }
}

