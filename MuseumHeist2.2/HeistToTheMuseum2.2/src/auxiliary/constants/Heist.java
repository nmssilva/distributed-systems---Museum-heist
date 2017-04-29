package auxiliary.constants;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class Heist {

    /**
     * Total number of AssaultParties in the Heist.
     */
    public static final int MAX_ASSAULT_PARTIES = 2;

    /**
     * Total number of AssaultThieves in the Heist.
     */
    public static final int THIEVES_NUMBER = 6;

    /**
     * AssaultThieves maximum displacement.
     */
    public static final int THIEVES_MAX_DISPLACEMENT = 6;

    /**
     * AssaultThieves minimum displacement.
     */
    public static final int THIEVES_MIN_DISPLACEMENT = 2;

    /**
     * AssaultThieves maximum distance between them while crawling in.
     */
    public static final int THIEVES_MAX_DISTANCE = 3;

    /**
     * Room maximum distance to Outside.
     */
    public static final int ROOM_MAX_DISTANCE = 30;

    /**
     * Maximum number of AssaultThieves in an AssaultParty.
     */
    public static final int MAX_ASSAULT_PARTY_THIEVES = 3;

    /**
     * Total number of Rooms in the Museum.
     */
    public static final int ROOMS_NUMBER = 5;

    /**
     * Paintings minimum number in a Room.
     */
    public static final int MIN_PAINTINGS = 8;

    /**
     * Paintings maximum number in a Room.
     */
    public static final int MAX_PAINTINGS = 16;

    /**
     * Maximum distance from the Room in the Museum to the Outside.
     */
    public static final int MAX_DIST_OUTSIDE = 30;

    /**
     * Minimum distance from the Room in the Museum to the Outside.
     */
    public static final int MIN_DIST_OUTSIDE = 15;

    public static final int N_ITER = 1;

    // Port numbers
    public static final int PORT_LOG = 4000;

    public static final int PORT_CS = 4100;

    public static final int PORT_CCS = 4200;

    public static final int PORT_AP = 4300;

    public static final int PORT_MUSEUM = 4400;

    // IPs
    public static final String HOST_LOG = "ROG";

    public static final String HOST_CS = "ROG";

    public static final String HOST_CCS = "ROG";

    public static final String[] HOST_AP = {"ROG", "ROG"};

    public static final String HOST_MUSEUM = "ROG";
}
