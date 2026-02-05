//Import some necessary javafx and java.util modules
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

public class Controller {
    //Necessary attributes for the controller class methods
    private final BooleanProperty upPressed = new SimpleBooleanProperty();
    private final BooleanProperty downPressed = new SimpleBooleanProperty();
    private final BooleanProperty rightPressed = new SimpleBooleanProperty();
    private final BooleanProperty leftPressed = new SimpleBooleanProperty();
    private final BooleanBinding keyPressed = upPressed.or(downPressed).or(rightPressed).or(leftPressed);
    private double movement = 0.3;
    private TankAnimation yellowTankAnimation;
    @FXML
    private ImageView sprite;
    @FXML
    private BorderPane root;
    private Stage stage;
    private Walls walls;
    private ArrayList<WhiteTanks> allTanks;
    private Timeline tankSpawnTimer;
    private final Random random = new Random();
    private boolean isPaused = false;
    private static boolean isEnded = false;
    private static Controller instance;
    private boolean canShoot = true;
    private static final double BULLET_COOLDOWN_SECONDS = 0.2;
    private static final double MIN_SPAWN_TIME = 1.0; // 5 seconds minimum
    private static final double MAX_SPAWN_TIME = 4.0; // 10 seconds maximum
    private Timeline bulletCooldownTimer;
    private static int lives = 3;
    private static int score = 0;
    private final Text pauseTitle = new Text("Paused");
    private final Text pauseScreen = new Text("\npress \"p\" to continue" + "\npress \"r\" to restart the game" + "\npress \"esc\" to exit game");
    private final Text gameover = new Text("Game Over");
    private final Text endScreen = new Text("\npress \"r\" to restart the game" + "\npress \"esc\" to exit game" + "\nfinal score: " + score);
    private static final Text LivesScoreText = new Text("Score: " + score + "\nLives: " + lives);
    //MakeMovable
    public void makeMovableY(Stage thestage, ImageView sprite, BorderPane root, Walls walls, ArrayList<WhiteTanks> allTanks) {
        //Change attributes
        this.sprite = sprite;
        this.root = root;
        this.walls = walls;
        this.allTanks = allTanks;
        this.stage = thestage;
        instance = this;
        pauseScreen.setFill(Color.RED);
        pauseScreen.setFont(Font.font("Arial", 40));
        pauseTitle.setFill(Color.RED);
        pauseTitle.setFont(Font.font("Arial", 60));
        LivesScoreText.setFill(Color.RED);
        root.setTop(LivesScoreText);
        //MovementSetup for the take pressed keys and do something
        movementSetup();
        //Create an animation for our tank from the TankAnimation class
        yellowTankAnimation = new TankAnimation(sprite, "assets/yellowTank");
        //startTankSpawning is a method for creating enemy tanks in the random time frame
        startTankSpawning();
        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if (!aBoolean && !isPaused) {
                timer.start();
                yellowTankAnimation.startAnimate();
            } else {
                timer.stop();
                yellowTankAnimation.stopAnimate();
            }
        }));
    }

    //Our Tanks Moves begins here
    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long timestamp) {
            //check paused and if not paused you can move
            if (isPaused) return;
            //going up if there is no tank or wall
            if (upPressed.get()) {
                if (!wouldCollideWithWalls(sprite.getLayoutX(), sprite.getLayoutY() - movement, "up")) {
                    if (!wouldCollideWithTank(sprite.getLayoutX(), sprite.getLayoutY() - movement, allTanks)) {
                        sprite.setLayoutY(sprite.getLayoutY() - movement);
                    }
                }
            }
            //going down if there is no tank or wall
            if (downPressed.get()) {
                if (!wouldCollideWithWalls(sprite.getLayoutX(), sprite.getLayoutY() + movement, "down")) {
                    if (!wouldCollideWithTank(sprite.getLayoutX(), sprite.getLayoutY() + movement, allTanks)) {
                        sprite.setLayoutY(sprite.getLayoutY() + movement);
                    }
                }
            }
            //going left if there is no tank or wall
            if (leftPressed.get()) {
                if (!wouldCollideWithWalls(sprite.getLayoutX() - movement, sprite.getLayoutY(), "left")) {
                    if (!wouldCollideWithTank(sprite.getLayoutX() - movement, sprite.getLayoutY(), allTanks)) {
                        sprite.setLayoutX(sprite.getLayoutX() - movement);
                    }
                }
            }
            //going right if there is no tank or wall
            if (rightPressed.get()) {
                if (!wouldCollideWithWalls(sprite.getLayoutX() + movement,sprite.getLayoutY(),"right")) {
                    if (!wouldCollideWithTank(sprite.getLayoutX() + movement,sprite.getLayoutY(), allTanks)) {
                        sprite.setLayoutX(sprite.getLayoutX() + movement);
                    }
                }
            }
        }
    };

    private void movementSetup() {
        //if pressed key is P make sure game is paused with togglePause()
        root.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.P) {
                if (!isEnded) {
                    togglePause();
                    return;
                }

            }
            //if game is paused or ended and pressed key is R make sure game is restarted with restartGame()
            if (e.getCode() == KeyCode.R) {
                if (isPaused || isEnded) {
                    restartGame();
                    return;
                }
            }
            //if game is paused or ended and pressed key is ESCAPE make sure game is closed with stage.close()
            if (e.getCode() == KeyCode.ESCAPE) {
                if (isPaused || isEnded) {
                    stage.close();
                }
            }
            //if game is not paused do not do anything below
            if (isPaused) return;
            //move to up
            if (e.getCode() == KeyCode.UP) {
                sprite.rotateProperty().set(-90);
                upPressed.set(true);
            }
            //move to left
            if (e.getCode() == KeyCode.LEFT) {
                sprite.rotateProperty().set(180);
                leftPressed.set(true);
            }
            //move to down
            if (e.getCode() == KeyCode.DOWN) {
                sprite.rotateProperty().set(90);
                downPressed.set(true);
            }
            //move to right
            if (e.getCode() == KeyCode.RIGHT) {
                sprite.rotateProperty().set(0);
                rightPressed.set(true);
            }
            //shoot with shootBullet()
            if (e.getCode() == KeyCode.X) {
                shootBullet();
            }

        });

        root.setOnKeyReleased(e -> {
            //Stop moving up
            if (e.getCode() == KeyCode.UP) {
                upPressed.set(false);
            }
            //Stop moving left
            if (e.getCode() == KeyCode.LEFT) {
                leftPressed.set(false);
            }
            //Stop moving down
            if (e.getCode() == KeyCode.DOWN) {
                downPressed.set(false);
            }
            //Stop moving right
            if (e.getCode() == KeyCode.RIGHT) {
                rightPressed.set(false);
            }
        });
    }
    private void shootBullet() {
        if (canShoot && !isPaused) {
            // Create bullet
            Bullets bullet = new Bullets(root, sprite, false, allTanks, walls);

            // Start cooldown
            canShoot = false;

            // Stop any existing cooldown timer
            if (bulletCooldownTimer != null) {
                bulletCooldownTimer.stop();
            }

            // Create new cooldown timer
            bulletCooldownTimer = new Timeline(new KeyFrame(Duration.seconds(BULLET_COOLDOWN_SECONDS), event -> {
                canShoot = true;
            }));
            bulletCooldownTimer.play();
        }
    }

    private boolean wouldCollideWithWalls(double newX, double newY, String direction) {
        // Get tank bounds at the new position
        double tankRight = newX + sprite.getBoundsInLocal().getWidth();
        double tankBottom = newY + sprite.getBoundsInLocal().getHeight();

        // Check collision with each wall
        for (ImageView wall : walls.getWallSprites()) {
            double wallLeft = wall.getLayoutX();
            double wallRight = wall.getLayoutX() + wall.getBoundsInLocal().getWidth();
            double wallTop = wall.getLayoutY();
            double wallBottom = wall.getLayoutY() + wall.getBoundsInLocal().getHeight();

            // Check if tank and wall rectangles overlap
            boolean horizontalOverlap = newX < wallRight && tankRight > wallLeft;
            boolean verticalOverlap = newY < wallBottom && tankBottom > wallTop;

            if (horizontalOverlap && verticalOverlap) {
                return true; // Collision detected
            }
        }

        return false; // No collision
    }
    public boolean wouldCollideWithTank(double newX, double newY, ArrayList<WhiteTanks> allTanks) {
        // Get tank bounds at the new position
        double tankRight = newX + sprite.getBoundsInLocal().getWidth();
        double tankBottom = newY + sprite.getBoundsInLocal().getHeight();
        // Check collision with each tank
        for (WhiteTanks otherTank : allTanks) {
            // Get enemy tank bounds at the new position
            double otherLeft = otherTank.getSprite().getLayoutX();
            double otherRight = otherTank.getSprite().getLayoutX() + otherTank.getSprite().getBoundsInLocal().getWidth();
            double otherTop = otherTank.getSprite().getLayoutY();
            double otherBottom = otherTank.getSprite().getLayoutY() + otherTank.getSprite().getBoundsInLocal().getHeight();
            // Check if tank and enemy tank rectangles overlap
            boolean horizontalOverlap = newX < otherRight && tankRight > otherLeft;
            boolean verticalOverlap = newY < otherBottom && tankBottom > otherTop;

            if (horizontalOverlap && verticalOverlap) {
                return true; // Collision detected
            }
        }
        return false; // No collision
    }
    private void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            // Pause everything
            timer.stop();
            TankAnimation.stopAllAnimations();
            Bullets.pauseAllBullets();

            // Pause all white tanks
            for (WhiteTanks tank : allTanks) {
                tank.pause();
            }
            root.getChildren().add(pauseTitle);
            pauseTitle.setLayoutX(300);
            pauseTitle.setLayoutY(50);
            root.setCenter(pauseScreen);
        } else {
            root.getChildren().remove(pauseTitle);
            root.setCenter(null);
            if (keyPressed.get()) {
                timer.start();
                TankAnimation.startAllAnimations();
            }
            Bullets.resumeAllBullets();

            // Resume all white tanks
            for (WhiteTanks tank : allTanks) {
                tank.resume();
            }
        }
    }
    private void restartGame() {
        // Stop all timers and animations
        timer.stop();
        TankAnimation.clearAllAnimations();
        Bullets.clearAllBullets(root);

        // Stop and destroy all white tanks
        for (WhiteTanks tank : allTanks) {
            tank.destroy();
        }
        allTanks.clear();
        WhiteTanks.allTanks = new ArrayList<>();

        // Clear all children from root (this removes all game objects)
        root.getChildren().clear();

        // Reset pause state
        isPaused = false;
        isEnded = false;
        root.setTop(null);
        root.setCenter(null);

        // Reset key states
        upPressed.set(false);
        downPressed.set(false);
        leftPressed.set(false);
        rightPressed.set(false);

        score = 0;
        lives = 3;

        // Recreate the game
        initializeGame();
    }

    private void initializeGame() {
        // Recreate walls
        walls = new Walls(root);

        // Recreate yellow tank
        Image tankImage = new Image("assets/yellowTank1.png");
        sprite = new ImageView(tankImage);
        sprite.setLayoutX(400); // Center horizontally
        sprite.setLayoutY(500); // Center vertically
        root.getChildren().add(sprite);

        // Recreate white tanks
        allTanks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            WhiteTanks whiteTank = new WhiteTanks(root, walls, sprite);
            allTanks.add(whiteTank);
            whiteTank.startRandomMovement();
        }
        startTankSpawning();
        // Recreate yellow tank animation
        yellowTankAnimation = new TankAnimation(sprite, "assets/yellowTank");

        // Re-setup key bindings for the new sprite
        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if (!aBoolean && !isPaused) {
                timer.start();
                yellowTankAnimation.startAnimate();
            } else {
                timer.stop();
                yellowTankAnimation.stopAnimate();
            }
        }));
    }
    private void gameOver() {
        //Clear pane
        togglePause();
        root.getChildren().clear();
        //Create gameover screen
        endScreen.setText("\npress \"r\" to restart the game" + "\npress \"esc\" to exit game" + "\nfinal score: " + score);
        gameover.setFill(Color.RED);
        endScreen.setFill(Color.RED);
        gameover.setLayoutX(200);
        gameover.setLayoutY(150);
        gameover.setFont(Font.font("Verdana", 70));
        endScreen.setFont(Font.font("Verdana", 40));
        root.getChildren().add(gameover);
        root.setCenter(endScreen);
    }
    public static void setlives(BorderPane root,ImageView tank, boolean shooted) {
        //add scores
        if (!shooted) {
            score++;
            LivesScoreText.setText("Score: " + score + "\nLives: " + lives);
            root.setTop(LivesScoreText);
        }
        //lose lives
        else {
            lives--;
            if (lives == 0) {
                //check game is ended
                isEnded = true;
                if (instance != null) {
                    instance.gameOver();
                }
            } else {
                LivesScoreText.setText("Score: " + score + "\nLives: " + lives);
                // Reset tank position to original spawn point
                tank.setLayoutX(400);
                tank.setLayoutY(500);
                tank.setVisible(false);
                Timeline respawnTimer = new Timeline(new KeyFrame(Duration.seconds(1.0), e -> {
                    tank.setRotate(0); // Reset rotation to face right
                    tank.setVisible(true);
                }));
                respawnTimer.play();
                root.setTop(LivesScoreText);
            }
        }
    }
    private void startTankSpawning() {
        scheduleNextTankSpawn();
    }

    private void scheduleNextTankSpawn() {
        if (tankSpawnTimer != null) {
            tankSpawnTimer.stop();
        }

        // Generate random spawn time between 5-10 seconds
        double spawnTime = MIN_SPAWN_TIME + (random.nextDouble() * (MAX_SPAWN_TIME - MIN_SPAWN_TIME));

        tankSpawnTimer = new Timeline(new KeyFrame(Duration.seconds(spawnTime), event -> {
            if (!isPaused && !isEnded) {
                spawnNewTank();
                scheduleNextTankSpawn(); // Schedule the next spawn
            }
        }));

        if (!isPaused && !isEnded) {
            tankSpawnTimer.play();
        }
    }

    private void spawnNewTank() {
        if (!isEnded) {
            //spawning new enemy tanks
            WhiteTanks newTank = new WhiteTanks(root, walls, sprite);
            allTanks.add(newTank);
            newTank.startRandomMovement();
        }
    }
}
