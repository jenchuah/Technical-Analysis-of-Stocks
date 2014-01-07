
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author jen_chuah
 */
public class Portfolio {
    private double transaction_cost, ROI, B_ROI;

    private ArrayList<DayAccount> account = null; //daily cash, invested, total value
    private ArrayList<Day> market; //current day index
    private StocksList stocks;
    private Stock selected_stock = null;
    private ArrayList<Stock> downloadedStocks;
    private Date current_date;
    private int index;
    private ArrayList<DVS> DVS_res = new ArrayList<DVS>();
    private int option;
//    final int printStatements = 3;
    int winSize = 100, winExtension = 20;

    public Portfolio(){
        transaction_cost = 0.015;
//        transaction_cost = 0.001;
        stocks = new StocksList();
    }

    public void setOption(int o){
        option = o;
    }
    /*
     * Manage transaction cost
     */
    public void setTransactionCost(double c){
        transaction_cost = c;
    }

    public double getTransactionCost(){
        return transaction_cost;
    }

    /*
     * Edit window
     */
    public void editWindow(int size, int ext){
        winSize = size;
        winExtension = ext;
        print("winSize: " + winSize);
        print("winExtension: " + winExtension);
    }

    /*
     * Add downloaded stock data to stock
     */

    public StocksList getStocksList(){
        return stocks;
    }

    public void addDownloadedStock(Stock s){
        if(downloadedStocks == null){
            downloadedStocks = new ArrayList<Stock>();
        }
        downloadedStocks.add(s);
    }

    public ArrayList<Stock> getDownloadedStocks(){
        return downloadedStocks;
    }

    public ArrayList<DayAccount> getAccount(){
        return account;
    }

    /*
     * Market index data
     */
    public void createMarket(ArrayList<Day> m){
        market = m;
        System.out.println("Market downloaded");
    }

    

    public void setDate(Date d, int ind){
        current_date = d;
        index = ind;
    }
    
    public Date getDate(){
        return current_date;
    }

    public void setStock(Stock stock){
        selected_stock = stock;
    }

    public Stock getStock(){
        return selected_stock;
    }
    
    /*
     * Select stock, set up account based on stock
     */
    public void initStock(Stock stock){

        setStock(stock);
        ArrayList<Day> daily = selected_stock.getData();
        //Set start date of simulator
        setDate(daily.get(daily.size()-1).getDate(), daily.size()-1);
        //Set up account
        account = new ArrayList<DayAccount>();
        for(int i = 0; i<daily.size(); i++){
            Day d = daily.get(i);
            DayAccount newDA = new DayAccount();
            newDA.setDate(d.getDate());
            newDA.setPrice(d.getClose());
            newDA.setCash(10000);
            newDA.setQuantity(0);
            newDA.setInvested();
            newDA.countTotal();
            account.add(newDA);
        }
    }


    /*
     * Methods to use portfolio
     */
    public void buy(int date_change, int quantity){
        //Set current date
        int newIndex = index - date_change;
        try{
            if(newIndex<0){
                newIndex = 0;
            }
            setDate(account.get(newIndex).getDate(), newIndex);
            DayAccount today = account.get(newIndex);
            double payment = today.getPrice()*(1+transaction_cost)*quantity;

            if(payment>today.getCash()){
                double singleP = today.getPrice()*(1+transaction_cost);
                quantity = (int)(today.getCash()/singleP);
                payment = singleP*quantity;
            }
            today.setCash(today.getCash()-payment);
            today.setQuantity(today.getQuantity()+quantity);
            today.setInvested();
            today.countTotal();
            updateFutureData(newIndex, today);
            
        }catch(ArrayIndexOutOfBoundsException e){
            print("arrayindexoutofbounds: newIndex is " + newIndex + " = " + index + "-" + date_change);
        }
    }

    public void sell(int date_change, int quantity){

        //Set current date
        int newIndex = index - date_change;
        try{
            if(newIndex<0){
                newIndex = 0;
            }
            setDate(account.get(newIndex).getDate(), newIndex);
            DayAccount today = account.get(newIndex);
            if(quantity>today.getQuantity()){
                quantity = today.getQuantity();
            }
            //Payment
            double sellP = today.getPrice()*(1-transaction_cost)*quantity;
            today.setCash(today.getCash()+sellP);
            today.setQuantity(today.getQuantity()-quantity);
            today.setInvested();
            today.countTotal();
            updateFutureData(newIndex, today);
        }catch(ArrayIndexOutOfBoundsException e){
            print("arrayindexoutofbounds: newIndex is " + newIndex + " = " + index + "-" + date_change);
        }
    }

    
    public double[] ROI(){
        double ROI_result[] = new double[2];
        //index = index of current date in account = final day
        ROI = returns(account.get(index).getTotal(),account.get(account.size()-1).getTotal());
        ROI_result[0] = ROI;

        Date finalDate = account.get(index).getDate();
        Date initDate = account.get(account.size()-1).getDate();

        int B_endIndex = getMarketIndexNo(finalDate);
        int B_startIndex = getMarketIndexNo(initDate);
      
        B_ROI = returns(market.get(B_endIndex).getClose(), market.get(B_startIndex).getClose());
        ROI_result[1] = B_ROI;

        return ROI_result;

    }

