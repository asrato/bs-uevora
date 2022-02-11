package t1_sd;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * Cliente
 * @author joao rouxinol & andre rato
 */
public class Client {
    
    static void dateChecker(String date){
        if(date.equals("geral")){
            return;
        }
        String aux[] = date.split("-");
        Integer.parseInt(aux[0]);
        Integer.parseInt(aux[1]);
        Integer.parseInt(aux[2]);
        return;
    }
    
    static void genreChecker(String genre) throws RuntimeException{
        if(genre.equals("m") || genre.equals("f")){
            return; 
        }
        throw new RuntimeException("genero invalido");
    }
    // converts HH:MM:SS to seconds
    static int convertToSeconds(String time){
        String aux[] = time.split(":");
        return (Integer.parseInt(aux[0])*3600) + Integer.parseInt(aux[1])*60 + Integer.parseInt(aux[2]);
    }
    
    // returns a String with the picked tier
    static String tierPicker(int choice) throws RuntimeException{
        String res = "";
        switch(choice){
            case 1:
                res = "juniores";
                break;
            case 2:
                res = "seniores";
                break;
            case 3:
                res = "veteranos 35";
                break;
            case 4:
                res = "veteranos 40";
                break;
            case 5:
                res = "veteranos 45";
                break;
            case 6:
                res = "veteranos 50";
                break;
            case 7:
                res = "veteranos 55";
                break;
            case 8:
                res = "veteranos 60";
                break;   
            case 9:
                res = "veteranos 65+";
                break;   
            default:
                throw new RuntimeException("Escalao Inválido");
        }
        return res;
    }
                                            
    // prints the options and reads the user's choice
    static int menu(){
        Scanner scan = new Scanner(System.in);
        int a;
        System.out.println("\nQual a operação que deseja realizar?\n"
                + "(Indique o numero correspondente à opção)");
        System.out.println("1-Inscrever um participante ");
        System.out.println("2-Obter a lista de eventos (geral ou para uma data)");
        System.out.println("3-Obter a lista de participantes de um evento");        
        System.out.println("4-Registar um novo evento");
        System.out.println("5-Registar um tempo de prova");
        System.out.println("6-Lista de classificacao geral");
        System.out.println("7-Lista de top 3");
        System.out.println("8-Sair\n");
        try{
            a = scan.nextInt();
        } catch (Exception e){
            a = 10;
        }
        return a;
    }
    
