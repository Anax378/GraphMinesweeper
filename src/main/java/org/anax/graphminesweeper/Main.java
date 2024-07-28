package org.anax.graphminesweeper;

import org.anax.graphminesweeper.app.Window;
import org.anax.graphminesweeper.game.*;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        Window w = new Window((int) (1920*0.8f), (int) (1080*0.7f));
        GameGraph graph = new GameGraph();

        Random random = new Random();

        int cellCount = 25;
        int spawnBoundingBoxSize = 100;
        int connections = 100;

        Cell[] cells = new Cell[cellCount];
        for(int i = 0; i < cellCount; i++){
            cells[i] = new Cell(new Coord(random.nextInt(spawnBoundingBoxSize), random.nextInt(spawnBoundingBoxSize), random.nextInt(spawnBoundingBoxSize)));
        }

        for(int i = 0; i < connections; i++){
            cells[random.nextInt(cellCount)].connectCell(cells[random.nextInt(cellCount)]);
        }

        graph.addAll(cells);

        for(CellNode cell : graph.cells){
            cell.box.refreshMineCount();
        }

        Camera camera = null;

        RotationMatrix3D yawPlus = RotationMatrix3D.fromYAngle(Math.toRadians(0.5));
        RotationMatrix3D yawMinus = RotationMatrix3D.fromYAngle(Math.toRadians(-0.5));

        Vector WMove = new Vector(0, 0, 1);
        Vector SMove = WMove.scale(-1);
        Vector AMove = new Vector(-1, 0, 0);
        Vector DMove = AMove.scale(-1);
        Vector spaceMove = new Vector(0, 1, 0);
        Vector shiftMove = spaceMove.scale(-1);

        long start = System.currentTimeMillis();

        while(!w.unprocessedKeyPresses[KeyEvent.VK_ESCAPE]){

            if(w.unprocessedKeyPresses[KeyEvent.VK_R] || camera == null){
                w.unprocessedKeyPresses[KeyEvent.VK_R] = false;
                camera = new Camera(w.width, w.height, new Vector(0, 0, 1), new Coord(0, 0, 0));
                camera.setFocalLengthFromFOV(Math.toRadians(170));
            }
            w.setImage(graph.render(camera));
            if(System.currentTimeMillis()-start > 1000/20f){
                graph.advanceTime(100, 5);
                start = System.currentTimeMillis();

                double m = w.downKeys[KeyEvent.VK_CONTROL] ? 2 : 1;
                if(w.isWDown){camera.relativeMove(WMove.scale(m));}
                if(w.isSDown){camera.relativeMove(SMove.scale(m));}
                if(w.isADown){camera.relativeMove(AMove.scale(m));}
                if(w.isDDown){camera.relativeMove(DMove.scale(m));}
                if(w.isSpaceDown){camera.relativeMove(spaceMove.scale(m));}
                if(w.isShiftDown){camera.relativeMove(shiftMove.scale(m));}
            }

            if(w.mouseXMotionChange != 0){
                boolean isPlus = w.mouseXMotionChange < 0;
                int rot = Math.abs(w.mouseXMotionChange);
                for(int i = 0; i < rot; i++){
                    camera.rotate(isPlus ? yawPlus : yawMinus);
                }
                w.mouseXMotionChange = 0;
            }

            if(w.mouseYMotionChange != 0){
                camera.rotate(-Math.toRadians(0.5)* w.mouseYMotionChange, camera.getRight());
                w.mouseYMotionChange = 0;
            }

/*
            if(w.isUnprocessedMouse3Press){
                w.isUnprocessedMouse3Press = false;
                Coord2D mousePos = new Coord2D(lastMousePosition.x, lastMousePosition.y);
                for(CellNode cell : graph.cells){
                    if(cell.getRenderPosition(offset, scaleFactor).distance(mousePos) < cell.radius*scaleFactor){
                        cell.box.isFlagged = !cell.box.isFlagged;
                        boolean won = true;
                        for(CellNode node : graph.cells){
                            if(node.box.isCovered && !node.box.isMine){
                                won = false;
                            }
                            if(!node.box.isCovered && node.box.isMine){
                                won = false;
                            }
                        }
                        if(won){
                            System.out.println("YOU WON!");
                        for(CellNode node : graph.cells){
                            if(node.box.isMine){
                                node.box.isFlagged = true;
                            }
                        }
                        }
                        break;
                    }
                }

            }

            if(w.isUnprocessedMouse1Press){
                w.isUnprocessedMouse1Press = false;
                Coord2D mousePos = new Coord2D(lastMousePosition.x, lastMousePosition.y);
                for(Cell cell : cells){
                    if(cell.cellNode.getRenderPosition(offset, scaleFactor).distance(mousePos) < (cell.cellNode.radius*scaleFactor)){
                        if(w.isShiftDown){
                            selectedCell = cell;
                            isInDragMode = true;
                        }else{
                            if(!cell.cellNode.box.isFlagged){
                                if(cell.cellNode.box.uncoverChain()){
                                    System.out.println("YOU LOST!");
                                    for(CellNode other : graph.cells){
                                        other.box.uncover();
                                    }
                                }
                            }
                       }
                        disableMovement = true;
                    }
                }
            }
            if(w.isUnprocessedMouse1Release){
                w.isUnprocessedMouse1Release = false;
                isInDragMode = false;
                disableMovement = false;
            }
            if(isInDragMode && selectedCell != null){
                Coord mousePosition = new Coord(lastMousePosition.x, lastMousePosition.y);
                mousePosition.add(offset.copy().scale(-1)).scale(1/scaleFactor);
                selectedCell.cellNode.coord = mousePosition;
            }

            if(w.unprocessedKeyPresses[KeyEvent.VK_C]){
                w.unprocessedKeyPresses[KeyEvent.VK_C] = false;
                offset = new Vector((double) w.width /2, (double) w.height /2);
            }
            if(w.unprocessedKeyPresses[KeyEvent.VK_R]){
                w.unprocessedKeyPresses[KeyEvent.VK_R] = false;
                offset.add(Vector.fromCoords(Coord.ZERO, rootCell.cellNode.getRenderPosition(offset, scaleFactor)).scale(-1)).add(new Vector((double) w.width /2, (double) w.height /2));
            }
            if(w.unprocessedKeyPresses[KeyEvent.VK_D]){
                w.unprocessedKeyPresses[KeyEvent.VK_D] = false;
                for(CellNode cell : graph.cells){
                    cell.coord.scale(0.1);
                }
            }
            if(w.unprocessedKeyPresses[KeyEvent.VK_A]){
                w.unprocessedKeyPresses[KeyEvent.VK_A] = false;
                for(Cell cell : cells){
                    cell.cellNode.goToAverageCoord();
                }
            }
            if(w.unprocessedKeyPresses[KeyEvent.VK_N]){
                w.unprocessedKeyPresses[KeyEvent.VK_N] = false;
                graph.repulsionConstant = 0;
            }
            if(w.unprocessedKeyReleases[KeyEvent.VK_N]){
                w.unprocessedKeyReleases[KeyEvent.VK_N] = false;
                graph.repulsionConstant = 3000f;
            }

            if(System.currentTimeMillis() - start > 20);{
                boolean foundCell = false;
                for(Cell cell : cells){
                    Coord mousePos = new Coord(lastMousePosition.x, lastMousePosition.y);
                    if(cell.cellNode.getRenderPosition(offset, scaleFactor).distance(mousePos) < (cell.cellNode.radius*scaleFactor)){
                        CellNode.highlightSource = cell;
                        foundCell = true;
                    }
                }
                if(!foundCell){
                    CellNode.highlightSource = null;
                }
                start = System.currentTimeMillis();
                graph.advanceTime(1, 1);
                graph.center();
            }
            Point mp = w.label.getMousePosition();
            if(mp != null){
                lastMousePosition = mp;
            }
            if(w.isMouse1Down && !(isInDragMode || disableMovement)){
                Vector mouseMovement = new Vector(lastMousePosition.x-w.mouse1DownX, lastMousePosition.y-w.mouse1DownY);
                offset = anchor.copy().add(mouseMovement);
            }
            else{
                anchor = offset.copy();
            }
            if(w.scaleAmount != 0){
                if(scaleFactor+w.scaleAmount > 0){
                    Coord cp = new Coord(lastMousePosition.x, lastMousePosition.y);
                    Coord newCursorPos = cp.copy().add(offset.copy().scale(-1)).scale(1/scaleFactor).scale(scaleFactor+w.scaleAmount).add(offset);
                    scaleFactor += w.scaleAmount;
                    offset.add(Vector.fromCoords(newCursorPos, cp));
                }
                w.scaleAmount = 0;
            }
            w.setImage(graph.render(w.width, w.height, offset, scaleFactor));

 */
        }
        w.frame.dispatchEvent(new WindowEvent(w.frame, WindowEvent.WINDOW_CLOSING));

    }
}