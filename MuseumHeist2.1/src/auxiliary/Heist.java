package auxiliary;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class Heist {


    /**
     * Total number of thieves in the Heist.
     */
    public static final int THIEVES_NUMBER = 6;

    /**
     * Thieves maximum displacement.
     */
    public static final int THIEVES_MAX_DISPLACEMENT = 6;

    /**
     * Thieves minimum displacement.
     */
    public static final int THIEVES_MIN_DISPLACEMENT = 2;

    /**
     * Thieves maximum distance between them while crawling.
     */
    public static final int THIEVES_MAX_DISTANCE = 3;

    /**
     * Room maximum distance to Outside.
     */
    public static final int ROOM_MAX_DISTANCE = 30;

    /**
     * Maximum number of thieves in an AssaultParty.
     */
    public static final int MAX_ASSAULT_PARTY_THIEVES = 3;
    
    /**
     * Total number of AssaultParties.
     */
    public static final int MAX_ASSAULT_PARTIES = THIEVES_NUMBER/MAX_ASSAULT_PARTY_THIEVES;

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
    
    /**
     * Port numbers for log interface
     */
    public static final int PORT_LOG = 4000;
    
    /**
     * Port numbers for concentration site interface
     */
    public static final int PORT_CS = 4001;
    
    /**
     * Port numbers for control and collection site interface
     */
    public static final int PORT_CCS = 4002;
    
    /**
     * Port numbers for assault party interface
     */
    public static final int PORT_AP = 4003;
    
    /**
     * Port numbers for museum interface
     */
    public static final int PORT_MUSEUM = 4005;
    
    /**
     * Number of iterations
     */
    public static int N_ITER = 1;
}
