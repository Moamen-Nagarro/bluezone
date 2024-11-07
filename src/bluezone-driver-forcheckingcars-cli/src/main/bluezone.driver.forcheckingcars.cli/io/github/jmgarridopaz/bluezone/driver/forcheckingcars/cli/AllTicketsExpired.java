package io.github.jmgarridopaz.bluezone.driver.forcheckingcars.cli;

import io.github.jmgarridopaz.bluezone.hexagon.ports.driven.forstoringtickets.Ticket;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AllTicketsExpired {

    public static boolean checkIfIllegallyParked(List<Ticket> tickets, LocalDateTime currentDateTime, String carPlate, String rateName) {
        for (Ticket ticket : tickets) {
            if (ticket.getCarPlate().equals(carPlate) && ticket.getRateName().equals(rateName)) {
                if (ticket.getEndingDateTime().isAfter(currentDateTime)) {
                    return false; // Not illegally parked
                }
            }
        }
        return true; // Illegally parked
    }
}
