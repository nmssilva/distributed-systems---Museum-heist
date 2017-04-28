package serverSide.servers;

import auxiliary.Heist;
import static auxiliary.Heist.PORT_LOG;
import auxiliary.Message;
import clientSide.com.ClientCom;
import clientSide.monitors.AssaultParty_Client;
import clientSide.monitors.Logger_Client;
import genclass.GenericIO;
import interfaces.IAssaultParty;
import interfaces.ILogger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import serverSide.com.ClientProxy;
import serverSide.ControlCollectionSite;
import serverSide.Interfaces.ControlCollectionSiteInterface;
import serverSide.com.ServerCom;

public class ControlCollectionSiteServer {

    /**
     * Número do port de escuta do serviço a ser prestado
     *
     * @serialField portNumb
     */
    private static final int portNumb = Heist.PORT_CCS;

    /**
     * Programa principal.
     *
     * @param args
     * @throws java.net.UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
        ControlCollectionSite ccs;
        ControlCollectionSiteInterface iControlCollectionSite;
        ServerCom scon, sconi;                        // canais de comunicação
        ClientProxy cliProxy;                         // thread agente prestador do serviço
        
        IAssaultParty[] parties = new AssaultParty_Client[Heist.MAX_ASSAULT_PARTIES];
        ILogger log = getLog();

        for(int i = 0; i < Heist.MAX_ASSAULT_PARTIES;i++){
            parties[i]=getAp(i);
        }
        
        
        /* estabelecimento do servico */
        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público
        ccs = new ControlCollectionSite(parties, log);                  // activação do serviço
        iControlCollectionSite = new ControlCollectionSiteInterface(ccs);  // activação do interface com o serviço

        GenericIO.writelnString("CCS - PORT " + portNumb);
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, iControlCollectionSite);    // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }

    private static ILogger getLog() throws UnknownHostException{
        ClientCom con;                                       // canal de comunicação
        Message inMessage, outMessage;                       // mensagens trocadas

        con = new ClientCom(InetAddress.getLocalHost().getHostName(), PORT_LOG);
        while (!con.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.GETLOG);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Logger nao retornado!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        return (Logger_Client) inMessage.getLog();
    }

    private static IAssaultParty getAp(int id) throws UnknownHostException {
        ClientCom con;                                       // canal de comunicação
        Message inMessage, outMessage;                       // mensagens trocadas

        con = new ClientCom(InetAddress.getLocalHost().getHostName(), Heist.PORT_AP + id);
        while (!con.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.GETAP);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Logger nao retornado!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        return (AssaultParty_Client) inMessage.getAp();
    }
}
