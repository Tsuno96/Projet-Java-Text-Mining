import java.util.Comparator;

public class SortByText implements Comparator<Tweets> {

    @Override
    public int compare(Tweets t1, Tweets t2) {
        if (t1.getStrText().compareTo(t2.getStrText())<0) return -1;
        else if (t1.getStrText().compareTo(t2.getStrText())>0) return 1;
        else return 0;
    }

}
