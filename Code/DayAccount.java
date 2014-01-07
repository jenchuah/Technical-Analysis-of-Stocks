
import java.util.Date;

/**
 *
 * @author jen_chuah
 */
public class DayAccount {

    private double cash, price, invested, total;
    private Date date;
    private int quantity;
    private int period, period2, period3;
    private double threshold;

    public void setDate(Date d){
        date = d;
    }

    public void setPrice(double p){
        price = p;
    }

    public void setPeriod(int p){
        period = p;
    }

    public void setPeriod2(int p){
        period2 = p;
    }

    public void setPeriod3(int p){
        period3 = p;
    }

    public void setThreshold(double t){
        threshold = t;
    }

    public void setCash(double c){
        cash = c;
    }

    public void setQuantity(int q){
        quantity = q;
    }

    public void setInvested(){
        invested = price*quantity;
    }

    public void countTotal(){
        total = cash + invested;
    }

    

    public Date getDate(){
        return date;
    }

    public double getPrice(){
        return price;
    }

    public int getPeriod(){
        return period;
    }

    public int getPeriod2(){
        return period2;
    }

    public int getPeriod3(){
        return period3;
    }

    public double getThreshold(){
        return threshold;
    }
    
    public double getCash(){
        return cash;
    }

    public int getQuantity(){
        return quantity;
    }
    
    public double getInvested(){
        return invested;
    }

    public double getTotal(){
        return total;
    }
    

}
