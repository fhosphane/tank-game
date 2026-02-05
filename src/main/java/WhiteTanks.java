//Import some necessary javafx and java.util modules
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

public class WhiteTanks {
    //Necessary attributes for the TankAnimation class methods
    private final ImageView sprite;
    private final BorderPane scene;
    private final Walls walls;
    private double movement = 0.3;
    private final Random random = new Random();
    private final TankAnimation tankAnimation;
    private static final String[] DIRECTIONS = {"up", "down", "left", "right"};
    private String currentDirection;
    private Timeline directionTimer;
    private Timeline bulletTimer;
    private AnimationTimer movementTimer;
    private boolean isMoving = false;
    private boolean isPaused = false;
    private final ImageView yellowTank;
    public static ArrayList<ImageView> allTanks = new ArrayList<>();
    public WhiteTanks(BorderPane root, Walls walls, ImageView yellowTank) {
        this.scene = root;
        this.walls = walls;
        this.yellowTank = yellowTank;
        if (!allTanks.contains(yellowTank)) {
            allTanks.add(yellowTank);
        }

        // Create white tank sprite
        Image tankImage = new Image("assets/whiteTank1.png");
        sprite = new ImageView(tankImage);
        allTanks.add(sprite);

        // Spawn in upper part of map (y between 50-150 to avoid immediate wall collision)
        double spawnX, spawnY;
        int tries = 0;
        do {
            spawnX = 50 + random.nextDouble() * (750 - 50);
            spawnY = 50 + random.nextDouble() * 50;
            sprite.setLayoutX(spawnX);
            sprite.setLayoutY(spawnY);
            tries++;
        } while (
            tries < 50 &&
            (wouldCollideWithTank(spawnX, spawnY, allTanks)
            || wouldCollideWithWalls(spawnX, spawnY, "spawn"))
        );
        scene.getChildren().add(sprite);
        tankAnimation = new TankAnimation(sprite, "assets/whiteTank");
    }
    public void startRandomMovement() {
        //start movement with choosing a new direction with chooseNewDirection()
        chooseNewDirection();

        movementTimer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                if (isMoving && !isPaused) {
                    moveInCurrentDirection();
                }
            }
        };

        movementTimer.start();
    }

    private void chooseNewDirection() {
        // Don't choose new direction if paused
        if (isPaused) return;

        // Stop current movement
        isMoving = false;
        tankAnimation.stopAnimate();

        // Choose new random direction
        currentDirection = DIRECTIONS[random.nextInt(DIRECTIONS.length)];

        // Set tank rotation based on direction
        switch (currentDirection) {
            case "up":
                sprite.rotateProperty().set(-90);
                break;
            case "down":
                sprite.rotateProperty().set(90);
                break;
            case "left":
                sprite.rotateProperty().set(180);
                break;
            case "right":
                sprite.rotateProperty().set(0);
                break;
        }

        // Start moving in new direction
        isMoving = true;
        if (!isPaused) {
            tankAnimation.startAnimate();
        }

        // Set timer for minimum 3 seconds (3000ms) + random additional time (0-2 seconds)
        double movementDuration = 1.0 + (random.nextDouble() * 3.0); // 3-5 seconds
        double bulletDuration = (random.nextDouble() * 2.0); // 3-5 seconds
        if (directionTimer != null) {
            directionTimer.stop();
        }
        if (bulletTimer != null) {
            bulletTimer.stop();
        }
        bulletTimer = new Timeline(new KeyFrame(Duration.seconds(bulletDuration), e -> {
            Bullets bullet = new Bullets(scene,sprite,false,yellowTank,walls);
        }));
        directionTimer = new Timeline(new KeyFrame(Duration.seconds(movementDuration), e -> {
            chooseNewDirection(); // Choose new direction after timer expires
        }));
        if (!isPaused) {
            bulletTimer.play();
            directionTimer.play();
        }
    }

    private void moveInCurrentDirection() {
        //set current direction
        double currentX = sprite.getLayoutX();
        double currentY = sprite.getLayoutY();
        double newX = currentX;
        double newY = currentY;

        // Calculate new position based on current direction
        switch (currentDirection) {
            case "up":
                newY = currentY - movement;
                break;
            case "down":
                newY = currentY + movement;
                break;
            case "left":
                newX = currentX - movement;
                break;
            case "right":
                newX = currentX + movement;
                break;
        }

        // Check for collision before moving
        if (!wouldCollideWithWalls(newX, newY, currentDirection)) {
            if (!wouldCollideWithTank(newX, newY, allTanks)) {
                sprite.setLayoutX(newX);
                sprite.setLayoutY(newY);
            } else {
                chooseNewDirection();
            }

        } else {
            // If collision detected, choose new direction immediately
            chooseNewDirection();
        }
    }

    private boolean wouldCollideWithWalls(double newX, double newY, String direction) {
        // Get tank bounds at the new position
        double tankLeft = newX;
        double tankRight = newX + sprite.getBoundsInLocal().getWidth();
        double tankTop = newY;
        double tankBottom = newY + sprite.getBoundsInLocal().getHeight();

        // Check bounds of the scene first
        if (tankLeft < 0 || tankRight > 800 || tankTop < 0 || tankBottom > 600) {
            return true;
        }

        // Check collision with each wall
        for (ImageView wall : walls.getWallSprites()) {
            double wallLeft = wall.getLayoutX();
            double wallRight = wall.getLayoutX() + wall.getBoundsInLocal().getWidth();
            double wallTop = wall.getLayoutY();
            double wallBottom = wall.getLayoutY() + wall.getBoundsInLocal().getHeight();

            // Check if tank and wall rectangles overlap
            boolean horizontalOverlap = tankLeft < wallRight && tankRight > wallLeft;
            boolean verticalOverlap = tankTop < wallBottom && tankBottom > wallTop;

            if (horizontalOverlap && verticalOverlap) {
                return true; // Collision detected
            }
        }

        return false; // No collision
    }

    // Method to check collision with other tanks (for future use)
    public boolean wouldCollideWithTank(double newX, double newY, ArrayList<ImageView> allTanks) {
        // Get tank bounds at the new position
        double tankLeft = newX;
        double tankRight = newX + sprite.getBoundsInLocal().getWidth();
        double tankTop = newY;
        double tankBottom = newY + sprite.getBoundsInLocal().getHeight();
        for (ImageView otherTank : allTanks) {
            if (otherTank == sprite) {
                continue;
            }
            // Get other tank bounds at the new position
            double otherLeft = otherTank.getLayoutX();
            double otherRight = otherTank.getLayoutX() + otherTank.getBoundsInLocal().getWidth();
            double otherTop = otherTank.getLayoutY();
            double otherBottom = otherTank.getLayoutY() + otherTank.getBoundsInLocal().getHeight();
            // Check if tank and other tank rectangles overlap
            boolean horizontalOverlap = tankLeft < otherRight && tankRight > otherLeft;
            boolean verticalOverlap = tankTop < otherBottom && tankBottom > otherTop;

            if (horizontalOverlap && verticalOverlap) {
                return true; // Collision detected
            }
        }
        return false; // No collision
    }

    // Cleanup method
    public void destroy() {
        if (movementTimer != null) {
            movementTimer.stop();
        }
        if (directionTimer != null) {
            directionTimer.stop();
        }
        if (tankAnimation != null) {
            tankAnimation.stopAnimate();
        }
        scene.getChildren().remove(sprite);
        allTanks.remove(sprite);
    }

    public ImageView  getSprite() {
        return sprite;
    }
    public void pause() {
        isPaused = true;

        // Pause timers
        if (directionTimer != null) {
            directionTimer.pause();
        }
        if (bulletTimer != null) {
            bulletTimer.pause();
        }
    }

    public void resume() {
        isPaused = false;

        // Resume timers
        if (directionTimer != null) {
            directionTimer.play();
        }
        if (bulletTimer != null) {
            bulletTimer.play();
        }
    }

}
