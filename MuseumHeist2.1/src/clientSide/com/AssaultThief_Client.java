package clientSide.com;

import auxiliary.Heist;
import static auxiliary.Heist.*;
import genclass.GenericIO;
import auxiliary.Message;
import clientSide.AssaultThief;
import clientSide.monitors.*;
import interfaces.IAssaultParty;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class AssaultThief_Client {

    /**
     * Programa principal.
     *
     * @param args parameter arguments
     * @throws java.net.UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
        AssaultThief[] assaultThief = new AssaultThief[THIEVES_NUMBER]; // array of threads thief
        int nIter;     // number of iterations of lifecycle of thieves
        String fName;           // logging file name

        /* Obtenção dos parâmetros do problema */
        GenericIO.writelnString("\n" + "      Museum Heist\n");
        GenericIO.writelnString("Number of Interations? ");
        nIter = N_ITER;//GenericIO.readlnInt();

        /*GenericIO.writelnString("Nome do sistema computacional onde está o servidor? ");
        InetAddress addr;
        addr = InetAddress.getLocalHost();
        serverHostName = addr.getHostName();//GenericO.readlnString();
        GenericIO.writelnString("Número do port de escuta do servidor? ");
        serverPortNumb = 4000;//GenericIO.readlnInt();*/
        ConcentrationSite_Client cs = getCS();
        ControlCollectionSite_Client ccs = getCCS();
        AssaultParty_Client[] assparties = new AssaultParty_Client[MAX_ASSAULT_PARTIES];
        Museum_Client museum = getMuseum();
        
        for(int i = 0; i < MAX_ASSAULT_PARTIES; i++){
            assparties[i] = getAp(i);
        }

        /* Criação dos threads thiefs*/
        for (int i = 0; i < THIEVES_NUMBER; i++) {
            assaultThief[i] = new AssaultThief(i, cs, ccs, assparties, museum);
        }

        /* Arranque da simulação */
        for (int i = 0; i < THIEVES_NUMBER; i++) {
            assaultThief[i].start();
        }

        /* Aguardar o fim da simulação */
        GenericIO.writelnString();
        for (int i = 0; i < THIEVES_NUMBER; i++) {
            try {
                assaultThief[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("O Thief " + i + " terminou.");
        }
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

    private static Museum_Client getMuseum() throws UnknownHostException {
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
    
    private static AssaultParty_Client getAp(int id) throws UnknownHostException {
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
