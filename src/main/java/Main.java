//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
//Importing necessary javafx modules
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.util.ArrayList;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    //TANK 2025
    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Tank Game");

        // Creating a BorderPane
        BorderPane root = new BorderPane();

        //Creating a scene and making background is black
        Scene theScene = new Scene(root, 800, 600);
        theScene.setFill(Paint.valueOf("Black"));

        // Creating text for the starting menu page
        Text title = new Text("TANK 2025");
        Text text = new Text("Press any key to start the game");
        title.setFill(Color.DARKCYAN);
        text.setFill(Color.RED);
        root.getChildren().add(title);
        title.setLayoutX(200);
        title.setLayoutY(200);
        title.setFont(Font.font("Verdana", 70));
        text.setFont(Font.font("Verdana", 30));
        title.setSmooth(true);
        title.setStroke(Color.BROWN);
        title.setStrokeWidth(2);
        root.setCenter(text);
        theScene.getRoot().setFocusTraversable(true);
        theScene.getRoot().requestFocus();
        theStage.setScene(theScene);
        theStage.show();

        //if any key pressed game will start
        theScene.setOnKeyPressed(e -> {
            //Clearing border pane
            root.setCenter(null);
            root.getChildren().remove(title);
            theScene.setOnKeyPressed(null);
            //Create walls from wall class
            Walls walls = new Walls(root);

            // Create the tank sprite
            Image tankImage = new Image("assets/yellowTank1.png");
            ImageView tankSprite = new ImageView(tankImage);
            // Position the tank initially
            tankSprite.setLayoutX(400);
            tankSprite.setLayoutY(500);

            // Add the tank to the scene
            root.getChildren().add(tankSprite);

            //Add initial enemy tanks
            ArrayList<WhiteTanks> whiteTanks = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                WhiteTanks whiteTank = new WhiteTanks(root, walls, tankSprite);
                whiteTanks.add(whiteTank);
                whiteTank.startRandomMovement();
            }

            // Create and set up the controller
            Controller controller = new Controller();
            controller.makeMovableY(theStage, tankSprite, root, walls, whiteTanks);
        });
    }
}