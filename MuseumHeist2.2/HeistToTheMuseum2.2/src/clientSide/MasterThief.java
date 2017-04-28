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

        return true;
    }
}
