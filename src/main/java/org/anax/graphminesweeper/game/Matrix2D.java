package org.anax.graphminesweeper.game;

public class Matrix2D {
    // the matrix
    // |a b|
    // |c d|

    double a, b, c, d;
    public Matrix2D (double a, double b, double c, double d){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Vector2D multiply(Vector2D vec){
        return new Vector2D(vec.x*a + vec.y*b, vec.x*c + vec.y*d);
    }
}

