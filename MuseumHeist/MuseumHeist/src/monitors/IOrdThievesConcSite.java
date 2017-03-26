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

    public AtomicInteger getnAssaultThievesCs();

    public boolean getFreeAssaultThief(int i);

    public void callAssaultThief(int i);
    
    public void setnAssaultThievesCs(AtomicInteger nAssaultThievesCs);
    
    public Thief[] getThievesInCs();

    public void amINeeded();

    public void waitForArrival();

    public void prepareExcursion();
    
    public void addThieveToCS();
    
    public void removeThieveToCS();
    
}
