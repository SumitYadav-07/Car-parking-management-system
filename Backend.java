import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Backend {

    static Map<String, String> users = new ConcurrentHashMap<>();
    static Map<String, String> roles = new ConcurrentHashMap<>();
    static Map<String, Boolean> slots = new ConcurrentHashMap<>();
    static Map<String, Booking> bookings = new ConcurrentHashMap<>();

    static class Booking {
        String id;
        String username;
        String slot;
        int hours;
        int amount;
        String status;

        Booking(String id, String username, String slot, int hours, int amount, String status) {
            this.id = id;
            this.username = username;
            this.slot = slot;
            this.hours = hours;
            this.amount = amount;
            this.status = status;
        }
    }

    public static void main(String[] args) throws Exception {

        users.put("admin", hash("admin123"));
        roles.put("admin", "admin");

        for (int i = 1; i <= 20; i++) {
            slots.put(String.format("S%02d", i), false);
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", Backend::home);
        server.createContext("/register", Backend::register);
        server.createContext("/login", Backend::login);
        server.createContext("/slots", Backend::getSlots);
        server.createContext("/book", Backend::book);
        server.createContext("/payment", Backend::payment);

        server.setExecutor(null);
        server.start();

        System.out.println("Server started at http://localhost:8080");
    }

    static void home(HttpExchange e) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get("Frontend.html"));
        e.getResponseHeaders().set("Content-Type", "text/html");
        e.sendResponseHeaders(200, data.length);
        e.getResponseBody().write(data);
        e.close();
    }

    static void register(HttpExchange e) throws IOException {
        if (!e.getRequestMethod().equalsIgnoreCase("POST")) {
            send(e, 405, "{\"message\":\"Method not allowed\"}");
            return;
        }

        Map<String, String> data = parse(readBody(e));

        String username = data.get("username");
        String password = data.get("password");

        if (username == null || password == null ||
                username.isEmpty() || password.isEmpty()) {
            send(e, 400, "{\"message\":\"Enter username and password\"}");
            return;
        }

        if (users.containsKey(username)) {
            send(e, 400, "{\"message\":\"Username already exists\"}");
            return;
        }

        users.put(username, hash(password));
        roles.put(username, "user");

        send(e, 200, "{\"message\":\"Registration successful\"}");
    }

    static void login(HttpExchange e) throws IOException {
        if (!e.getRequestMethod().equalsIgnoreCase("POST")) {
            send(e, 405, "{\"message\":\"Method not allowed\"}");
            return;
        }

        Map<String, String> data = parse(readBody(e));

        String username = data.get("username");
        String password = data.get("password");

        if (username == null || password == null ||
                !users.containsKey(username)) {
            send(e, 401, "{\"message\":\"Invalid username or password\"}");
            return;
        }

        if (!users.get(username).equals(hash(password))) {
            send(e, 401, "{\"message\":\"Invalid username or password\"}");
            return;
        }

        String role = roles.get(username);

        send(e, 200,
                "{\"message\":\"Login successful\",\"username\":\"" +
                        username + "\",\"role\":\"" + role + "\"}");
    }

    static void getSlots(HttpExchange e) throws IOException {
        if (!e.getRequestMethod().equalsIgnoreCase("GET")) {
            send(e, 405, "{\"message\":\"Method not allowed\"}");
            return;
        }

        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        for (Map.Entry<String, Boolean> entry : slots.entrySet()) {
            if (!first) {
                json.append(",");
            }

            json.append("{");
            json.append("\"slot\":\"").append(entry.getKey()).append("\",");
            json.append("\"occupied\":").append(entry.getValue());
            json.append("}");

            first = false;
        }

        json.append("]");

        send(e, 200, json.toString());
    }

    static void book(HttpExchange e) throws IOException {
        if (!e.getRequestMethod().equalsIgnoreCase("POST")) {
            send(e, 405, "{\"message\":\"Method not allowed\"}");
            return;
        }

        Map<String, String> data = parse(readBody(e));

        String username = data.get("username");
        String slot = data.get("slot");
        String hoursText = data.get("hours");

        if (username == null || slot == null || hoursText == null) {
            send(e, 400, "{\"message\":\"Missing booking details\"}");
            return;
        }

        if (!users.containsKey(username)) {
            send(e, 401, "{\"message\":\"User not found\"}");
            return;
        }

        int hours;

        try {
            hours = Integer.parseInt(hoursText);
        } catch (Exception ex) {
            send(e, 400, "{\"message\":\"Invalid hours\"}");
            return;
        }

        if (hours <= 0) {
            send(e, 400, "{\"message\":\"Hours must be greater than zero\"}");
            return;
        }

        if (!slots.containsKey(slot)) {
            send(e, 400, "{\"message\":\"Slot does not exist\"}");
            return;
        }

        synchronized (slots) {
            if (slots.get(slot)) {
                send(e, 400, "{\"message\":\"Slot is already occupied\"}");
                return;
            }

            slots.put(slot, true);
        }

        String bookingId = UUID.randomUUID().toString();
        int amount = hours * 50;

        Booking booking = new Booking(
                bookingId,
                username,
                slot,
                hours,
                amount,
                "PENDING"
        );

        bookings.put(bookingId, booking);

        send(e, 200,
                "{\"message\":\"Booking successful\"," +
                        "\"bookingId\":\"" + bookingId + "\"," +
                        "\"slot\":\"" + slot + "\"," +
                        "\"amount\":" + amount + "}");
    }

    static void payment(HttpExchange e) throws IOException {
        if (!e.getRequestMethod().equalsIgnoreCase("POST")) {
            send(e, 405, "{\"message\":\"Method not allowed\"}");
            return;
        }

        Map<String, String> data = parse(readBody(e));

        String bookingId = data.get("bookingId");

        if (bookingId == null || !bookings.containsKey(bookingId)) {
            send(e, 400, "{\"message\":\"Booking not found\"}");
            return;
        }

        Booking booking = bookings.get(bookingId);

        if (booking.status.equals("PAID")) {
            send(e, 400, "{\"message\":\"Payment already completed\"}");
            return;
        }

        booking.status = "PAID";

        send(e, 200,
                "{\"message\":\"Payment successful\"," +
                        "\"bookingId\":\"" + bookingId + "\"," +
                        "\"slot\":\"" + booking.slot + "\"}");
    }

    static Map<String, String> parse(String body) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();

        if (body == null || body.isEmpty()) {
            return map;
        }

        String[] pairs = body.split("&");

        for (String pair : pairs) {
            String[] parts = pair.split("=", 2);

            if (parts.length == 2) {
                String key = URLDecoder.decode(parts[0], "UTF-8");
                String value = URLDecoder.decode(parts[1], "UTF-8");
                map.put(key, value);
            }
        }

        return map;
    }

    static String readBody(HttpExchange e) throws IOException {
        return new String(
                e.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );
    }

    static void send(HttpExchange e, int status, String response) throws IOException {
        e.getResponseHeaders().set("Content-Type", "application/json");
        byte[] data = response.getBytes(StandardCharsets.UTF_8);
        e.sendResponseHeaders(status, data.length);
        e.getResponseBody().write(data);
        e.close();
    }

    static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();

            for (byte b : bytes) {
                result.append(String.format("%02x", b));
            }

            return result.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}