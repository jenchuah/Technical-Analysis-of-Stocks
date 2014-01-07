
import java.util.Date;

/**
 *
 * @author jen_chuah
 */
public interface DayInt {

    //set daily details
    public void setDate(String date);
    public void setOpen(double open);
    public void setHigh(double high);
    public void setLow(double low);
    public void setClose(double close);
    public void setVolume(long vol);
    public void setAdjClose(double adj);

    //get daily details
    public Date getDate();
    public double getOpen();
    public double getHigh();
    public double getLow();
    public double getClose();
    public long getVolume();
    public double getAdjClose();
    
}
