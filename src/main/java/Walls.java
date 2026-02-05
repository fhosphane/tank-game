//Import some necessary javafx and java.util modules
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;

public class Walls {
    //Necessary attributes for the TankAnimation class methods
    int wallWidth = 16;
    int wallHeight = 14;
    int windowWidth = 800;
    int windowHeight = 600;
    public ArrayList<ImageView> wallSprites;
    public Walls(BorderPane root) {
        wallSprites = new ArrayList<>();
        Image wallImage = new Image("assets/wall.png");
        // Top wall
        for (int i = 0; i < windowWidth / wallWidth; i++) {
            ImageView wallSprite = new ImageView(wallImage);
            wallSprite.setLayoutX(i * wallWidth);
            wallSprite.setLayoutY(0);
            root.getChildren().add(wallSprite);
            wallSprites.add(wallSprite);
        }
        // Bottom wall
        for (int i = 0; i < windowWidth / wallWidth; i++) {
            ImageView wallSprite = new ImageView(wallImage);
            wallSprite.setLayoutX(i * wallWidth);
            wallSprite.setLayoutY(windowHeight - wallHeight);
            root.getChildren().add(wallSprite);
            wallSprites.add(wallSprite);
        }
        // Left wall (skip corners to avoid overlap)
        for (int i = 1; i < (windowHeight / wallHeight); i++) {
            ImageView wallSprite = new ImageView(wallImage);
            wallSprite.setLayoutX(0);
            wallSprite.setLayoutY(i * wallHeight);
            root.getChildren().add(wallSprite);
            wallSprites.add(wallSprite);
        }
        // Right wall (skip corners to avoid overlap)
        for (int i = 1; i < (windowHeight / wallHeight); i++) {
            ImageView wallSprite = new ImageView(wallImage);
            wallSprite.setLayoutX(windowWidth - wallWidth);
            wallSprite.setLayoutY(i * wallHeight);
            root.getChildren().add(wallSprite);
            wallSprites.add(wallSprite);
        }
        //other walls in the map
        for (int i = 1; i < 11; i++) {
            ImageView wallSprite = new ImageView(wallImage);
            wallSprite.setLayoutY(300);
            wallSprite.setLayoutX(i * wallHeight+320);
            root.getChildren().add(wallSprite);
            wallSprites.add(wallSprite);
        }
        //other walls in the map
        for (int i = 1; i < 11; i++) {
            ImageView wallSprite = new ImageView(wallImage);
            wallSprite.setLayoutY(300+wallHeight);
            wallSprite.setLayoutX(i * wallHeight+320);
            root.getChildren().add(wallSprite);
            wallSprites.add(wallSprite);
        }
        //other walls in the map
        for (int i = 1; i < 31; i++) {
            ImageView wallSprite = new ImageView(wallImage);
            wallSprite.setLayoutY(200);
            wallSprite.setLayoutX(i * wallHeight+180);
            root.getChildren().add(wallSprite);
            wallSprites.add(wallSprite);
        }
        //other walls in the map
        for (int i = 1; i < 31; i++) {
            ImageView wallSprite = new ImageView(wallImage);
            wallSprite.setLayoutY(200+wallHeight);
            wallSprite.setLayoutX(i * wallHeight+180);
            root.getChildren().add(wallSprite);
            wallSprites.add(wallSprite);
        }
        //other walls in the map
        for (int i = 1; i < 15; i++) {
            ImageView wallSprite = new ImageView(wallImage);
            wallSprite.setLayoutY(i * wallHeight+300);
            wallSprite.setLayoutX(150);
            root.getChildren().add(wallSprite);
            wallSprites.add(wallSprite);
        }
        //other walls in the map
        for (int i = 1; i < 15; i++) {
            ImageView wallSprite = new ImageView(wallImage);
            wallSprite.setLayoutY(i * wallHeight+300);
            wallSprite.setLayoutX(150-wallWidth);
            root.getChildren().add(wallSprite);
            wallSprites.add(wallSprite);
        }
        //other walls in the map
        for (int i = 1; i < 15; i++) {
            ImageView wallSprite = new ImageView(wallImage);
            wallSprite.setLayoutY(i * wallHeight+300);
            wallSprite.setLayoutX(650);
            root.getChildren().add(wallSprite);
            wallSprites.add(wallSprite);
        }
        //other walls in the map
        for (int i = 1; i < 15; i++) {
            ImageView wallSprite = new ImageView(wallImage);
            wallSprite.setLayoutY(i * wallHeight+300);
            wallSprite.setLayoutX(650+wallWidth);
            root.getChildren().add(wallSprite);
            wallSprites.add(wallSprite);
        }
    }
    public ArrayList<ImageView> getWallSprites() {
        return wallSprites;
    }
}
