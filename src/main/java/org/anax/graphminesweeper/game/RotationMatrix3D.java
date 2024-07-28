package org.anax.graphminesweeper.game;

public class RotationMatrix3D extends Matrix3D{
    public RotationMatrix3D(double a, double b, double c,
                            double d, double e, double f,
                            double g, double h, double i) {
        super(a, b, c,
              d, e, f,
              g, h, i);
    }

    public static RotationMatrix3D fromZAngle(double angle){
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        return new RotationMatrix3D(
                cos, -sin, 0,
                sin, cos,  0,
                  0,   0,  1
        );
    }
    public static RotationMatrix3D fromYAngle(double angle){
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        return new RotationMatrix3D(
                 cos, 0, sin,
                   0, 1, 0,
                -sin, 0, cos
        );
    }
    public static RotationMatrix3D fromXAngle(double angle){
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        return new RotationMatrix3D(
                1, 0, 0,
                0, cos, -sin,
                0, sin, cos
        );
    }
}
