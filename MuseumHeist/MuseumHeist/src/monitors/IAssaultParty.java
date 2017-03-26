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
public interface IAssaultParty {
    
    /**
     *
     * @return Assault Party ID
     */
    public int getId();
    
    /**
     *
     * @return returns true is Assault PArty is free. false if otherwise;
     */
    public boolean isFree();

    /**
     *
     * @return array of thieves in party
     */
    public Thief[] getThieves();
    
    /**
     *
     * @param thieves array of thieves to be set
     */
    public void setThieves(Thief[] thieves);
    
    /**
     *
     @param free true to set Assault party free (not used) or false if not
     * free (with thieves)
     */
    public void setFree(boolean free);

    /**
     * Crawl in function
     */
    public void crawlIn();
    
    /**
     *
     * @return gets Room where Assault Party is going
     */
    public IRoom getRoom();

    /**
     *
     * @param room sets Room where Assault Party is going
     */
    public void setRoom(IRoom room);

    /**
     *
     * @return true if room has painting, false otherwise
     */
    public boolean rollACanvas();

    /**
     * function to transit state to crawling outwards
     */
    public void reverseDirection();

    /**
     * crawl out function
     */
    public void crawlOut();

}
