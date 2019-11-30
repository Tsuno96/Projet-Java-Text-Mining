
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class TextMining extends Application {

    public static void main(String[] args) {
        Application.launch(TextMining.class, args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World");
        Group root = new Group();
        Scene scene = new Scene(root, 300, 250, Color.LIGHTGREEN);

        Circle cercle = new Circle();
        cercle.setCenterX(300);
        cercle.setCenterY(200);
        cercle.setRadius(100);
        cercle.setFill(Color.YELLOW);
        cercle.setStroke(Color.ORANGE);
        cercle.setStrokeWidth(5);

        Button btn = new Button();
        btn.setLayoutX(100);
        btn.setLayoutY(80);
        btn.setText("Hello World");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.out.println("Hello World");
            }
        });
        root.getChildren().add(btn);
        root.getChildren().add(cercle);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

