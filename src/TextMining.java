// Java Program to illustrate reading from
// FileReader using FileReader

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TextMining extends Application {
    public static BaseDeTweets bdt = new BaseDeTweets();

    public static void main(String[] args) throws Exception {
        Application.launch(TextMining.class, args);
    }

    public static List<String> stock (BufferedReader br) throws IOException {
        List<String> lstStr = new ArrayList<String>();
        String str;
        while (( str = br.readLine()) != null) {
            lstStr.add(str);
        }
        return lstStr;
    }



    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Importation de text
        FileReader foot =
                new FileReader(".\\data\\Foot.txt");
        BufferedReader tampon = new BufferedReader(foot);
        LineNumberReader count = new LineNumberReader(foot);
        //Liste des lignes
        List<String> lstStrLignes = stock(tampon);
        //bdt.ajoute(new Tweets(lstStrLignes.get(0)));
        //bdt.ajoute(new Tweets(lstStrLignes.get(1)));
        for(int i = 0; i<lstStrLignes.size();i++)
        {
            //System.out.println(i);
            Tweets t = new Tweets(lstStrLignes.get(i));
            bdt.ajoute(t);
        }
        bdt.trierParText();

        TableView<Tweets> table = new TableView<>();
        final ObservableList<Tweets> data =
                FXCollections.observableArrayList(bdt.getTweets());

        final Label label = new Label("Tweets");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn userCol = new TableColumn("Utilisateur");
        userCol.setCellValueFactory(
                new PropertyValueFactory<Tweets, String>("strUtilisateur"));

        TableColumn dateCol = new TableColumn("Date");
        dateCol.setCellValueFactory(
                new PropertyValueFactory<Tweets, String>("strDate"));

        TableColumn timeCol = new TableColumn("Heure");
        timeCol.setCellValueFactory(
                new PropertyValueFactory<Tweets, String>("strTime"));

        TableColumn<Tweets, Boolean> brtCol = new TableColumn<>("Retweet");
        brtCol.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getbRT()));


        TableColumn userRTCol = new TableColumn("Utilisateur Retweeté");
        userRTCol.setCellValueFactory(
                new PropertyValueFactory<Tweets, String>("strUtilisateurRT"));

        TableColumn<Tweets, Number> rtCol = new TableColumn("Nombre de RT");
        rtCol.setCellValueFactory( cellData -> new SimpleIntegerProperty(cellData.getValue().getnNbRT()));

        TableColumn textCol = new TableColumn("Contenu du tweet");
        textCol.setCellValueFactory(
                new PropertyValueFactory<Tweets, String>("strText"));


        FilteredList<Tweets> flTweets= new FilteredList(data, p -> true);//Pass the data to a filtered list
        table.setItems(flTweets);//Set the table's items using the filtered list
        table.getColumns().addAll(userCol, dateCol, rtCol, brtCol, userRTCol, timeCol, textCol);

        //Adding ChoiceBox and TextField here!
        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("Utilisateur", "Contenu du tweet");
        choiceBox.setValue("Utilisateur");
        TextField textField = new TextField();
        textField.setPromptText("Search here!");
        textField.setOnKeyReleased(keyEvent ->
        {
            switch (choiceBox.getValue())//Switch on choiceBox value
            {
                case "Utilisateur":
                    flTweets.setPredicate(p -> p.getStrUtilisateur().toLowerCase().contains(textField.getText().toLowerCase().trim()));

                    break;
                case "Contenu du tweet":
                    flTweets.setPredicate(p -> p.getStrText().toLowerCase().contains(textField.getText().toLowerCase().trim()));

                    break;
            }
        });

        Label allLabel = new Label();
        allLabel.textProperty().bind(Bindings.size(table.getItems()).asString("All items: %s"));




        primaryStage.setTitle("TextMining");

        HBox hBox = new HBox(choiceBox, textField);
        VBox root = new  VBox();

        root.setSpacing(10);
        root.setPadding(new Insets(15,20, 10,10));

        Scene scene = new Scene(root, 1000, 700, Color.WHITE);

        table.prefHeightProperty().bind(primaryStage.heightProperty());
        table.prefWidthProperty().bind(primaryStage.widthProperty());

        root.getChildren().addAll(label,table, hBox, allLabel);
        primaryStage.setScene(scene);
        primaryStage.show();



        List<String> WORDS = new ArrayList<>();

        for(Tweets t: bdt.getTweets())
        {
            String[] twords = t.getStrText().split(" ");
            for(String s : twords)
            {
                WORDS.add(s);
            }

        }


        JFrame frame = new JFrame(TextMining.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        Cloud cloud = new Cloud();
        Random random = new Random();
        for (String s : WORDS) {
            for (int i = random.nextInt(50); i > 0; i--) {
                cloud.addTag(s);
            }
        }
        for (Tag tag : cloud.tags()) {
            final JLabel jlabel = new JLabel(tag.getName());
            jlabel.setOpaque(false);
            jlabel.setFont(jlabel.getFont().deriveFont((float) tag.getWeight() * 100));
            panel.add(jlabel);
        }

        frame.add(panel);
        frame.setSize(800, 600);
        frame.setVisible(true);



    }
}

