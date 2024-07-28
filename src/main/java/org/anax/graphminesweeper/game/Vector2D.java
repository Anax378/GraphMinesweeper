package org.anax.graphminesweeper.game;

public class Vector2D {
    double x;
    double y;
    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2D copy(){
        return new Vector2D(x, y);
    }
    public Vector2D add(Vector2D other){
        return new Vector2D(x+other.x, y+other.y);
    }
    public static Vector2D fromCoords2D(Coord2D a, Coord2D b){
        return new Vector2D(b.x-a.x, b.y-a.y);
    }

    public Vector2D scale(double f){
        return new Vector2D(x*f, y*f);
    }
}
