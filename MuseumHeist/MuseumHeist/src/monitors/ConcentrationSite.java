package monitors;

import entities.AssaultThief;
import entities.MasterThief;
import interfaces.ILogger;
import interfaces.IControlCollectionSite;
import interfaces.IConcentrationSite;
import interfaces.IAssaultParty;
import static auxiliary.Heist.*;
import static auxiliary.States.*;
import auxiliary.MemFIFO;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class ConcentrationSite implements IConcentrationSite {

    private MemFIFO waitQueue;                     // Wainting queue for ready assault thieves               
    private int nAssaultThievesCS;                 // Number of assault thieves in the concentration site

    // Monitors
    private IAssaultParty[] parties;
    private IControlCollectionSite ccs;
    private ILogger log;

    /**
     *
     * @param ccs Control Collection Site
     * @param parties Assault Parties
     * @param log Logger
     */
    public ConcentrationSite(IControlCollectionSite ccs, IAssaultParty[] parties, ILogger log) {
        waitQueue = new MemFIFO(THIEVES_NUMBER);
        nAssaultThievesCS = 0;

        this.parties = parties;
        this.ccs = ccs;
        this.log = log;
    }

    /**
     *
     * Master Thief blocks until the number of Assault Thieves in the
     * Concentration Site is equal to the number of total Assault Thieves of the
     * heist. The status of the Master Thief is changed to DECIDING_WHAT_TO_DO
     * in the end of the operation.
     *
     */
    @Override
    public synchronized void startOfOperations() {
        MasterThief mthief = (MasterThief) Thread.currentThread();

        log.setMasterState();
        log.reportStatus();

        while (nAssaultThievesCS != THIEVES_NUMBER) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        mthief.setStatus(DECIDING_WHAT_TO_DO);

        log.setMasterState();
        log.reportStatus();
    }

    /**
     * Resets the Assault Thief current thread, adds it's reference to the
     * waiting queue and blocks it until the Master Thief executes
     * prepareAssaultParty or the heist ends. The status of the Assault Thief
     * current thread is changed to OUTSIDE in the end of the operation.
     * @return 
     */
    @Override
    public synchronized boolean amINeeded() {
        AssaultThief thief = ((AssaultThief) Thread.currentThread());

        // Reset thief
        thief.setPartyID(-1);
        thief.setHasCanvas(0);
        thief.setStatus(OUTSIDE);

        nAssaultThievesCS++;
        waitQueue.write(thief);
        ccs.isReady();

        log.setAssaultThief();
        log.reportStatus();

        notifyAll();

        // Blocks if thief is assigned to any party
        while (!ccs.inParty()) {
            try {
                if (ccs.nextEmptyRoom() == -1 && nAssaultThievesCS == THIEVES_NUMBER) {
                    return false;
                }
                wait();
            } catch (InterruptedException e) {

            }
        }

        return true;
    }

    /**
     * Master Thief prepares and Assault Party. It adds elements to the Assault
     * Party and then assigns it a room and the turn of the element to first
     * crawl in. The status of the Master Thief is changed to ASSEMBLING_A_GROUP
     * in the end of the operation.
     */
    @Override
    public synchronized void prepareAssaultParty() {
        MasterThief mthief = (MasterThief) Thread.currentThread();

        mthief.setStatus(ASSEMBLING_A_GROUP);

        log.setMasterState();
        log.reportStatus();
        
        int partyID = ccs.getNextParty();
        
        for (int i = 0; i < MAX_ASSAULT_PARTY_THIEVES; i++) {
            parties[partyID].addThief((AssaultThief) waitQueue.read());
            nAssaultThievesCS--;
        }

        parties[partyID].setRoom(ccs.getNextRoom());
        parties[partyID].setFirstToCrawl();

        notifyAll();
    }

    /**
     * Get the number of Assault Thieves in Concentration Site.
     *
     * @return Number of Assault Thieves in Concentration Site
     */
    @Override
    public int getnAssaultThievesCS() {
        return nAssaultThievesCS;
    }
}
