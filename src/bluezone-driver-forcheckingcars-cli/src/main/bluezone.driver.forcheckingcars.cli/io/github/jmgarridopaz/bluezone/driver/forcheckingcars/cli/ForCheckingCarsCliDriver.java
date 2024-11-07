package io.github.jmgarridopaz.bluezone.driver.forcheckingcars.cli;

import io.github.jmgarridopaz.bluezone.hexagon.ports.driving.forcheckingcars.ForCheckingCars;
import io.github.jmgarridopaz.bluezone.hexagon.ports.driving.forconfiguringapp.ForConfiguringApp;
import io.github.jmgarridopaz.lib.portsadapters.Driver;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ForCheckingCarsCliDriver implements Driver {
    private final ForCheckingCars carChecker;
    private final ForConfiguringApp appConfigurator;

    public ForCheckingCarsCliDriver(ForCheckingCars carChecker, ForConfiguringApp appConfigurator) {
        this.carChecker = carChecker;
        this.appConfigurator = appConfigurator;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Car Checking CLI Application!");

        while (true) {
            System.out.print("Enter car plate or 'exit' to quit: ");
            String carPlate = scanner.nextLine();
            if ("exit".equalsIgnoreCase(carPlate)) {
                break;
            }

            System.out.print("Enter rate name: ");
            String rateName = scanner.nextLine();

            System.out.print("Enter current date and time (yyyy-MM-dd HH:mm): ");
            String dateTimeInput = scanner.nextLine();
            LocalDateTime currentDateTime = LocalDateTime.parse(dateTimeInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            Clock clock = Clock.fixed(currentDateTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
            boolean illegallyParked = carChecker.illegallyParkedCar(clock, carPlate, rateName);

            System.out.println("Is the car illegally parked? " + illegallyParked);
        }

        scanner.close();
    }

    @Override
    public void run(String... strings) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Car Checking CLI Application!");

        while (true) {
            System.out.print("Enter car plate or 'exit' to quit: ");
            String carPlate = scanner.nextLine().trim();
            if ("exit".equalsIgnoreCase(carPlate)) {
                break; // Exit the loop if the user wants to quit
            }

            System.out.print("Enter rate name (e.g., 'GREEN_ZONE', 'BLUE_ZONE'): ");
            String rateName = scanner.nextLine().trim();

            System.out.print("Enter current date and time (yyyy-MM-dd HH:mm): ");
            String dateTimeInput = scanner.nextLine().trim();

            // Validate the date format
            LocalDateTime currentDateTime;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                currentDateTime = LocalDateTime.parse(dateTimeInput, formatter);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use 'yyyy-MM-dd HH:mm'.");
                continue; // Go to the next iteration to ask for input again
            }

            // Create a clock fixed to the provided date and time
            Clock clock = Clock.fixed(currentDateTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

            // Check if the car is illegally parked
            boolean illegallyParked = carChecker.illegallyParkedCar(clock, carPlate, rateName);

            // Output the result to the user
            System.out.println("Is the car '" + carPlate + "' illegally parked in the '" + rateName + "' area? " + (illegallyParked ? "Yes" : "No"));
        }

        scanner.close();
        System.out.println("Thank you for using the Car Checking CLI Application. Goodbye!");

    }
}
