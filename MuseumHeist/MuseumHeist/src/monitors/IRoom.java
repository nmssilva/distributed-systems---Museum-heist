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
    
    public int getDistance() ;
    
    public int getNPaintings();
    
    public void setnPaintings(int nPaintings);
    
    public void setFree(boolean f);
    
    public int getId();
    
}
