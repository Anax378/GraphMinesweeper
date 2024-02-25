package org.anax.graphminesweeper.game;

import java.util.HashSet;
import java.util.Set;

public class Box {
    public Cell cell;
    public boolean isCovered = true;
    public boolean isFlagged = false;
    public boolean isMine = false;
    public int mines;
    public Box(Cell cell){
        this.cell = cell;
    }

    public void refreshMineCount(){
        int m = 0;
        for(Cell cell : cell.connectedCells){
            if (cell.cellNode.box.isMine){m++;}
        }
        this.mines = m;
    }

    public void setFlag(boolean flag){
        this.isFlagged = flag;
    }

    public boolean uncover(){
        this.isCovered = false;
        return isMine;
    }

    public boolean uncoverChain(){
        return uncoverChain(new HashSet<>());
    }
    public boolean uncoverChain(Set<Cell> visitedCells){
        visitedCells.add(this.cell);
        if(this.mines == 0){
            for(Cell c : cell.connectedCells){
                if(!visitedCells.contains(c)){
                    visitedCells.add(this.cell);
                    c.cellNode.box.uncoverChain(visitedCells);
                }
            }
        }
        return uncover();
    }

}
