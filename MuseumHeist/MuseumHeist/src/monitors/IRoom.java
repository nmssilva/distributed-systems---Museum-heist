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
public interface IRoom {
    
    /**
     *
     * @return gets distance to room
     */
    public int getDistance();
    
    /**
     *
     * @return gets number of painting remaining in room
     */
    public int getNPaintings();
    
    /**
     *
     * @param nPaintings sets number of paintings in room
     */
    public void setnPaintings(int nPaintings);
    
    /**
     *
     * @param f  value to set room free
     */
    public void setFree(boolean f);
    
    /**
     *
     * @return gets room ID
     */
    public int getId();
    
}
