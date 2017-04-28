package serverSide.servers;

import auxiliary.Heist;
import static auxiliary.Heist.PORT_LOG;
import auxiliary.Message;
import clientSide.com.ClientCom;
import clientSide.monitors.AssaultParty_Client;
import clientSide.monitors.ControlCollectionSite_Client;
import clientSide.monitors.Logger_Client;
import genclass.GenericIO;
import interfaces.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import serverSide.com.ClientProxy;
import serverSide.ConcentrationSite;
import serverSide.Interfaces.ConcentrationSiteInterface;
import serverSide.com.ServerCom;

public class ConcentrationSiteServer {

    /**
     * Número do port de escuta do serviço a ser prestado
     *
     * @serialField portNumb
     */

    private static final int portNumb = Heist.PORT_CS;

    /**
     * Programa principal.
     *
     * @param args
     * @throws java.net.UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
        ConcentrationSite cs;
        ConcentrationSiteInterface iConcentrationSite;
        ServerCom scon, sconi;                        // canais de comunicação
        ClientProxy cliProxy;                         // thread agente prestador do serviço

        IControlCollectionSite ccs = getCCS();
        IAssaultParty[] assparties = new AssaultParty_Client[Heist.MAX_ASSAULT_PARTIES];
        ILogger log = getLog();

        for (int i = 0; i < assparties.length; i++) {
            assparties[i] = getAp(i);
        }

        /* estabelecimento do servico */
        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público
        cs = new ConcentrationSite(ccs, assparties, log);                             // activação do serviço
        iConcentrationSite = new ConcentrationSiteInterface(cs);  // activação do interface com o serviço
        GenericIO.writelnString("CS - PORT " + portNumb);
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, iConcentrationSite);    // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }

    private static Logger_Client getLog() throws UnknownHostException {
        ClientCom con;                      // canal de comunicação
        Message inMessage, outMessage;      // mensagens trocadas

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
            GenericIO.writelnString("LOG nao retornado!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        return (Logger_Client) inMessage.getLog();
    }

    private static IControlCollectionSite getCCS() throws UnknownHostException {
        ClientCom con;                                       // canal de comunicação
        Message inMessage, outMessage;                       // mensagens trocadas

        con = new ClientCom(InetAddress.getLocalHost().getHostName(), Heist.PORT_CCS);
        while (!con.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.GETCCS);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Logger nao retornado!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        return (ControlCollectionSite_Client) inMessage.getCcs();
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
