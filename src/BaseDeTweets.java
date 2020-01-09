import java.util.*;

public class BaseDeTweets {

    //private TreeMap<Tweets, Integer> tmTweets;

    private List<Tweets> lstTweets;

    public BaseDeTweets() {
        lstTweets = new ArrayList<Tweets>();
    }

    public void ajoute(Tweets t) {
        if (t.getbValide()) {
            lstTweets.add(t);
        }
    }

    public void afficher() {

        for (Tweets t : lstTweets) {
            if (t.getStrUtilisateur().contains("")) {
                System.out.println("\n" + t);
            }
        }
    }

    public void trierParText() {
        Collections.sort(lstTweets, new SortByText());
    }

    public void trierParUser() {
        Collections.sort(lstTweets, new SortByUser());
    }
    public void trierParRT() {
        Collections.sort(lstTweets, new SortByRt());
    }


    public void compteurRT() {
        trierParText();
        Tweets t0 = lstTweets.get(0);
        for (int i = 1; i < lstTweets.size(); i++) {
            if (lstTweets.get(i).getStrText().contains(t0.getStrText())) {
                if (lstTweets.get(i).getbRT()) {
                    t0.addRT();
                } else {
                    {
                        t0 = lstTweets.get(i);
                    }
                }
            } else {
                t0 = lstTweets.get(i);
            }

        }
    }

    public List<Tweets> getTweets() {
        return lstTweets;
    }


}