    public double[] volatility(){
        /*
         *Calculate volatility for stock
         */
        //Calculate daily ROI and add result to array
        ArrayList<Double> vol = new ArrayList<Double>();
        for(int i = account.size()-1; i>=index+1; i--){
            double dayROI = returns(account.get(i-1).getTotal(), account.get(i).getTotal());
            vol.add(dayROI);
        }

        //Calculate volatility
        double mean = calcMean(vol);
        double volatility = calcVol(vol, mean);


        /*
         *Calculate volatility for market index
         */
        //Get array index for market array dates that corresponds to stock begin and end date
        int startDate = getMarketIndexNo(account.get(account.size()-1).getDate());
        int endDate = getMarketIndexNo(account.get(index).getDate());

        //Calculate daily ROI and add result to array
        ArrayList<Double> B_vol = new ArrayList<Double>();
        for(int j = startDate-1; j>=endDate+1; j--){
            double mktDayROI = returns(market.get(j-1).getClose(), market.get(j).getClose());
            B_vol.add(mktDayROI);
        }

        //Calculate volatility
        double B_mean = calcMean(B_vol);
        double B_volatility = calcVol(B_vol, B_mean);

        double volatility_res[] = new double[2];
        volatility_res[0] = volatility;
        volatility_res[1] = B_volatility;

        return volatility_res;
        
    }

    public void DVS(int runC){ //daily value series
        for(int i = account.size()-1; i>=index; i--){
            DVS dvs = new DVS(selected_stock.getName(), runC, account.get(i).getDate(),account.get(i).getTotal());
            DVS_res.add(dvs);
        }
    }

    public void DVSPrint(){
        print("Stock,Run,Date,Total");
        for(int i = 0; i<DVS_res.size(); i++){
            print(DVS_res.get(i).toString());
        }
    }
    
    /******************\
     * Helper methods *
    \******************/
    private static void print(String s){
        System.out.println(s);
    }

    /*
     * Update following days of stock account
     */
    public void updateFutureData(int newIndex, DayAccount today){
        for(int j = newIndex-1; j>=0; j--){
            DayAccount future = account.get(j);
            future.setCash(today.getCash());
            future.setQuantity(today.getQuantity());
            future.setInvested();
            future.countTotal();
        }
    }

    public void printPF(){
        for(int i = 0; i<account.size(); i++){
            DayAccount printA = account.get(i);
            print("\n" + printA.getDate().toString());
            print("Price: " + printA.getPrice());
            print("Cash: "+printA.getCash());
            print("Quantity: "+printA.getQuantity());
            print("Invested: "+printA.getInvested());
            print("Total: "+printA.getTotal());
        }
    }

    /*
     * Searches for the corresponding market index index number, given date of account index
     */
    private int getMarketIndexNo(Date d){
        int B_Index = -1;
        for(int i = 0; i<market.size(); i++){
            if(market.get(i).getDate().compareTo(d)<0 || market.get(i).getDate().equals(d)){
                B_Index = i;
                return B_Index;
            }
        }
        
        return B_Index;
    }
    
    /*
     * Calculate the mean value for the data
     */
    private double calcMean(ArrayList<Double> d){
        double sum = 0;
        for(int i = 0; i<d.size(); i++){
            sum+=d.get(i);
        }
        double mean = sum/d.size();
        return mean;
    }

    /*
     * Calculate the volatility for the data
     */
    private double calcVol(ArrayList<Double> d, double mean){
        double sumSD = 0;
        for(int j = 0; j<d.size(); j++){
            sumSD += Math.pow((d.get(j)-mean), 2);
        }
        double volatility = Math.sqrt(sumSD/d.size());
        return volatility;
    }

    /*
     * Calculate Returns for ROI and volatility calculations
     */
    private double returns(double finalVal, double initVal){
        return Math.log(finalVal/initVal);
    }

