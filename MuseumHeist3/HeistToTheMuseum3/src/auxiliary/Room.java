/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliary;

import static auxiliary.Heist.*;
import java.io.Serializable;

/**
 *
 * @author Nuno Silva 72708, Pedro Coelho 59517
 */
public class Room implements Serializable {

    int id;
    int nPaintings;
    public int distOutside;

    public Room(int id) {
        this.id = id;
        nPaintings = (int) (Math.random() * (MAX_PAINTINGS - MIN_PAINTINGS) + 1) + MIN_PAINTINGS;
        distOutside = (int) (Math.random() * (MAX_DIST_OUTSIDE - MIN_DIST_OUTSIDE) + 1) + MIN_DIST_OUTSIDE;
    }

    public int getNPaintings() {
        return nPaintings;
    }

    public int getDistOutside() {
        return distOutside;
    }

    public void setnPaintings(int nPaintings) {
        this.nPaintings = nPaintings;
    }

    public void setDistOutside(int distOutside) {
        this.distOutside = distOutside;
    }

    @Override
    public String toString() {
        return "Room [" + "id=" + id + ", nPaintings=" + nPaintings + ", distOutside=" + distOutside + ']';
    }

}
