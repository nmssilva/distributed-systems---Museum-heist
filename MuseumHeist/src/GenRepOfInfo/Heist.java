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
    // Estados do AssaultThief
    public final static int OUTSIDE = 0,
                            CRAWLING_INWARDS = 1,
                            AT_A_ROOM = 2,
                            CRAWLING_OUTWARDS = 3,
                            AT_COLLECTION_SITE = 4,
                            HEIST_END = 5;

    // Estados do MasterThief
    public final static int PLANNING_THE_HEIST = 0,
                            DECIDING_WHAT_TO_DO = 1,
                            ASSEMBLING_A_GROUP = 2,
                            WAITING_FOR_GROUP_ARRIVAL = 3,
                            PRESENTING_THE_REPORT = 4;
    
    // Total number of AssaultThieves in the Hesit
    public static final int THIEVES_NUMBER = 6;   
    // Thieves max displacement
    public static final int THIEVES_MAX_DISPLACEMENT = 6;
    // Thieves minimum displacement
    public static final int THIEVES_MIN_DISPLACEMENT = 2;
    // Thieves maximum distance between them while crawling in
    public static final int THIEVES_MAX_DISTANCE = 3;
    // Room maximum distance to Outside
    public static final int ROOM_MAX_DISTANCE = 30;
    // Max number of AssaultThieves in an AssaultParty
    public static final int MAX_ASSAULT_PARTY_THIEVES = 3;
    // Total number of Rooms in the Museum
    public static final int ROOMS_NUMBER = 5;
    // Paintings minimum number in a Room
    public static final int MIN_PAINTINGS = 8;
    // Paintings maximum number in a Room
    public static final int MAX_PAINTINGS = 16;
    // Maximum distance from the Room in the Museum to the Outside
    public static final int MAX_DIST_OUTSIDE = 30;
    // Minimum distance from the Room in the Museum to the Outside
    public static final int MIN_DIST_OUTSIDE = 15;  
}
