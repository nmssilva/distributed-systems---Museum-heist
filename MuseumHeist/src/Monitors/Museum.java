/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import static GenRepOfInfo.Heist.*;

/**
 *
 * @author Nuno Silva
 * @author Pedro Coelho
 *
 */
public class Museum implements IMuseum {

    private final Room[] rooms = new Room[ROOMS_NUMBER];

    public Museum() {

        for (int i = 0; i < ROOMS_NUMBER; i++) {
            this.rooms[i] = new Room(i);
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
        System.out.println("Room " + nRoom + " has got " + nPaintings);
        if (nPaintings > 0) {
            rooms[nRoom].setnPaintings(nPaintings - 1);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Room nextRoom() {
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            System.out.println("III: " + i);
            if (this.rooms[i].getNPaintings() != 0) {
                if (this.rooms[i].isFree()) {
                    this.rooms[i].setFree(false);
                    System.out.println("RETURNING: " + i);
                    return this.rooms[i];
                }
            } else {
                this.rooms[i].setFree(false);
            }
        }

        return null;
    }

    public synchronized int getNPaintings(int nRoom) {
        return this.rooms[nRoom].getNPaintings();
    }

    public Room[] getRooms() {
        return rooms;
    }

}
