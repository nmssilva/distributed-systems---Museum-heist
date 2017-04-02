/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitors;

import entities.Thief;

/**
 *
 * @author Nuno Silva
 */
public interface IOrdThievesConcSite {

    /**
     *
     * @return gets number of thieves in concentration site
     */
    public int getnAssaultThievesCs();

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
    public void setnAssaultThievesCs(int nAssaultThievesCs);
    
    /**
     *
     * @return gets array of thieves in concentration site
     */
    public boolean[] getThievesInCs();
    
    /**
     *
     * @return gets array of thieves
     */
    public Thief[] getThieves();

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
     *
     * @return true if all thieves finished the heist. False if otherwise
     */
    public boolean checkThievesEndHeist();
    
    /**
     * Thief informs that he ended the heist
     */
    public void heistComplete();
    
    
}
