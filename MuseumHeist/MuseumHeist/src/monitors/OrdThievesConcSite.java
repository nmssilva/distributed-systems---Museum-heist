/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitors;

import static GenRepOfInfo.Heist.*;
import entities.Thief;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Silva
 */
public class OrdThievesConcSite implements IOrdThievesConcSite {

    private AtomicInteger nAssaultThievesCs;
    private Thief[] thievesInCs;
    private boolean incrementing;

    public OrdThievesConcSite(Thief[] thievesInCs) {
        this.incrementing = false;
        this.nAssaultThievesCs = new AtomicInteger(0);
        this.thievesInCs = thievesInCs;
    }

    @Override
    public synchronized AtomicInteger getnAssaultThievesCs() {
        return nAssaultThievesCs;
    }

    @Override
    public synchronized void setnAssaultThievesCs(AtomicInteger nAssaultThievesCs) {
        this.nAssaultThievesCs = nAssaultThievesCs;
    }

    @Override
    public Thief[] getThievesInCs() {
        return thievesInCs;
    }

    public void setThievesInCs(Thief[] thieves) {
        this.thievesInCs = thieves;
    }

    @Override
    public boolean getFreeAssaultThief(int thiefid) {
        return thievesInCs[thiefid].isFree();
    }

    @Override
    public synchronized void callAssaultThief(int thiefid) {

        this.thievesInCs[thiefid].setFree(false);
        this.thievesInCs[thiefid] = null;
        notifyAll();

    }

    @Override
    public synchronized void waitForArrival() {
        notifyAll();
        while (this.nAssaultThievesCs.get() < MAX_ASSAULT_PARTY_THIEVES) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(OrdThievesConcSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void amINeeded() {
        Thief thief = (Thief) Thread.currentThread();

        this.nAssaultThievesCs.incrementAndGet();
        this.thievesInCs[thief.getThiefid()] = thief;

        notifyAll();
        while (thief.isFree()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(OrdThievesConcSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void prepareExcursion() {
        Thief thief = (Thief) Thread.currentThread();
        thief.setState(CRAWLING_INWARDS);
        notifyAll();
    }

    @Override
    public synchronized void addThieveToCS() {
        
        while(incrementing){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(OrdThievesConcSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        this.incrementing = true;
        this.nAssaultThievesCs.set(this.nAssaultThievesCs.get()+1);
        this.incrementing = false;
    }
    
    @Override
    public synchronized void removeThieveToCS() {
        
        while(incrementing){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(OrdThievesConcSite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        this.incrementing = true;
        this.nAssaultThievesCs.set(this.nAssaultThievesCs.get()-1);
        this.incrementing = false;
    }

}
