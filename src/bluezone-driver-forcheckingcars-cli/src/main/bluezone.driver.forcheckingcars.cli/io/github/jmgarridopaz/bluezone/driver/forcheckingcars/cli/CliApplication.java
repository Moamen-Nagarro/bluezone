package io.github.jmgarridopaz.bluezone.driver.forcheckingcars.cli;

import io.github.jmgarridopaz.bluezone.hexagon.ports.driving.forcheckingcars.ForCheckingCars;
import io.github.jmgarridopaz.bluezone.hexagon.ports.driving.forconfiguringapp.ForConfiguringApp;
import io.github.jmgarridopaz.bluezone.hexagon.ports.driven.forstoringtickets.Ticket;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliApplication {
    private final ForCheckingCars carChecker;
    private final ForConfiguringApp appConfigurator;

    public CliApplication(ForCheckingCars carChecker, ForConfiguringApp appConfigurator) {
        this.carChecker = carChecker;
        this.appConfigurator = appConfigurator;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        List<Ticket> tickets = new ArrayList<>();
        boolean running = true;

        while (running) {
            System.out.println("Welcome to the Parking Check CLI!");
            System.out.println("1. Add Ticket");
            System.out.println("2. Check Illegally Parked Car");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    tickets.add(addTicket(scanner));
                    break;
                case 2:
                    checkIllegallyParkedCar(scanner, tickets);
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private Ticket addTicket(Scanner scanner) {
        System.out.print("Enter ticket code: ");
        String code = scanner.nextLine();
        System.out.print("Enter car plate: ");
        String carPlate = scanner.nextLine();
        System.out.print("Enter rate name: ");
        String rateName = scanner.nextLine();
        System.out.print("Enter starting date time (yyyy-MM-dd HH:mm): ");
        LocalDateTime startingDateTime = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.print("Enter ending date time (yyyy-MM-dd HH:mm): ");
        LocalDateTime endingDateTime = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.print("Enter price: ");
        BigDecimal price = new BigDecimal(scanner.nextLine());

        Ticket ticket = new Ticket(code, carPlate, rateName, startingDateTime, endingDateTime, price);
        appConfigurator.createTicket(ticket);
        System.out.println("Ticket added successfully.");
        return ticket;
    }

    private void checkIllegallyParkedCar(Scanner scanner, List<Ticket> tickets) {
        System.out.print("Enter current date time (yyyy-MM-dd HH:mm): ");
        LocalDateTime currentDateTime = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.print("Enter car plate: ");
        String carPlate = scanner.nextLine();
        System.out.print("Enter rate name: ");
        String rateName = scanner.nextLine();

        for (Ticket ticket : tickets) {
            if (ticket.getCarPlate().equals(carPlate) && ticket.getRateName().equals(rateName)) {
                LocalDateTime endingDateTime = ticket.getEndingDateTime();
                if (endingDateTime.isBefore(currentDateTime)) {
                    System.out.println("Illegally parked car: true");
                    return;
                }
            }
        }
        System.out.println("Illegally parked car: false");
    }

    public static void main(String[] args) {
        // Initialize your ForCheckingCars and ForConfiguringApp instances here
        ForCheckingCars carChecker = null; // Replace with actual implementation
        ForConfiguringApp appConfigurator = null; // Replace with actual implementation

        ForCheckingCarsCliDriver cliDriver = new ForCheckingCarsCliDriver(carChecker, appConfigurator);
        cliDriver.run();
    }
}
