package org.anax.graphminesweeper.game;

public class Vector {
    public static final Vector ZERO = new Vector(0, 0, 0);

    double x;
    double y;
    double z;

    public Vector copy(){
        return new Vector(x, y, z);
    }
    public Vector(double x, double y,double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public double length(){
        return (double) Math.sqrt(x*x+y*y+z*z);
    }
    public Vector scale(double factor){
        return new Vector(x*factor, y*factor, z*factor);
    }
    public Vector add(Vector vec){
        return new Vector(x+vec.x, y+vec.y, z+vec.z);
    }
    public Vector normalize(){
        return this.scale(1/length());
    }
    public Vector scaleTo(double length){
        return this.normalize().scale(length);
    }

    public static Vector fromCoords(Coord a, Coord b){
        return new Vector(b.x-a.x, b.y-a.y, b.z-a.z);
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
        if(z > cap){
            z = cap;
        }
        if(z < -cap){
            z = -cap;
        }
        if(Double.isNaN(x)){
            x = 0;
        }
        if(Double.isNaN(y)){
            y = 0;
        }
        if(Double.isNaN(z)){
            z = 0;
        }
        return this;
    }

    public double dot(Vector other){
        return x*other.x + y*other.y + z*other.z;
    }

    public Vector cross(Vector other){
        return new Vector(y*other.z-other.y*z, - (x*other.z-z*other.x), x*other.y-y*other.x);
    }

    public Vector rotateAboutAxis(double angle, Vector axis){
        Vector k = axis.normalize();
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        return this.scale(cos)
                .add(k.cross(this).scale(sin))
                .add(k.scale(k.dot(this) * (1-cos)));
    }

    public void printSelf() {
        System.out.println("x: " + x + ", y: " + y + ", z: " + z);
    }
}
