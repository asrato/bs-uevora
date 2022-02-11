package t1_sd;

import java.io.FileInputStream;
import java.io.InputStream;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

/**
 * Remote object
 * @author joao rouxinol & andre rato
 */
public class InscricaoImpl extends UnicastRemoteObject 
        implements Inscricao, java.io.Serializable {
    Scanner scan = new Scanner(System.in);

    public InscricaoImpl() throws java.rmi.RemoteException {
        super();
    }
        
    // converts seconds to HH:MM:SS
    static String convertToHhMmSs(int time){
        String res = "";
        int hours = time / 3600;
        time = time % 3600;
        if(hours<10){
            res +=0;
        }
        res += hours + ":";
        hours = time / 60;
        if(hours<10){
            res +=0;
        }
        res += hours + ":";
        hours = time % 60;
        if(hours<10){
            res +=0;
        }
        res += hours;
        return res;
    }
        
    // inserts a new row in the inscricoes table
    public String novaInscricao(String s) throws java.rmi.RemoteException{
        System.err.println("Invocacao de novaInscricao() - Tentativa de nova inscrição.");
        String res = "";
        try{
            res = acessDatabase(1, s);
        }catch (Exception e){
            
        }
                
        return res;
    }
    
    // returns the list of events in a given date
    public String getProva(String d) throws java.rmi.RemoteException{
        System.err.println("Invocacao de getProva() - Tentativa de consulta de provas para uma data.");        
        String lista = " ";
        try{
            lista = acessDatabase(2, d);
        }catch (Exception e){
            
        }
        return lista;
    }
    
    // returns the list of participants in a given event
    public String getInscritos(String d) throws java.rmi.RemoteException{
        System.err.println("Invocacao de getInscritos() - Tentativa de consulta de inscritos numa prova.");
        String lista = " ";
        try{
            lista = acessDatabase(3, d);
        }catch (Exception e){
            
        }
        return lista;
    }

    // inserts a new event in the eventos table
    public String novoEvento(String s) throws java.rmi.RemoteException{
        System.err.println("Invocacao de novoEvento() - Tentativa de registo de novo evento.");
        String res = " ";
        try{
            res = acessDatabase(4, s);
        }catch (Exception e){
            
        }
        return res;
    }
    
    // inserts a new time in the tempos table
    public String novoTempo(String s) throws java.rmi.RemoteException{
        System.err.println("Invocacao de novoTempo() - Tentativa de registo de tempo num evento.");
        String res = "";
        try{
            res = acessDatabase(5, s);
        }catch (Exception e){
            
        }
        return res;
    }
    
    // returns the leaderboard of a given event
    public String getClassificacoes(String s) throws java.rmi.RemoteException{
        System.err.println("Invocacao de getClassificacoes() - Tentativa de consulta de classificacoes.");
        String lista = "";
        try{
            lista = acessDatabase(6, s);
        }catch (Exception e){
            
        }
        return lista;
    }
    
    // returns the top 3 of a given event
    public String getTop(String s) throws java.rmi.RemoteException{
        System.err.println("Invocacao de getTop() - Tentativa de consulta do top 3 de um evento.");
        String lista = "";
        try{
            lista = acessDatabase(7, s);
        }catch (Exception e){
            
        }
        return lista;
    }
    
    // acesses the database and does all the needed operations
    public String acessDatabase(int op, String s) throws Exception{
        String host = "", db = "", user = "", pw = "";
        try (InputStream input = new FileInputStream("resources/config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);
            host = prop.getProperty("host");
            db = prop.getProperty("db");
            user = prop.getProperty("user");
            pw = prop.getProperty("password");

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        PostGresConnect pc = new PostGresConnect(host, db, user, pw);
        pc.connect();
        Statement stmt = pc.getStatement();
        String to_return  = "";
        String aux[];
        switch(op){
                case 1:
                    aux = s.split("\n");
                    String atleta = aux[0];
                    String genero = aux[1];
                    String escalao = aux[2];
                    String evento = aux[3];
                    int dorsal=0;
                    
                    try {

                        
                        ResultSet rs = stmt.executeQuery("SELECT nomeevento from eventos where nomeevento = '" + evento + "'");
                        
                        if (rs.next()){
                            rs = stmt.executeQuery("SELECT dorsal from inscricoes where nomeevento = '" + evento + "' ORDER BY dorsal DESC LIMIT 1");
                            if (rs.next()) dorsal =rs.getInt("dorsal") + 1;
                            else dorsal = 1;
                                stmt.executeUpdate("insert into inscricoes values('" + atleta + "', '" + evento+ "', '" + genero + "', '" + escalao + "', " + dorsal + ") ON CONFLICT DO NOTHING");     
                            to_return = "\nInscricao efetuada com sucesso, o seu numero de dorsal é: " + dorsal;
                        }else{   
                            to_return = "\nInscricao nao efetuada, evento nao existente";
                        }
                    } catch (Exception e) {
                      e.printStackTrace();
                    } 
                                        
                    break;
                case 2:
                    try{
                        if(s.equals("geral")){
                            ResultSet rs = stmt.executeQuery("SELECT nomeevento, tipo, dataevento from eventos");
                            
                            if(rs.next()){
                                to_return = "\nLista de eventos disponiveis:\n" + rs.getString("dataevento") + " - " + rs.getString("nomeevento") + " - " + rs.getString("tipo");
                                while(rs.next()){
                                    to_return+="\n" + rs.getString("dataevento") + " - " +  rs.getString("nomeevento") + " - " + rs.getString("tipo");
                                }

                                to_return += "\n";
                            } else{
                                to_return = "\nAinda não existem eventos marcados\n";
                            }
                        } else{   
                            ResultSet rs = stmt.executeQuery("SELECT nomeevento, tipo from eventos where dataevento = '" + s + "'");

                            if(rs.next()){
                                to_return = "\nLista de eventos disponiveis:\n" + rs.getString("nomeevento") + " - " + rs.getString("tipo");
                                while(rs.next()){
                                    to_return+="\n" + rs.getString("nomeevento") + " - " + rs.getString("tipo");
                                }

                                to_return += "\n";
                            } else{
                                to_return = "\nAinda não existem eventos marcados para a data solicitada\n";
                            }
                        }
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                    break;
                case 3:
                    
                        ResultSet rs = stmt.executeQuery("SELECT nomeevento from eventos where nomeevento = '" + s + "'");
                        
                        if (rs.next()){
                            try{
                                rs = stmt.executeQuery("SELECT nomeparticipante from inscricoes where nomeevento = '" + s + "'");

                                if(rs.next()){
                                    to_return = "\nLista de inscritos:\n" + rs.getString("nomeparticipante");
                                    while(rs.next()){
                                        to_return+="\n" + rs.getString("nomeparticipante");
                                    }
                                    
                                    to_return += "\n";
                                } else{                   
                                    to_return = "\nAinda nao existem inscritos neste evento\n";
                                }
                            } catch (Exception e) {
                              e.printStackTrace();
                              System.err.println("Problems retrieving data from db...");
                            }
                        }else{
                            to_return = "\nNão existem inscritos pois o evento ainda não existe\n";
                        }                    

                    break;
                case 4:
                    aux = s.split("\n");
                    String nome = aux[0];
                    String data = aux[1];
                    String tipo = aux[2];
                    
                    try {
                        rs = stmt.executeQuery("SELECT nomeevento from eventos where nomeevento = '" + nome + "' and  dataevento = '" + data + "'");
                        if(rs.next()){
                            to_return = "\nJá existe um evento com o mesmo nome na mesma data\n";
                            break;
                        } else{
                                stmt.executeUpdate("insert into eventos values('" + nome + "', '" + data + "', '" + tipo + "') ON CONFLICT DO NOTHING");
                            to_return = "\nEvento registado com sucesso\n";
                            break;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                case 5:
                    String gender, tier;
                    aux = s.split("\n");
                    nome = aux[0];
                    dorsal = Integer.parseInt(aux[1]);
                    int time_done = Integer.parseInt(aux[2]);
                    try{
                        rs = stmt.executeQuery("SELECT nomeevento from eventos where nomeevento = '" + nome + "'");

                        if (rs.next()){
                                    rs = stmt.executeQuery("SELECT dorsal from tempos where nomeevento = '" + nome + "' and dorsal = '" + dorsal + "'");
                                    
                                    if(rs.next()){
                                        to_return = "\nDorsal ja tem um tempo registado para a corrida\n";
                                    } else{
                                        rs = stmt.executeQuery("SELECT nomeparticipante, genero, escalao from inscricoes where nomeevento = '" + nome + "' and dorsal = '" + dorsal + "'");
                                        if(rs.next()){
                                            gender = rs.getString("genero");
                                            tier = rs.getString("escalao");
                                                stmt.executeUpdate("insert into tempos values('" + nome + "', " + dorsal + ", '" + gender + "', '" + tier + "', " + time_done + ") ON CONFLICT DO NOTHING");
                                                to_return = "\nTempo registado com sucesso\n";
                                        } else {
                                            to_return = "\nDorsal ainda não inscrito na corrida, impossivel registar tempo\n";
                                        }
                                    }
                        } else{
                            
                            to_return =  "\nEvento não existente, impossivel registar tempo\n";
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    aux = s.split("\n");
                    nome = aux[0];
                    gender = aux[1];
                    int lugar = 1;
                    if(gender.equals("geral")){
                        try{
                        rs = stmt.executeQuery("SELECT nomeevento from eventos where nomeevento = '" + nome + "'");
                        if (rs.next()){

                                to_return = "Lista de classificacoes:\nProva: " + nome + " | Genero: "+ gender+ "\n"
                                        + "Classificacoes gerais:\n\n"
                                        + "-------------------------------------------------------"
                                        + "\n";
                                
                                rs = stmt.executeQuery("SELECT tempos.dorsal, nomeparticipante, tempo "
                                        + "from tempos inner join inscricoes "
                                        + "ON (tempos.nomeevento = inscricoes.nomeevento AND tempos.dorsal = inscricoes.dorsal)"
                                        + "WHERE tempos.nomeevento= '" + nome + "'"
                                        + "ORDER BY tempo");

                                if(rs.next()){
                                    to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                    while(rs.next()){
                                        lugar++;
                                        to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                    }                                    
                                    to_return += "\n";
                                } else{                   
                                    to_return += "Ainda não existem inscritos neste evento\n\n";
                                    
                                }

                        }else{
                            to_return = "Não existem classificacoes pois o evento ainda não existe\n\n";
                        } 
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("Problems retrieving data from db...");
                        }
                    } else if(gender.equals("m")){
                        try{
                        rs = stmt.executeQuery("SELECT nomeevento from eventos where nomeevento = '" + nome + "'");
                        if (rs.next()){
                                to_return = "Lista de classificacoes:\nProva: " + nome + " | Genero: "+ gender+ "\n"
                                        + "Classificacoes gerais:\n\n"
                                        + "-------------------------------------------------------"
                                        + "\n";
                                
                                rs = stmt.executeQuery("SELECT tempos.dorsal, nomeparticipante, tempo "
                                        + "from tempos inner join inscricoes "
                                        + "ON (tempos.nomeevento = inscricoes.nomeevento AND tempos.dorsal = inscricoes.dorsal)"
                                        + "WHERE tempos.nomeevento= '" + nome + "' and tempos.genero = 'm'"
                                        + "ORDER BY tempo");

                                if(rs.next()){
                                    to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                    while(rs.next()){
                                        lugar++;
                                        to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                    }                                    
                                    to_return += "\n";
                                } else{                   
                                    to_return += "Ainda não existem inscritos masculinos neste evento\n\n";       
                                }
                        }else{
                            to_return = "Não existem classificacoes pois o evento ainda não existe\n\n";
                        } 
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else if(gender.equals("f")){
                        try{
                        rs = stmt.executeQuery("SELECT nomeevento from eventos where nomeevento = '" + nome + "'");
                        if (rs.next()){
                                to_return = "Lista de classificacoes:\nProva: " + nome + " | Genero: "+ gender+ "\n"
                                        + "Classificacoes gerais:\n\n"
                                        + "-------------------------------------------------------"
                                        + "\n";
                                
                                rs = stmt.executeQuery("SELECT tempos.dorsal, nomeparticipante, tempo "
                                        + "from tempos inner join inscricoes "
                                        + "ON (tempos.nomeevento = inscricoes.nomeevento AND tempos.dorsal = inscricoes.dorsal)"
                                        + "WHERE tempos.nomeevento= '" + nome + "' and tempos.genero = 'f'"
                                        + "ORDER BY tempo");

                                if(rs.next()){
                                    to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                    while(rs.next()){
                                        lugar++;
                                        to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                    }                                    
                                    to_return += "\n";
                                } else{                   
                                    to_return += "Ainda nao existem inscritos femininos neste evento\n\n";                                    
                                }
                        }else{
                            to_return = "Não existem classificacoes pois o evento ainda não existe\n\n";
                        }  
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else{
                        to_return = "Género Inválido";
                    }
                    break;  
                case 7:
                    aux = s.split("\n");
                    nome = aux[0];
                    gender = aux[1];
                    escalao = aux[2];
                    lugar = 1;
                    if(gender.equals("geral")){
                        try{
                        rs = stmt.executeQuery("SELECT nomeevento from eventos where nomeevento = '" + nome + "'");
                        if (rs.next()){

                                to_return = "TOP 3:\nProva: " + nome + " | Genero: "+ gender+ " | Escalao: " + escalao + "\n"
                                        + "Classificacoes gerais:\n\n"
                                        + "-------------------------------------------------------"
                                        + "\n";
                                if(escalao.equals("geral")){
                                    rs = stmt.executeQuery("SELECT tempos.dorsal, nomeparticipante, tempo "
                                            + "from tempos inner join inscricoes "
                                            + "ON (tempos.nomeevento = inscricoes.nomeevento AND tempos.dorsal = inscricoes.dorsal)"
                                            + "WHERE tempos.nomeevento= '" + nome + "' " 
                                            + "ORDER BY tempo LIMIT 3");

                                    if(rs.next()){
                                        to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                        while(rs.next()){
                                            lugar++;
                                            to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                        }                                    
                                        to_return += "\n";
                                    } else{                   
                                        to_return += "Ainda não existem inscritos neste evento para o escalao indicado\n\n";
                                    }                                    
                                } else{
                                    rs = stmt.executeQuery("SELECT tempos.dorsal, nomeparticipante, tempo "
                                            + "from tempos inner join inscricoes "
                                            + "ON (tempos.nomeevento = inscricoes.nomeevento AND tempos.dorsal = inscricoes.dorsal)"
                                            + "WHERE tempos.nomeevento= '" + nome + "' AND tempos.escalao = '" + escalao + "' " 
                                            + "ORDER BY tempo LIMIT 3");

                                    if(rs.next()){
                                        to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                        while(rs.next()){
                                            lugar++;
                                            to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                        }                                    
                                        to_return += "\n";
                                    } else{                   
                                        to_return += "Ainda não existem inscritos neste evento para o escalao indicado\n\n";
                                    }
                                }


                        }else{
                            to_return = "Não existem classificacoes pois o evento ainda não existe\n\n";
                        } 
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("Problems retrieving data from db...");
                        }
                    } else if(gender.equals("m")){
                        try{
                        rs = stmt.executeQuery("SELECT nomeevento from eventos where nomeevento = '" + nome + "'");
                        if (rs.next()){
                                to_return = "TOP 3:\nProva: " + nome + " | Genero: "+ gender+ " | Escalao: " + escalao + "\n"
                                        + "Classificacoes gerais:\n\n"
                                        + "-------------------------------------------------------"
                                        + "\n";
                                
                                                                if(escalao.equals("geral")){
                                rs = stmt.executeQuery("SELECT tempos.dorsal, nomeparticipante, tempo "
                                        + "from tempos inner join inscricoes "
                                        + "ON (tempos.nomeevento = inscricoes.nomeevento AND tempos.dorsal = inscricoes.dorsal)"
                                        + "WHERE tempos.nomeevento= '" + nome + "' AND tempos.genero = 'm'"
                                        + "ORDER BY tempo LIMIT 3");

                                    if(rs.next()){
                                        to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                        while(rs.next()){
                                            lugar++;
                                            to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                        }                                    
                                        to_return += "\n";
                                    } else{                   
                                    to_return += "Ainda não existem inscritos masculinos neste evento para o escalao indicado\n\n";  
                                    }                                    
                                } else{
                                rs = stmt.executeQuery("SELECT tempos.dorsal, nomeparticipante, tempo "
                                        + "from tempos inner join inscricoes "
                                        + "ON (tempos.nomeevento = inscricoes.nomeevento AND tempos.dorsal = inscricoes.dorsal)"
                                        + "WHERE tempos.nomeevento= '" + nome + "' AND tempos.escalao = '" + escalao + "' AND tempos.genero = 'm'"
                                        + "ORDER BY tempo LIMIT 3");

                                if(rs.next()){
                                    to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                    while(rs.next()){
                                        lugar++;
                                        to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                    }                                    
                                    to_return += "\n";
                                } else{                   
                                    to_return += "Ainda não existem inscritos masculinos neste evento para o escalao indicado\n\n";       
                                }
                                                                }
                        }else{
                            to_return = "Não existem classificacoes pois o evento ainda não existe\n\n";
                        } 
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else if(gender.equals("f")){
                        try{
                        rs = stmt.executeQuery("SELECT nomeevento from eventos where nomeevento = '" + nome + "'");
                        if (rs.next()){
                                to_return = "TOP 3:\nProva: " + nome + " | Genero: "+ gender+ " | Escalao: " + escalao + "\n"
                                        + "Classificacoes gerais:\n\n"
                                        + "-------------------------------------------------------"
                                        + "\n";

                                                                if(escalao.equals("geral")){
                                rs = stmt.executeQuery("SELECT tempos.dorsal, nomeparticipante, tempo "
                                        + "from tempos inner join inscricoes "
                                        + "ON (tempos.nomeevento = inscricoes.nomeevento AND tempos.dorsal = inscricoes.dorsal)"
                                        + "WHERE tempos.nomeevento= '" + nome + "' AND tempos.genero = 'f'"
                                        + "ORDER BY tempo LIMIT 3");

                                    if(rs.next()){
                                        to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                        while(rs.next()){
                                            lugar++;
                                            to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                        }                                    
                                        to_return += "\n";
                                    } else{                   
                                    to_return += "Ainda não existem inscritos masculinos neste evento para o escalao indicado\n\n";  
                                    }                                    
                                } else{                                
                                rs = stmt.executeQuery("SELECT tempos.dorsal, nomeparticipante, tempo "
                                        + "from tempos inner join inscricoes "
                                        + "ON (tempos.nomeevento = inscricoes.nomeevento AND tempos.dorsal = inscricoes.dorsal)"
                                        + "WHERE tempos.nomeevento= '" + nome + "'AND tempos.escalao = '" + escalao + "' AND tempos.genero = 'f'"
                                        + "ORDER BY tempo LIMIT 3");

                                if(rs.next()){
                                    to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                    while(rs.next()){
                                        lugar++;
                                        to_return += "Lugar- " + lugar +"\nNome-"  + rs.getString("nomeparticipante") +"\nDorsal-" + rs.getInt("dorsal") + "\nTempo de Prova=" + convertToHhMmSs(rs.getInt("tempo")) + " |\n-------------------------------------------------------\n"; 
                                    }                                    
                                    to_return += "\n";
                                } else{                   
                                    to_return += "Ainda nao existem inscritos femininos neste evento para o escalao indicado\n\n";                                    
                                }
                                }                                
                        }else{
                            to_return = "Não existem classificacoes pois o evento ainda não existe\n\n";
                        }  
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else{
                        to_return = "Género Inválido";
                    }
                    break;
                    
                default:
                    break;
               
        }           
        
        try {
            stmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        pc.disconnect();      
        return to_return;
    }
}
