/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Museum;

/**
 *
 * @author Nuno Silva
 * @author Pedro Coelho
 */
public interface IMuseum {

    public int nextRoom();

    public boolean rollACanvas(int roomID);

    public int getDistOutside(int roomID);
    
}
