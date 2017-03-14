/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Museum;

import Room.Room;

/**
 *
 * @author Nuno Silva
 * @author Pedro Coelho
 * 
 */
public class Museum {
    
    private Room[] rooms;     /* array de rooms que o museu tem */

    public Museum(int nrooms) {
        this.rooms = new Room [nrooms];
    }
    
    /**
    *  Operação de rollCanvas
    *
    *    @param roomId identificação da sala
    *
    *    @return <li> true, se o thief for necessário
    *            <li> false, em caso contrário
    */

    public synchronized boolean rollACanvas (int roomId)
    {
        if(this.rooms[roomId].removePainting())
            return true;
        else
            return false;
    }
    
}
