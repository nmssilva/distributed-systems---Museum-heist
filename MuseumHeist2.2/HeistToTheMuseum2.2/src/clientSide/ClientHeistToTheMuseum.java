package clientSide;

import static auxiliary.constants.Heist.*;
import auxiliary.messages.Message;
import genclass.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Este tipo de dados simula uma solução do lado do cliente do Problema dos
 * Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro. A
 * comunicação baseia-se em passagem de mensagens sobre sockets usando o
 * protocolo TCP.
 */
public class ClientHeistToTheMuseum {

    /**
     * Programa principal.
     * @param args
     */
    public static void main(String[] args) throws UnknownHostException {
        int nIter;                                           // número de iterações do ciclo de vida dos clientes
        String fName;                                        // nome do ficheiro de logging
        String serverHostName = null;                        // nome do sistema computacional onde está o servidor
        int serverPortNumb;                                  // número do port de escuta do servidor

        /* Obtenção dos parâmetros do problema */
        GenericIO.writelnString("\n" + "      Heist to the Museum\n");
        GenericIO.writeString("Numero de iterações? ");
        nIter = 1;
        GenericIO.writeString("Nome do ficheiro de logging? ");
        fName = "teste";
        GenericIO.writeString("Nome do sistema computacional onde está o servidor? ");
        serverHostName = InetAddress.getLocalHost().getHostName();

        /* Criação dos threads barbeiro e cliente */
        MasterThief mthief = new MasterThief(serverHostName);
        AssaultThief thiefs[] = new AssaultThief[THIEVES_NUMBER];

        for (int i = 0; i < THIEVES_NUMBER; i++) {
            thiefs[i] = new AssaultThief(i, nIter, serverHostName, PORT_CS + i);
        }

        /* Comunicação ao servidor dos parâmetros do problema */
        ClientCom con;                                       // canal de comunicação
        Message inMessage, outMessage;                       // mensagens trocadas

        con = new ClientCom(serverHostName, PORT_LOG);
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
            GenericIO.writelnString("O cliente " + i + " terminou.");
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

        GenericIO.writelnString();
    }
}
