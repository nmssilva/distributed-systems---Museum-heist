package clientSide;

import static auxiliary.constants.Heist.*;
import static auxiliary.constants.States.*;
import auxiliary.messages.Message;
import static auxiliary.messages.Message.*;
import genclass.GenericIO;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class MasterThief extends Thread {

    private int status;

    private String serverHostName = null;
    private int serverPortNumb;

    public MasterThief(String hostName) {
        status = PLANNING_THE_HEIST;

        serverHostName = hostName;
    }

    @Override
    public void run() {
        boolean heistend = false;
        startOfOperations();
        while (!heistend) {
            switch (appraiseSit(getnAssaultThievesCS())) {
                case 1:
                    prepareAssaultParty();
                    sendAssaultParty();
                    break;
                case 0:
                    takeARest();
                    collectCanvas();
                    break;
                case 2:
                    sumUpResults();
                    heistend = true;
                    break;
            }
        }
    }

    private boolean startOfOperations() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CS);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(STARTOP);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        this.status = DECIDING_WHAT_TO_DO;
        logSetMasterState();
        
        return false;
    }

    private int getnAssaultThievesCS() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CS);

        if (!con.open()) {
            return -1;
        }
        outMessage = new Message(GET_ASSAULT_THIEVES_CS);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        //con.close();

        return inMessage.getInteger();
    }

    private int appraiseSit(int nAssaultThievesCS) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CCS);

        while (!con.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(APPRAISE_SIT, nAssaultThievesCS);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        //con.close();
        
        this.status = DECIDING_WHAT_TO_DO;
        logSetMasterState();

        return inMessage.getInteger();
    }

    private boolean prepareAssaultParty() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CS);

        if (!con.open()) {
            return false;
        }
        outMessage = new Message(PREPARE_AP);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        this.status = ASSEMBLING_A_GROUP;
        logSetMasterState();

        return false;
    }

    private boolean sendAssaultParty() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CCS);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(SENDAP);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        this.status = DECIDING_WHAT_TO_DO;
        logSetMasterState();
        
        return true;
    }

    private boolean takeARest() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CCS);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(TAKE_A_REST);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        this.status = WAITING_FOR_ARRIVAL;
        logSetMasterState();

        return true;
    }

    private boolean collectCanvas() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CCS);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(COLLECT_CANVAS);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        this.status = DECIDING_WHAT_TO_DO;
        logSetMasterState();

        return true;
    }

    private boolean sumUpResults() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CCS);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(SUM_UP_RESULTS);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        
        this.status = PRESENTING_REPORT;
        logSetMasterState();

        return true;
    }

    private void logSetMasterState() {
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

        outMessage = new Message(Message.SETMTSTATUS, this.status);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        con.close();
    }
    
    private boolean reportStatus() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_LOG);

        if (!con.open()) {
            return false;
        }
        outMessage = new Message(SETREP);        // pede a realização do serviço
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != REPDONE) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        if (inMessage.getType() == REPDONE) {
            return true;                                                // operação bem sucedida - corte efectuado
        }

        return false;
    }
}
