package org.anax.graphminesweeper.game;

public class RotationMatrix2D extends Matrix2D{
    private double angle;
    public RotationMatrix2D(double angle) {
        super(Math.cos(angle), -Math.sin(angle), Math.sin(angle), Math.cos(angle));
        this.angle = angle;
    }

    public double getAngle(){return angle;}
}
