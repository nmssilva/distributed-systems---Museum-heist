/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

/**
 *
 * @author Nuno Silva
 * @author Pedro Coelho
 * 
 */
public class Room {
    
    private int id;         /* identificador do quarto */
    private int distance;   /* distance from the gathering site */
    private int nPaintings; /* number of paintings */    
    
    public Room(int distance, int nPaint, int id) {
        this.distance = distance;
        this.nPaintings = nPaint;
        this.id = id;
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
