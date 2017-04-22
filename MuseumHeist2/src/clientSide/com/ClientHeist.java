package clientSide.com;

import static auxiliary.Heist.*;
import genclass.GenericIO;
import auxiliary.Message;
import clientSide.AssaultThief;
import clientSide.MasterThief;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClientHeist {

    /**
     * Programa principal.
     *
     * @param args parameter arguments
     */
    public static void main(String[] args) {
        AssaultThief[] assaultThief = new AssaultThief[THIEVES_NUMBER]; // array of threads thief
        MasterThief masterThief;        // Master Thief
        int nIter;                      // number of iterations of lifecycle of thieves
        String fName;                   // logging file name
        String serverHostName;   // server computational system name
        int serverPortNumb;             // server listen port number

        /* Obtenção dos parâmetros do problema */
        GenericIO.writelnString("\n" + "      Museum Heist\n");
        GenericIO.writeString("Number of Interations? ");
        nIter = 1;//GenericIO.readlnInt();

        // log file name
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
        fName = "Heistothemuseum_" + date.format(today) + ".log";

        GenericIO.writeString("Nome do sistema computacional onde está o servidor? ");
        serverHostName = "ROG";//GenericIO.readlnString();
        GenericIO.writeString("Número do port de escuta do servidor? ");
        serverPortNumb = 4000;//GenericIO.readlnInt();

        /* Criação dos threads thiefs e masterthief */
        masterThief = new MasterThief(serverHostName, serverPortNumb);

        for (int i = 0; i < THIEVES_NUMBER; i++) {
            assaultThief[i] = new AssaultThief(i, serverHostName, serverPortNumb);
        }

        /* Comunicação ao servidor dos parâmetros do problema */
        ClientCom con;                                       // canal de comunicação
        Message inMessage, outMessage;                       // mensagens trocadas

        con = new ClientCom(serverHostName, serverPortNumb);
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
        con.close();

        /* Arranque da simulação */
        masterThief.start();

        for (int i = 0; i < THIEVES_NUMBER; i++) {
            assaultThief[i].start();
        }

        /* Aguardar o fim da simulação */
        GenericIO.writelnString();
        for (int i = 0; i < THIEVES_NUMBER ;i++) {
            try {
                assaultThief[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("O Thief " + i + " terminou.");
        }
        GenericIO.writelnString();
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
}
