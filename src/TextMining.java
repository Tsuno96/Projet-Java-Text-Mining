// Java Program to illustrate reading from
// FileReader using FileReader

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
        for (int i = 0; i < lstStrLignes.size(); i++) {
            //System.out.println(i);
            Tweets t = new Tweets(lstStrLignes.get(i));
            bdt.ajoute(t);
        }

        bdt.trierParText();
        bdt.compteurRT();

        //Creation du tableau de données
        TableView<Tweets> tableView = new TableView<>();

        final Label label = new Label("Tweets");
        label.setFont(new Font("Arial", 20));
        tableView.setEditable(true);


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

        final ObservableList<Tweets>[] data = new ObservableList[]{FXCollections.observableArrayList(bdt.getTweets())};

        final FilteredList<Tweets>[] flTweets = new FilteredList[]{new FilteredList(data[0], p -> true)};//Pass the data to a filtered list
        tableView.setItems(flTweets[0]);//Set the table's items using the filtered list

        tableView.getColumns().addAll(userCol, dateCol, rtCol, brtCol, userRTCol, timeCol, textCol);

        //Adding ChoiceBox and TextField here!
        ChoiceBox<String> choiceBox = new ChoiceBox();
        TextField textField = new javafx.scene.control.TextField();
        createSearchBar(choiceBox, textField, flTweets);

        Label allLabel = new Label();
        allLabel.textProperty().bind(Bindings.size(tableView.getItems()).asString("Nombre d'items: %s"));

        List<String> lstStrWords = new ArrayList<>();

        List<String> textTweets = new ArrayList<>();
        for (Tweets t : bdt.getTweets()) {
            textTweets.add(t.getStrText());
        }


        Button buttonFreqWords = new Button("Afficher les mots les plus frequents");
        buttonFreqWords.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                frequencyWords(lstStrWords, lstSW);
            }
        });

        Button buttonTFIDF = new Button("Afficher toutes les Infos des différents mots");
        buttonTFIDF.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tfidf();
            }
        });


        Button buttonSortByUser = new Button("Trier par utilisateurs");
        buttonSortByUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bdt.trierParUser();
                importBase(data, flTweets, tableView);
                createSearchBar(choiceBox, textField, flTweets);
            }
        });

        Button buttonSortByRT = new Button("Trier par RT");
        buttonSortByRT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bdt.trierParRT();
                importBase(data, flTweets, tableView);
                createSearchBar(choiceBox, textField, flTweets);
            }
        });

        Button buttonSortbyText = new Button("Trier par Text");
        buttonSortbyText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bdt.trierParText();
                importBase(data, flTweets, tableView);
                createSearchBar(choiceBox, textField, flTweets);
            }
        });

        primaryStage.setTitle("TextMining");

        VBox root = new VBox();

        root.setSpacing(10);
        root.setPadding(new Insets(15, 20, 10, 10));

        HBox hBox = new HBox(choiceBox, textField);

        tableView.prefHeightProperty().bind(primaryStage.heightProperty());
        tableView.prefWidthProperty().bind(primaryStage.widthProperty());

        HBox hBox1 = new HBox(buttonSortByRT, buttonSortbyText, buttonSortByUser);

        root.getChildren().addAll(label, tableView, hBox, allLabel, hBox1, buttonFreqWords,buttonTFIDF);

        Scene scene = new Scene(root, 1000, 700, Color.WHITE);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void tfidf() {
        List<Words> infoWords = new ArrayList<>();
        int globalneg = 0;
        int n = 0;
        double pourcentage = 0;
        System.out.println("ProgressBar Spliting Tweets :");
        for (Tweets t : bdt.getTweets()) {
            double it = bdt.getTweets().indexOf(t) + 1;
            if (pourcentage != Math.round((it / bdt.getTweets().size()) * 100)) {
                pourcentage = Math.round((it / bdt.getTweets().size()) * 100);
                String progress = new String(pourcentage + "% ");
                System.out.print(progress);
            }

            if (!t.getbRT()) {
                String[] arrTWords = t.getStrText().replaceAll("[^A-Za-z]+", " ").split(" ");
                List<String> lstTWords = Arrays.asList(arrTWords);
                lowerCase(lstTWords);
                Map<String, Integer> frequencyTWords = countFrequencies(lstTWords);
                for (Map.Entry me : frequencyTWords.entrySet()) {
                    String s = new String(me.getKey().toString());
                    if (s.length() > 1) {
                        Optional<Words> optional = infoWords.stream()
                                .filter(x -> s.equals(x.getText()))
                                .findFirst();
                        double c = (Integer) me.getValue();
                        double tf = c / (lstTWords.size());
                        tf = (double) Math.round(tf * 10000d) / 10000d;
                        if (!optional.isPresent()) {//Check whether optional has element you are looking for
                            Words w = new Words(s, (Integer) me.getValue());
                            w.addTF(tf);
                            infoWords.add(w);
                        } else {
                            optional.get().setCount(optional.get().getCount() + (Integer) me.getValue());
                            optional.get().addTF(tf);
                        }
                        n += (Integer) me.getValue();
                    }

                }
            }
        }

        for (Words w : infoWords) {
            w.setIdf(w.getCount(), n);
            for (double d : w.getTf())
            {
                w.getTfidf().add(d* w.getIdf());
            }
        }

        Collections.sort(infoWords, new CountComparator());

        System.out.println("\n");
        for (int i = 0; i <= 10; i++) {
            System.out.println(infoWords.get(i).getText() +
                    " " + infoWords.get(i).getCount()+
                    " " + infoWords.get(i).getIdf()
                    );
        }
    }


    public static void frequencyWords(List<String> lstStrWords, List<String> lstSW) {
        List<Words> infoWords = new ArrayList<>();
        System.out.println("ProgressBar Spliting Tweets :");
        double pourcentage = 0;
        for (Tweets t : bdt.getTweets()) {
            double it = bdt.getTweets().indexOf(t) + 1;
            if (pourcentage != Math.round((it / bdt.getTweets().size()) * 100)) {
                pourcentage = Math.round((it / bdt.getTweets().size()) * 100);
                String progress = new String(pourcentage + "% ");
                System.out.print(progress);
            }

            if (!t.getbRT()) {
                String[] arrTWords = t.getStrText().replaceAll("[^A-Za-z]+", " ").split(" ");
                for (String s : arrTWords) {
                    if (s.length() > 1) {
                        lstStrWords.add(s.toLowerCase());
                    }
                }
            }
        }
        lstStrWords.removeAll(lstSW);
        Map<String, Integer> frequencyWords = countFrequencies(lstStrWords);
        for (Map.Entry me : frequencyWords.entrySet()) {
            Words w = new Words(me.getKey().toString(), (Integer) me.getValue());
            //w.setIdf(w.getCount(), textTweets.size());
            infoWords.add(w);
        }

        Collections.sort(infoWords, new CountComparator());

        System.out.println("\n");
        for (int i = 0; i <= 10; i++) {
            System.out.println(infoWords.get(i).getText() +
                    " " + infoWords.get(i).getCount());
        }
    }

    public static void importBase(ObservableList<Tweets>[] data, FilteredList<Tweets>[] flTweets, TableView<Tweets> tableView) {
        data[0] = FXCollections.observableArrayList(bdt.getTweets());
        flTweets[0] = new FilteredList(data[0], p -> true);//Pass the data to a filtered list
        tableView.setItems(flTweets[0]);//Set the table's items using the filtered list


    }

    public static void createSearchBar(ChoiceBox<String> choiceBox, TextField textField, FilteredList<Tweets>[] flTweets) {
        choiceBox.getItems().addAll("Utilisateur", "Contenu du tweet");
        choiceBox.setValue("Utilisateur");
        textField.setPromptText("Rechercher ici !");
        textField.setOnKeyReleased(keyEvent ->
        {
            switch (choiceBox.getValue())//Switch on choiceBox value
            {
                case "Utilisateur":
                    flTweets[0].setPredicate(p -> p.getStrUtilisateur().toLowerCase().contains(textField.getText().toLowerCase().trim()));
                    break;
                case "Contenu du tweet":
                    flTweets[0].setPredicate(p -> p.getStrText().toLowerCase().contains(textField.getText().toLowerCase().trim()));
                    break;
            }
        });

    }

    public static List<String> stock(BufferedReader br) throws IOException {
        List<String> lstStr = new ArrayList<String>();
        String str;
        while ((str = br.readLine()) != null ) {
            if(lstStr.size() < 100000) {
                lstStr.add(str);
            }
        }

        return lstStr;
    }

    public static void lowerCase(List<String> strings) {
        ListIterator<String> iterator = strings.listIterator();
        while (iterator.hasNext()) {
            iterator.set(iterator.next().toLowerCase());
        }
    }

    public static int count(List<String> words, String term) {
        int count = 0;
        int idterm = words.indexOf(term);

        for (int i = idterm; term.equalsIgnoreCase(words.get(i)); i++) {
            count++;
            if (i + 1 == words.size()) {
                break;
            }
        }

        return count;
    }

    public static Map countFrequencies(List<String> list) {
        // hashmap to store the frequency of element
        Map<String, Integer> hm = new HashMap<String, Integer>();

        for (String i : list) {
            Integer j = hm.get(i);
            hm.put(i, (j == null) ? 1 : j + 1);
        }

        return hm;
    }

}

