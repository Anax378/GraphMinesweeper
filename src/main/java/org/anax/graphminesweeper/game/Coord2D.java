package org.anax.graphminesweeper.game;

import java.security.cert.CertPathParameters;

public class Coord2D {
    double x;
    double y;
    public Coord2D(double x,double y){
        this.x = x;
        this.y = y;
    }
    public double distance(Coord2D other){
        return Math.sqrt((x-other.x)*(x-other.x) + (y-other.y)*(y-other.y));
    }
    public int roundX() {
        return (int) Math.round(x);
    }
    public int roundY() {
        return (int) Math.round(y);
    }

    public Coord2D copy() {
        return new Coord2D(x, y);
    }

    public Coord2D add(Vector2D other) {
        return new Coord2D(this.x+other.x, this.y+other.y);
    }

    public void printSelf() {
        System.out.println("x: " + x + ", y: " + y);
    }
}
