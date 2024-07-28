package org.anax.graphminesweeper.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Window {
    public JFrame frame;
    JPanel panel;
    public JLabel label;
    Robot robot;

    public int width;
    public int height;

    public volatile boolean isMouse1Down;
    public volatile boolean isUnprocessedMouse1Press = false;
    public volatile boolean isUnprocessedMouse1Release = false;
    public volatile boolean isUnprocessedMouse3Press = false;
    public volatile boolean isShiftDown = false;

    public volatile boolean[] unprocessedKeyPresses = new boolean[KeyEvent.RESERVED_ID_MAX];
    public volatile boolean[] unprocessedKeyReleases = new boolean[KeyEvent.RESERVED_ID_MAX];
    public volatile boolean[] downKeys = new boolean[KeyEvent.RESERVED_ID_MAX];

    public volatile int mouse1DownX;
    public volatile int mouse1DownY;

    public volatile boolean isWDown;
    public volatile boolean isSDown;
    public volatile boolean isADown;
    public volatile boolean isDDown;


    public volatile boolean isUpDown;
    public volatile boolean isDownDown;
    public volatile boolean isLeftDown;
    public volatile boolean isRightDown;

    public volatile boolean isSpaceDown;

    private boolean disableMouseMotion = false;
    public volatile int mouseXMotionChange = 0;
    public volatile int mouseYMotionChange = 0;

    public volatile float scaleAmount = 0;
    float sensitivity = -0.03f;

    public Window(int width, int height){
        this.width = width;
        this.height = height;

        frame = new JFrame();
        panel = new JPanel();
        label = new JLabel();
        panel.add(label);
        frame.add(panel);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                unprocessedKeyPresses[e.getKeyCode()] = true;
                downKeys[e.getKeyCode()] = true;
                if(e.getKeyCode() == KeyEvent.VK_SHIFT){isShiftDown = true;}
                if(e.getKeyCode() == KeyEvent.VK_W){isWDown = true;}
                if(e.getKeyCode() == KeyEvent.VK_S){isSDown = true;}
                if(e.getKeyCode() == KeyEvent.VK_A){isADown = true;}
                if(e.getKeyCode() == KeyEvent.VK_D){isDDown = true;}

                if(e.getKeyCode() == KeyEvent.VK_UP){isUpDown = true;}
                if(e.getKeyCode() == KeyEvent.VK_DOWN){isDownDown = true;}
                if(e.getKeyCode() == KeyEvent.VK_LEFT){isLeftDown = true;}
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){isRightDown = true;}
                if(e.getKeyCode() == KeyEvent.VK_SPACE){isSpaceDown = true;}

                super.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                unprocessedKeyReleases[e.getKeyCode()] = true;
                downKeys[e.getKeyCode()] = false;
                if(e.getKeyCode() == KeyEvent.VK_SHIFT){isShiftDown = false;}
                if(e.getKeyCode() == KeyEvent.VK_W){isWDown = false;}
                if(e.getKeyCode() == KeyEvent.VK_S){isSDown = false;}
                if(e.getKeyCode() == KeyEvent.VK_A){isADown = false;}
                if(e.getKeyCode() == KeyEvent.VK_D){isDDown = false;}

                if(e.getKeyCode() == KeyEvent.VK_UP){isUpDown = false;}
                if(e.getKeyCode() == KeyEvent.VK_DOWN){isDownDown = false;}
                if(e.getKeyCode() == KeyEvent.VK_LEFT){isLeftDown = false;}
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){isRightDown = false;}
                if(e.getKeyCode() == KeyEvent.VK_SPACE){isSpaceDown = false;}

                super.keyReleased(e);
            }
        });
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1){
                    mouse1DownX = e.getX();
                    mouse1DownY = e.getY();
                    isMouse1Down = true;
                    isUnprocessedMouse1Press = true;
                }
                if(e.getButton() == MouseEvent.BUTTON3){
                    isUnprocessedMouse3Press = true;
                }
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1){
                    isMouse1Down = false;
                    isUnprocessedMouse1Release = true;
                }
                super.mouseReleased(e);
            }
        });

        label.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(disableMouseMotion){return;}

                int centerX = label.getWidth()/2;
                int centerY = label.getHeight()/2;

                mouseXMotionChange += e.getX() - centerX;
                mouseYMotionChange += e.getY() - centerY;

                disableMouseMotion = true;
                robot.mouseMove(label.getLocationOnScreen().x + centerX, label.getLocationOnScreen().y + centerY);
                disableMouseMotion = false;

                super.mouseMoved(e);
            }
        });

        label.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                scaleAmount = sensitivity*e.getUnitsToScroll();
                super.mouseWheelMoved(e);
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Window.this.width = frame.getWidth();
                Window.this.height = frame.getHeight();
                super.componentResized(e);
            }
        });

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
    }
    public void setImage(BufferedImage image){
        label.setIcon(new ImageIcon(image));
        SwingUtilities.updateComponentTreeUI(frame);
    }
}
