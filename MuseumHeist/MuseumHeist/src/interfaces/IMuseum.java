package interfaces;

import monitors.Room;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public interface IMuseum {

    /**
     *
     * @param roomID ID of a room
     * @return Returns the room with the given ID
     */
    Room getRoom(int roomID);

    /**
     *
     * @param nRoom ID of the room where the thieves are
     * @return Returns 1 if thief was successful in retrieving a canvas, 0 if
     * not
     */
    int rollACanvas(int nRoom);

}
