/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitors;

import static GenRepOfInfo.Heist.MAX_DIST_OUTSIDE;
import static GenRepOfInfo.Heist.MAX_PAINTINGS;
import static GenRepOfInfo.Heist.MIN_DIST_OUTSIDE;
import static GenRepOfInfo.Heist.MIN_PAINTINGS;
import java.util.Random;

/**
 *
 * @author Nuno Silva
 */
public class Room implements IRoom{
    
    private int id;         /* identificador do quarto */
    private int distance;   /* distance from the gathering site */
    private int nPaintings; /* number of paintings */    
    private boolean free;   /* assault party in room */
    
    public Room(int id) {
        this.distance =  new Random().nextInt((MAX_DIST_OUTSIDE - MIN_DIST_OUTSIDE) + 1) + MIN_DIST_OUTSIDE;
        this.nPaintings = new Random().nextInt((MAX_PAINTINGS - MIN_PAINTINGS) + 1) + MIN_PAINTINGS;
        this.id = id;
        this.free = true;
    }
    
    public boolean isFree() {
        return free;
    }
    
    @Override
    public void setFree(boolean f) {
        this.free = f;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getNPaintings() {
        return nPaintings;
    }

    @Override
    public int getDistance() {
        return distance;
    }

    @Override
    public void setnPaintings(int nPaintings) {
        this.nPaintings = nPaintings;
    }

    public void setDistance(int dist) {
        this.distance = dist;
    }
    
}
