package org.anax.graphminesweeper;

import org.anax.graphminesweeper.app.Window;
import org.anax.graphminesweeper.game.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Window w = new Window(1080-500, 1080-500);
        GameGraph graph = new GameGraph();

        Scanner scanner = new Scanner(System.in);
        System.out.print("number of cells: ");
        int cell_count = Integer.parseInt(scanner.nextLine());
        System.out.print("number of connections: ");
        int connections = Integer.parseInt(scanner.nextLine());
        System.out.print("mine density (from 0.0 to 1.0): ");
        double density = Double.parseDouble(scanner.nextLine());

        Cell[] cells = new Cell[cell_count];

        Random random = new Random();

        Arrays.setAll(cells, i -> {
            Cell cell = new Cell();
            cell.cellNode.coord.add(new Vector(random.nextFloat(cells.length*10), random.nextFloat(cells.length*10)));
            cell.cellNode.box.isMine = random.nextDouble(1) < density;
            return cell;
        });

        while(connections > 0){
            int a = random.nextInt(cells.length);
            int b = random.nextInt(cells.length);
            if(a != b){
                cells[a].connectCell(cells[b]);
                connections--;
            }
        }

        Cell rootCell = null;
        int maxConnections = 0;
        for(Cell cell : cells){
            if(cell.connectedCells.size() > maxConnections){
                maxConnections = cell.connectedCells.size();
                rootCell = cell;
            }
        }


        while(true){
            Set<Cell> subTreeCells = rootCell.getSubTreeCells();
            boolean canBreak = true;
            for(Cell cell : cells){
                if(!subTreeCells.contains(cell)){
                    ((Cell)subTreeCells.toArray()[random.nextInt(subTreeCells.size())]).connectCell(cell);
                    canBreak = false;
                }
            }
            if(canBreak){break;}
        }

        graph.addAll(cells);

        long start = System.currentTimeMillis();

        Vector offset = new Vector(0, 0);
        Vector anchor = new Vector(200, 200);

        offset.add(Vector.fromCoords(Coord.ZERO, rootCell.cellNode.coord));

        Point lastMousePosition = new Point(0, 0);
        float scaleFactor = 1;

        int simulationTime = 0;
        for(int i = 0; i < simulationTime; i++){
            System.out.println("simulating graph " + i + "/" + simulationTime);
            graph.advanceTime(1, 1);
        }

        boolean isInDragMode = false;
        boolean disableMovement = false;
        Cell selectedCell = null;

        for(CellNode cell : graph.cells){
            cell.box.refreshMineCount();
        }

        while(true){
            if(w.isUnprocessedMouse3Press){
                w.isUnprocessedMouse3Press = false;
                Coord mousePos = new Coord(lastMousePosition.x, lastMousePosition.y);
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
                Coord mousePos = new Coord(lastMousePosition.x, lastMousePosition.y);
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
        }
    }
}