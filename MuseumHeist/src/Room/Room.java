/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Room;

/**
 *
 * @author Nuno Silva
 * @author Pedro Coelho
 * 
 */
public class Room {
    
    private int id;         /* identificador do quarto */
    private int distance;   /* distance from the gathering site */
    private int nPaintings; /* number of paintings */    
    
    public Room(int distance, int nPaint) {
        this.distance = distance;
        this.nPaintings = nPaint;
    }

    public boolean removePainting() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
}
