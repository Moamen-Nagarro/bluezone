package io.github.jmgarridopaz.bluezone.adapter.forstoringtickets.fake;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import io.github.jmgarridopaz.bluezone.hexagon.ports.driven.forstoringtickets.Ticket;
import io.github.jmgarridopaz.bluezone.hexagon.ports.driven.forstoringtickets.ForStoringTickets;
import io.github.jmgarridopaz.lib.portsadapters.Adapter;


/**
 * Driven adapter that implements "forstoringtickets" port with file-based JSON storage.
 */
@Adapter(name="file-json")
public class FileTicketStoreAdapter implements ForStoringTickets {

    private static final int MAX_TICKET_CODE_LENGTH = 10;
    private static final String FILE_PATH = "tickets.json";
    private Map<String, Ticket> ticketsByCode;
    private AtomicLong value;
    private ObjectMapper objectMapper;

    public FileTicketStoreAdapter() {
        this.ticketsByCode = loadTicketsFromFile();
        this.objectMapper = new ObjectMapper();
        setNextCode("1");
    }

    private Map<String, Ticket> loadTicketsFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<Map<String, Ticket>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private void saveTicketsToFile() {
        try {
            objectMapper.writeValue(new File(FILE_PATH), ticketsByCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String nextCode() {
        String currentTicketCodeAndIncrement = Long.toString(value.getAndIncrement());
        return leftPaddedTicketCode(currentTicketCodeAndIncrement);
    }

    @Override
    public void setNextCode(String ticketCode) {
        if (ticketCode.length() > MAX_TICKET_CODE_LENGTH) {
            throw new RuntimeException("Ticket code overflow");
        }
        long codeAsLong = Long.valueOf(ticketCode);
        this.value = new AtomicLong(codeAsLong);
    }

    @Override
    public String nextAvailableCode() {
        String currentTicketCode = Long.toString(value.get());
        return leftPaddedTicketCode(currentTicketCode);
    }

    private String leftPaddedTicketCode(String ticketCode) {
        int numberOfZeroesToPad = MAX_TICKET_CODE_LENGTH - ticketCode.length();
        if (numberOfZeroesToPad < 0) {
            throw new RuntimeException("Ticket code overflow");
        }
        for (int counter = 0; counter < numberOfZeroesToPad; counter++) {
            ticketCode = "0" + ticketCode;
        }
        return ticketCode;
    }

    @Override
    public Ticket findByCode(String ticketCode) {
        if (!exists(ticketCode)) {
            return null;
        }
        return this.ticketsByCode.get(ticketCode);
    }

    @Override
    public void store(Ticket ticket) {
        if (exists(ticket.getCode())) {
            throw new RuntimeException("Cannot store ticket. Code '" + ticket.getCode() + "' already exists.");
        }
        this.ticketsByCode.put(ticket.getCode(), ticket);
        saveTicketsToFile();
    }

    @Override
    public List<Ticket> findByCarRateOrderByEndingDateTimeDesc(String carPlate, String rateName) {
        List<Ticket> ticketsOfCarAndRate = new ArrayList<>();
        for (Ticket ticket : this.ticketsByCode.values()) {
            if (ticket.getCarPlate().equals(carPlate) && ticket.getRateName().equals(rateName)) {
                ticketsOfCarAndRate.add(ticket);
            }
        }
        ticketsOfCarAndRate.sort(Comparator.comparing(Ticket::getEndingDateTime).reversed());
        return ticketsOfCarAndRate;
    }

    @Override
    public void delete(String ticketCode) {
        if (!exists(ticketCode)) {
            throw new RuntimeException("Cannot delete ticket. Code '" + ticketCode + "' does not exist.");
        }
        this.ticketsByCode.remove(ticketCode);
        saveTicketsToFile();
    }

    @Override
    public boolean exists(String ticketCode) {
        return this.ticketsByCode.containsKey(ticketCode);
    }
}
