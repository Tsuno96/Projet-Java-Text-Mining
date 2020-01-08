import java.util.Comparator;

public class SortByUser implements Comparator<Tweets> {

    @Override
    public int compare(Tweets t1, Tweets t2) {
        if (t1.getStrUtilisateur().compareTo(t2.getStrUtilisateur())<0) return -1;
        else if (t1.getStrUtilisateur().compareTo(t2.getStrUtilisateur())>0) return 1;
        else return 0;
    }
}
