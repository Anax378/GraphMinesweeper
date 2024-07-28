package org.anax.graphminesweeper.game;

public class Camera {
    public int width;
    public int height;
    Coord position;
    private Vector facing;

    public Vector getFacing() {
        return facing.copy();
    }

    public Vector getDown() {
        return down.copy();
    }

    public Vector getRight() {
        return right.copy();
    }

    private Vector down = new Vector(0, -1, 0);
    private Vector right;
    public Camera(int width, int height, Vector facing, Coord position){
        this.width = width;
        this.height = height;
        this.facing = facing;
        this.position = position;

        this.right = down.cross(facing).scaleTo(1);
    }
    public void relativeMove(Vector vec){
        this.position = position.add(right.scaleTo(vec.x)).add(down.scaleTo(-vec.y)).add(facing.scaleTo(vec.z));
    }
    public void rotate(RotationMatrix3D mat){
        this.facing = mat.multiply(facing);
        this.right = mat.multiply(right);
        this.down = mat.multiply(down);
    }

    public void rotate(double angle, Vector axis){
        this.facing = facing.rotateAboutAxis(angle, axis);
        this.down = down.rotateAboutAxis(angle, axis);
        this.right = right.rotateAboutAxis(angle, axis);
    }

    public Coord2D intersection(Coord P,Vector R){
        R = R.scale(-1);
        Coord O = position.add(facing);

        double t = Vector.fromCoords(P, O).dot(facing) / R.dot(facing);

        Coord Q = P.add(R.scale(t));
        Vector cQ = Vector.fromCoords(O, Q);

        double x = cQ.dot(right);
        double y = cQ.dot(down);

        return new Coord2D(x + (width/2d), y + (height/2d));
    }
    public double getScaleFactor(Coord point){
        double distance = Vector.fromCoords(position, point).dot(facing.normalize());
        return facing.length()/distance;
    }

    public Coord2D toCameraCoordinates(Coord point){

        Vector r = Vector.fromCoords(position, point);

        double t = facing.dot(facing) / r.dot(facing);
        if(t < 0){
            return  null;
        }

        Coord P = position.add(r.scale(t));

        Coord center = position.add(facing);
        Vector cP = Vector.fromCoords(center, P);

        double x = cP.dot(right);
        double y = cP.dot(down);

        return new Coord2D(x + width/2f, y + height/2f);
    }

    public void setFocalLengthFromFOV(double FOV){
        double focalLength = width/(2*Math.tan(FOV*0.5));
        facing = facing.scaleTo(focalLength);
    }

}
