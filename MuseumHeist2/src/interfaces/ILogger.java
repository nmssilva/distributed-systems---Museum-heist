package interfaces;

import serverSide.Room;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public interface ILogger {

    /**
     * Log the final status of the Heist.
     *
     */
    void reportFinalStatus();

    /**
     * Log the initial status of the Heist.
     *
     */
    void reportInitialStatus();

    /**
     * Log the status of everything in the General Repository of Information.
     *
     */
    void reportStatus();

    /**
     * Set an Assault Party in the General Repository of Information.
     *
     * @param id
     * @param elements
     * @param positions
     * @param roomID
     */
    void setAssaultParty(int id, int[] elements, int[] positions, int roomID);

    /**
     * Set an Assault Thief in the General Repository of Information.
     *
     */
    void setAssaultThief();

    /**
     * Set the Master Thief in the General Repository of Information.
     *
     */
    void setMasterState();

    /**
     * Set the Museum in the General Repository of Information.
     *
     * @param rooms
     */
    void setMuseum(Room[] rooms);

    /**
     * Set the number of Paintings in the General Repository of Information.
     *
     * @param nPaintings
     */
    void setnPaintings(int nPaintings);

}
