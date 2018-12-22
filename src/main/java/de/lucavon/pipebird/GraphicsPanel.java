package de.lucavon.pipebird;

import de.lucavon.pipebird.entities.GameEntity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GraphicsPanel extends JPanel {

    BufferedImage background;

    public GraphicsPanel() {
        setPreferredSize(new Dimension(PipeBirdGame.getInstance().getWindowWidth(), PipeBirdGame.getInstance().getWindowHeight()));
        setBackground(Color.BLACK);
        background = loadImageFromResources("Brickwall1.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        PipeBirdGame f = PipeBirdGame.getInstance();
        if(!f.isGameReady())
            return;
        int width = getWidth();
        int height = getHeight();
        for (int x = 0; x < width; x += background.getWidth()) {
            for (int y = 0; y < height; y += background.getHeight()) {
                g.drawImage(background, x, y, null);
            }
        }
        for(GameEntity ent : f.getEntities()) {
            if(ent.getSprite() != null)
                g.drawImage(ent.getSprite(), ent.getLocation().getX(), ent.getLocation().getZ(), null);
        }
        g.setColor(Color.RED);
        g.drawString("Score: " + PipeBirdGame.getInstance().getScore(), 10, 15);
        if(PipeBirdGame.getInstance().isDead()) {
            g.drawString("You died! Press R to respawn.", 10, 28);
        }
        /*for(GameEntity ent : f.getEntities()) {
            if(ent instanceof PipeScoreEntity) {
                g.setColor(Color.red);
                g.drawRect(ent.getLocation().getX(), ent.getLocation().getZ(), ent.getHitbox().width, ent.getHitbox().height);
            }
        }*/
    }

    public static BufferedImage loadImageFromResources(String fileName) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        BufferedImage sprite = null;
        try {
            sprite = ImageIO.read(cl.getResourceAsStream(fileName));
            System.out.println(fileName + " sprite loaded - " + sprite.getWidth() + " pixels wide and " + sprite.getHeight() + " pixels high.");
        } catch (IOException e) {
            e.printStackTrace();
            //Kill game. Nobody likes missing textures.
            System.exit(0);
            return null;
        }
        return sprite;
    }
}
