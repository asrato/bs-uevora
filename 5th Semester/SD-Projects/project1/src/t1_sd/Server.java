package t1_sd;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Servidor 
 * @author joao rouxinol & andre rato
 */
public class Server {

    public static void main(String args[]) {
        
        
	int regPort= 1099; // default RMIRegistry port

        try (InputStream input = new FileInputStream("resources/config.properties")) {

            Properties prop = new Properties();

            // loading properties file
            prop.load(input);
            regPort = Integer.parseInt(prop.getProperty("regPortServer"));

        } catch (Exception e) {
            e.printStackTrace();
        }
	
	try {

	    // remote object creation
	    Inscricao obj= new InscricaoImpl();


	    java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.getRegistry(regPort);

	    registry.rebind("inscricao", obj);

            // server is working and expecting connections
            System.out.println("Servidor Ok");
	} 
	catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
    
}
