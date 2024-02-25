package org.anax.graphminesweeper.game;

public class Vector {
    public static final Vector ZERO = new Vector(0, 0);

    double x;
    double y;

    public Vector copy(){
        return new Vector(x, y);
    }
    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }
    public double length(){
        return (double) Math.sqrt(x*x+y*y);
    }
    public Vector scale(double factor){
        x = x*factor;
        y = y*factor;
        return this;
    }
    public Vector add(Vector vec){
        x += vec.x;
        y += vec.y;
        return this;
    }
    public Vector normalize(){
        this.scale(1/length());
        return this;
    }
    public Vector scaleTo(double length){
        this.normalize().scale(length);
        return this;
    }

    public static Vector fromCoords(Coord a, Coord b){
        return new Vector(b.x-a.x, b.y-a.y);
    }

    public Vector cap(double cap) {
        if(x > cap){
            x = cap;
        }
        if(x < -cap){
            y = -cap;
        }
        if(y > cap){
            y = cap;
        }
        if(y < -cap){
            y = -cap;
        }
        if(Double.isNaN(x)){
            x = 0;
        }
        if(Double.isNaN(y)){
            y = 0;
        }
        return this;
    }

    public Vector rotate(double angle){
        double nx = x*Math.cos(angle) - y*Math.sin(angle);
        double ny = y*Math.cos(angle) + x*Math.sin(angle);
        x = nx;
        y = ny;
        return this;
    }

    public void printSelf() {
        System.out.println("x: " + x + ", y: " + y);
    }
}
