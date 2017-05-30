package clientSide;

import static auxiliary.States.*;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class MasterThief extends Thread {

    private int status;
    private interfaces.LogIMasterThief log;
    private interfaces.CSIMasterThief cs;
    private interfaces.CCSIMasterThief ccs;

    public MasterThief(interfaces.LogIMasterThief log, interfaces.CSIMasterThief cs, interfaces.CCSIMasterThief ccs) {
        status = PLANNING_THE_HEIST;
        this.log = log;
        this.cs = cs;
        this.ccs = ccs;
    }

    @Override
    public void run() {
        boolean heistend = false;
        try {
            startOfOperations();
        } catch (RemoteException ex) {
            Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (!heistend) {
            try {
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
            } catch (RemoteException ex) {
                Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void startOfOperations() throws RemoteException {
        
        this.cs.startOfOperations();

        this.status = DECIDING_WHAT_TO_DO;
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);

    }

    private int getnAssaultThievesCS() throws RemoteException {

        return this.cs.getnAssaultThievesCS();
    }

    private int appraiseSit(int nAssaultThievesCS) throws RemoteException {

        int value = this.ccs.appraiseSit(nAssaultThievesCS);

        this.status = DECIDING_WHAT_TO_DO;
        
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);

        return value;
    }

    private void prepareAssaultParty() throws RemoteException {
        
        this.cs.prepareAssaultParty();

        this.status = ASSEMBLING_A_GROUP;
        
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);
    }

    private void sendAssaultParty() throws RemoteException {
        
        this.ccs.sendAssaultParty();

        this.status = DECIDING_WHAT_TO_DO;
        
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);
        
    }

    private void takeARest() throws RemoteException {
        
        this.ccs.takeARest();

        this.status = WAITING_FOR_ARRIVAL;
        
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);

    }

    private void collectCanvas() throws RemoteException {
        
        this.ccs.collectCanvas();

        this.status = DECIDING_WHAT_TO_DO;
        
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);

    }

    private void sumUpResults() throws RemoteException {
        this.ccs.sumUpResults();
        
        this.status = PRESENTING_REPORT;
        
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);

    }
}
