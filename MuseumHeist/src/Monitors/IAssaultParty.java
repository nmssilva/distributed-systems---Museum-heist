/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import Entities.Thief;

/**
 *
 * @author Pedro Coelho
 * @author Nuno Silva
 */
public interface IAssaultParty {
    
    public Room getRoom();

    public void crawlIn();

    public boolean rollACanvas();

    public void crawlOut(Thief aThis);

    public void reverseDirection();
    
    public void setRoom(Room room);
    
    public int getDistOutsideRoom();
    
}
