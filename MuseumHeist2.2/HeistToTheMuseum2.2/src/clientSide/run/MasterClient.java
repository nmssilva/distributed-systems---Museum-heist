package clientSide.run;

import static auxiliary.constants.Heist.*;
import auxiliary.messages.Message;
import clientSide.ClientCom;
import clientSide.MasterThief;
import genclass.*;

/**
 * Client for MasterThief
 */
public class MasterClient {

    /**
     * Programa principal.
     *
     * @param args
     */
    public static void main(String[] args) {
        int nIter;                                           // número de iterações do ciclo de vida dos clientes
        String fName;                                        // nome do ficheiro de logging

        /* Obtenção dos parâmetros do problema */
        GenericIO.writelnString("\n" + "      Heist to the Museum - Master Thief\n");
        nIter = N_ITER;
        //GenericIO.writeString("Nome do ficheiro de logging? ");
        fName = "logger.log";
        //GenericIO.writeString("Nome do sistema computacional onde está o servidor? ");
        //serverHostName = InetAddress.getLocalHost().getHostName();

        /* Criação dos threads barbeiro e cliente */
        MasterThief mthief = new MasterThief();

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
        /* Aguardar o fim da simulação */
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
