// Java Program to illustrate reading from
// FileReader using FileReader

import java.io.*;
import java.util.*;

public class TextMining {
    public static void main(String[] args) throws Exception {
        // Importation de text
        FileReader foot =
                new FileReader(".\\data\\Foot.txt");
        BufferedReader tampon = new BufferedReader(foot);

        LineNumberReader count = new LineNumberReader(foot);


        //Liste des lignes
        List<String> lstStrLignes = stock(tampon);

        //System.out.println(lstStrLignes.get(0));

        // Afficher le nombre de ligne
        int result = 0;
        while (count.skip(Long.MAX_VALUE) > 0) {
            result = count.getLineNumber() + 1; //Comme le 1er ligne = 0
        }
        System.out.println(result);

        /// Separer les champs

        String chaine = lstStrLignes.get(2);
        String values[] = chaine.split("\t");
        String datetime[]   = values[2].split(" ");
        String http = values[3].substring(values[3].indexOf("https://"));
        // values[1] = Utilisateur
        // values[2] = Date et temps avec date=datetime[0] et temp=datetime[2]
        // values[3] = Tweet + https=http[]
        // values[4] = Utilisateur retweet
        System.out.println(http);
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

