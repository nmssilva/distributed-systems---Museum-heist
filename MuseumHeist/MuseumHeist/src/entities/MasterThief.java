package entities;

import static auxiliary.States.*;
import interfaces.*;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class MasterThief extends Thread {

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
}
