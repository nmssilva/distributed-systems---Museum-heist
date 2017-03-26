/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitors;

import static GenRepOfInfo.Heist.*;

/**
 *
 * @author Nuno Silva
 */
public class Museum implements IMuseum {

    private Room rooms[];

    public Museum() {
        
        this.rooms = new Room[ROOMS_NUMBER];
        
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            this.rooms[i] = new Room(i);
        }
    }

    @Override
    public boolean[] getFreeRooms() {
        boolean[] free = new boolean[ROOMS_NUMBER];
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            free[i] = this.rooms[i].isFree();
        }
        return free;
    }
    
    @Override
    public int[] getNPaintingsRoom(){
        int[] a = new int[ROOMS_NUMBER];
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            a[i] = this.rooms[i].getNPaintings();
        }
        return a;
    }

    @Override
    public Room[] getRooms() {
        return rooms;
    }

}
