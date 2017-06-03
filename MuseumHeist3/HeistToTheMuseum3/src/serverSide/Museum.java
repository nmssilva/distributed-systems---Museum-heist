/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

/**
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
import auxiliary.Room;
import auxiliary.Heist;
import static auxiliary.Heist.*;
import auxiliary.VectorTimestamp;
import interfaces.LoggerInterface;
import interfaces.MuseumInterface;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Museum implements MuseumInterface {

    private Room[] rooms;

    private LoggerInterface log;

    private VectorTimestamp clocks;

    /**
     * Museum constructor
     *
     * @param log Logger
     */
    public Museum(LoggerInterface log) throws RemoteException {

        this.log = log;
        this.rooms = new Room[ROOMS_NUMBER];

        this.clocks = new VectorTimestamp(Heist.THIEVES_NUMBER+1, 0);

        for (int i = 0; i < ROOMS_NUMBER; i++) {
            this.rooms[i] = new Room(i);
        }

        logSetMuseum(this.clocks.clone());

        //System.out.println("Museum has " + getTotalPaintings() + " paintings");
    }

    /**
     *
     * @param nRoom ID of the room where the thieves are
     * @param vt VectorTimestamp
     * @return Returns 1 if thief was successful in retrieving a canvas, 0 if
     * not
     */
    @Override
    public synchronized VectorTimestamp rollACanvas(int nRoom, VectorTimestamp vt) {
        this.clocks.update(vt);
        int nPaintings = rooms[nRoom].getNPaintings();
        if (nPaintings > 0) {
            rooms[nRoom].setnPaintings(nPaintings - 1);
            System.out.println("REMAINING CANVAS: " + getTotalPaintings());
            try {
                logSetMuseum(this.clocks.clone());
            } catch (RemoteException ex) {
                Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.clocks.setInteger(1);
            return this.clocks.clone();
        } else {
            try {
                logSetMuseum(this.clocks.clone());
            } catch (RemoteException ex) {
                Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.clocks.setInteger(0);
            return this.clocks.clone();
        }
    }

    /**
     *
     * @param roomID ID of a room
     * @param vt VectorTimestamp
     * @return Returns the room with the given ID
     */
    @Override
    public VectorTimestamp getRoom(int roomID, VectorTimestamp vt) {
        this.clocks.update(vt);
        this.clocks.setRoom(rooms[roomID]);
        return this.clocks.clone();
    }

    private int getTotalPaintings() {
        int count = 0;
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            count += rooms[i].getNPaintings();
        }

        return count;
    }

    private VectorTimestamp logSetMuseum(VectorTimestamp vt) throws RemoteException {
        this.clocks.update(vt);
        return this.log.setMuseum(roomsdistance(), roomspaintings(), this.clocks.clone());
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
    public VectorTimestamp getDistRoom(int roomID, VectorTimestamp vt) {
        this.clocks.update(vt);
        this.clocks.setInteger(rooms[roomID].distOutside);
        return this.clocks.clone();
    }

}
