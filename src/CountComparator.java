import java.util.Comparator;

public class CountComparator implements Comparator<Words> {

    @Override
    public int compare(Words w1, Words w2) {

        Integer intw1 = new Integer(w1.getCount());
        Integer  intw2 = new Integer(w2.getCount());
        int retval =  intw1.compareTo(intw2);

        if(retval < 0) {
            return 1;
        } else if(retval > 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
