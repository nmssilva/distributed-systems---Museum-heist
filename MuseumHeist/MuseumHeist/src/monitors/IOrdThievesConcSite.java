/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitors;

import entities.Thief;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Nuno Silva
 */
public interface IOrdThievesConcSite {

    /**
     *
     * @return gets number of thieves in concentration site
     */
    public AtomicInteger getnAssaultThievesCs();

    /**
     *
     * @param i Thief ID
     * @return true if thief is free, false if otherwise
     */
    public boolean getFreeAssaultThief(int i);

    /**
     *
     * @param i thief ID
     */
    public void callAssaultThief(int i);
    
    /**
     *
     * @param nAssaultThievesCs number of thieves in concentration site to be set
     */
    public void setnAssaultThievesCs(AtomicInteger nAssaultThievesCs);
    
    /**
     *
     * @return gets array of thieves in concentration site
     */
    public Thief[] getThievesInCs();

    /**
     * Thief waits to be needed
     */
    public void amINeeded();

    /**
     * MasterThief waits for Thieves Arrival
     */
    public void waitForArrival();

    /**
     * transits thief state to CRAWLING_INWARDS
     */
    public void prepareExcursion();
    
    /**
     * adds a thief to concentration site
     */
    public void addThieveToCS();
    
    /**
     * removes a thief to concentration site
     */
    public void removeThieveToCS();
    
}
