package de.lucavon.pipebird.entities;

import de.lucavon.pipebird.PipeBirdGame;
import de.lucavon.pipebird.Location;

import java.awt.*;

public class PipeScoreEntity extends GameEntity {

    private ObstactlePipeEntity topPipe;
    private int height;
    private boolean scored;

    public PipeScoreEntity(ObstactlePipeEntity topPipe, int height) {
        //-3 because width is 6, to have it centered exactly
        super(new Location(topPipe.getLocation().getX() + (topPipe.getHitbox().width / 2) - 3,
                (int)(topPipe.getLocation().getZ() + topPipe.getHitbox().getHeight())), null);
        this.topPipe = topPipe;
        this.height = height;
    }

    @Override
    protected void updateHitbox() {
        setHitbox(new Rectangle(getLocation().getX(), getLocation().getZ(), 6, height));
    }

    @Override
    public void gameTick() {
        Location loc = new Location(topPipe.getLocation().getX() + (topPipe.getHitbox().width / 2), (int)(topPipe.getLocation().getZ() + topPipe.getHitbox().getHeight()));
        setLocation(loc);
        updateHitbox();
    }

    @Override
    public void handleIntersect(GameEntity intersector) {
        if(scored)
            return;
        scored = true;
        PipeBirdGame.getInstance().incrementScore();
    }
}
