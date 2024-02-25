package org.anax.graphminesweeper.render;

import org.anax.graphminesweeper.game.Coord;
import org.anax.graphminesweeper.game.Vector;

import java.awt.image.BufferedImage;

public interface IRenderable {
    public BufferedImage renderOnImage(BufferedImage image, Vector offset, double scaleFactor);
}
