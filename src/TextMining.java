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

        System.out.println(lstStrLignes.get(10));

        // Afficher le nombre de ligneù
        int result = 0;
        while (count.skip(Long.MAX_VALUE) > 0) {
            result = count.getLineNumber() + 1; //Comme le 1er ligne = 0
        }

        System.out.println(result);



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

