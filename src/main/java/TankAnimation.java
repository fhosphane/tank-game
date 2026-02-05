//Import some necessary javafx and java.util modules
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.ArrayList;

public class TankAnimation {
    //Necessary attributes for the TankAnimation class methods
    private final ImageView Tank;
    private int num = 1;
    private final Timeline timeline;
    private static boolean isPaused = false;
    private static final ArrayList<TankAnimation> allAnimations = new ArrayList<>();
    private final String tankName;

    public TankAnimation(ImageView Tank, String name) {
        this.Tank = Tank;
        this.tankName = name;

        // Create individual timeline for each tank
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            if(num == 1){
                getTank().setImage(new Image(tankName + "2.png"));
                num = 2;
            } else if(num == 2){
                getTank().setImage(new Image(tankName + "1.png"));
                num = 1;
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        allAnimations.add(this);
    }

    public void startAnimate(){
        // Start animation of the object
        if (!isPaused && timeline != null) {
            timeline.play();
        }
    }

    public void stopAnimate(){
        // Stop animation of the object
        if (timeline != null) {
            timeline.stop();
        }
    }

    public static void startAllAnimations(){
        // Start all animations
        isPaused = false;
        for (TankAnimation animation : allAnimations) {
            animation.startAnimate();
        }
    }

    public static void stopAllAnimations(){
        // Stop all animations
        for (TankAnimation animation : allAnimations) {
            animation.stopAnimate();
        }
    }
    public static void clearAllAnimations() {
        // Stop all animations first
        for (TankAnimation animation : allAnimations) {
            animation.stopAnimate();
        }

        // Clear the list
        allAnimations.clear();
        isPaused = false;
    }

    public ImageView getTank() {
        return Tank;
    }
}