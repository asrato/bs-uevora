package client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private static final String API_URL = "http://localhost:8050/api";

    private static JSONArray convertJsonStringToJsonArray(String json) {
        return new JSONArray(json);
    }

    public static int menu() {
        int option;
        Scanner scan = new Scanner(System.in);
        System.out.println("Choose an option:\n" +
                "   1 - Create an event\n" +
                "   2 - List events by date\n" +
                "   3 - Register participant in event\n" +
                "   4 - List participants by event\n" +
                "   5 - See general classification in an event point\n" +
                "   6 - Number of athletes that have passed in a point\n" +
                "   7 - Exit");
        try {
            System.out.print("Option: ");
            option = scan.nextInt();
        } catch (Exception e) {
            option = 8;
        }
        return option;
    }

    public static boolean postEvent(String name, String type, String date) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL + "/event").openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String jsonObject = "{\"name\":\"" + name + "\",\"type\":\"" + type + "\",\"date\":\"" + date + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonObject.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            return response.toString().equals("true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getEvents(String date) {
        List<String> events = new LinkedList<>();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL + "/events" + (date.equals("") ? "" : "?date=" + date)).openConnection();

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();

            in.close();

            JSONArray objects = convertJsonStringToJsonArray(response);

            for (int i = 0; i < objects.length(); i++) {
                JSONObject object = objects.getJSONObject(i);
                String event = "   Event | " + object.getString("name") +
                        "\n    Type | " + object.getString("type") +
                        "\n    Date | " + object.getString("date");

                events.add(event);
            }


            return events;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }

    public static JSONArray getFilteredEvents(String filter) {
        if (!filter.equals("future") && !filter.equals("past") && !filter.equals("now") && !filter.equals("past-and-now")) {
            return null;
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL + "/events?filter=" + filter).openConnection();

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();

            in.close();


            return convertJsonStringToJsonArray(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int registerParticipant(String name, String genre, String tier, String eventName) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL + "/register").openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String jsonObject = "{\"name\":\"" + name + "\",\"genre\":\"" + genre.toUpperCase() + "\",\"tier\":\"" + tier + "\",\"eventName\":\"" + eventName + "\",\"dorsal\":0, \"chipID\":0}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonObject.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            return Integer.parseInt(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static JSONArray getParticipants(String eventName) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL + "/participants?name=" + eventName.replaceAll(" ", "%20")).openConnection();

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();

            in.close();

            return convertJsonStringToJsonArray(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getNumberOfParticipantsAtPoint(String event, String point) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL + "/at_point?event=" + event.replaceAll(" ", "%20") + "&location=" + point).openConnection();

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();

            in.close();

            return Integer.parseInt(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static JSONArray getClassification(String event, String end, String start) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL + "/classification?event=" + event.replaceAll(" ", "%20") + "&start=" + start + "&end=" + end).openConnection();

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();

            in.close();


            return convertJsonStringToJsonArray(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static JSONObject findParticipant(JSONArray array, int chipId) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if (object.getInt("chipID") == chipId) {
                return object;
            }
        }

        return null;
    }

    private static boolean eventExists(JSONArray events, String event) {
        for (int i = 0; i < events.length(); i++) {
            JSONObject object = events.getJSONObject(i);
            if (object.getString("name").equals(event))
                return true;
        }
        return false;
    }

    private static String decodeTier(int tierCode) {
        String[] options = new String[]{"Junior", "Senior", "Vet35", "Vet40", "Vet45", "Vet50", "Vet55", "Vet60", "Vet65"};
        if (tierCode > options.length || tierCode < 1) {
            return null;
        }
        return options[tierCode - 1];
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String eventName, eventType, eventDate, participantName, tier, genre;
        int tierNumber, dorsal;
        boolean controller;

        while (true) {
            int option = menu();

            if (option == 7) {
                System.out.println("Shutting down the client...");
                Thread.sleep(1000);
                break;
            }

            switch (option) {
                // Create an event
                case 1:
                    System.out.println();
                    System.out.println("Event name:");
                    eventName = scanner.nextLine();

                    System.out.println("Event type:");
                    eventType = scanner.nextLine();

                    System.out.println("Event date (in dd-MM-yyyy format):");
                    eventDate = scanner.nextLine();

                    Date date = new SimpleDateFormat("dd-MM-yyyy").parse(eventDate);
                    Date today = new Date();

                    if (today.after(date)) {
                        System.out.println("Event should be scheduled to the future\n");
                        break;
                    }

                    controller = postEvent(eventName, eventType, eventDate);

                    System.out.println(controller ?
                            eventName + " created successfully\n"
                            :
                            "An error occurred while creating the desired event\n"
                    );
                    break;
                case 2:
                    System.out.println();
                    System.out.println("Event date in dd-MM-yyyy format (leave empty for all events):");
                    eventDate = scanner.nextLine();

                    List<String> events = getEvents(eventDate);

                    if (events.size() == 0) {
                        System.out.println("There are no scheduled events for the desired date\n");
                    } else {
                        for (String event : events)
                            System.out.println(event + "\n");
                    }

                    break;
                case 3:
                    System.out.println();
                    System.out.println("Participant name:");
                    participantName = scanner.nextLine();

                    if (participantName.equals("")) {
                        System.out.println("Invalid name\n");
                        break;
                    }

                    System.out.println("Participant genre (m/M or f/F):");
                    genre = scanner.nextLine();

                    if (genre.charAt(0) != 'm' && genre.charAt(0) != 'M' && genre.charAt(0) != 'f' && genre.charAt(0) != 'F' && genre.length() > 1) {
                        System.out.println("Invalid genre\n");
                        break;
                    }

                    System.out.println("Participant tier:\n   1 - Junior\n   2 - Senior\n   3 - Vet35\n   4 - Vet40\n   5 - Vet45\n   6 - Vet50\n   7 - Vet55\n   8 - Vet60\n   9 - Vet65");

                    try {
                        tierNumber = scanner.nextInt();
                        scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Invalid tier\n");
                        break;
                    }

                    tier = decodeTier(tierNumber);
                    if (tier == null) {
                        System.out.println("Invalid tier\n");
                        break;
                    }

                    JSONArray futureEvents = getFilteredEvents("future");
                    System.out.println("Choose one of the following events:");

                    if (futureEvents == null) {
                        System.out.println("An error occurred while displaying future events\n");
                        break;
                    } else if (futureEvents.length() == 0) {
                        System.out.println("There are no future events\n");
                        break;
                    }
                    for (int i = 0; i < futureEvents.length(); i++) {
                        JSONObject event = futureEvents.getJSONObject(i);
                        System.out.println(" - " + event.getString("name") + " [" + event.getString("type") + "] " + event.getString("date"));
                    }

                    eventName = scanner.nextLine();

                    if (eventExists(futureEvents, eventName)) {
                        dorsal = registerParticipant(participantName, genre, tier, eventName);
                        if (dorsal == -1) {
                            System.out.println("An error occurred while creating the desired event\n");
                        } else {
                            System.out.println(participantName + " registered in " + eventName + " with dorsal number " + dorsal + "\n");
                        }
                    } else {
                        System.out.println("Invalid event name\n");
                        break;
                    }

                    break;

                case 4:
                    System.out.println();
                    List<String> eventsList = getEvents("");
                    System.out.println("Chose the event:");

                    int i = 1;

                    for (String event : eventsList) {
                        System.out.println(event + "\n");
                    }

                    eventName = scanner.nextLine();

                    JSONArray participants = getParticipants(eventName);

                    if (participants.length() == 0) {
                        System.out.println("There are no participants in the selected event\n");
                        break;
                    }

                    System.out.println();

                    for (i = 0; i < participants.length(); i++) {
                        JSONObject object = participants.getJSONObject(i);
                        String participantString = " - Name: " + object.getString("name") +
                                "\n - Genre: " + object.getString("genre").charAt(0) +
                                "\n - Tier: " + object.getString("tier") +
                                "\n - Dorsal: " + object.getInt("dorsal") +
                                "\n - ChipID: " + object.getInt("chipID") + "\n";
                        System.out.println(participantString);

                    }

                    break;
                case 5:
                    JSONArray pastEvents = getFilteredEvents("past");
                    System.out.println("Choose one of the following events:");

                    if (pastEvents == null) {
                        System.out.println("An error occurred while displaying past events\n");
                        break;
                    } else if (pastEvents.length() == 0) {
                        System.out.println("There are no past events\n");
                        break;
                    }

                    for (i = 0; i < pastEvents.length(); i++) {
                        JSONObject event = pastEvents.getJSONObject(i);
                        System.out.println(" - " + event.getString("name") + " [" + event.getString("type") + "] " + event.getString("date"));
                    }

                    eventName = scanner.nextLine();

                    if (!eventExists(pastEvents, eventName)) {
                        System.out.println("Invalid event name\n");
                        break;
                    }

                    String[] startOptions = {"start", "p1", "p2", "p3"};
                    String[] endOptions = {"p1", "p2", "p3", "finish"};

                    System.out.println("Choose a start location:");
                    System.out.println("   1 - start\n   2 - p1\n   3 - p2\n   4 - p3");
                    int start = Integer.parseInt(scanner.nextLine());

                    System.out.println("Choose an end location:");
                    System.out.println("   1 - p1\n   2 - p2\n   3 - p3\n   4 - finish");
                    int end = Integer.parseInt(scanner.nextLine());

                    if (start < 1 || start > 4) {
                        System.out.println("Invalid start location\n");
                        break;
                    } else if (end < 1 || end > 4) {
                        System.out.println("Invalid end location\n");
                        break;
                    } else if (start > end) {
                        System.out.println("Invalid location sequence (follow the order start -> p1 -> p2 -> p3 -> finish)\n");
                        break;
                    }

                    JSONArray classification = getClassification(eventName, startOptions[start - 1], endOptions[end - 1]);

                    JSONArray participantsArray = getParticipants(eventName);

                    if (classification == null || participantsArray == null) {
                        System.out.println("An error occurred while getting classification...\n");
                        break;
                    }

                    System.out.println();
                    for (i = 0; i < classification.length(); i++) {
                        JSONObject classificationItem = classification.getJSONObject(i);
                        JSONObject participant = findParticipant(participantsArray, classificationItem.getInt("chipID"));
                        System.out.println("#" + (i + 1) + " [" + classificationItem.getString("elapsedTime") + "] " + participant.getString("name") + " (dorsal #" + participant.getInt("dorsal") + ")" + " [" + participant.getString("tier") + "]");
                    }

                    break;
                case 6:
                    System.out.println("Choose an event:");
                    JSONArray eventsArray = getFilteredEvents("past-and-now");
                    System.out.println("Choose one of the following events:");

                    if (eventsArray == null) {
                        System.out.println("An error occurred while displaying past events\n");
                        break;
                    } else if (eventsArray.length() == 0) {
                        System.out.println("There are no past events\n");
                        break;
                    }

                    for (i = 0; i < eventsArray.length(); i++) {
                        JSONObject event = eventsArray.getJSONObject(i);
                        System.out.println(" - " + event.getString("name") + " [" + event.getString("type") + "] " + event.getString("date"));
                    }

                    eventName = scanner.nextLine();

                    if (!eventExists(eventsArray, eventName)) {
                        System.out.println("Invalid event name\n");
                        break;
                    }

                    System.out.println("Choose a location:");
                    System.out.println("   1 - start\n   2 - p1\n   3 - p2\n   4 - p3\n   5 - finish");
                    int point = Integer.parseInt(scanner.nextLine());

                    if (point < 1 || point > 5) {
                        System.out.println("Invalid start location\n");
                        break;
                    }

                    String[] options = {"start", "p1", "p2", "p3", "finish"};

                    int numberOfParticipantsAtThisPoint = getNumberOfParticipantsAtPoint(eventName, options[point - 1]);

                    System.out.println("Number of participants: " + numberOfParticipantsAtThisPoint + "\n");

                    break;
                default:
                    System.out.println("Invalid option\n");
                    break;
            }
        }
    }
}
