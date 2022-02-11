package client;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;

public class ChipClient {

    private static final String API_URL = "http://localhost:8050/api";

    private static JSONArray convertJsonStringToJsonArray(String json) {
        return new JSONArray(json);
    }

    public static int menu() {
        int option;
        Scanner scan = new Scanner(System.in);
        System.out.println("Choose an option:\n" +
                "   1 - Register time\n" +
                "   2 - Exit");
        try {
            System.out.print("Option: ");
            option = scan.nextInt();
        } catch (Exception e) {
            option = 3;
        }
        System.out.println();
        return option;
    }

    protected static boolean registerTime(int chipId, String location) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL + "/sensor/register_time").openConnection();

            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm:ss");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String time = sdf3.format(timestamp);

            String jsonObject = "{\"chipID\":\"" + chipId + "\",\"section\":\"" + location + "\",\"time\":\"" + time + "\"}";

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

    public static void main(String[] args) throws Exception {
        int chipId;
        String locationId;
        boolean controller = false;
        Scanner scan = new Scanner(System.in);

        while (true) {
            int option = menu();

            if (option == 2) {
                System.out.println("Shutting down the client...");
                Thread.sleep(1000);
                break;
            }

            switch (option) {
                case 1:
                    System.out.print("Insert Chip ID: ");
                    chipId = Integer.parseInt(scan.nextLine());

                    if (chipId < 1) {
                        System.out.println("Invalid Chip ID\n");
                        break;
                    }

                    System.out.print("Insert Location ID: ");
                    locationId = scan.nextLine();

                    switch (locationId.toLowerCase()) {
                        case "start":
                        case "p1":
                        case "p2":
                        case "p3":
                        case "finish":
                            controller = true;
                            break;
                        default:
                            controller = false;
                            break;
                    }

                    if (!controller) {
                        System.out.println("Location Id is invalid (try 'start', 'p1', 'p2', 'p3' or 'finish')");
                        break;
                    }

                    if(registerTime(chipId, locationId)) {
                        System.out.println("Time for chip " + chipId + " in location '" + locationId + "' registered\n");
                    } else {
                        System.out.println("An error occurred while registering a new time\n");
                    }
                    break;
                default:
                    System.out.println("Invalid option\n");
                    break;
            }
        }
    }
}
