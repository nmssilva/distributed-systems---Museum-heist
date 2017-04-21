package clientSide;

import auxiliary.Message;
import static auxiliary.States.*;
import genclass.GenericIO;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class MasterThief extends Thread {

    private int status;

    /*private final IControlCollectionSite ccs;
    private final IConcentrationSite cs;*/
    
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
     * @param hostName
     * @param port
     */
    public MasterThief(String hostName, int port) {
        status = PLANNING_THE_HEIST;

        /*this.ccs = ccs;
        this.cs = cs;*/
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *
     */
    @Override
    public void run() {
        boolean heistend = false;
        startOfOperations();
        while (!heistend) {
            switch (appraiseSit(getnAssaultThievesCS())) {
                case 1: // prepareAssaultParty()
                    prepareAssaultParty();
                    sendAssaultParty();
                    break;
                case 0: // takeARest()
                    takeARest();
                    collectCanvas();
                    break;
                case 2: // sumUpResults()
                    sumUpResults();
                    heistend = true;
                    break;
            }
        }

    }

    /**
     * Sets Master Thief State
     *
     * @param status Next status of master thief
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     *
     * @return Returns status of MasterThief
     */
    public int getStatus() {
        return status;
    }

    /**
     * Alertar o masterthief do fim de operações (solicitação do serviço).
     */
    public void sendInterrupt() {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.ENDOP);   // alertar masterThief do fim de operações
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    private void startOfOperations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int appraiseSit(int nThieves) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int getnAssaultThievesCS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void prepareAssaultParty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void sendAssaultParty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void takeARest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void collectCanvas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void sumUpResults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
