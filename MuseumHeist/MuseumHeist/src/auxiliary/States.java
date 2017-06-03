/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliary;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class States {

    // Status of AssaultThief
    public final static int 
            /**
             *  blocking state
             *  the ordinary thief is waken up by one of the following operations of the master thief:
             *  prepareAssaultParty, during heist operations, or sumUpResults, at the end of the heist
             */
            OUTSIDE = 1000,
            /**
             * the thief is waiting to be sent to an assault party
             */
            WAITING_SEND_ASSAULT_PARTY = 1001,
            /**
             *  transitional state with eventual waiting
             *  for the crawling in movement to start, the first party member is waken up by
             *  the operation sendAssaultParty of master thief
             *  the ordinary thief proceeds until the target room at the museum is reached and
             *  blocks if he can not generate a new increment of position (before blocking, he
             *  wakes up the fellow party member that is just behind him in the crawling queue,
             *  or the first one still crawling, if he is the last)
             *  when blocking occurs, the ordinary thief is waken up by the operation of
             *  crawlIn of a fellow party member
             */
            CRAWLING_INWARDS = 2000,
            /**
             * transitional state
             */
            AT_A_ROOM = 3000,
            /**
             * transitional state with eventual waiting
             *  for the crawling out movement to start, the first party member is waken up by
             *  the operation reverseDirection of the last party member to decide to leave the room
             *  the ordinary thief proceeds until he reaches the outside gathering site and
             *  blocks if he can not generate a new increment of position (before blocking, he
             *  wakes up the fellow party member that is just behind him in the crawling
             *  queue, or the first one still crawling, if he is the last)
             *  when blocking occurs, the ordinary thief is waken up by the operation of
             *  crawlOut of a fellow party member
             */
            CRAWLING_OUTWARDS = 4000,
            /**
             * transitional state
             * Thief returns from crawling out and is at collection site to hand canvas
             */
            AT_COLLECTION_SITE = 5000,
            /**
             * Thief ends heist
             */
            HEIST_END = 6000;

    // Status of MasterThief
    public final static int /**
             * Status PLANNING_THE_HEIST.
             */
            PLANNING_THE_HEIST = 1000,
            /**
             * Status DECIDING_WHAT_TO_DO.
             */
            DECIDING_WHAT_TO_DO = 2000,
            /**
             * Status ASSEMBLING_A_GROUP.
             */
            ASSEMBLING_A_GROUP = 3000,
            /**
             * Status WAITING_FOR_GROUP_ARRIVAL.
             */
            WAITING_FOR_GROUP_ARRIVAL = 4000,
            /**
             * Status PRESENTING_THE_REPORT.
             */
            PRESENTING_THE_REPORT = 5000;

}
