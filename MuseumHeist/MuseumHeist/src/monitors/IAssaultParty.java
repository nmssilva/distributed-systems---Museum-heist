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
    
    public int getId();
    
    public boolean isFree();

    public Thief[] getThieves();
    
    public void setThieves(Thief[] thieves);
    
    public void setFree(boolean free);

    public void crawlIn();
    
    public IRoom getRoom();

    public void setRoom(IRoom room);

    public boolean rollACanvas();

    public void reverseDirection();

    public void crawlOut();

}
