package clientSide.run;

import static auxiliary.constants.Heist.*;
import auxiliary.messages.Message;
import clientSide.AssaultThief;
import clientSide.ClientCom;
import clientSide.MasterThief;
import genclass.*;
import java.net.UnknownHostException;

/**
 * Client with both MasterThief and AssaultThieves
 */
public class ClientHeistToTheMuseum {

    /**
     * Programa principal.
     *
     * @param args
     */
    public static void main(String[] args) throws UnknownHostException {
        int nIter;                                           // número de iterações do ciclo de vida dos clientes
        String fName;                                        // nome do ficheiro de logging

        /* Obtenção dos parâmetros do problema */
        GenericIO.writelnString("\n" + "      Heist to the Museum\n");
        //GenericIO.writeString("Numero de iterações? ");
        nIter = N_ITER;
        //GenericIO.writeString("Nome do ficheiro de logging? ");
        fName = "logger.log";
        //GenericIO.writeString("Nome do sistema computacional onde está o servidor? ");
        //serverHostName = InetAddress.getLocalHost().getHostName();

        /* Criação dos threads barbeiro e cliente */
        MasterThief mthief = new MasterThief();
        AssaultThief thiefs[] = new AssaultThief[THIEVES_NUMBER];

        for (int i = 0; i < THIEVES_NUMBER; i++) {
            thiefs[i] = new AssaultThief(i, nIter);
        }

        /* Comunicação ao servidor dos parâmetros do problema */
        ClientCom con;                                       // canal de comunicação
        Message inMessage, outMessage;                       // mensagens trocadas

        con = new ClientCom(HOST_LOG, PORT_LOG);
        while (!con.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.SETNFIC, fName, nIter);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        con.close();

        /* Arranque da simulação */
        mthief.start();

        for (int i = 0; i < THIEVES_NUMBER; i++) {
            thiefs[i].start();
        }

        /* Aguardar o fim da simulação */
        GenericIO.writelnString();
        for (int i = 0; i < THIEVES_NUMBER; i++) {
            try {
                thiefs[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("O thief " + i + " terminou.");
        }
        GenericIO.writelnString();
        while (mthief.isAlive()) {
            //mthief.sendInterrupt();
            Thread.yield();
        }
        try {
            mthief.join();
        } catch (InterruptedException e) {
        }
        GenericIO.writelnString("O masterthief terminou.");

        GenericIO.writelnString();

        reportFinalStatus();

    }

    private static void reportFinalStatus() {
        ClientCom con;                  // canal de comunicação
        Message inMessage, outMessage;  // mensagens trocadas

        con = new ClientCom(HOST_LOG, PORT_LOG);
        while (!con.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.ENDOP);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        con.close();
    }
}