    private String getCurrentDir(){
        File dir1 = new File (".");
        String dirName = "";
        try {
            dirName = dir1.getCanonicalPath();

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return dirName;
    }

    private String setNewDir(String secDir){
        String dirName = getCurrentDir() + secDir;

        File dir = new File(dirName);
        if(dir.exists() == false){
            dir.mkdir();
        }
        return dirName;
    }

    public void printSingleParamCSV(String secDir, String filename){
        try {
            //Set directory
            setNewDir("/Output");
            String filePath = setNewDir("/Output/"+ secDir) + "/" + filename;
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath));

            double roi[] = ROI();
            double vol[] = volatility();
            out.write("Date,Closing Price,Quantity,Period,Cash,Investment,Total Value,ROI,Volatility\n");
            for(int i = account.size()-1; i>=index; i--){
                DayAccount day = account.get(i);
                if(i==index){
                    out.write(day.getDate() + "," + day.getPrice() + "," + day.getQuantity() + "," + day.getPeriod() + "," + day.getCash() + "," + day.getInvested() + "," + day.getTotal() + "," + roi[0]  + "," + vol[0] + "\n" );
                }
                else{
                    out.write(day.getDate() + "," + day.getPrice() + "," + day.getQuantity() + "," + day.getPeriod() + "," + day.getCash() + "," + day.getInvested() + "," + day.getTotal() + "\n");

                }

            }
            out.flush();
            out.close();
        } catch (IOException e) {
        }
    }

    public void printDoubleParamCSV(String secDir, String filename){
        try {
            //Set directory
            setNewDir("/Output");
            String filePath = setNewDir("/Output/"+ secDir) + "/" + filename;
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
            
            double roi[] = ROI();
            double vol[] = volatility();
            out.write("Date,Closing Price,Quantity,Period,Threshold,Cash,Investment,Total Value,ROI,Volatility\n");
            for(int i = account.size()-1; i>=index; i--){
                DayAccount day = account.get(i);
                if(i==index){
                    out.write(day.getDate() + "," + day.getPrice() + "," + day.getQuantity() + "," + day.getPeriod() + "," + day.getThreshold() + "," + day.getCash() + "," + day.getInvested() + "," + day.getTotal() + "," + roi[0]  + "," + vol[0] + "\n" );
                }
                else{
                    out.write(day.getDate() + "," + day.getPrice() + "," + day.getQuantity() + "," + day.getPeriod() + "," + day.getThreshold() + "," + day.getCash() + "," + day.getInvested() + "," + day.getTotal() + "\n");

                }

            }
            out.flush();
            out.close();
        } catch (IOException e) {
        }
    }

    public void printTripleParamCSV(String secDir, String filename){
        try {
            //Set directory
            setNewDir("/Output");
            String filePath = setNewDir("/Output/"+ secDir) + "/" + filename;
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
            
            double roi[] = ROI();
            double vol[] = volatility();
            out.write("Date,Closing Price,Quantity,Period1,Period2,SignalLinePeriod,Cash,Investment,Total Value,ROI,Volatility\n");
            for(int i = account.size()-1; i>=index; i--){
                DayAccount day = account.get(i);
                if(i==index){
                    out.write(day.getDate() + "," + day.getPrice() + "," + day.getQuantity() + "," + day.getPeriod() + "," + day.getPeriod2() + "," + day.getPeriod3() + "," + day.getCash() + "," + day.getInvested() + "," + day.getTotal() + "," + roi[0]  + "," + vol[0] + "\n" );
                }
                else{
                    out.write(day.getDate() + "," + day.getPrice() + "," + day.getQuantity() + "," + day.getPeriod() + "," + day.getPeriod2() + "," + day.getPeriod3() + "," + day.getCash() + "," + day.getInvested() + "," + day.getTotal() + "\n");

                }

            }
            out.flush();
            out.close();
        } catch (IOException e) {
        }
    }

    public void printCombiCSV(String filename){
        try {
            //Set directory
            String secDir = "Combination";
            setNewDir("/Output");
            String filePath = setNewDir("/Output/"+ secDir) + "/" + filename;
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
            
            double roi[] = ROI();
            double vol[] = volatility();
            out.write("Date,Closing Price,Quantity,Cash,Investment,Total Value,ROI,Volatility\n");
            for(int i = account.size()-1; i>=index; i--){
                DayAccount day = account.get(i);
                if(i==index){
                    out.write(day.getDate() + "," + day.getPrice() + "," + day.getQuantity() + "," + day.getCash() + "," + day.getInvested() + "," + day.getTotal() + "," + roi[0]  + "," + vol[0] + "\n" );
                }
                else{
                    out.write(day.getDate() + "," + day.getPrice() + "," + day.getQuantity() + "," + day.getCash() + "," + day.getInvested() + "," + day.getTotal() + "\n");

                }

            }
            out.flush();
            out.close();
        } catch (IOException e) {
        }
    }
}
