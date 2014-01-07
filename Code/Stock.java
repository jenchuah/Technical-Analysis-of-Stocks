
import java.util.ArrayList;

/**
 *
 * @author jen_chuah
 */
public class Stock {

    private String name;
    private ArrayList<Day> data;
    
    public Stock(String n){
        name = n;
    }

    public void setData(ArrayList<Day> d){
        data = d;
    }

    public String getName(){
        return name;
    }
    
    public ArrayList<Day> getData(){
        return data;
    }

}
