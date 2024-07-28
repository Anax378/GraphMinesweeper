package org.anax.graphminesweeper.render;

import org.anax.graphminesweeper.game.Camera;
import java.awt.image.BufferedImage;

public interface IRenderable {
    public BufferedImage renderOnImage(BufferedImage image, Camera camera);
}
