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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


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
        FileReader foot =
                new FileReader(".\\data\\FootLight.txt");
        BufferedReader tampon = new BufferedReader(foot);
        LineNumberReader count = new LineNumberReader(foot);

        //Liste des lignes
        List<String> lstStrLignes = stock(tampon);
        for(int i = 0; i<lstStrLignes.size();i++)
        {
            //System.out.println(i);
            Tweets t = new Tweets(lstStrLignes.get(i));
            bdt.ajoute(t);
        }
        bdt.trierParText();

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
        table.setItems(flTweets);//Set the table's items using the filtered list
        table.getColumns().addAll(userCol, dateCol, rtCol, brtCol, userRTCol, timeCol, textCol);


        //Adding ChoiceBox and TextField here!
        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("Utilisateur", "Contenu du tweet");
        choiceBox.setValue("Utilisateur");
        javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
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



        List<String> lstWords = new ArrayList<>();
        List<String> textTweets = new ArrayList<>();

        for(Tweets t: bdt.getTweets())
        {
            textTweets.add(t.getStrText());
        }

        for(Tweets t: bdt.getTweets())
        {
            String[] twords = t.getStrText().replaceAll("[^A-Za-z]+", " ").split(" ");
            for(String s : twords)
            {
                lstWords.add(s);
            }

        }



        lowerCase(lstWords);
        lstWords.removeAll(lstSW);

        /*for (String w : lstWords)
        {
            if(lstSW.contains(w.toLowerCase())){
                while (lstWords.indexOf(w)!=-1)
                {
                    lstWords.remove(w);
                }
            }
        }*/
        
        index(lstWords);
        count();


        /*JFrame frame = new JFrame(TextMining.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        Cloud cloud = new Cloud();
        Random random = new Random();
        for (String s : textTweets) {
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
        frame.setVisible(true);*/
    }

    public static void lowerCase(List<String> strings)
    {
        ListIterator<String> iterator = strings.listIterator();
        while (iterator.hasNext())
        {
            iterator.set(iterator.next().toLowerCase());
        }
    }
    final static String index = "index";
    final static String field = "text";

    public static void index(List<String> lines) {
        try {
            Directory dir = FSDirectory.open(Paths.get(index));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(dir, iwc);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                    Document doc = new Document();
                    doc.add(new StringField("id", "" + i, Store.YES));
                    doc.add(new TextField(field, line.trim(), Store.YES));
                    writer.addDocument(doc);

            }

            System.out.println("indexed " + lines.size() + " sentences");
            writer.close();
        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
    }

    public static void count() {
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
            int numTerms = 100;
            org.apache.lucene.misc.TermStats[] stats = HighFreqTerms.getHighFreqTerms(reader, numTerms, field, new HighFreqTerms.DocFreqComparator());
            for (org.apache.lucene.misc.TermStats termStats : stats) {
                String termText = termStats.termtext.utf8ToString();
                System.out.println(termText + " " + termStats.docFreq);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
    }




}