    // where the client runs
    public static void main(String args[]) {
        String regHost = "";
	String regPort = "";
        
        try (InputStream input = new FileInputStream("resources/config.properties")) {

            Properties prop = new Properties();

            // loading properties file
            prop.load(input);
            regHost = prop.getProperty("regHost");
            regPort = prop.getProperty("regPort");

        } catch (Exception e) {
            e.printStackTrace();
        }

	try {
	    // remote object
	    Inscricao obj =
	      (Inscricao) java.rmi.Naming.lookup("rmi://" 
                      + regHost +":"+regPort +"/inscricao");
	
        // connected successfully 
        System.out.println("Conexão com sucesso\nBem vindo!\n");
        
        while(true){
            int opt = menu();
            String to_add, first;
            Scanner scan = new Scanner(System.in);
            switch(opt){
                // to add a new participant
                case 1:
                    System.out.println("Insira o nome do atleta:");
                    String ath_name = scan.nextLine();
                    System.out.println("Insira o genero do atleta (m/f):");
                    String sex = scan.nextLine();
                    try{
                        genreChecker(sex);
                    } catch (Exception e){
                        System.out.println("Género Inválido");
                        break;
                    }
                    System.out.println("Insira o escalão do atleta:"
                            + "Escaloes disponiveis:\n"
                            + "1 - Juniores      (18 a 19)\n2 - Seniores      (20 a 34)\n"
                            + "3 - Veteranos 35  (35 a 39)\n4 - Veteranos 40  (40 a 44)\n"
                            + "5 - Veteranos 45  (45 a 49)\n6 - Veteranos 50  (50 a 54)\n"
                            + "7 - Veteranos 55  (55 a 59)\n8 - Veteranos 60  (60 a 64)\n"
                            + "9 - Veteranos 65+ (65 ou +)"
                            + "(Indique o número correspondente ao escalão)");
                    
                    String age = scan.nextLine();
                    try{
                        age = tierPicker(Integer.parseInt(age));
                    } catch (Exception e){
                        System.out.println("Opção Inválida");
                        break;
                    }
                    System.out.println("Insira o nome da prova:");
                    String prova = scan.nextLine();

                    to_add = ath_name + "\n" + sex + "\n" + age + "\n" +  prova;

                    first = obj.novaInscricao(to_add.toLowerCase());
                    System.out.println(first);
                    break;
                // to search for events in a specific date    
                case 2:
                    System.out.println("Insira a data a pesquisar (insira geral para todos os eventos disponiveis ou use o formato YYYY-MM-DD para uma data especifica):");
                    String search_date = scan.nextLine();
                    try{
                        dateChecker(search_date);
                    } catch(Exception e){
                        System.out.println("Data inválida, insira a data no formato correto.");
                        break;
                    }
                    first = obj.getProva(search_date);
                    System.out.println(first);
                    break;
                    // to search for participants in a given event                    
                case 3:
                    System.out.println("Insira o nome do evento a pesquisar:");
                    String search_name = scan.nextLine();
                    first = obj.getInscritos(search_name.toLowerCase());
                    System.out.println(first);
                    break;
                    // to add a new event to the database
                case 4:
                    System.out.println("Insira o nome da prova:");
                    String event_name = scan.nextLine();
                    System.out.println("Insira a data da prova (formato YYYY-MM-DD):");
                    String event_date = scan.nextLine();
                    try{
                        dateChecker(event_date);
                    } catch(Exception e){
                        System.out.println("Data inválida, insira a data no formato correto.");
                        break;
                    }
                    System.out.println("Insira o tipo da prova:");
                    String event_type = scan.nextLine();
                    to_add = event_name + "\n" + event_date + "\n" + event_type;
                    first = obj.novoEvento(to_add.toLowerCase());
                    System.out.println(first);
                    break;
                    // to register a new time record to the database
                case 5:
                    int time_done = 0;
                    System.out.println("Insira o nome do evento:");
                    String time_name = scan.nextLine();
                    System.out.println("Insira o numero de dorsal do participante");
                    String time_number = scan.nextLine();
                    try{
                        Integer.parseInt(time_number);
                    } catch(Exception e){
                        System.out.println("Numero de dorsal inválido");
                        break;
                    }
                    System.out.println("Insira o tempo de prova (formato HH:MM:SS):");
                    String time_done_unformat = scan.nextLine();
                    try{
                        time_done = convertToSeconds(time_done_unformat);
                    } catch(Exception e){
                        System.out.println("Tempo invalido, insira o tempo no formato correto");
                        break;
                    }
                    to_add = time_name + "\n" + time_number + "\n" + time_done;
                    first = obj.novoTempo(to_add.toLowerCase());
                    System.out.println(first);
                    break;
                    // to get the leaderboard of an event 
                case 6:
                    System.out.println("Insira o nome da prova:");
                    String event_to_search = scan.nextLine();
                    System.out.println("Insira o parametro de pesquisa (m/f/geral):");
                    String event_to_gender = scan.nextLine();  
                    to_add = event_to_search + "\n" + event_to_gender;
                    first = obj.getClassificacoes(to_add.toLowerCase());
                    System.out.println(first);
                    break;
                    // to get the top 3 of an event
                case 7:
                    System.out.println("Insira o nome da prova:");
                    event_to_search = scan.nextLine();
                    System.out.println("Insira o parametro de pesquisa (m/f/geral):");
                    event_to_gender = scan.nextLine();
                    System.out.println("Insira o escalão para pesquisa:\n"
                            + "Escaloes disponiveis:\n"
                            + "1 - Juniores      (18 a 19)\n2 - Seniores      (20 a 34)\n"
                            + "3 - Veteranos 35  (35 a 39)\n4 - Veteranos 40  (40 a 44)\n"
                            + "5 - Veteranos 45  (45 a 49)\n6 - Veteranos 50  (50 a 54)\n"
                            + "7 - Veteranos 55  (55 a 59)\n8 - Veteranos 60  (60 a 64)\n"
                            + "9 - Veteranos 65+ (65 ou +)\n"
                            + "10 - Geral"
                            + "(Indique o numero correspondente ao escalao)");
                    String event_to_tier = scan.nextLine();
                    if(event_to_tier.equals("10")){
                        event_to_tier = "geral";
                    } else{
                        try{
                        event_to_tier = tierPicker(Integer.parseInt(event_to_tier));
                        } catch (Exception e){
                            System.out.println("Opção Inválida");
                            break;
                        }
                    }

                    to_add = event_to_search + "\n" + event_to_gender + "\n" + event_to_tier;
                    //invocacao remota de um metodo de pesquisa dos top 3
                    first = obj.getTop(to_add.toLowerCase());
                    System.out.println(first);
                    break;
                case 8:
                    System.out.println("Até a próxima.");
                    System.exit(1);
                default:
                    System.out.println("Opção Inválida");
                    break;
            }
        }
	} 
	catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}