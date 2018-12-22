package de.lucavon.pipebird.entities;

import de.lucavon.pipebird.Location;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public abstract class GameEntity {

    private static final HashMap<String, BufferedImage> spriteCache = new HashMap<>();

    private Location location;
    private Rectangle hitbox;
    private BufferedImage sprite;

    public GameEntity(Location location, String spriteFileName) {
        this.location = location;
        if(spriteFileName != null)
            loadSprite(spriteFileName);
        updateHitbox();
    }

    protected void loadSprite(String fileName) {
        if(spriteCache.containsKey(fileName)) {
            sprite = spriteCache.get(fileName);
            return;
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            sprite = ImageIO.read(cl.getResourceAsStream(fileName));
            System.out.println(fileName + " sprite loaded - " + sprite.getWidth() + " pixels wide and " + sprite.getHeight() + " pixels high.");
            if(sprite == null) {
                System.out.println("Sprite " + fileName + " is null!");
                System.exit(0);
                return;
            }
            spriteCache.put(fileName, sprite);
        } catch (IOException e) {
            e.printStackTrace();
            //Kill game. Nobody likes invisible entities.
            System.exit(0);
            return;
        }
    }

    protected void updateHitbox() {
        hitbox = new Rectangle(location.getX(), location.getZ(), sprite.getWidth(), sprite.getHeight());
    }

    public abstract void gameTick();

    public abstract void handleIntersect(GameEntity intersector);

    public static HashMap<String, BufferedImage> getSpriteCache() {
        return spriteCache;
    }

    public Location getLocation() {
        return location;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
}
