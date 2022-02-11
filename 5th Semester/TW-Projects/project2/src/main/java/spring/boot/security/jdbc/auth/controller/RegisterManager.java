/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spring.boot.security.jdbc.auth.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterManager {
    private String name, password, real_name, gender, tier;
    private String eventName, description, value;
    private String time, section;

    public RegisterManager() {

    }

    // para inscricoes no servico
    public RegisterManager(String name, String password, String real_name, String gender, String tier) {
        this.name = name;
        this.password = password;
        this.real_name = real_name;
        this.gender = gender;
        this.tier = tier;
    }

    // para inscricoes em eventos
    public RegisterManager(String name, String eventName) {
        this.name = name;
        this.eventName = eventName;
    }

    // para registar tempos
    public RegisterManager(String name, String eventName, String time, String section) {
        this.name = name;
        this.eventName = eventName;
        this.time = time;
        this.section = section;
    }

    // para registar eventos
    public RegisterManager(String eventName, String description, String value) {
        this.eventName = eventName;
        this.description = description;
        this.value = value;
    }

    // para procuras
    public RegisterManager(String name) {
        this.name = name;
    }

    static String getPreviousSection(String section) {
        if (section.equals("p1")) {
            return "start";
        } else if (section.equals("p2")) {
            return "p1";
        } else if (section.equals("p3")) {
            return "p2";
        } else if (section.equals("finish")) {
            return "p3";
        }
        return "";
    }

    protected int getLastDorsal(String eventName) {
        int lastDorsal = -1;
        String query = "SELECT MAX(dorsal) as max FROM inscricoes WHERE event_name = '" + eventName + "'";
        try {
            PostGresConnect pc = new PostGresConnect();
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

    public String getRole() throws Exception {
        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();
        ResultSet resultSet = statement.executeQuery("SELECT user_role from user_role where user_name = '" + name + "'");
        resultSet.next();
        return resultSet.getString("user_role");
    }


    public static JSONObject getMB(String amount) throws Exception {
        try {
            URL url = new URL("http://alunos.di.uevora.pt/tweb/t2/mbref4payment?amount=" + amount);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            StringBuilder result = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }

            return new JSONObject(result.toString());


        } catch (Exception e) {
            return null;
        }
    }

    public int RegisterName() throws Exception {
        BCryptPasswordEncoder benc = new BCryptPasswordEncoder();
        String ep = benc.encode(password.subSequence(0, password.length()));

        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();


        ResultSet resultSet = statement.executeQuery("SELECT user_name from userinfo where user_name = '" + name + "'");
        if (resultSet.next()) {
            connect.disconnect();
            return -1;
        }

        statement.executeUpdate("INSERT INTO userinfo VALUES ('" + name + "', '" + ep + "', '" + real_name + "', '" + gender + "', '" + tier + "', 1)");
        statement.executeUpdate("INSERT INTO user_role VALUES ('" + name + "', 'ROLE_ATLETA')");

        connect.disconnect();

        return 1;
    }

    public JSONObject RegisterInscricao() throws Exception {

        int dorsal = getLastDorsal(eventName) + 1;
        String reference = "", ent = "", val = "";


        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();

        ResultSet resultSet = statement.executeQuery("SELECT user_name from userinfo where user_name = '" + name + "'");
        if (!resultSet.next()) {
            connect.disconnect();
            return null;
        }

        resultSet = statement.executeQuery("SELECT event_name, value from events where event_name = '" + eventName + "'");
        if (!resultSet.next()) {
            connect.disconnect();
            return null;
        }

        val = resultSet.getString("value");

        resultSet = statement.executeQuery("SELECT participant_name from inscricoes where event_name = '" + eventName + "' and participant_name = '" + name + "'");
        if (resultSet.next()) {
            connect.disconnect();
            return null;
        }

        JSONObject a = getMB(val);

        ent = a.getString("mb_entity");
        reference = a.getString("mb_reference");

        statement.executeUpdate("INSERT INTO inscricoes VALUES ('" + name + "', '" + eventName + "', " + dorsal + ")");
        statement.executeUpdate("INSERT INTO refs VALUES ('" + name + "', " + ent + ", " + reference + ", " + a.getString("mb_amount") + ", '" + eventName + "')");

        connect.disconnect();

        JSONObject ret = new JSONObject();
        ret.put("dorsal", dorsal);
        ret.put("entity", ent);
        ret.put("reference", reference);
        ret.put("value", val);

        return ret;
    }

    public JSONArray getPaid(String username) throws Exception {
        JSONArray paidEvents = new JSONArray();

        PostGresConnect connect = new PostGresConnect();
        connect.connect();

        String queryPaid = "SELECT * FROM events NATURAL INNER JOIN paid WHERE username='" + username + "' ORDER BY event_date";

        Statement statement = connect.getStatement();
        ResultSet resultSet = statement.executeQuery(queryPaid);

        while (resultSet.next()) {
            JSONObject event = new JSONObject();
            event.put("event", resultSet.getString("event_name"));
            event.put("price", resultSet.getString("value"));
            event.put("date", resultSet.getString("event_date"));
            paidEvents.put(event);
        }

        return paidEvents;
    }

    public JSONArray getUnpaid(String username) throws Exception {
        JSONArray unpaidEvents = new JSONArray();

        PostGresConnect connect = new PostGresConnect();
        connect.connect();

        String queryUnpaid = "SELECT event_name, event_date, value FROM events NATURAL INNER JOIN inscricoes WHERE participant_name = '" + username +
                "' EXCEPT SELECT event_name, event_date, value FROM paid NATURAL INNER JOIN events WHERE username='" + username + "' ORDER BY event_date";

        Statement statement = connect.getStatement();
        ResultSet resultSet = statement.executeQuery(queryUnpaid);

        while (resultSet.next()) {
            JSONObject event = new JSONObject();
            event.put("event", resultSet.getString("event_name"));
            event.put("price", resultSet.getString("value"));
            event.put("date", resultSet.getString("event_date"));
            unpaidEvents.put(event);
        }

        return unpaidEvents;
    }

    public int RegisterEvent(String date) throws Exception {
        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();

        ResultSet resultSet = statement.executeQuery("SELECT event_name from events where event_name = '" + eventName + "'");
        if (resultSet.next()) {
            connect.disconnect();
            return -1;
        }
        statement.executeUpdate("INSERT INTO events VALUES ('" + eventName + "', '" + description + "', '" + date + "', " + Float.parseFloat(value) + ")");

        connect.disconnect();
        return 1;
    }

    public String getNameByUsername(String username) throws Exception {
        String query = "SELECT nome_proprio FROM userinfo WHERE user_name='" + username + "';";
        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();

        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        String name = resultSet.getString("nome_proprio");
        connect.disconnect();

        return name;
    }

    public int RegisterTime() throws Exception {
        String dorsal = name;
        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();

        ResultSet resultSet = statement.executeQuery("SELECT dorsal from inscricoes where event_name = '" + eventName + "' and dorsal = '" + dorsal + "'");

        if (!resultSet.next()) {
            connect.disconnect();
            return -1; // nao esta inscrito esse dorsal na corrida (AINDA)
        }

        resultSet = statement.executeQuery("select event_name, dorsal from paid natural inner join inscricoes where event_name ='" + eventName + "' and dorsal ='" + dorsal + "'");

        if (!resultSet.next()) {
            connect.disconnect();
            return -4; // nao esta pago (AINDA)
        }

        String prevSection = getPreviousSection(section);

        if (!section.equals("start")) {
            String query = "SELECT * from times where event_name = '" + eventName + "' and dorsal = " + dorsal + " and sectionID = '" + prevSection + "'";
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connect.disconnect();
                return -3; // ainda nao existe tempo para a seccao anterior logo nao podemos registar nesta      
            }
        }


        resultSet = statement.executeQuery("SELECT * from times where event_name = '" + eventName + "' and dorsal = '" + dorsal + "' and sectionID = '" + section + "'");
        if (resultSet.next()) {
            connect.disconnect();
            return -2; //ja existe um tempo registado para aquela secao naquele evento para aquele dorsal
        }

        resultSet = statement.executeQuery("SELECT * from events where event_name = '" + eventName + "'");
        resultSet.next();

        String date = resultSet.getString("event_date");

        statement.executeUpdate("INSERT INTO times VALUES ('" + eventName + "', '" + dorsal + "', '" + section + "', TO_TIMESTAMP('" + date + " " + time + "', 'YYYY-MM-DD HH24:MI:SS'))");

        connect.disconnect();
        return 1;
    }

    public JSONObject getPaymentDetails() throws Exception {
        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();

        String query = "SELECT * FROM refs WHERE username='" + name + "' AND event_name='" + eventName + "'";

        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();

        JSONObject reference = new JSONObject();
        reference.put("entity", resultSet.getString("entidade"));
        reference.put("reference", resultSet.getString("referencia"));
        reference.put("value", resultSet.getString("valor"));

        return reference;
    }

    public void pay() throws Exception {
        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();

        String query = "INSERT INTO paid VALUES ('" + name + "', '" + eventName + "')";

        statement.executeUpdate(query);

    }

    public JSONArray getEventsToJoin() throws Exception {
        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String query = "SELECT event_name, event_date, value FROM events WHERE event_date>'" + dateFormat.format(date) + "'" +
                " EXCEPT SELECT event_name, event_date, value from events NATURAL INNER JOIN inscricoes WHERE participant_name='" + name + "' ORDER BY event_date";

        ResultSet resultSet = statement.executeQuery(query);

        JSONArray events = new JSONArray();

        while (resultSet.next()) {
            JSONObject event = new JSONObject();
            event.put("event", resultSet.getString("event_name"));
            event.put("price", resultSet.getString("value"));
            event.put("date", resultSet.getString("event_date"));
            events.put(event);
        }

        return events;
    }

    public JSONArray getEvents(String filter) throws Exception {
        char comparison = '=';

        switch (filter) {
            case "previous":
                comparison = '<';
                break;
            case "future":
                comparison = '>';
                break;
            case "all":
                comparison = ' ';
            default:
                break;
        }

        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String query = "";

        if (comparison != ' ') {
            query = "SELECT * FROM events WHERE event_date" + comparison + "'" + dateFormat.format(date) + "' ORDER BY event_date";
        } else {
            query = "SELECT * FROM events";
        }


        ResultSet resultSet = statement.executeQuery(query);

        JSONArray events = new JSONArray();

        while (resultSet.next()) {
            JSONObject event = new JSONObject();
            event.put("event", resultSet.getString("event_name"));
            event.put("price", resultSet.getString("value"));
            event.put("date", resultSet.getString("event_date"));
            event.put("description", resultSet.getString("description"));
            events.put(event);
        }

        return events;
    }

    public JSONObject getEventInfo() throws Exception {
        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();

        String query = "SELECT * FROM events WHERE event_name='" + name + "'";

        ResultSet resultSet = statement.executeQuery(query);

        resultSet.next();

        JSONObject eventInfo = new JSONObject();
        eventInfo.put("description", resultSet.getString("description"));
        eventInfo.put("date", resultSet.getString("event_date"));
        eventInfo.put("price", resultSet.getString("value"));

        return eventInfo;
    }

    public JSONArray getParticipants() throws Exception {
        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();

        String query = "SELECT * FROM inscricoes INNER JOIN userinfo ON user_name=participant_name WHERE event_name='" + name + "'";

        ResultSet resultSet = statement.executeQuery(query);

        JSONArray participants = new JSONArray();

        while (resultSet.next()) {
            JSONObject participant = new JSONObject();

            participant.put("name", resultSet.getString("nome_proprio"));
            participant.put("username", resultSet.getString("user_name"));
            participant.put("tier", resultSet.getString("tier"));
            participant.put("dorsal", resultSet.getInt("dorsal"));
            participant.put("paid", false);

            participants.put(participant);
        }

        query = "SELECT username, dorsal from paid inner join inscricoes ON participant_name=username WHERE paid.event_name='" + name + "'";

        resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int dorsal = resultSet.getInt("dorsal");

            participants.getJSONObject(dorsal - 1).put("paid", true);
        }

        return participants;
    }

    public JSONObject getClassification(String begin, String end) throws Exception {
        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();

        JSONObject classification = new JSONObject();
        JSONArray general = new JSONArray();
        JSONArray female = new JSONArray();
        JSONArray male = new JSONArray();

        switch (begin) {
            case "start":
            case "p1":
            case "p2":
            case "p3":
                break;
            default:
                throw new Exception();
        }
        switch (end) {
            case "p1":
            case "p2":
            case "p3":
            case "finish":
                break;
            default:
                throw new Exception();
        }

        String query = "select participant_name, nome_proprio, tier, gender, dorsal, time " +
                "from (select dorsal, time, participant_name " +
                "from (select dorsal, t_finish - t_start as time " +
                "from (select dorsal, elapsed_time as t_start " +
                "from times " +
                "where event_name = '" + name + "' " +
                "and sectionid = '" + begin + "') as start_table " +
                "inner join (select dorsal, " +
                "elapsed_time " +
                "as t_finish " +
                "from times " +
                "where event_name = '" + name + "' " +
                "and sectionid = '" + end + "') as finish_table " +
                "using (dorsal)) as difference_table " +
                "inner join inscricoes using (dorsal) " +
                "where event_name = '" + name + "') as info " +
                "inner join userinfo on participant_name = user_name";

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            JSONObject participant = new JSONObject();

            String gender = resultSet.getString("gender");

            participant.put("dorsal", resultSet.getInt("dorsal"));
            participant.put("time", resultSet.getString("time"));
            participant.put("name", resultSet.getString("nome_proprio"));
            participant.put("gender", gender);
            participant.put("tier", resultSet.getString("tier"));

            switch (gender) {
                case "f":
                    female.put(participant);
                    break;
                case "m":
                    male.put(participant);
            }

            general.put(participant);
        }

        classification.put("general", general);
        classification.put("female", female);
        classification.put("male", male);

        return classification;
    }

    public JSONArray getParticipantsForSection(String section) throws Exception {
        PostGresConnect connect = new PostGresConnect();
        connect.connect();
        Statement statement = connect.getStatement();

        JSONArray participants = new JSONArray();

        try {

            String query = "select distinct participant_name, nome_proprio as nome, elapsed_time, dorsal from " +
                    "(select distinct dorsal, participant_name, elapsed_time from inscricoes natural inner join times " +
                    "where times.event_name='" + name + "' and sectionid='" + section + "')as times_table inner join " +
                    "userinfo on participant_name = user_name order by elapsed_time";

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                JSONObject participant = new JSONObject();

                participant.put("name", resultSet.getString("nome"));
                participant.put("time", resultSet.getString("elapsed_time").split(" ")[1]);
                participant.put("dorsal", resultSet.getInt("dorsal"));

                participants.put(participant);
            }
        } catch (Exception e) {
            return new JSONArray();
        }

        return participants;
    }
}