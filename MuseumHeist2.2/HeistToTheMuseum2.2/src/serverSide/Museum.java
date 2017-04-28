/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

/**
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */

import static auxiliary.constants.Heist.*;

/**
 *
 * @author António Mota
 * @author Marcos Pires
 */
public class Museum {

    private Room[] rooms;

    /**
     *
     */
    public Museum() {
        this.rooms = new Room[ROOMS_NUMBER];
        
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            this.rooms[i] = new Room(i);
        }
        
        System.out.println("Museum has " + getTotalPaintings() + " paintings");
    }

    /**
     *
     * @param nRoom ID of the room where the thieves are
     * @return Returns 1 if thief was successful in retrieving a canvas, 0 if
     * not
     */
    public synchronized int rollACanvas(int nRoom) {
        int nPaintings = rooms[nRoom].getNPaintings();
        if (nPaintings > 0) {
            rooms[nRoom].setnPaintings(nPaintings - 1);
            System.out.println("REMAINING CANVAS: " + getTotalPaintings());
            return 1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param roomID ID of a room
     * @return Returns the room with the given ID
     */
    public Room getRoom(int roomID) {
        return rooms[roomID];
    }
    
    public int getTotalPaintings(){
        int count = 0;

        for (int i = 0; i < ROOMS_NUMBER; i++) {
            count += rooms[i].getNPaintings();
        }
        
        return count;
    }

}
