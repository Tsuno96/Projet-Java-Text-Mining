import java.util.Comparator;

public class Words  {
    private String text;
    private int count;
    private double tf;
    private double idf;
    private double tfidf;

    public Words(String text, int count, double tf, double idf, double tfidf) {
        this.text = text;
        this.count = count;
        this.tf = tf;
        this.idf = idf;
        this.tfidf = tfidf;
    }

    public String getText() {
        return text;
    }

    public int getCount() {
        return count;
    }

    public double getTf() {
        return tf;
    }

    public double getIdf() {
        return idf;
    }

    public double getTfidf() {
        return tfidf;
    }


}
