package org.anax.graphminesweeper.game;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    public Set<Cell> connectedCells;
    public CellNode cellNode;
    public Cell(){
        cellNode = new CellNode(this, 0, 0);
        connectedCells = new HashSet<>();
    }
    public Cell(Set<Cell> connectedCells){
        this.connectedCells = connectedCells;
    }

    public CellNode getCellNode(){
        if(this.cellNode == null){
            this.cellNode = new CellNode(this, 0, 0);
        }
        return cellNode;
    }

    public void connectCell(Cell cell){
        if(cell == null){return;}
        connectedCells.add(cell);
        cell.connectedCells.add(this);
    }
    public void removeCell(Cell cell){
        connectedCells.remove(cell);
    }
    public Set<Cell> getConnectedCells(){
        return connectedCells;
    }
    public Set<Cell> getSubTreeCells(){
        Set<Cell> set = new HashSet<>();
        set.add(this);
        addConnectedCells(set);
        return set;
    }
    public void addConnectedCells(Set<Cell> cells){
        cells.add(this);
        for(Cell cell : connectedCells){
            if(!cells.contains(cell)){
                cells.add(cell);
                cell.addConnectedCells(cells);
            }
        }
    }


}
