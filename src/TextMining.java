// Java Program to illustrate reading from
// FileReader using FileReader

import javax.sound.midi.Synthesizer;
import java.io.*;
import java.util.*;

public class TextMining {
    public static BaseDeTweets bdt = new BaseDeTweets();

    public static void main(String[] args) throws Exception {
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
        bdt.afficher();


        // Afficher le nombre de ligne
        int result = 0;
        while (count.skip(Long.MAX_VALUE) > 0) {
            result = count.getLineNumber() + 1; //Comme le 1er ligne = 0
        }



        int Utilisateur[ ] = new int[10];

        //for (String i: http) {
            //System.out.println(i);}

    }

    public static List<String> stock (BufferedReader br) throws IOException {
        List<String> lstStr = new ArrayList<String>();
        String str;
        while (( str = br.readLine()) != null) {
            lstStr.add(str);
        }
        return lstStr;

    }

}

