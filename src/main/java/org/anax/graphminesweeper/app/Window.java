package org.anax.graphminesweeper.app;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Window {
    public JFrame frame;
    JPanel panel;
    public JLabel label;

    public int width;
    public int height;

    public volatile boolean isMouse1Down;
    public volatile boolean isUnprocessedMouse1Press = false;
    public volatile boolean isUnprocessedMouse1Release = false;
    public volatile boolean isUnprocessedMouse3Press = false;
    public volatile boolean isShiftDown = false;
    public volatile boolean[] unprocessedKeyPresses = new boolean[KeyEvent.RESERVED_ID_MAX];
    public volatile boolean[] unprocessedKeyReleases = new boolean[KeyEvent.RESERVED_ID_MAX];
    public volatile int mouse1DownX;
    public volatile int mouse1DownY;
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

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                unprocessedKeyPresses[e.getKeyCode()] = true;
                if(e.getKeyCode() == KeyEvent.VK_SHIFT){isShiftDown = true;}
                super.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                unprocessedKeyReleases[e.getKeyCode()] = true;
                if(e.getKeyCode() == KeyEvent.VK_SHIFT){isShiftDown = false;}
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
