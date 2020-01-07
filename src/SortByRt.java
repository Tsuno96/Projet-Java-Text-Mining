import java.util.Comparator;

public class SortByRt implements Comparator<Tweets> {

    @Override
    public int compare(Tweets t1, Tweets t2) {
        Integer intt1 = new Integer(t1.getnNbRT());
        Integer  intt2 = new Integer(t2.getnNbRT());
        int retval =  intt1.compareTo(intt2);
        if(retval < 0) {
            return 1;
        } else if(retval > 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
