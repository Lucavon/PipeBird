package de.lucavon.pipebird.entities;

import de.lucavon.pipebird.PipeBirdGame;
import de.lucavon.pipebird.Location;

import java.awt.image.BufferedImage;

public class Bird extends GameEntity {

    private float vertMovement;

    public Bird(Location location) {
        super(location, "PipeBird.png");
        vertMovement = 0;
    }

    @Override
    public void gameTick() {
        Location location = getLocation();
        BufferedImage sprite = getSprite();
        if(vertMovement < 0) {
            vertMovement += 0.075;
        }
        else if(vertMovement < 1) {
            vertMovement += 0.15;
        }
        else {
            vertMovement = 1;
        }

        float moveFactor = vertMovement <= 0 ? 4.5f : 5f;
        int zChange = (int)Math.ceil(moveFactor * vertMovement);

        int newZ = location.getZ() + zChange;
        if(newZ > PipeBirdGame.getInstance().getWindowHeight() - sprite.getHeight()) {
            newZ = PipeBirdGame.getInstance().getWindowHeight() - sprite.getHeight();
            PipeBirdGame.getInstance().birdHit();
        } else if(newZ < 0) {
            newZ = 0;
            //PipeBirdGame.getInstance().birdHit(); //Roof should not be deadly.
        }
        location.setZ(newZ);

        updateHitbox();
    }

    public void setVertMovement(float vertMovement) {
        if(vertMovement < -1f)
            vertMovement = -1.75f;
        if(vertMovement < -3f)
            vertMovement = -3f;
        this.vertMovement = vertMovement;
    }

    public float getVertMovement() {
        return vertMovement;
    }

    @Override
    public void handleIntersect(GameEntity intersector) {}
}
