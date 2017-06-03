package clientSide;

import auxiliary.Heist;
import static auxiliary.States.*;
import auxiliary.VectorTimestamp;
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

    private VectorTimestamp myClock;
    private VectorTimestamp receivedClock;

    public MasterThief(interfaces.LogIMasterThief log, interfaces.CSIMasterThief cs, interfaces.CCSIMasterThief ccs) {
        status = PLANNING_THE_HEIST;
        this.log = log;
        this.cs = cs;
        this.ccs = ccs;

        this.myClock = new VectorTimestamp(Heist.THIEVES_NUMBER + 1, 0);
    }

    /**
     * Run thread
     */
    @Override
    public void run() {
        try {
            this.log.reportInitialStatus();
        } catch (RemoteException ex) {
            Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean heistend = false;
        try {
            
            this.myClock.increment();
            startOfOperations();
            this.myClock.update(this.receivedClock);

        } catch (RemoteException ex) {
            Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (!heistend) {
            try {
                switch (appraiseSit(getnAssaultThievesCS())) {
                    case 1:
                        this.myClock.increment();
                        prepareAssaultParty();
                        this.myClock.update(this.receivedClock);

                        this.myClock.increment();
                        sendAssaultParty();
                        this.myClock.update(this.receivedClock);
                        break;
                    case 0:
                        this.myClock.increment();
                        takeARest();
                        this.myClock.update(this.receivedClock);

                        this.myClock.increment();
                        collectCanvas();
                        this.myClock.update(this.receivedClock);
                        break;
                    case 2:
                        this.myClock.increment();
                        sumUpResults();
                        this.myClock.update(this.receivedClock);

                        heistend = true;
                        break;
                }
            } catch (RemoteException ex) {
                Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            this.log.reportFinalStatus();
        } catch (RemoteException ex) {
            Logger.getLogger(MasterThief.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startOfOperations() throws RemoteException {

        this.receivedClock = this.cs.startOfOperations(this.myClock.clone());

        this.status = DECIDING_WHAT_TO_DO;
        //logSetMasterState();
        this.receivedClock = this.log.setMasterThief(DECIDING_WHAT_TO_DO, this.myClock.clone());

    }

    private int getnAssaultThievesCS() throws RemoteException {

        this.receivedClock = this.cs.getnAssaultThievesCS(this.myClock.clone());

        return receivedClock.getInteger();
    }

    private int appraiseSit(int nAssaultThievesCS) throws RemoteException {

        this.receivedClock = this.ccs.appraiseSit(nAssaultThievesCS, this.myClock.clone());

        int value = this.receivedClock.getInteger();

        this.status = DECIDING_WHAT_TO_DO;

        //logSetMasterState();
        this.receivedClock = this.log.setMasterThief(DECIDING_WHAT_TO_DO, this.myClock.clone());

        return value;
    }

    private void prepareAssaultParty() throws RemoteException {

        this.receivedClock = this.cs.prepareAssaultParty(this.myClock.clone());

        this.status = ASSEMBLING_A_GROUP;

        this.receivedClock = this.log.setMasterThief(ASSEMBLING_A_GROUP, this.myClock.clone());
    }

    private void sendAssaultParty() throws RemoteException {

        this.receivedClock = this.ccs.sendAssaultParty(this.myClock.clone());

        this.status = DECIDING_WHAT_TO_DO;

        //logSetMasterState();
        this.receivedClock = this.log.setMasterThief(DECIDING_WHAT_TO_DO, this.myClock.clone());

    }

    private void takeARest() throws RemoteException {

        this.receivedClock = this.ccs.takeARest(this.myClock.clone());

        this.status = WAITING_FOR_ARRIVAL;

        //logSetMasterState();
        this.receivedClock = this.log.setMasterThief(WAITING_FOR_ARRIVAL, this.myClock.clone());

    }

    private void collectCanvas() throws RemoteException {

        this.receivedClock = this.ccs.collectCanvas(this.myClock.clone());

        this.status = DECIDING_WHAT_TO_DO;

        //logSetMasterState();
        this.receivedClock = this.log.setMasterThief(DECIDING_WHAT_TO_DO, this.myClock.clone());

    }

    private void sumUpResults() throws RemoteException {
        this.receivedClock = this.ccs.sumUpResults(this.myClock.clone());

        this.status = PRESENTING_REPORT;

        //logSetMasterState();
        this.receivedClock = this.log.setMasterThief(PRESENTING_REPORT, this.myClock.clone());

    }
}
