
import java.util.Date;

/**
 *
 * @author jen_chuah
 */
public class DVS {

    private String share;
    private int runCount = -1;
    private Date date;
    private double total;

    public DVS(String s, int rc, Date d, double t){
        share = s;
        runCount = rc;
        date = d;
        total = t;
    }

    public void setShare(String s){
        share = s;
    }

    public void setRunCount(int rc){
        runCount = rc;
    }

    public void setDate(Date d){
        date = d;
    }

    public void setTotal(double t){
        total = t;
    }

    public String getShare(){
        return share;
    }

    public int getRunCount(){
        return runCount;
    }

    public Date getDate(){
        return date;
    }

    public double getTotal(){
        return total;
    }

    public String toString(){
        return share+","+runCount+","+date+","+total;
    }

}
