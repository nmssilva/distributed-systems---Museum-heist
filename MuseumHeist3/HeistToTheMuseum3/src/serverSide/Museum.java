/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

/**
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
import static auxiliary.Heist.*;
import interfaces.LoggerInterface;
import interfaces.MuseumInterface;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Museum implements MuseumInterface{ 

    private Room[] rooms;

    private LoggerInterface log;
    
    /**
     * Museum constructor 
     * @param log Logger
     */
    public Museum(LoggerInterface log) throws RemoteException {
        
        this.log = log;
        this.rooms = new Room[ROOMS_NUMBER];

        for (int i = 0; i < ROOMS_NUMBER; i++) {
            this.rooms[i] = new Room(i);
        }

        logSetMuseum();

        //System.out.println("Museum has " + getTotalPaintings() + " paintings");
    }

    /**
     *
     * @param nRoom ID of the room where the thieves are
     * @return Returns 1 if thief was successful in retrieving a canvas, 0 if
     * not
     */
    @Override
    public synchronized int rollACanvas(int nRoom) {
        int nPaintings = rooms[nRoom].getNPaintings();
        if (nPaintings > 0) {
            rooms[nRoom].setnPaintings(nPaintings - 1);
            System.out.println("REMAINING CANVAS: " + getTotalPaintings());
            try {
                logSetMuseum();
            } catch (RemoteException ex) {
                Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 1;
        } else {
            try {
                logSetMuseum();
            } catch (RemoteException ex) {
                Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }
    }

    /**
     *
     * @param roomID ID of a room
     * @return Returns the room with the given ID
     */
    @Override
    public Room getRoom(int roomID) {
        return rooms[roomID];
    }

    private int getTotalPaintings() {
        int count = 0;
        
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            count += rooms[i].getNPaintings();
        }

        return count;
    }

    private void logSetMuseum() throws RemoteException {
        
        this.log.setMuseum(roomsdistance(),roomspaintings());
    }

    private int[] roomsdistance() {
        int[] tmp = new int[ROOMS_NUMBER];

        for (int i = 0; i < ROOMS_NUMBER; i++) {
            tmp[i] = rooms[i].getDistOutside();
        }

        return tmp;
    }

    private int[] roomspaintings() {
        int[] tmp = new int[ROOMS_NUMBER];

        for (int i = 0; i < ROOMS_NUMBER; i++) {
            tmp[i] = rooms[i].getNPaintings();
        }

        return tmp;
    }

    @Override
    public int getDistRoom(int roomID) {
        return rooms[roomID].distOutside;
    }

}
