package OrdThievesConcSite;

import AssaultParty.AssaultParty;
import GenRepOfInfo.MemFIFO;

/**
 *
 * @author Nuno Silva
 */
public class OrdThievesConcSite {

    private int nThieves = 0;

    private int nMax;
        private MemFIFO waitQueue;
    private boolean needed = false;
    private int neededCounter = 0;
    private int masterThiefWaitPrepareExcursion = 0;  // ??????
    private int AssaultThiefWaitPrepareAssaultParty = 0;

    // construtor
    public OrdThievesConcSite(int nThief, int nMax) {
        this.nMax = nMax;
        waitQueue = new MemFIFO(nMax);
    }

    // procedimentos do monitor 
    // @Override
    public synchronized void amINeeded() {
        while (!needed) {
            try {
                wait();
            } catch (InterruptedException ex) {
                // ERRO LOGGER
            }
        }
    }

    // wait do AssaultThief até ser feito prepareAssaultParty pelo MasterThief
    // @Override
    public synchronized void waitPrepareAssaultParty() {
        while (AssaultThiefWaitPrepareAssaultParty != 0) {
            try {
                wait();
            } catch (InterruptedException ex) {
                // ERRO LOGGER
            }
        }
    }

    // MasterThief acorda AssaultThief
    // @Override
    public synchronized void prepareAssaultParty() {
        needed = true;
        notifyAll();
    }

    // masterchief espera pelos assault thiefs
    // @Override
    public synchronized void waitForPrepareExcursion() {
        this.masterThiefWaitPrepareExcursion++;

        while ((this.masterThiefWaitPrepareExcursion != 0)) {
            try {
                wait();
            } catch (InterruptedException ex) {
                // ERRO LOGGER
            }
        }
    }

    // chefe prepara excursao
    //@Override
    public synchronized AssaultParty prepareExcursion() {

        try {
            wait();
        } catch (InterruptedException ex) {
            // ERRO DE LOGGER 
        }

        AssaultParty ass = new AssaultParty(nMax);
        this.masterThiefWaitPrepareExcursion--;
        notifyAll();

        return ass;
    }

}
