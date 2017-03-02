/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MasterThief;

/**
 * This enum represents the state of master thief entity
 *
 * @author Pedro Coelho
 * @author Nuno Silva
 */
public enum MasterThiefState {
    PLANNING_THE_HEIST("PTH"),
    DECIDING_WHAT_TO_DO("DWTD"),
    ASSEMBLING_A_GROUP("AAG"),
    WAINTING_FOR_GROUP_ARRIVAL("WFGA"),
    PRESENTING_THE_REPORT("PTR");

    private final String acronym;

    private MasterThiefState(String acronym) {
        this.acronym = acronym;
    }

    /**
     * Returns a smaller representation of the state (an acronym)
     *
     * @return state as an acronym
     */
    public String getAcronym() {
        return acronym;
    }

    /**
     * The method is used to get a String representation of the state
     *
     * @return the state as String
     */
    @Override
    public String toString() {
        return this.name().replace("_", " ").toLowerCase();
    }
}
