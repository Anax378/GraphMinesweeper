package org.anax.graphminesweeper.game;

public class Matrix3D {
    double a, b, c;
    double d, e, f;
    double g, h, i;

    public Matrix3D(double a, double b, double c, double d, double e, double f, double g, double h, double i){
        this.a=a;
        this.b=b;
        this.c=c;
        this.d=d;
        this.e=e;
        this.f=f;
        this.g=g;
        this.h=h;
        this.i=i;
    }
    public Vector multiply(Vector v){
        return new Vector(
                v.x*a + v.y*b + v.z*c,
                v.x*d + v.y*e + v.z*f,
                v.x*g + v.y*h + v.z*i
        );
    }

    public void printSelf(){
        System.out.println(a + " " + b + " " + c);
        System.out.println(d + " " + e + " " + f);
        System.out.println(g + " " + h + " " + i);
    }
}
