package serverSide.servers;

import static auxiliary.Heist.PORT_AP;
import static auxiliary.Heist.PORT_LOG;
import static auxiliary.Heist.PORT_MUSEUM;
import auxiliary.Message;
import clientSide.com.ClientCom;
import clientSide.monitors.Logger_Client;
import clientSide.monitors.Museum_Client;
import genclass.GenericIO;
import interfaces.ILogger;
import interfaces.IMuseum;
import java.net.InetAddress;
import java.net.UnknownHostException;
import serverSide.AssaultParty;
import serverSide.com.ClientProxy;
import serverSide.Interfaces.AssaultPartyInterface;
import serverSide.com.ServerCom;

public class AssaultPartyServer {

    /**
     * Número do port de escuta do serviço a ser prestado
     *
     * @serialField portNumb
     */

    private static int apID;
    private static int portNumb;

    /**
     * Programa principal.
     *
     * @param args
     * @throws java.net.UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {

        GenericIO.writelnString("AssaultParty ID: ");
        apID = GenericIO.readlnInt();
        portNumb = PORT_AP + apID;
        
        AssaultParty ap;
        AssaultPartyInterface iAssaultParty;
        ServerCom scon, sconi;       // canais de comunicação
        ClientProxy cliProxy;        // thread agente prestador do serviço
        
        IMuseum museum = getMuseum();
        ILogger log = getLog();

        /* estabelecimento do servico */
        scon = new ServerCom(portNumb);                 // criação do canal de escuta e sua associação
        scon.start();                                   // com o endereço público
        ap = new AssaultParty(apID, museum, log);                    // activação do serviço
        iAssaultParty = new AssaultPartyInterface(ap);  // activação do interface com o serviço
        GenericIO.writelnString("AP" + apID + " - PORT " + portNumb);
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                               // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, iAssaultParty);   // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }

    private static IMuseum getMuseum() throws UnknownHostException {
        ClientCom con;                                       // canal de comunicação
        Message inMessage, outMessage;                       // mensagens trocadas

        con = new ClientCom(InetAddress.getLocalHost().getHostName(), PORT_MUSEUM);
        while (!con.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.GETMUSEUM);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Museu nao retornado!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        return (Museum_Client) inMessage.getMuseum();
    }

    private static ILogger getLog() throws UnknownHostException {
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
}
