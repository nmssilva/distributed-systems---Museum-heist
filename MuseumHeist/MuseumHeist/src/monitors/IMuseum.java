/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitors;

/**
 *
 * @author Nuno Silva
 */
public interface IMuseum {

    /**
     *
     * @return gets boolean array. true room is free, false if not free.
     */
    public boolean[] getFreeRooms();
    
    /**
     *
     * @return Rooms
     */
    public Room[] getRooms();
    
    /**
     *
     * @return gets int array with number of paintings of all rooms
     */
    public int[] getNPaintingsRoom();
}
