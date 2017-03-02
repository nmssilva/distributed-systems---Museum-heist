/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thieves;

/**
 * This enum represents the state of thieves entity
 *
 * @author Pedro Coelho
 * @author Nuno Silva
 */
public enum ThievesState {
    OUTSIDE("O"),
    CRAWLING_INWARDS("CI"),
    AT_A_ROOM("AAR"),
    CRAWLING_OUTWARDS("CO");

    private final String acronym;

    private ThievesState(String acronym) {
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
