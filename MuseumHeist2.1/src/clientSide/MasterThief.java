package clientSide;

import static auxiliary.Heist.*;
import auxiliary.Message;
import static auxiliary.States.*;
import clientSide.com.ClientCom;
import genclass.GenericIO;
import interfaces.*;
import java.io.Serializable;
import static java.lang.Thread.sleep;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class MasterThief extends Thread implements Serializable{

    private static final long serialVersionUID = 1003L;
    
    private int status;

    private final IControlCollectionSite ccs;
    private final IConcentrationSite cs;

    /**
     *
     * @param ccs Control Collection Site
     * @param cs Concentration Site
     */
    public MasterThief(IControlCollectionSite ccs, IConcentrationSite cs) {
        status = PLANNING_THE_HEIST;

        this.ccs = ccs;
        this.cs = cs;
    }

    /**
     *
     */
    @Override
    public void run() {
        boolean heistend = false;
        cs.startOfOperations();
        while (!heistend) {
            switch (ccs.appraiseSit(cs.getnAssaultThievesCS())) {
                case 1: // prepareAssaultParty()
                    cs.prepareAssaultParty();
                    ccs.sendAssaultParty();
                    break;
                case 0: // takeARest()
                    ccs.takeARest();
                    ccs.collectCanvas();
                    break;
                case 2: // sumUpResults()
                    ccs.sumUpResults();
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
        ClientCom con = new ClientCom("ROG", PORT_LOG);
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

            GenericIO.writelnString("Class: " + this.getClass().getName());
            GenericIO.writelnString("Linha: " + new Exception().getStackTrace()[0].getLineNumber());
            System.exit(1);
        }
        //con.close();
    }

}
