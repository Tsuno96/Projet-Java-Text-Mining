public class Tweets {

    String strUtilisateur;
    String strDate;
    String strTime;
    String strText;
    Boolean bRT;
    Boolean bLinked;
    String[] arrstrLiens;
    String strUtilisateurRT;

    public Tweets(String chaine)
    {
        bRT = false;
        bLinked = false;
        String values[] = chaine.split("\t");
        strUtilisateur = values[1];
        strDate = values[2].split(" ")[0];
        strTime = values[2].split(" ")[1];
        if(values[3].contains("https://"))
        {
            bLinked = true;
            strText = values[3].substring(0, values[3].indexOf("https://")-1);
            arrstrLiens = values[3].substring(values[3].indexOf("https://")).split(" ");
        }
        else
        {
            strText = values[3];
        }

        if(values.length >4)
        {
            bRT = true;
            strUtilisateurRT = values[4];
        }
    }

    @Override
    public String toString() {
        String strTweet= new String("Utilisateur : " +strUtilisateur
                +"\nDate : "+strDate
                +"\nTemps : "+strTime
                +"\nText : "+strText
                );
        if(bLinked) {
            strTweet += new String("\n Liens : ");
            for (String str : arrstrLiens) {
                strTweet += new String("\n" + str);
            }
        }
        if(bRT)
        {
            strTweet += new String("\nUtilisateur Retweeté : "+strUtilisateurRT);
        }
        return strTweet;
    }
}
