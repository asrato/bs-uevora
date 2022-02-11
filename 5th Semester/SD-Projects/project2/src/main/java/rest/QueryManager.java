package rest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

public class QueryManager {

    public QueryManager() {
    }

    protected List<Event> getFilteredEvents(String filter) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String query;

        switch (filter) {
            case "future":
                query = "SELECT * FROM events WHERE date > '" + dateFormat.format(date) + "' ORDER BY date";
                break;
            case "past":
                query = "SELECT * FROM events WHERE date < '" + dateFormat.format(date) + "' ORDER BY date";
                break;
            case "past-and-now":
                query = "SELECT * FROM events WHERE date < '" + dateFormat.format(date) + "' or date = '" + dateFormat.format(date) +"' ORDER BY date";
                break;
            default:
                query = "SELECT * FROM events WHERE date = '" + dateFormat.format(date) + "' ORDER BY date";
                break;
        }


        List<Event> events = new LinkedList<>();
        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                events.add(new Event(resultSet.getString("eventName"), resultSet.getString("type"), resultSet.getString("date")));
            }

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }

    protected int getLastChipID() {
        int lastChipID = 0;
        String query = "SELECT MAX(chipid) as max FROM registrations";
        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();
            lastChipID = Integer.parseInt(resultSet.getString("max"));

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastChipID;
    }

    protected int getLastDorsal(String eventName) {
        int lastDorsal = -1;
        String query = "SELECT MAX(dorsal) as max FROM registrations WHERE eventName = '" + eventName + "'";
        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();
            if (resultSet.getString("max") == null) {
                lastDorsal = 0;
            } else {
                lastDorsal = Integer.parseInt(resultSet.getString("max"));
            }
            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastDorsal;
    }

    PostgresConnect setupDataBase() throws Exception {
        String host = "", db = "", user = "", pw = "";
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            host = prop.getProperty("host");
            db = prop.getProperty("db");
            user = prop.getProperty("user");
            pw = prop.getProperty("password");
        }
        return new PostgresConnect(host, db, user, pw);
        //return new PostgresConnect("localhost", "trabalho2", "user1", "pw");
    }

    void closeDBConnection(PostgresConnect pc) throws Exception {
        pc.disconnect();
    }

    protected boolean createEvent(Event event) {
        String query = "INSERT INTO events VALUES ('" + event.getName() + "', '" + event.getType() + "', '" + event.getDate() + "')";

        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            statement.executeUpdate(query);

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected List<Event> searchEvents(String date) {
        String query = "SELECT * FROM events";

        if (!date.equals("")) {
            query += " WHERE date = '" + date + "'";
        }

        query += " ORDER BY date";

        List<Event> events = new LinkedList<>();
        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                events.add(new Event(resultSet.getString("eventName"), resultSet.getString("type"), resultSet.getString("date")));
            }

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }

    protected boolean registerParticipant(Participant participant) {
        String query = "INSERT INTO registrations VALUES ('" + participant.getName() + "', '" + participant.getEventName() + "', '" + participant.getGenre() + "', '" + participant.getTier() + "', '" + participant.getDorsal() + "', '" + participant.getChipID() + "')";

        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            statement.executeUpdate(query);

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected List<Participant> getParticipants(String eventName) {
        String query = "SELECT * FROM registrations WHERE eventname='" + eventName + "'";
        List<Participant> participants = new LinkedList<>();

        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                participants.add(new Participant(resultSet.getString("participantname"), resultSet.getInt("dorsal"), resultSet.getString("genre"), resultSet.getString("eventname"), resultSet.getString("tier"), resultSet.getInt("chipid")));
            }

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return participants;
    }

    protected boolean registerTime(Chip chip, String event) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        String query = "INSERT INTO times VALUES ('" + event + "', '" + chip.getChipID() + "', '" + chip.getSection() +
                "', TIMESTAMP'" + sdf.format(date) + " " + chip.getTime() + "')";

        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            statement.executeUpdate(query);

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected List<String> getLocationsByChipId(int chipId) {
        String query = "SELECT * FROM times WHERE chipid=" + chipId + "";

        List<String> locations = new LinkedList<>();

        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                locations.add(resultSet.getString("locationid"));
            }

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return locations;
    }

    protected List<Integer> getChipIdByCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String query = "select chipid from events natural inner join registrations where date='" + sdf.format(date) + "'";

        List<Integer> chips = new LinkedList<>();

        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                chips.add(Integer.parseInt(resultSet.getString("chipid")));
            }

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chips;
    }

    protected String[] getEventByChipId(int chipId) {
        String query = "SELECT * FROM registrations WHERE chipid='" + chipId + "'";

        String[] eventName = new String[2];

        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();
            eventName[0] = resultSet.getString("eventname");

            String query1 = "SELECT * FROM events WHERE eventName='" + eventName[0] + "'";

            statement = pc.getStatement();
            resultSet = statement.executeQuery(query1);

            resultSet.next();
            eventName[1] = resultSet.getString("date");

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eventName;
    }

    protected int getNumberOfParticipantsAtPoint(String eventName, String location) {
        int numberOfParticipants = 0;

        String query = "select distinct eventname, count(chipid) as number from times where eventname = '" + eventName + "' and locationid='" + location + "' group by eventname;";

        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();
            numberOfParticipants = resultSet.getInt("number");

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return numberOfParticipants;
    }

    protected List<HashMap<String, String>> getClassification(String eventName, String start_point, String end_point) {
        List<HashMap<String, String>> classification = new LinkedList<>();


        String query =
                "select chipid, t_start-t_point as elapsed_time from (select * from(select chipid, times.timestamp as t_start, eventname from times natural inner join registrations where locationid='" + start_point + "' and eventname='" + eventName + "') as start_table natural inner join (select chipid, timestamp as t_point, eventname from times natural inner join registrations where locationid='" + end_point + "' and eventname='" + eventName + "') as point_table) as difference_table order by elapsed_time";

        try {
            PostgresConnect pc = setupDataBase();
            pc.connect();

            Statement statement = pc.getStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                HashMap<String, String> pair = new HashMap<>();
                pair.put("chipID", Integer.toString(resultSet.getInt("chipid")));
                pair.put("elapsedTime", resultSet.getString("elapsed_time"));
                classification.add(pair);
            }

            pc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classification;
    }
}