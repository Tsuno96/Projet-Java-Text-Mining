// Java Program to illustrate reading from
// FileReader using FileReader

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


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

        FileReader stopWords =
                new FileReader(".\\data\\StopWords.txt");
        BufferedReader tamponSW = new BufferedReader(stopWords);
        List<String> lstSW = stock(tamponSW);


        // Importation des données
        FileReader fr =
                new FileReader(".\\data\\Foot.txt");
        BufferedReader tampon = new BufferedReader(fr);


        //Liste des lignes
        List<String> lstStrLignes = stock(tampon);
        for(int i = 0; i<lstStrLignes.size();i++)
        {
            //System.out.println(i);
            Tweets t = new Tweets(lstStrLignes.get(i));
            bdt.ajoute(t);
        }
        bdt.trierParText();

        //Collections.sort(bdt.getTweets(), new SortByRt());

        //Creation du tableau de données
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
        brtCol.setCellValueFactory(
                cellData -> new SimpleBooleanProperty(cellData.getValue().getbRT()));

        TableColumn userRTCol = new TableColumn("Utilisateur Retweeté");
        userRTCol.setCellValueFactory(
                new PropertyValueFactory<Tweets, String>("strUtilisateurRT"));

        TableColumn<Tweets, Number> rtCol = new TableColumn("Nombre de RT");
        rtCol.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getnNbRT()));

        TableColumn textCol = new TableColumn("Contenu du tweet");
        textCol.setCellValueFactory(
                new PropertyValueFactory<Tweets, String>("strText"));


        FilteredList<Tweets> flTweets= new FilteredList(data, p -> true);//Pass the data to a filtered list
        SortedList<Tweets> sortedTweets = new SortedList<>(flTweets);
        sortedTweets.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedTweets);//Set the table's items using the filtered list

        table.getColumns().addAll(userCol, dateCol, rtCol, brtCol, userRTCol, timeCol, textCol);


        //Adding ChoiceBox and TextField here!
        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("Utilisateur", "Contenu du tweet");
        choiceBox.setValue("Utilisateur");
        TextField textField = new javafx.scene.control.TextField();
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




        List<String> lstStrWords = new ArrayList<>();

        List<String> textTweets = new ArrayList<>();
        for(Tweets t: bdt.getTweets())
        {
            textTweets.add(t.getStrText());
        }


        System.out.println("ProgressBar Spliting Tweets :");
        double pourcentage = 0;
        for(Tweets t: bdt.getTweets())
        {
            double it =  bdt.getTweets().indexOf(t) +1 ;
            if (pourcentage != Math.round((it / bdt.getTweets().size()) * 100))
            {
                pourcentage = Math.round((it / bdt.getTweets().size()) * 100);
                String progress = new String(pourcentage+"% ");
                System.out.print(progress);
            }

            String[] twords = t.getStrText().replaceAll("[^A-Za-z]+", " ").split(" ");
            for(String s : twords)
            {
                if (s.length() > 1) {
                    lstStrWords.add(s.toLowerCase());
                }
            }
        }


        //lowerCase(lstWords);
        lstStrWords.removeAll(lstSW);
        Set<String> uniqueWords = new TreeSet<>(lstStrWords);

        List<Words> infoWords = new ArrayList<>();

        Map<String, Integer> frequencyWords = countFrequencies(lstStrWords);

        for (Map.Entry me : frequencyWords.entrySet())
        {
            infoWords.add(new Words(me.getKey().toString(),(Integer)me.getValue(),0,0,0));

        }

        Collections.sort(infoWords, new CountComparator());

        System.out.println("\n");
        for (int i = 0; i<=10;i++)
        {
            System.out.println(infoWords.get(i).getText()+" " + infoWords.get(i).getCount());
        }

        table.prefHeightProperty().bind(primaryStage.heightProperty());
        table.prefWidthProperty().bind(primaryStage.widthProperty());
        root.getChildren().addAll(label,table, hBox, allLabel);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void lowerCase(List<String> strings)
    {
        ListIterator<String> iterator = strings.listIterator();
        while (iterator.hasNext())
        {
            iterator.set(iterator.next().toLowerCase());
        }
    }

    public static int count(List<String> words,String term)
    {
        int count = 0;
        int idterm = words.indexOf(term);

        for (int i = idterm; term.equalsIgnoreCase(words.get(i));i++)
        {
            count++;
            if (i+1 == words.size())
            {
                break;
            }
        }

        return count;
    }

    public static Map countFrequencies(List<String> list)
    {
        // hashmap to store the frequency of element
        Map<String, Integer> hm = new HashMap<String, Integer>();

        for (String i : list) {
            Integer j = hm.get(i);
            hm.put(i, (j == null) ? 1 : j + 1);
        }

        return hm;
    }

}

