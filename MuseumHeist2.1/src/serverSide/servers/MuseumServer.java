package serverSide.servers;

import static auxiliary.Heist.PORT_LOG;
import auxiliary.Message;
import clientSide.com.ClientCom;
import clientSide.monitors.Logger_Client;
import genclass.GenericIO;
import interfaces.ILogger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import serverSide.com.ClientProxy;
import serverSide.Interfaces.MuseumInterface;
import serverSide.*;
import serverSide.com.ServerCom;

public class MuseumServer {

    /**
     * Número do port de escuta do serviço a ser prestado
     *
     * @serialField portNumb
     */
    private static final int portNumb = 4005;

    /**
     * Programa principal.
     *
     * @param args
     * @throws java.net.UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
        Museum museum;
        MuseumInterface iMuseum;
        ServerCom scon, sconi;                          // canais de comunicação
        ClientProxy cliProxy;                           // thread agente prestador do serviço
        
        ILogger log = getLog();

        /* estabelecimento do servico */
        scon = new ServerCom(portNumb);                 // criação do canal de escuta e sua associação
        scon.start();                                   // com o endereço público
        museum = new Museum(log);                          // activação do serviço
        iMuseum = new MuseumInterface(museum);          // activação do interface com o serviço
        
        GenericIO.writelnString("MUSEUM - PORT " + portNumb);
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, iMuseum);    // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }

    private static Logger_Client getLog() throws UnknownHostException {
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
            GenericIO.writelnString("LOG nao retornado!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        return (Logger_Client) inMessage.getLog();
    }
}
