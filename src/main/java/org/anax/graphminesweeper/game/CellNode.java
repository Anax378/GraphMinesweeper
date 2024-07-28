package org.anax.graphminesweeper.game;

import org.anax.graphminesweeper.render.IRenderable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CellNode implements IRenderable {
    public static volatile Cell highlightSource = null;

    public double velocityCap = 1000;

    public Coord coord;
    Vector velocity = new Vector(0, 0, 0);
    Cell cell;
    public int radius = 3;
    public static Color outlineColor = Color.white;
    public static Color color = Color.BLACK;
    public Color connectionColor = Color.white;
    public static Color highlightColor = Color.CYAN;
    public static Color mineColor = Color.RED;
    public Box box;

    public void applyVelocity(){
        coord.add(velocity.cap(velocityCap));
        coord.restore();
    }
    public CellNode(Cell cell, Coord coord){
        Random random = new Random();
        this.connectionColor = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));

        this.cell = cell;
        this.coord = coord;
        this.box = new Box(cell);
    }
    public CellNode(Cell cell, int x, int y, int z){
        this(cell, new Coord(x, y, z));
    }
    public BufferedImage renderCell(BufferedImage image, Camera camera){
        Coord2D renderCoord = getRenderPosition(camera);
        if(renderCoord == null){return image;}


        int radius = (int) Math.round(camera.getScaleFactor(coord)*this.radius);

        Graphics2D g2d = image.createGraphics();
        g2d.setPaint(this.color);
        g2d.fillOval(renderCoord.roundX()-radius, renderCoord.roundY()-radius, radius*2, radius*2);

        g2d.setPaint(this.outlineColor);
        if(CellNode.highlightSource == this.cell || this.cell.connectedCells.contains(CellNode.highlightSource)){
            g2d.setPaint(highlightColor);
        }
        g2d.drawOval(renderCoord.roundX()-radius, renderCoord.roundY()-radius, radius*2, radius*2);

        if(!box.isCovered){
            if(box.isMine){
                g2d.setPaint(mineColor);
                g2d.drawOval(renderCoord.roundX()-radius/2, renderCoord.roundY()-radius/2, radius, radius);
            }
            else{
                Font font = new Font("Arial", Font.PLAIN, Math.max((radius*2), 0));
                g2d.setFont(font);
                g2d.setPaint(outlineColor);
                int width = g2d.getFontMetrics().getMaxAdvance();
                Coord2D charCoord = renderCoord.add(new Vector2D((double) -radius /2, ((double) font.getSize() /2)));
                g2d.drawString(String.valueOf(this.box.mines), charCoord.roundX(), charCoord.roundY());
            }
        }
        else if(box.isFlagged){
            g2d.setColor(mineColor);
            g2d.fillRect(renderCoord.roundX()-radius/2, renderCoord.roundY() -radius/2, radius, radius);
        }

        g2d.dispose();
        return image;
    }

    public BufferedImage renderConnections(BufferedImage image, Camera camera){
        Coord2D renderCoord = getRenderPosition(camera);
        if(renderCoord == null){
            return image;
        }

        Graphics2D g2d = image.createGraphics();
        g2d.setPaint(connectionColor);
        if(this.cell == highlightSource){
            g2d.setPaint(highlightColor);
        }
        Coord2D destCoord;
        for(Cell connectedCell : cell.connectedCells){
            //if (coord.y > connectedCell.cellNode.coord.y){continue;}

            if(connectedCell == highlightSource){continue;}

            destCoord = connectedCell.cellNode.getRenderPosition(camera);
            if(destCoord == null){

                int factor = camera.width* camera.height*2;
                destCoord = camera.intersection(coord, Vector.fromCoords(coord, connectedCell.cellNode.coord));

                Vector2D delta = Vector2D.fromCoords2D(renderCoord, destCoord);
                destCoord = renderCoord.add(delta.scale(-factor)); //should not have to multiply by -1, but without it, it breaks;
            }
            g2d.drawLine(renderCoord.roundX(), renderCoord.roundY(), destCoord.roundX(), destCoord.roundY());
        }


        g2d.dispose();
        return image;
    }
    @Override
    public BufferedImage renderOnImage(BufferedImage image, Camera camera) {
        return this.renderConnections(this.renderCell(image, camera), camera);
    }

    public Coord2D getRenderPosition(Camera camera){
        Coord2D renderPos = camera.toCameraCoordinates(coord);
        return renderPos;
    }


    public void goToAverageCoord(){
        Vector average = Vector.ZERO.copy();
        double weight = (double) 1 /cell.connectedCells.size();
        for(Cell connectedCell : cell.connectedCells){
            average.add(Vector.fromCoords(Coord.ZERO, connectedCell.cellNode.coord.copy().scale(weight)));
        }
        this.coord = Coord.ZERO.copy().add(average);
    }
}
