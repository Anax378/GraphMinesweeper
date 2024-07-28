package org.anax.graphminesweeper.game;

import java.awt.image.BufferedImage;
import java.util.*;

public class GameGraph {
    public Set<CellNode> cells;
    public double repulsionConstant = 3000f;
    public double connectionSprigConstant = 0.01f;
    public double speedCoefficient = 1f;

    public GameGraph(){
        cells = new HashSet<>();
    }

    public void addCell(Cell cell){
        cells.add(cell.cellNode);
    }
    public void addAll(Cell[] cells){
        for(Cell cell : cells){
            addCell(cell);
        }
    }

    public void addAll(CellNode[] cells){
        for(CellNode node : cells){
            addCell(node.cell);
        }
    }

    public void addConnectedCells(Cell cell1, Cell cell2){
        cell1.connectedCells.add(cell2);
        cell2.connectedCells.add(cell1);
        cells.add(cell1.cellNode);
        cells.add(cell2.cellNode);
    }

    public double getCellRepulsionForce(CellNode cell1, CellNode cell2){
        double distance = (double) Math.sqrt(Math.pow(cell1.coord.x-cell2.coord.x, 2) + Math.pow(cell1.coord.y-cell2.coord.y, 2));
        return (1f/(distance*distance))*repulsionConstant;
    }

    public double getConnectedCellsAttractionForce(CellNode cell1, CellNode cell2){
        double distance = Math.sqrt(Math.pow(cell1.coord.x-cell2.coord.x, 2) + Math.pow(cell1.coord.y-cell2.coord.y, 2));
        return connectionSprigConstant*distance;
    }

    public BufferedImage render(Camera camera){
        BufferedImage image = new BufferedImage(camera.width, camera.height, BufferedImage.TYPE_INT_RGB);

        CellNode[] cellsArray = new CellNode[cells.size()];

        int i = 0;
        for(CellNode node : cells){
            cellsArray[i] = node;
            i++;
        }

        Arrays.sort(cellsArray, Comparator.comparingDouble(o -> camera.getScaleFactor(o.coord)));
        for(CellNode node : cellsArray){
            node.renderConnections(image, camera);
        }
        for(CellNode node : cellsArray){
            node.renderCell(image, camera);
        }
        return image;
    }

    public void center(){
        double averageX = 0;
        double averageY = 0;
        double averageZ = 0;
        int size = cells.size();

        for(CellNode node : cells){
            averageX += node.coord.x*((double) 1 /size);
            averageY += node.coord.y*((double) 1 /size);
            averageZ += node.coord.z*((double) 1 /size);
        }
        Vector averageOffset = new Vector(averageX, averageY, averageZ).scale(-1);

        for(CellNode node : cells){
            node.coord.add(averageOffset);
        }
    }
    public void advanceTime(double time, int subSteps){
        double timeStep = time/subSteps;
        for(int i = 0; i < subSteps; i++){
            for(CellNode cellNode : cells){
                Vector forces = new Vector(0, 0, 0);
                for(Cell connectedCell : cellNode.cell.connectedCells){
                    forces.add(Vector.fromCoords(cellNode.coord, connectedCell.cellNode.coord).scaleTo(getConnectedCellsAttractionForce(cellNode, connectedCell.cellNode)));
                }
                for(CellNode cell : cells){
                    if(!cellNode.equals(cell)){
                        forces.add(Vector.fromCoords(cell.coord, cellNode.coord).scaleTo(getCellRepulsionForce(cell, cellNode)));
                    }
                }
                cellNode.velocity = (forces.scale(timeStep));
            }
        }
        for(CellNode node : cells){
            node.applyVelocity();
        }

    }

}
