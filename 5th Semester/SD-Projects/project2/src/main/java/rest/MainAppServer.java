package rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

public class MainAppServer {

    private static int getPort(int defaultPort) {
        return defaultPort;
    }

    private static URI getBaseURI() {
        String baseURI = "";
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            baseURI = prop.getProperty("baseuri");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return URI.create(baseURI);
    }

    public static final URI BASE_URI = getBaseURI();

    public static HttpServer startServer() throws IOException {
        // ativar um servico com os REST resources existentes neste pacote:
        ResourceConfig rc = new ResourceConfig().packages("rest");

        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting grizzly...");
        HttpServer httpServer = startServer();

        System.out.println("\n## Server on at " + BASE_URI);
        System.out.println("\n## Hit enter to stop the server...");

        System.in.read();
        // depois do enter:
        httpServer.stop();
    }

}