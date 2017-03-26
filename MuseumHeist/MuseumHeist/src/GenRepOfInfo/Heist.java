/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GenRepOfInfo;

/**
 *
 * @author Nuno Silva
 */
public class Heist {

    // Estados do Thief
    /**
     * Inital Thief State. Blocking state the ordinary thief is waken up by one
     * of the following operations of the master thief: prepareAssaultParty,
     * during heist operations, or sumUpResults, at the end of the heist.
     */
    public final static int OUTSIDE = 1000,
            /**
             * transitional state with eventual waiting for the crawling in
             * movement to start, the first party member is waken up by the
             * operation sendAssaultParty of master thief the ordinary thief
             * proceeds until the target room at the museum is reached and
             * blocks if he can not generate a new increment of position (before
             * blocking, he wakes up the fellow party member that is just behind
             * him in the crawling queue, or the first one still crawling, if he
             * is the last) when blocking occurs, the ordinary thief is waken up
             * by the operation of crawlIn of a fellow party member.
             */
            CRAWLING_INWARDS = 2000,
            /**
             * transitional state
             *
             */
            AT_A_ROOM = 3000,
            /**
             * transitional state with eventual waiting for the crawling out
             * movement to start, the first party member is waken up by the
             * operation reverseDirection of the last party member to decide to
             * leave the room the ordinary thief proceeds until he reaches the
             * outside gathering site and blocks if he can not generate a new
             * increment of position (before blocking, he wakes up the fellow
             * party member that is just behind him in the crawling queue, or
             * the first one still crawling, if he is the last) when blocking
             * occurs, the ordinary thief is waken up by the operation of
             * crawlOut of a fellow party member
             */
            CRAWLING_OUTWARDS = 4000,
            /**
             * transitional state used to hand a canvas at the master thief's
             * collection site
             */
            AT_COLLECTION_SITE = 4001,
            /**
             * final state
             */
            HEIST_END = 4002;

    // Estados do MasterThief
    /**
     * initial state (transitional)
     *
     */
    public final static int PLANNING_THE_HEIST = 1000,
            /**
             * transitional state with eventual waiting master thief proceeds if
             * the next operation is takeARest and blocks if it is one of the
             * other two and there is not a sufficient number of ordinary
             * thieves available (the totality for sumUpResults and enough to
             * create an assault party for prepareAssaultParty) when master
             * thief blocks, she is waken up by the operation amINeeded of an
             * ordinary thief
             */
            DECIDING_WHAT_TO_DO = 2000,
            /**
             * blocking state master thief is waken up by the operation
             * prepareExcursion of the last of the ordinary thieves to join the
             * party
             */
            ASSEMBLING_A_GROUP = 3000,
            /**
             * blocking state master thief is waken up by the operation
             * handACanvas of one of the assault party members returning from
             * the museum
             *
             */
            WAITING_FOR_ARRIVAL = 4000,
            /**
             * final state
             *
             */
            PRESENTING_THE_REPORT = 5000;

    // Total number of Thieves in the Hesit
    /**
     *
     * total number of thieves
     */
    public static final int THIEVES_NUMBER = 6;
    // Thieves max displacement

    /**
     * maximum displacement of thieves
     */
    public static final int MAX_DISPLACEMENT = 6;
    // Thieves minimum displacement

    /**
     * minimum displacement of thieves
     */
    public static final int MIN_DISPLACEMENT = 2;
    // Thieves maximum distance between them while crawling in

    /**
     * maximum distance between thieves
     */
    public static final int THIEVES_MAX_DISTANCE = 3;
    // Max number of AssaultThieves in an AssaultParty

    /**
     * maximum number of thieves in assault party
     */
    public static final int MAX_ASSAULT_PARTY_THIEVES = 3;
    // Total number of Rooms in the Museum

    /**
     * total number of rooms
     */
    public static final int ROOMS_NUMBER = 5;
    // Paintings minimum number in a Room

    /**
     * minimum number of paintings in room
     */
    public static final int MIN_PAINTINGS = 8;
    // Paintings maximum number in a Room

    /**
     * maximum number of paintings in room
     */
    public static final int MAX_PAINTINGS = 16;
    // Maximum distance from the Room in the Museum to the Outside

    /**
     * maximum distance to rooms
     */
    public static final int MAX_DIST_OUTSIDE = 30;
    // Minimum distance from the Room in the Museum to the Outside

    /**
     * minimum distance to rooms
     */
    public static final int MIN_DIST_OUTSIDE = 15;

    //auxiliary functions
    /**
     *
     * @param array array to be evaluated
     * @return true if all values of array are true. false if otherwise.
     */
    public static boolean areAllTrue(boolean[] array) {
        for (boolean b : array) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param array array to be evaluated
     * @return true if all values of array are false. false if otherwise.
     */
    public static boolean areAllFalse(boolean[] array) {
        for (boolean b : array) {
            if (b) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param array array to be evaluated
     * @return number of elements that are not null in array
     */
    public static int countNotNull(Object[] array) {
        int i = 0;
        for (Object b : array) {
            if (b != null) {
                i++;
            }
        }
        return i;
    }

    /**
     *
     * @param array array to be evaluated
     * @return number of elements that are not zero in array
     */
    public static int countNotZero(int[] array) {
        int i = 0;
        for (int b : array) {
            if (b != 0) {
                i++;
            }
        }
        return i;
    }
}
