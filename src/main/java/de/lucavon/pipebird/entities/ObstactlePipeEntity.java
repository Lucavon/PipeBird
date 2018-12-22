package de.lucavon.pipebird.entities;

import de.lucavon.pipebird.PipeBirdGame;
import de.lucavon.pipebird.Location;

public class ObstactlePipeEntity extends GameEntity {

    private boolean wideSideUp;

    public ObstactlePipeEntity(Location location, boolean wideSideUp) {
        super(location, wideSideUp ? "PipeBirdPipeUp.png" : "PipeBirdPipeDown.png");
    }

    @Override
    public void gameTick() {
        Location loc = getLocation();
        int newX = loc.getX() - PipeBirdGame.getInstance().getPipeMoveSpeed();
        loc.setX(newX);
        updateHitbox();
    }

    @Override
    public void handleIntersect(GameEntity intersector) {
        if(!(intersector instanceof Bird)) {
            System.out.println("A pipe intersected with a non-bird GameEntity! Type: " + intersector.getClass().getName());
            return;
        }

        PipeBirdGame.getInstance().birdHit();
    }
}
