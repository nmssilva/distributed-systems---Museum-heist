/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Museum;

import static GenRepOfInfo.Heist.*;
import Room.Room;

/**
 *
 * @author Nuno Silva
 * @author Pedro Coelho
 *
 */
public class Museum implements IMuseum {

    private final Room[] rooms = new Room[ROOMS_NUMBER];

    public Museum() {
        int nPaintings;
        int distOutside;

        for (int i = 0; i < ROOMS_NUMBER; i++) {
            nPaintings = (int) (Math.random() * (MAX_PAINTINGS - MIN_PAINTINGS) + 1) + MIN_PAINTINGS;
            distOutside = (int) (Math.random() * (MAX_DIST_OUTSIDE - MIN_DIST_OUTSIDE) + 1) + MIN_DIST_OUTSIDE;
            this.rooms[i] = new Room(distOutside, nPaintings, i);
        }
    }

    /**
     *
     * @param nRoom Number of the room of the museum
     * @return
     */
    @Override
    public synchronized boolean rollACanvas(int nRoom) {
        int nPaintings = rooms[nRoom].getNPaintings();
        if (nPaintings > 0) {
            rooms[nRoom].setnPaintings(nPaintings - 1);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int nextRoom() {
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            if (this.rooms[i].getNPaintings() != 0) {
                return this.rooms[i].getId();
            }
        }

        return -1;
    }

    public synchronized int getDistOutside(int nRoom) {
        return this.rooms[nRoom].getDistance();
    }

    public synchronized int getNPaintings(int nRoom) {
        return this.rooms[nRoom].getNPaintings();
    }
    
    public Room[] getRooms() {
        return rooms;
    }

}