package org.anax.graphminesweeper.game;

public class Coord {
    public static final Coord ZERO = new Coord(0, 0);

    public double x;
    public double y;
    public Coord(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Coord copy(){
        return new Coord((int) x, (int) y);
    }

    public int roundX(){
        return (int) Math.round(x);
    }

    public int roundY(){
        return (int) Math.round(y);
    }

    public Coord add(Vector vec){
        this.x += vec.x;
        this.y += vec.y;
        return this;
    }

    public double distance(Coord coord){
        return Vector.fromCoords(this, coord).length();
    }

    public Coord scale(double factor){
        this.x *= factor;
        this.y *= factor;
        return this;
    }

    public void restore(){
        if(Double.isNaN(x)){
            x = 0;
        }
        if(Double.isNaN(y)){
            y = 0;
        }
    }

    public void printSelf(){
        System.out.println("x: " + x + ", y: " + y);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Coord){
            return this.x == ((Coord) obj).x && this.y == ((Coord) obj).y;
        }
        return false;
    }
}
