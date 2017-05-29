package clientSide;

import static auxiliary.States.*;

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

    private void startOfOperations() {
        
        this.cs.startOfOperations();

        this.status = DECIDING_WHAT_TO_DO;
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);

    }

    private int getnAssaultThievesCS() {

        return this.cs.getnAssaultThievesCS();
    }

    private int appraiseSit(int nAssaultThievesCS) {

        int value = this.ccs.appraiseSit(nAssaultThievesCS);

        this.status = DECIDING_WHAT_TO_DO;
        
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);

        return value;
    }

    private void prepareAssaultParty() {
        
        this.cs.prepareAssaultParty();

        this.status = ASSEMBLING_A_GROUP;
        
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);
    }

    private void sendAssaultParty() {
        
        this.ccs.sendAssaultParty();

        this.status = DECIDING_WHAT_TO_DO;
        
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);
        
    }

    private void takeARest() {
        
        this.ccs.takeARest();

        this.status = WAITING_FOR_ARRIVAL;
        
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);

    }

    private void collectCanvas() {
        
        this.ccs.collectCanvas();

        this.status = DECIDING_WHAT_TO_DO;
        
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);

    }

    private void sumUpResults() {
        this.ccs.sumUpResults();
        
        this.status = PRESENTING_REPORT;
        
        //logSetMasterState();
        this.log.setMasterThief(DECIDING_WHAT_TO_DO);

    }
}
