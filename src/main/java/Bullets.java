//Import some necessary javafx and java.util modules
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import java.util.ArrayList;

public class Bullets {
    //Necessary attributes for the TankAnimation class methods
    private boolean enemy;
    private final ImageView bullet;
    private Timeline timeline = new Timeline();
    private static final ArrayList<Timeline> allBulletTimelines = new ArrayList<>();
    private static final ArrayList<ImageView> allBulletSprites = new ArrayList<>();

    public Bullets(BorderPane scene, ImageView tank, boolean enemy, ArrayList<WhiteTanks> allTanks, Walls walls) {
        Image bulletImage = new Image("assets/bullet.png");
        bullet = new ImageView(bulletImage);
        bullet.rotateProperty().set(tank.rotateProperty().get());
        double direction = bullet.rotateProperty().get();
        //Move the position for the direction of tank
        switch ((int) direction) {
            case 0:
                bullet.setLayoutX(tank.getBoundsInLocal().getWidth() / 2 + tank.getLayoutX());
                bullet.setLayoutY(tank.getBoundsInLocal().getHeight() / 2 + tank.getLayoutY() - 6);
                break;
            case 90:
                bullet.setLayoutY(tank.getBoundsInLocal().getHeight() / 2 + tank.getLayoutY());
                bullet.setLayoutX(tank.getBoundsInLocal().getWidth() / 2 + tank.getLayoutX() - 6);
                break;
            case 180:
                bullet.setLayoutX(tank.getLayoutX());
                bullet.setLayoutY(tank.getBoundsInLocal().getHeight() / 2 + tank.getLayoutY() - 6);
                break;
            case -90:
                bullet.setLayoutY(tank.getLayoutY());
                bullet.setLayoutX(tank.getBoundsInLocal().getWidth() / 2 + tank.getLayoutX() - 6);
                break;
        }
        scene.getChildren().add(bullet);
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), event -> {
            //Movement of bullet for the directions
            switch ((int) direction) {
                case 0:
                    bullet.setLayoutX(bullet.getLayoutX() + 5);
                    break;
                case 90:
                    bullet.setLayoutY(bullet.getLayoutY() + 5);
                    break;
                case 180:
                    bullet.setLayoutX(bullet.getLayoutX() - 5);
                    break;
                case -90:
                    bullet.setLayoutY(bullet.getLayoutY() - 5);
                    break;
            }
            if (CollisionwithWalls(walls)) {
                //Create explosion and delete bullet if bullet collide with walls
                Image exp = new Image("assets/smallExplosion.png");
                ImageView smallExplosion = new ImageView(exp);
                smallExplosion.setX(bullet.getLayoutX());
                smallExplosion.setY(bullet.getLayoutY());
                scene.getChildren().remove(bullet);
                scene.getChildren().add(smallExplosion);
                timeline.stop();
                Timeline explosionTimeline = new Timeline(new KeyFrame(Duration.seconds(0.2), e -> {
                    scene.getChildren().remove(smallExplosion);
                }));
                explosionTimeline.play();
                allBulletTimelines.remove(timeline);
            }
            ArrayList<WhiteTanks> tanksToCheck = new ArrayList<>(allTanks);
            for (WhiteTanks whiteTank : tanksToCheck) {
                //Create explosion and delete bullet if bullet collide with white tanks
                if (CollisionwithTanks(whiteTank.getSprite())) {
                    Image exp = new Image("assets/explosion.png");
                    ImageView Explosion = new ImageView(exp);
                    Explosion.setX(bullet.getLayoutX());
                    Explosion.setY(bullet.getLayoutY());
                    scene.getChildren().remove(bullet);
                    Controller.setlives(scene,tank,false); //add score
                    whiteTank.destroy();
                    allTanks.remove(whiteTank);
                    scene.getChildren().add(Explosion);
                    timeline.stop();
                    allBulletTimelines.remove(timeline);
                    Timeline explosionTimeline = new Timeline(new KeyFrame(Duration.seconds(0.4), e -> {
                        scene.getChildren().remove(Explosion);
                    }));
                    explosionTimeline.play();
                    allBulletTimelines.remove(timeline);
                    break; // Exit the loop since we found a collision
                }
            }

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        allBulletTimelines.add(timeline);
    }

