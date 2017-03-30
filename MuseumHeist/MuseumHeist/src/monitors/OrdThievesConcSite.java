/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitors;

import static GenRepOfInfo.Heist.*;
import entities.Thief;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Silva
 */
public class OrdThievesConcSite implements IOrdThievesConcSite {

    private int nAssaultThievesCs;
    private Thief[] thieves = new Thief[THIEVES_NUMBER];
    private boolean[] thievesInCs = new boolean[THIEVES_NUMBER];

    /**
     * Constructor
     * @param thieves array of thieves in Concentration Site
     */
    public OrdThievesConcSite(Thief[] thieves) {
        this.nAssaultThievesCs = 0;
        System.arraycopy(thieves, 0, this.thieves, 0, THIEVES_NUMBER);
    }

    @Override
    public boolean[] getThievesInCs() {
        return thievesInCs;
    }

    public void setThievesInCs(int thiefid, boolean f) {
        this.thievesInCs[thiefid] = f;
    }

    
    
    /**
     *
     * @return gets number of thieves in concentration site
     */
    @Override
    public synchronized int getnAssaultThievesCs() {
        return nAssaultThievesCs;
    }

    /**
     *
     * @param nAssaultThievesCs number of thieves in concentration site to be set
     */
    @Override
    public synchronized void setnAssaultThievesCs(int nAssaultThievesCs) {
        this.nAssaultThievesCs = nAssaultThievesCs;
    }

    /**
     *
     * @return gets array of thieves
     */

    @Override
    public Thief[] getThieves() {
        return thieves;
    }

    /**
     *
     * @param thieves array of thieves to be set
     */
    public void setThieves(Thief[] thieves) {
        this.thieves = thieves;
    }

    /**
     *
     * @param thiefid Thief ID
     * @return true if thief is free, false if otherwise
     */
    @Override
    public boolean getFreeAssaultThief(int thiefid) {
        return thieves[thiefid].isFree();
    }

    /**
     *
     * @param thiefid thief ID
     */
    @Override
    public synchronized void callAssaultThief(int thiefid) {
        this.thievesInCs[thiefid] = false;
        this.thieves[thiefid].setFree(false);
        notifyAll();

    }

    /**
     * MasterThief waits for Thieves Arrival
     */
    @Override
    public synchronized void waitForArrival() {
        notifyAll();
        while (this.nAssaultThievesCs < MAX_ASSAULT_PARTY_THIEVES) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(OrdThievesConcSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Thief waits to be needed
     */
    @Override
    public synchronized void amINeeded() {
        Thief thief = (Thief) Thread.currentThread();

        this.nAssaultThievesCs++;
        this.thievesInCs[thief.getThiefid()] = true;

        notifyAll();
        while (thief.isFree()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(OrdThievesConcSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * transits thief state to CRAWLING_INWARDS
     */
    @Override
    public synchronized void prepareExcursion() {
        Thief thief = (Thief) Thread.currentThread();
        thief.setState(CRAWLING_INWARDS);
        notifyAll();
    }
}
