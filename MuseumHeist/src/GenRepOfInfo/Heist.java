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
    public final static int OUTSIDE = 1000,
            CRAWLING_INWARDS = 2000,
            AT_A_ROOM = 3000,
            CRAWLING_OUTWARDS = 4000,
            AT_COLLECTION_SITE = 4001,
            HEIST_END = 4002;

    // Estados do MasterThief
    public final static int PLANNING_THE_HEIST = 1000,
            DECIDING_WHAT_TO_DO = 2000,
            ASSEMBLING_A_GROUP = 3000,
            WAITING_FOR_ARRIVAL = 4000,
            PRESENTING_THE_REPORT = 5000;

    // Total number of Thieves in the Hesit
    public static final int THIEVES_NUMBER = 6;
    // Thieves max displacement
    public static final int MAX_DISPLACEMENT = 6;
    // Thieves minimum displacement
    public static final int MIN_DISPLACEMENT = 2;
    // Thieves maximum distance between them while crawling in
    public static final int THIEVES_MAX_DISTANCE = 3;
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

    // Assault parties information
    public static int[][] ASSAULT_PARTIES = new int[THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES][MAX_ASSAULT_PARTY_THIEVES];
    
    // função auxiliar para obtenção do ID da assaultparty
    public static int getParty(int thiefid){
        for(int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++){
            for(int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++){
                if(ASSAULT_PARTIES[i][j] == thiefid){
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static int getPosInParty(int thiefid){
        for(int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++){
            for(int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++){
                if(ASSAULT_PARTIES[i][j] == thiefid){
                    return j;
                }
            }
        }
        return -1;
    }
    
    public static void removeThiefFromParty(int thiefid){
        for(int i = 0; i < THIEVES_NUMBER / MAX_ASSAULT_PARTY_THIEVES; i++){
            for(int j = 0; j < MAX_ASSAULT_PARTY_THIEVES; j++){
                if(ASSAULT_PARTIES[i][j] == thiefid){
                    ASSAULT_PARTIES[i][j] = -1;
                }
            }
        }
    }
}
