package clientSide;

import static auxiliary.constants.Heist.*;
import static auxiliary.constants.States.*;
import auxiliary.messages.*;
import static auxiliary.messages.Message.*;
import genclass.GenericIO;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class AssaultThief extends Thread {

    private final int thiefID;
    private int status;
    private final int maxDisp;
    private int partyID;
    private int hasCanvas;

    private int nIter;
    private String serverHostName = null;
    private int serverPort;

    public AssaultThief(int thiefID, int nIter, String hostName, int serverPort) {
        super("Thief_" + thiefID);

        this.thiefID = thiefID;
        status = OUTSIDE;
        maxDisp = (int) (Math.random() * (THIEVES_MAX_DISPLACEMENT + 1 - THIEVES_MIN_DISPLACEMENT)) + THIEVES_MIN_DISPLACEMENT;
        partyID = -1;
        hasCanvas = 0;

        this.nIter = nIter;
        serverHostName = hostName;
        this.serverPort = serverPort;
    }

    /**
     *
     */
    @Override
    public void run() {
        while (amINeeded() != -1) {
            prepareExcursion();
            while (crawlIn());
            rollACanvas(getRoomID());
            reverseDirection();
            while (crawlOut());
            handCanvas();
        }
    }

    private int amINeeded() {
        partyID = -1;
        hasCanvas = -1;
        status = OUTSIDE;

        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CS);

        if (!con.open()) {
            return -1;
        }

        outMessage = new Message(AMINEEDED, thiefID, maxDisp);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        if (inMessage.getInteger() != -1) {
            System.out.println("THIEF #" + thiefID + "NEEDED");
            this.partyID = inMessage.getInteger();
            this.hasCanvas = 0;
            reportStatus();
        }
 
        return inMessage.getInteger();
    }

    private boolean prepareExcursion() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CCS);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(PREPAREEXCURSION);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        return false;
    }

    private int getDistOutsideRoom() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_AP + partyID);
        if (!con.open()) {
            return -1;
        }
        outMessage = new Message(GET_DIST_OUTSIDE);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        return inMessage.getInteger();
    }

    private boolean crawlIn() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_AP + partyID);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(CRAWL_IN, thiefID, maxDisp);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        return false;
    }

    private int getRoomID() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_AP + partyID);
        if (!con.open()) {
            return -1;
        }
        outMessage = new Message(GET_ROOM_ID);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        return inMessage.getInteger();
    }

    private boolean rollACanvas(int roomID) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_MUSEUM);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(ROLL_A_CANVAS, roomID);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        hasCanvas = inMessage.getInteger();

        return true;
    }

    private boolean reverseDirection() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_AP + partyID);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(REVERSE_DIRECTION, thiefID);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        return true;
    }

    private boolean crawlOut() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_AP + partyID);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(CRAWL_OUT, thiefID, maxDisp);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        return false;
    }

    private boolean handCanvas() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CCS);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(HAND_CANVAS, thiefID, partyID, hasCanvas);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        return true;
    }

    private boolean reportStatus() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_LOG);

        if (!con.open()) {
            return false;
        }
        outMessage = new Message(SETREP, thiefID, status, maxDisp, partyID, hasCanvas);        // pede a realização do serviço
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