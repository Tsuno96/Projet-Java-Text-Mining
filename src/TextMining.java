// Java Program to illustrate reading from
// FileReader using FileReader
import java.io.*;
    public class TextMining
    {
        public static void main(String[] args) throws Exception
        {
            // Importation de text
            FileReader foot =
                    new FileReader(".\\data\\Foot.txt");
            BufferedReader tampon = new BufferedReader(foot);

            LineNumberReader count = new LineNumberReader(foot);

            // Afficher le 1er ligne
            String ligne = tampon.readLine();
            // Afficher le n-eme ligne


            // Afficher le nombre de ligneù
            int result = 0;
            while(count.skip(Long.MAX_VALUE)>0) {
                result = count.getLineNumber() +1; //Comme le 1er ligne = 0
            }

            System.out.println(result);
            System.out.println(ligne);

        }



    }

