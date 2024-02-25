package org.anax.graphminesweeper.game;

import org.anax.graphminesweeper.render.IRenderable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class CellNode implements IRenderable {
    public static volatile Cell highlightSource = null;

    public double velocityCap = 1000;

    public Coord coord;
    Vector velocity = new Vector(0, 0);
    Cell cell;
    public int radius = 10;
    public static Color outlineColor = Color.white;
    public static Color color = Color.BLACK;
    public static Color connectionColor = Color.white;
    public static Color highlightColor = Color.CYAN;
    public static Color mineColor = Color.RED;
    public Box box;

    public void applyVelocity(){
        coord.add(velocity.cap(velocityCap));
        coord.restore();
    }
    public CellNode(Cell cell, Coord coord){
        this.cell = cell;
        this.coord = coord;
        this.box = new Box(cell);
    }
    public CellNode(Cell cell, int x, int y){
        this(cell, new Coord(x, y));
    }
    public BufferedImage renderCell(BufferedImage image, Vector offset, double scaleFactor){
        Coord renderCoord = getRenderPosition(offset, scaleFactor);
        int radius = (int) Math.round(this.radius*scaleFactor);

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
                Coord charCoord = renderCoord.copy().add(new Vector((double) -radius /2, ((double) font.getSize() /2)));
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

    public BufferedImage renderConnections(BufferedImage image, Vector offset, double scaleFactor){
        Coord renderCoord = getRenderPosition(offset, scaleFactor);
        Graphics2D g2d = image.createGraphics();
        g2d.setPaint(connectionColor);
        if(this.cell == highlightSource){
            g2d.setPaint(highlightColor);
        }
        Coord destCoord;
        for(Cell connectedCell : cell.connectedCells){
            if(connectedCell == highlightSource){continue;}
            destCoord = connectedCell.cellNode.getRenderPosition(offset, scaleFactor);
            g2d.drawLine(renderCoord.roundX(), renderCoord.roundY(), destCoord.roundX(), destCoord.roundY());
        }


        g2d.dispose();
        return image;
    }
    @Override
    public BufferedImage renderOnImage(BufferedImage image, Vector offset, double scaleFactor) {
        return this.renderConnections(this.renderCell(image, offset, scaleFactor), offset, scaleFactor);
    }
    public void distributeNeighbors(double angle, Vector direction){
        HashSet<Cell> visitedCells = new HashSet<>();
        visitedCells.add(this.cell);
        distributeNeighbors(angle, direction, visitedCells);
    }

    public Coord getRenderPosition(Vector offset, double scaleFactor){
        return toRenderPosition(coord, offset, scaleFactor);
    }

    public static Coord toRenderPosition(Coord coord, Vector offset, double scaleFactor){
        return coord.copy().scale(scaleFactor).add(offset);
    }

    public void goToAverageCoord(){
        Vector average = Vector.ZERO.copy();
        double weight = (double) 1 /cell.connectedCells.size();
        for(Cell connectedCell : cell.connectedCells){
            average.add(Vector.fromCoords(Coord.ZERO, connectedCell.cellNode.coord.copy().scale(weight)));
        }
        this.coord = Coord.ZERO.copy().add(average);
    }
    public void distributeNeighbors(double angle, Vector direction, Set<Cell> visitedCells){
        int connections = cell.connectedCells.size();
        double incrementAngle = angle/(double)connections;
        System.out.println("increment angle: " + incrementAngle);

        Vector initialVector = direction.copy().rotate(Math.PI*2 - (angle/2));
        for(Cell connectedCell : cell.connectedCells){
            if(!visitedCells.contains(connectedCell)){
                visitedCells.add(connectedCell);

                connectedCell.cellNode.coord = this.coord.copy().add(initialVector);
                connectedCell.cellNode.distributeNeighbors(incrementAngle, initialVector.copy(), visitedCells);
                initialVector.rotate(incrementAngle);
            }
        }
    }
}
