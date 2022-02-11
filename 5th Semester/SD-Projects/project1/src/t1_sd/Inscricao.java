package t1_sd;

/**
 * remote interface
 * @author joao rouxinol & andre rato
 */
public interface Inscricao extends java.rmi.Remote {

    public String novaInscricao(String s) throws java.rmi.RemoteException;
    
    public String getProva(String d) throws java.rmi.RemoteException;
    
    public String getInscritos(String d) throws java.rmi.RemoteException;
    
    public String novoEvento(String s) throws java.rmi.RemoteException;
    
    public String novoTempo(String s) throws java.rmi.RemoteException;

    public String getClassificacoes(String s) throws java.rmi.RemoteException;
    
    public String getTop(String s) throws java.rmi.RemoteException;

}
