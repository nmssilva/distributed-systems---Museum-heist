package clientSide.com;

import auxiliary.Heist;
import static auxiliary.Heist.*;
import genclass.GenericIO;
import auxiliary.Message;
import clientSide.MasterThief;
import clientSide.monitors.ConcentrationSite_Client;
import clientSide.monitors.ControlCollectionSite_Client;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MasterThief_Client {

    /**
     * Programa principal.
     *
     * @param args parameter arguments
     * @throws java.net.UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
        MasterThief masterThief;        // Master Thief
        int nIter;                      // number of iterations of lifecycle of thieves
        String fName;                   // logging file name
        //String serverHostName;          // server computational system name
        //int serverPortNumb;             // server listen port number
        
        ConcentrationSite_Client cs = getCS();
        ControlCollectionSite_Client ccs = getCCS();

        /* Obtenção dos parâmetros do problema */
        GenericIO.writelnString("\n" + "      Museum Heist\n");
        //GenericIO.writelnString("Number of Interations? ");
        nIter = Heist.N_ITER;//GenericIO.readlnInt();

        // log file name
        /*Date today = Calendar.getInstance().getTime();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
        fName = "Heistothemuseum_" + date.format(today) + ".log";*/
        fName = "logger.log";

        //GenericIO.writelnString("Nome do sistema computacional onde está o servidor? ");
        //InetAddress addr;
        //addr = InetAddress.getLocalHost();
        //serverHostName = addr.getHostName();//GenericIO.readlnString();
        //GenericIO.writelnString("Número do port de escuta do servidor? ");
        //serverPortNumb = 4000;//GenericIO.readlnInt();

        /* Criação dos threads thiefs e masterthief */
        masterThief = new MasterThief(ccs,cs);

        /* Comunicação ao servidor dos parâmetros do problema */
        ClientCom con;                                       // canal de comunicação
        Message inMessage, outMessage;                       // mensagens trocadas

        con = new ClientCom("ROG", PORT_LOG);
        while (!con.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.SETNFIC, fName, nIter);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.NFICDONE) {
            GenericIO.writelnString("Arranque da simulação: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());

            System.exit(1);
        }
        //con.close();

        /* Arranque da simulação */
        masterThief.start();

        /* Aguardar o fim da simulação */
        while (masterThief.isAlive()) {
            masterThief.sendInterrupt();
            Thread.yield();
        }
        try {
            masterThief.join();
        } catch (InterruptedException e) {
        }
        GenericIO.writelnString("O masterThief terminou.");
        GenericIO.writelnString();
    }

    private static ConcentrationSite_Client getCS() throws UnknownHostException {
        ClientCom con;                                       // canal de comunicação
        Message inMessage, outMessage;                       // mensagens trocadas

        con = new ClientCom(InetAddress.getLocalHost().getHostName(), PORT_CS);
        while (!con.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.GETCS);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("CS nao retornado!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        return (ConcentrationSite_Client) inMessage.getCs();
    }

    private static ControlCollectionSite_Client getCCS() throws UnknownHostException {
        ClientCom con;                                       // canal de comunicação
        Message inMessage, outMessage;                       // mensagens trocadas

        con = new ClientCom(InetAddress.getLocalHost().getHostName(), PORT_CCS);
        while (!con.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.GETCS);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("CS nao retornado!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        return (ControlCollectionSite_Client) inMessage.getCcs();
    }
}
