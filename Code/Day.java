
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jen_chuah
 */
public class Day{

    //private String name;
    private Date date;
    private double open, high, low, close, adj;
    private long vol;

    //set daily details
    public void setDate(String d){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = df.parse(d);
        }
        catch(ParseException pe){
            pe.printStackTrace();
        }
    }
    public void setOpen(double open){
        this.open = open;
    }
    
    public void setHigh(double high){
        this.high = high;
    }

    public void setLow(double low){
        this.low = low;
    }

    public void setClose(double close){
        this.close = close;
    }

    public void setVolume(long vol){
        this.vol = vol;
    }

    public void setAdjClose(double adj){
        this.adj = adj;
    }

    //get daily details
    public Date getDate(){
        return date;
    }

    public double getOpen(){
        return open;
    }

    public double getHigh(){
        return high;
    }

    public double getLow(){
        return low;
    }

    public double getClose(){
        return close;
    }

    public long getVolume(){
        return vol;
    }

    public double getAdjClose(){
        return adj;
    }
}
