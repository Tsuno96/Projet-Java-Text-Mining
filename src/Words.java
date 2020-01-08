import java.util.ArrayList;
import java.util.List;

public class Words {
    private String text;
    private int count;
    private List<Double> lsttf;
    private double idf;
    private List<Double> lsttfidf;

    public Words(String text, int count) {
        this.text = text;
        this.count = count;
        lsttf = new ArrayList<>();
        lsttfidf = new ArrayList<>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Double> getTf() {
        return lsttf;
    }

    public void addTF(double d) {
        lsttf.add(d);
    }

    public List<Double> getTfidf() {
        return lsttfidf;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public double getIdf() {
        return idf;
    }

    public void setIdf(int n, int docsSize){
        this.idf = Math.log(docsSize / n);
        this.idf = (double) Math.round(this.idf * 10000d) / 10000d;
    }



}
