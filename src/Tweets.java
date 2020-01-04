public class Tweets {

    private String strUtilisateur;
    private String strDate;
    private String strTime;
    private String strText;

    private boolean bRT;

    private boolean bValide;

    private String strUtilisateurRT;
    private int nNbRT = 0;

    private Tweets twOriginal;

    public Tweets(String chaine)
    {

        ///séparer les champs
        String values[] = chaine.split("\t");
        if (values.length >=3) {
            bValide = true;
            bRT = false;
            strUtilisateur = values[1];
            strDate = values[2].split(" ")[0];
            strTime = values[2].split(" ")[1];

            strText = values[3];

            if (values.length > 4) {
                bRT = true;
                strUtilisateurRT = values[4];
            }
        }
        else
        {
            bValide = false;
        }
    }

    @Override
    public String toString() {
        String strTweet= new String("Utilisateur : " +strUtilisateur
                +"\nDate : "+strDate
                +"\nTemps : "+strTime
                +"\nText : "+strText
                +"\nRT : "+nNbRT
                );

        if(bRT)
        {
            strTweet += new String("\nUtilisateur Retweeté : "+strUtilisateurRT);
        }
        return strTweet;
    }

    public String getStrUtilisateur() {
        return strUtilisateur;
    }

    public String getStrText() {
        return strText;
    }

    public Boolean getbValide() {
        return bValide;
    }

    public void addRT() {
        this.nNbRT +=1;
    }

    public String getStrTime() {
        return strTime;
    }
    public String getStrUtilisateurRT() {
        return strUtilisateurRT;
    }

    public int getnNbRT() {
        return nNbRT;
    }

    public String getStrDate() {
        return strDate;
    }

    public Boolean getbRT() {
        return bRT;
    }

}
