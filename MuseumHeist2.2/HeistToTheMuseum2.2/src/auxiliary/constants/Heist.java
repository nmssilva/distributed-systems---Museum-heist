package auxiliary.constants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class Heist{

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

    //public static final int N_ITER = 1;

    // Port numbers
    public static final int PORT_LOG = 22350;

    public static final int PORT_CS = 22351;

    public static final int PORT_CCS = 22352;

    public static final int PORT_MUSEUM = 22353;

    public static final int PORT_AP = 22354;

    // IPs
    
    public static final String HOST_LOG = "l040101-ws03.ua.pt";

    public static final String HOST_CS = "l040101-ws05.ua.pt";

    public static final String HOST_CCS = "l040101-ws07.ua.pt";

    public static final String[] HOST_AP = {"l040101-ws09.ua.pt", "l040101-ws10.ua.pt"};

    public static final String HOST_MUSEUM = "l040101-ws04.ua.pt";
    
    /*
    public static final String HOST_LOG = getPcname();

    public static final String HOST_CS = getPcname();

    public static final String HOST_CCS = getPcname();

    public static final String[] HOST_AP = {getPcname(), getPcname()};

    public static final String HOST_MUSEUM = getPcname();
    */
    private static String getPcname(){
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Heist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