    public Bullets(BorderPane scene, ImageView tank, boolean enemy, ImageView yellowTank, Walls walls) {
        Image bulletImage = new Image("assets/bullet.png");
        bullet = new ImageView(bulletImage);
        bullet.rotateProperty().set(tank.rotateProperty().get());
        double direction = bullet.rotateProperty().get();
        //Move the position for the direction of tank
        switch ((int) direction) {
            case 0:
                bullet.setLayoutX(tank.getBoundsInLocal().getWidth() / 2 + tank.getLayoutX());
                bullet.setLayoutY(tank.getBoundsInLocal().getHeight() / 2 + tank.getLayoutY() - 6);
                break;
            case 90:
                bullet.setLayoutY(tank.getBoundsInLocal().getHeight() / 2 + tank.getLayoutY());
                bullet.setLayoutX(tank.getBoundsInLocal().getWidth() / 2 + tank.getLayoutX() - 6);
                break;
            case 180:
                bullet.setLayoutX(tank.getLayoutX());
                bullet.setLayoutY(tank.getBoundsInLocal().getHeight() / 2 + tank.getLayoutY() - 6);
                break;
            case -90:
                bullet.setLayoutY(tank.getLayoutY());
                bullet.setLayoutX(tank.getBoundsInLocal().getWidth() / 2 + tank.getLayoutX() - 6);
                break;
        }
        scene.getChildren().add(bullet);
        allBulletSprites.add(bullet);
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), event -> {
            //Movement of bullet for the directions
            switch ((int) direction) {
                case 0:
                    bullet.setLayoutX(bullet.getLayoutX() + 5);
                    break;
                case 90:
                    bullet.setLayoutY(bullet.getLayoutY() + 5);
                    break;
                case 180:
                    bullet.setLayoutX(bullet.getLayoutX() - 5);
                    break;
                case -90:
                    bullet.setLayoutY(bullet.getLayoutY() - 5);
                    break;
            }
            if (CollisionwithWalls(walls)) {
                //Create explosion and delete bullet if bullet collide with walls
                Image exp = new Image("assets/smallExplosion.png");
                ImageView smallExplosion = new ImageView(exp);
                smallExplosion.setX(bullet.getLayoutX());
                smallExplosion.setY(bullet.getLayoutY());
                scene.getChildren().remove(bullet);
                scene.getChildren().add(smallExplosion);
                timeline.stop();
                Timeline explosionTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
                    scene.getChildren().remove(smallExplosion);
                }));
                explosionTimeline.play();
                allBulletTimelines.remove(timeline);
            }
            if (CollisionwithTanks(yellowTank)) {
                //Create explosion and delete bullet if bullet collide with our tank
                Image exp = new Image("assets/explosion.png");
                ImageView Explosion = new ImageView(exp);
                Explosion.setX(bullet.getLayoutX());
                Explosion.setY(bullet.getLayoutY());
                scene.getChildren().remove(bullet);
                Controller.setlives(scene,yellowTank,true);  //lose lives
                scene.getChildren().add(Explosion);
                timeline.stop();
                allBulletTimelines.remove(timeline);
                timeline.stop();
                allBulletTimelines.remove(timeline);
                Timeline explosionTimeline = new Timeline(new KeyFrame(Duration.seconds(0.4), e -> {
                    scene.getChildren().remove(Explosion);
                }));
                explosionTimeline.play();
                allBulletTimelines.remove(timeline);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        allBulletTimelines.add(timeline);
    }
    //Control the collision with tanks
    public boolean CollisionwithTanks(ImageView sprite) {
        if (sprite.getBoundsInParent().intersects(bullet.getBoundsInParent())) {
            return true;
        }
        return false;
    }
    //Control the collision with walls
    public boolean CollisionwithWalls(Walls walls) {
        // Check collision with each wall
        for (ImageView wall : walls.getWallSprites()) {
            if (wall.getBoundsInParent().intersects(bullet.getBoundsInParent()))
                return true;
        }
        return false;
    }
    //Pause all bullets
    public static void pauseAllBullets() {
        for (Timeline bulletTimeline : allBulletTimelines) {
            bulletTimeline.pause();
        }
    }
    //Resume all Bullets
    public static void resumeAllBullets() {
        for (Timeline bulletTimeline : allBulletTimelines) {
            bulletTimeline.play();
        }
    }
    //Delete all bullets
    public static void clearAllBullets(BorderPane scene) {
        // Stop all bullet timelines
        for (Timeline bulletTimeline : allBulletTimelines) {
            bulletTimeline.stop();
        }
        // Remove all bullet sprites from scene
        for (ImageView bulletSprite : allBulletSprites) {
            scene.getChildren().remove(bulletSprite);
        }
        // Clear the lists
        allBulletTimelines.clear();
        allBulletSprites.clear();
    }
}
