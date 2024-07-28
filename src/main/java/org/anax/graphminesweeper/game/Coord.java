package org.anax.graphminesweeper.game;

public class Coord {
    public static final Coord ZERO = new Coord(0, 0, 0);

    public double x;
    public double y;
    public double z;

    public Coord(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Coord copy(){
        return new Coord(x, y, z);
    }

    public int roundX(){
        return (int) Math.round(x);
    }
    public int roundY(){
        return (int) Math.round(y);
    }
    public int roundZ(){
        return (int) Math.round(z);
    }
    public Coord add(Vector vec){
        return new Coord(x+vec.x, y+vec.y, z+vec.z);
    }

    public double distance(Coord coord){
        return Vector.fromCoords(this, coord).length();
    }

    public Coord scale(double factor){
        return new Coord(x*factor, y*factor, z*factor);
    }

    public void restore(){
        if(Double.isNaN(x)){
            x = 0;
        }
        if(Double.isNaN(y)){
            y = 0;
        }
        if(Double.isNaN(z)){
            z = 0;
        }
    }

    public void printSelf(){
        System.out.println("x: " + x + ", y: " + y + ", z: " + z);
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Coord){
            return this.x == ((Coord) obj).x && this.y == ((Coord) obj).y && this.z == ((Coord)obj).z;
        }
        return false;
    }
}
