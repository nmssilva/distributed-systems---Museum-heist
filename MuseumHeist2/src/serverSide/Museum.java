package serverSide;

import clientSide.AssaultThief;
import interfaces.IMuseum;
import interfaces.ILogger;
import static auxiliary.Heist.*;
import static auxiliary.States.*;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class Museum implements IMuseum {

    private Room[] rooms;                   // Array of rooms of the museum

    /**
     *
     */
    public Museum() {
        rooms = new Room[ROOMS_NUMBER];

        //log.setMuseum(rooms);
        for (int i = 0; i < ROOMS_NUMBER; i++) {
            rooms[i] = new Room(i);
        }
    }

    /**
     *
     * @param nRoom ID of the room where the thieves are going
     * @return Returns 1 if thief was successful in retrieving a canvas, 0 if
     * otherwise
     */
    @Override
    public synchronized int rollACanvas(int nRoom) {
        AssaultThief thief = (AssaultThief) Thread.currentThread();

        thief.setStatus(AT_A_ROOM);

        int nPaintings = rooms[nRoom].getNPaintings();
        
        if (nPaintings > 0) {
            rooms[nRoom].setnPaintings(nPaintings - 1);

            return 1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param roomID ID of room
     * @return Returns the room with the given ID
     */
    @Override
    public Room getRoom(int roomID) {
        return rooms[roomID];
    }

    @Override
    public int getRoomDistance(int roomId) {
        return rooms[roomId].getDistOutside();
    }

    @Override
    public int getPaintingsNumber(int roomId) {
        return rooms[roomId].getNPaintings();
    }

}
