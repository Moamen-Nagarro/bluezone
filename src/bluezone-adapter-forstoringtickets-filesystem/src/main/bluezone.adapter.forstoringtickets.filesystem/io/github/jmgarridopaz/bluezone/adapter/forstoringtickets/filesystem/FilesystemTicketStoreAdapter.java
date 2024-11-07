package io.github.jmgarridopaz.bluezone.adapter.forstoringtickets.filesystem;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.jmgarridopaz.bluezone.hexagon.ports.driven.forstoringtickets.Ticket;
import io.github.jmgarridopaz.bluezone.hexagon.ports.driven.forstoringtickets.ForStoringTickets;
import io.github.jmgarridopaz.lib.portsadapters.Adapter;


/**
 * Driven adapter that implements "forstoringtickets" port with file-based JSON storage.
 */
@Adapter(name="file-json")
public class FilesystemTicketStoreAdapter implements ForStoringTickets {
    private static final String TICKETS_FILE_PATH = "./tickets.json";
    private Map<String, Ticket> tickets = new HashMap<>();
    private static final int MAX_TICKET_CODE_LENGTH = 10;
    private static final String FILE_PATH = "tickets.json";

    private Map<String, Ticket> ticketsByCode;
    private AtomicLong value;
    private final ObjectMapper objectMapper;

    public FilesystemTicketStoreAdapter() {
        this.ticketsByCode = new HashMap<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        setNextCode("1");
        initializeFile();
        loadTicketsFromFile();
    }

    private void initializeFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                // Create the file and initialize with empty JSON object
                Files.write(Paths.get(FILE_PATH), "{}".getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Could not initialize tickets file", e);
            }
        }
    }

    private void loadTicketsFromFile() {
        File file = new File(TICKETS_FILE_PATH);
        if (file.exists()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                tickets = mapper.readValue(file, Map.class);
            } catch (JsonMappingException e) {
                System.err.println("Corrupted JSON file. Re-creating empty JSON file.");
                recreateEmptyFile();
            } catch(JsonProcessingException e){
                    System.err.println("Corrupted JSON file. Re-creating empty JSON file.");
                    recreateEmptyFile();
            } catch (IOException e) {
                System.err.println("Could not read JSON file: " + e.getMessage());
            }
        } else {
            recreateEmptyFile();
        }
    }

    private void recreateEmptyFile() {
        try {
            Files.createDirectories(Paths.get(TICKETS_FILE_PATH).getParent());
            Files.write(Paths.get(TICKETS_FILE_PATH), "{}".getBytes());
            tickets = new HashMap<>();
        } catch (IOException e) {
            System.err.println("Failed to create new JSON file: " + e.getMessage());
        }
    }

    private void saveTicketsToFile() {
        try {
            String jsonString = objectMapper.writeValueAsString(ticketsByCode);
            Files.write(Paths.get(FILE_PATH), jsonString.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Could not save tickets to file", e);
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
        long codeAsLong = Long.parseLong(ticketCode);
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
        return ticketsByCode.get(ticketCode);
    }

    @Override
    public void store(Ticket ticket) {
        if (exists(ticket.getCode())) {
            throw new RuntimeException("Cannot store ticket. Code '" + ticket.getCode() + "' already exists.");
        }
        ticketsByCode.put(ticket.getCode(), ticket);
        saveTicketsToFile();
    }

    @Override
    public List<Ticket> findByCarRateOrderByEndingDateTimeDesc(String carPlate, String rateName) {
        List<Ticket> ticketsOfCarAndRate = new ArrayList<>();
        for (Ticket ticket : ticketsByCode.values()) {
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
        ticketsByCode.remove(ticketCode);
        saveTicketsToFile();
    }

    @Override
    public boolean exists(String ticketCode) {
        return ticketsByCode.containsKey(ticketCode);
    }
}
