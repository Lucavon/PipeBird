package de.lucavon.pipebird;

import de.lucavon.pipebird.entities.Bird;
import de.lucavon.pipebird.entities.GameEntity;
import de.lucavon.pipebird.entities.ObstactlePipeEntity;
import de.lucavon.pipebird.entities.PipeScoreEntity;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class PipeBirdGame extends JFrame {

    private static PipeBirdGame instance;

    private GraphicsPanel panel;
    private Timer gameTimer;
    private long tick = 0;

    private final int windowWidth = 600, windowHeight = 600;

    private int score = 0;

    private int pipePairDistance = 400;
    private int pipeMinVertDistance = 120;
    private int pipeMaxVertDistance = 160;
    private int pipeMoveSpeed = 4;
    private int pipeUnitsMoved = 0;
    private ArrayList<GameEntity> entities;

    //Ticks & frames per second
    private int gameSpeed = 60;

    private boolean gameReady;
    private boolean dead;

    public PipeBirdGame() {
        instance = this;

        reset();
        System.out.println("Game setup complete.");
    }

    private void reset() {
        gameReady = false;
        if(gameTimer != null)
            gameTimer.cancel();
        pipeUnitsMoved = 0;
        score = 0;
        entities = null;
        gameSpeed = 60;
        setupFrame();
        setupGame();
        setupTimer();
        gameReady = true;
    }

    private void setupFrame() {
        System.out.println("Setting up frame.");
        setTitle("PipeBird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new GraphicsPanel();
        add(panel);

        System.out.println("Adding key listener.");
        addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {
                //SPACE (32) for jumping, R (82) for respawning.
                if(e.getKeyCode() != 32 && e.getKeyCode() != 82)
                    return;
                if(!dead && e.getKeyCode() == 32) {
                    Bird b = (Bird)entities.get(0);
                    b.setVertMovement(b.getVertMovement() - 3f);
                } else if(dead && e.getKeyCode() == 82) {
                    reset();
                }
            }

            public void keyReleased(KeyEvent e) {

            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        System.out.println("Frame setup complete.");
    }

    private void setupGame() {
        System.out.println("Setting up game.");
        entities = new ArrayList<>();
        Bird bird = new Bird(new Location(50, windowHeight / 2));
        entities.add(bird);
        spawnPipes(400);
        spawnPipes(800);
        dead = false;
        System.out.println("Game setup complete.");
    }

    private void setupTimer() {
        System.out.println("Setting up timer.");
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameTick();
            }
        }, 0, 1000/gameSpeed);
        System.out.println("Timer setup complete.");
    }

    private void gameTick() {
        Bird b = (Bird)entities.get(0);
        HashSet<GameEntity> remove = new HashSet<>();
        for(GameEntity entity : entities) {
            if(!(entity instanceof Bird) && b.getHitbox().intersects(entity.getHitbox())) {
                entity.handleIntersect(b);
            }

            //Remove entities that go off screen, allow 500 pixels to the right side for pipes spawning in advance - every 20th tick, for optimization
            if(tick % 20 == 0 && (entity.getLocation().getX() < 0 - (entity.getSprite() != null ? entity.getSprite().getWidth() : 0)
                    || entity.getLocation().getX() > windowWidth + (entity.getSprite() != null ? entity.getSprite().getWidth() : 0) + 500
                    || entity.getLocation().getZ() < 0 - (entity.getSprite() != null ? entity.getSprite().getHeight() : 0)
                    || entity.getLocation().getZ() > windowHeight + (entity.getSprite() != null ? entity.getSprite().getHeight() : 0))) {
                if(entity instanceof Bird) {
                    System.out.println("Bird got off the screen... HOW!? Teleporting bird to middle of screen.");
                    entity.setLocation(new Location(50, windowHeight / 2));
                    continue;
                }
                remove.add(entity);
            }
            entity.gameTick();
        }

        entities.removeAll(remove);


        if(pipeUnitsMoved / pipePairDistance >= 1.0f) {
            int x = windowWidth + (windowWidth % pipePairDistance);
            spawnPipes(x);
            pipeUnitsMoved = 0;
        }

        panel.repaint();
        tick += 1;
        pipeUnitsMoved += pipeMoveSpeed;
    }

    public void birdHit() {
        //Maybe add lives?
        System.out.println("Bird got hit!");
        gameTimer.cancel();
        dead = true;
        //Todo: death notification
    }

    public void spawnPipes(int x) {
        int pipeVertDist = ThreadLocalRandom.current().nextInt(pipeMinVertDistance, pipeMaxVertDistance);
        int minZ = (pipeVertDist / 2) + 40, maxZ = windowHeight - (pipeVertDist / 2) - 40;
        int z = ThreadLocalRandom.current().nextInt(minZ, maxZ);
        ObstactlePipeEntity bottomPipe = new ObstactlePipeEntity(new Location(x, z + (pipeVertDist / 2)), true);
        //Since the sprites have the same height, this should work out.
        ObstactlePipeEntity topPipe = new ObstactlePipeEntity(new Location(x, z - (pipeVertDist / 2) - bottomPipe.getSprite().getHeight()), false);
        PipeScoreEntity scoreEntity = new PipeScoreEntity(topPipe, pipeVertDist);
        entities.add(topPipe);
        entities.add(scoreEntity);
        entities.add(bottomPipe);
    }

    public void incrementScore() {
        score += 1;
    }

    public static PipeBirdGame getInstance() {
        return instance;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public boolean isGameReady() {
        return gameReady;
    }

    public int getPipeMoveSpeed() {
        return pipeMoveSpeed;
    }

    public ArrayList<GameEntity> getEntities() {
        return entities;
    }
    public int getScore() {
        return score;
    }

    public boolean isDead() {
        return dead;
    }
}
