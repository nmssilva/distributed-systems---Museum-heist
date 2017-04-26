package clientSide;

import clientSide.com.ClientCom;
import static auxiliary.Heist.*;
import auxiliary.Message;
import static auxiliary.Message.*;
import static auxiliary.States.*;
import genclass.GenericIO;
import java.io.Serializable;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class AssaultThief extends Thread implements Serializable {

    /**
     * Chave de serialização
     *
     * @serialField serialVersionUID
     */
    private static final long serialVersionUID = 1002L;

    private final int thiefID;
    private int status;
    private final int maxDisp;
    private int partyID;
    private int position;
    private int hasCanvas;

    /*private final IConcentrationSite cs;
    private final IControlCollectionSite ccs;
    private final IAssaultParty[] assparties;
    private final IMuseum museum;*/
    /**
     * Nome do sistema computacional onde está localizado o servidor
     *
     * @serialField serverHostName
     */
    private String serverHostName = null;

    /**
     * Número do port de escuta do servidor
     *
     * @serialField serverPortNumb
     */
    private int serverPortNumb;

    /**
     *
     * @param thiefID
     * @param hostName
     * @param port
     */
    public AssaultThief(int thiefID, String hostName, int port) {
        this.thiefID = thiefID;
        status = OUTSIDE;
        maxDisp = (int) (Math.random() * (THIEVES_MAX_DISPLACEMENT + 1 - THIEVES_MIN_DISPLACEMENT)) + THIEVES_MIN_DISPLACEMENT;
        partyID = -1;
        this.position = 0;
        hasCanvas = 0;

        /*this.cs = cs;
        this.ccs = ccs;
        this.assparties = assparties;
        this.museum = museum;*/
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *
     */
    @Override
    public void run() {
        while (amINeeded()) {
            prepareExcursion();
            while (position != getDistOutsideRoom()) {
                crawlIn();
            }
            hasCanvas = rollACanvas();
            reverseDirection();
            while (position != 0) {
                crawlOut();
            }

            handCanvas();
        }
    }

    /**
     *
     * @return Returns ID of Thief
     */
    public int getThiefID() {
        return thiefID;
    }

    /**
     * Sets status of thief
     *
     * @param status new status for thief
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     *
     * @return Returns status of thief
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @return Returns maximum displacement of thief.
     */
    public int getMaxDisp() {
        return maxDisp;
    }

    /**
     * Assign to thief an assault party
     *
     * @param partyID ID of assigned assault party
     */
    public void setPartyID(int partyID) {
        this.partyID = partyID;
    }

    /**
     *
     * @return Returns assault party ID of thief
     */
    public int getPartyID() {
        return partyID;
    }

    /**
     * Sets new position for thief
     *
     * @param position New position of thief
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     *
     * @return Returns position of thief
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets thief with canvas (1) or without canvas (0)
     *
     * @param hasCanvas
     */
    public void setHasCanvas(int hasCanvas) {
        this.hasCanvas = hasCanvas;
    }

    /**
     *
     * @return Returns 1 if thief has canvas, or 0 if otherwise
     */
    public int getHasCanvas() {
        return hasCanvas;
    }

    private boolean amINeeded() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CS);
        if (!con.open()) {
            return false;
        }
        outMessage = new Message(AMINEEDED, this);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getType() != ACK_BOOL) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            GenericIO.writelnString("Class: " + this.getClass().getName());
            GenericIO.writelnString("Linha: " + new Exception().getStackTrace()[0].getLineNumber());
            System.exit(1);
        }
        if (inMessage.getType() == ACK_BOOL) {
            this.partyID = -1;
            this.hasCanvas = 0;
            this.status = OUTSIDE;
            System.out.println("STATUS: " + this.status);
            reportStatus();
            return true;     
        }
        //con.close();

        return false;
    }

    private void prepareExcursion() {
        System.out.println("#" + thiefID + " prepareExcursion");
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_CCS);
        if (!con.open()) {
            System.out.println("Client Con Not Open");
            System.exit(1);
        }
        outMessage = new Message(PREPARE_EXCURSION, this);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            
            GenericIO.writelnString("Class: " + this.getClass().getName());
            GenericIO.writelnString("Linha: " + new Exception().getStackTrace()[0].getLineNumber());
            System.exit(1);
        }
        //con.close();
        if (inMessage.getType() == ACK) {
            System.out.println("Thief: " + thiefID
                    + "\nParty: " + partyID);
            reportStatus();
        }
    }

    private void crawlIn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int getDistOutsideRoom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int rollACanvas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void reverseDirection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void handCanvas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void crawlOut() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean reportStatus() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(serverHostName, PORT_LOG);

        if (!con.open()) {
            return false;
        }
        outMessage = new Message(REPORT_STATUS, this);        // pede a realização do serviço
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            
            GenericIO.writelnString("Class: " + this.getClass().getName());
            GenericIO.writelnString("Linha: " + new Exception().getStackTrace()[0].getLineNumber());
            System.exit(1);
        }
        //con.close();

        return inMessage.getType() == ACK;
    }

    @Override
    public String toString() {
        return "to string Thief #" + this.thiefID;
    }
}
