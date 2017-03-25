/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;


import static GenRepOfInfo.Heist.*;
import java.util.Random;

/**
 *
 * @author Nuno Silva
 * @author Pedro Coelho
 * 
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
    
    public void setFree(boolean f) {
        this.free = f;
    }

    public int getId() {
        return id;
    }

    public int getNPaintings() {
        return nPaintings;
    }

    public int getDistance() {
        return distance;
    }

    public void setnPaintings(int nPaintings) {
        this.nPaintings = nPaintings;
    }

    public void setDistance(int dist) {
        this.distance = dist;
    }
    
}
