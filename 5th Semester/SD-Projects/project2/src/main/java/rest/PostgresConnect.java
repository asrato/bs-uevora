package rest;
import java.sql.*;

/**
 * Connects to a PostGres database
 * @author joao rouxinol & andre rato
 */
public class PostgresConnect {
    private String PG_HOST;
    private String PG_DB;
    private String USER;
    private String PWD;

    Connection con = null;
    Statement stmt = null;

    public PostgresConnect(String host, String db, String user, String pw) {
        PG_HOST=host;
        PG_DB= db;
        USER=user;
        PWD=pw;
    }

    // connects
    public void connect() throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
            // url = "jdbc:postgresql://host:port/database",
            con = DriverManager.getConnection("jdbc:postgresql://" + PG_HOST + ":5432/" + PG_DB,
                    USER,
                    PWD);

            stmt = con.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems setting the connection");
        }
    }

    // ends the connection
    public void disconnect() {
        try {
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // returns the statement
    public Statement getStatement() {
        return stmt;
    }
}