
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jen_chuah
 */
public class Strategy {

    Portfolio pf;
    double movingAvg[];
    double priceMmt[];
    double ROC[];

    //RSI
    double change[];
    double up[];
    double down[];
    double upEMA[];
    double downEMA[];
    double RS[];
    double RSI[];

    //MACD
    double alpha[];
    double EMA0[];
    double EMA1[];
    double MACD[];
    double signalLine[];

    //Voting signals
    String MASig[];
    String ROCSig[];
    String RSISig[];
    String PMSig[];
    String MACDSig[];


    public Strategy(Portfolio pf){
        this.pf = pf;
        pf.initStock(pf.getStock());
    }

    public void initSignals(String strategy){
        if(strategy.equals("MA")){
            MASig = new String[pf.getAccount().size()];
        }
        else if(strategy.equals("PM")){
            PMSig = new String[pf.getAccount().size()];
        }
        else if(strategy.equals("ROC")){
            ROCSig = new String[pf.getAccount().size()];
        }
        else if(strategy.equals("MACD")){
            MACDSig = new String[pf.getAccount().size()];
        }
        else if(strategy.equals("RSI")){
            RSISig = new String[pf.getAccount().size()];
        }
    }

    /*
     * Moving Average
     */
    public void movingAverageCalc(int initDate, int endDate, int period){

        ArrayList<DayAccount> account = pf.getAccount();
        int ma_index = 0;
        //everytime there is a new call to this method, recalculate the averages
        boolean calculate = false;
        //Calculate the moving average of the stock over the period to simulate, given stock's historical data
        ArrayList<Day> stockAccount = pf.getStock().getData();
        for(int i = initDate; i>=endDate+(period-1); i--){
            double sum = 0;
            for(int j = i; j>(i-period); j--){
                sum += stockAccount.get(j).getClose();
            }
            double avg = sum/period;
            //initial setup
            if(calculate == false){
               movingAvg = new double[stockAccount.size()];
               //Start moving average data at period-1 day
               ma_index = i-period+1;
               calculate = true;
            }
            movingAvg[ma_index] = avg;
            //Makes sure that index of movingAvg corresponds to account's index of the last day in given period
            ma_index--;
        }
    }

    public void movingAveragePerform(int initDate, int endDate, int period){
        movingAverageApply( initDate, endDate, period, "Perform");
    }

    public String[] movingAverageSignals(int initDate, int endDate, int period){
        movingAverageApply( initDate, endDate, period, "Signal");
        return MASig;
    }

    public void movingAverageApply(int initDate, int endDate, int period, String action){
        final int same = 1;
        final int higher = 2;
        final int lower = 3;
        int compare = same;

        ArrayList<DayAccount> account = pf.getAccount();
       
        for(int apply = initDate; apply>=endDate; apply--){
            DayAccount today = account.get(apply);
            pf.setDate(today.getDate(), apply);
            today.setPeriod(period);
            
            //Compare if stock price is initially lower or higher than moving average
            if(apply==initDate){
                if(today.getPrice()>movingAvg[apply+1]){
                    compare = higher;
                }else if(today.getPrice()<movingAvg[apply+1]){
                    compare = lower;
                }else{
                    compare = same;
                }
            }
            else{
                //Becomes lower
                if((compare == higher||compare == same) && today.getPrice()<movingAvg[apply+1]){
                    compare = lower;

                    if(action.equals("Perform")){
                        //Sell maximum
                        if(today.getQuantity()>0){
                            pf.sell(0, today.getQuantity());
                        }
                    }else{
                        MASig[apply] = "S";
                    }
                }
                //Becomes higher
                else if((compare == lower || compare == same) && today.getPrice()>movingAvg[apply+1]){
                    compare = higher;

                    if(action.equals("Perform")){
                        //Buy maximum
                        double singleP = today.getPrice()*(1+pf.getTransactionCost());
                        int quantity = (int)(today.getCash()/singleP);
                        pf.buy(0, quantity);
                    }else{
                        MASig[apply] = "B";
                    }
                }
                //Becomes same
                else if(today.getPrice()==movingAvg[apply+1]){
                    compare = same;
                }
            }
        }
    }


    
    /*
     * Price Momentum
     */
    public void priceMomentumCalc(int initDate, int endDate, int period, double threshold){

        ArrayList<DayAccount> account = pf.getAccount();
        priceMmt = new double[account.size()];

        int applyDate = account.size()-(1+period);

        //Calculate price momentum
          for(int i = initDate; i>=endDate; i--){
            if(i<=applyDate){
                priceMmt[i] = account.get(i+1).getPrice()/account.get(i+period).getPrice();
            }
        }  
    }

    public void priceMomentumPerform(int initDate, int endDate, int period, double threshold){
        priceMomentumApply( initDate, endDate, period, threshold, "Perform");
    }

    public String[] priceMomentumSignals(int initDate, int endDate, int period, double threshold){
        priceMomentumApply( initDate, endDate, period, threshold, "Signal");
        return PMSig;
    }

    public void priceMomentumApply(int initDate, int endDate, int period, double threshold, String action){
        final int same = 1;
        final int higher = 2;
        final int lower = 3;
        int compare = same;

        ArrayList<DayAccount> account = pf.getAccount();
      
        //Apply Price Momentum to historical data
        for(int apply = initDate; apply>=endDate; apply--){ //For window application in testing
            DayAccount today = account.get(apply);
            pf.setDate(today.getDate(), apply);
            today.setPeriod(period);
            today.setThreshold(threshold);

            //Determine if price momentum is initially higher or lower than threshold limit
            if(apply == initDate){
                
                if(priceMmt[apply]>(1+threshold)){
                    compare = higher;
                }else if(priceMmt[apply]<(1-threshold)){
                    compare = lower;
                }
            }
            else{
                //Becomes Higher
                if((compare == lower || compare == same) && priceMmt[apply]>(1+threshold)){
                    compare = higher;

                    if(action.equals("Perform")){
                        //BUY MAXIMUM
                        double singleP = today.getPrice()*(1+pf.getTransactionCost());
                        int quantity = (int)(today.getCash()/singleP);
                        pf.buy(0, quantity);
                    }else{
                        PMSig[apply] = "B";
                    }
                    
                }
                //Becomes Lower
                else if((compare == higher || compare == same) && priceMmt[apply]<(1-threshold)){
                    compare = lower;

                    if(action.equals("Perform")){
                        //SELL MAXIMUM
                        if(today.getQuantity()>0){
                            pf.sell(0, today.getQuantity());

                        }
                    }else{
                        PMSig[apply] = "S";
                    }
                }

            }
        }
    }

    /*
     * Rate of Change
     */
    public void rateOfChangeCalc(int initDate, int endDate, int period){

        ArrayList<DayAccount> account = pf.getAccount();
        ROC = new double[account.size()];

        int applyDate = account.size()-(1+period);

        //Calculate ROC
        for(int i = initDate; i>=endDate; i--){
            if(i<=applyDate){
                ROC[i] = ((account.get(i).getPrice()-account.get(i+period).getPrice())/account.get(i+period).getPrice())*100;
            }
        }
    }

    public void rateOfChangePerform(int initDate, int endDate, int period){
        rateOfChangeApply( initDate, endDate, period, "Perform");
    }

    public String[] rateOfChangeSignals(int initDate, int endDate, int period){
        rateOfChangeApply( initDate, endDate, period, "Signal");
        return ROCSig;
    }

    public void rateOfChangeApply(int initDate, int endDate, int period, String action){

        final int same = 1;
        final int higher = 2;
        final int lower = 3;
        int compare = same;

        ArrayList<DayAccount> account = pf.getAccount();
        
        //Apply ROC to historical data
        for(int apply = initDate; apply>=endDate; apply--){
            DayAccount today = account.get(apply);
            pf.setDate(today.getDate(), apply);
            today.setPeriod(period);

            //Determine if ROC is initially higher or lower than 0
            if(apply == initDate){
                
                if(ROC[apply]>0){
                    compare = higher;
                }else if(ROC[apply]<0){
                    compare = lower;
                }
            }
            else{
                //Becomes Higher
                if((compare == lower || compare == same) && ROC[apply]>0){
                    compare = higher;

                    if(action.equals("Perform")){
                        //BUY MAXIMUM
                        double singleP = today.getPrice()*(1+pf.getTransactionCost());
                        int quantity = (int)(today.getCash()/singleP);
                        pf.buy(0, quantity);
                    }else{
                        ROCSig[apply] = "B";
                    }
                   
                }
                //Becomes Lower
                else if((compare == higher || compare == same) && ROC[apply]<0){
                    compare = lower;

                    if(action.equals("Perform")){
                        //SELL MAXIMUM
                        if(today.getQuantity()>0){
                          pf.sell(0, today.getQuantity());
                        }
                    }else{
                        ROCSig[apply] = "S";
                    }
                }
            }   
        }
    }

    /*
     * Moving Average Convergence Divergence
     */
    public void MACDCalc(int initDate, int endDate, int period[]){

        //Calculate alpha and EMA(initDate-1) for period 1 and 2
        alpha = new double[2];
        ArrayList<DayAccount> account = pf.getAccount();
        int lastElementInd = account.size()-1;
        EMA0 = new double[account.size()];
        EMA1 = new double[account.size()];
        MACD = new double[account.size()];
        
        for(int i = 0; i<2; i++){
            
            int seriesSum = 0;

            double alpha1 = period[i]+1;
            double alpha2 = 2/alpha1;
            alpha[i] = alpha2;
            
            for(int j = 0; j<period[i]; j++){
                double r = (Math.pow((1-alpha[i]), j))*account.get(lastElementInd-j).getPrice();
                seriesSum += r;
            }

            double ema = alpha[i] * seriesSum;
            if(i == 0){
                EMA0[account.size() - period[i]] = ema;
            }else{
                EMA1[account.size() - period[i]] = ema;
            }

            //Calculate EMA whole chart
            for(int k = lastElementInd-period[i]; k>=0; k--){
                if(i == 0){
                    EMA0[k] = EMA0[k+1] + (alpha[i]*(account.get(k).getPrice()-EMA0[k+1]));
                }else{
                    EMA1[k] = EMA1[k+1] + (alpha[i]*(account.get(k).getPrice()-EMA1[k+1]));
                }
            }
        }

        ///////////////////////////////////////////////////////////////////////////
        //Calculate difference
        int largerPeriod = Math.min(period[0], period[1]);
        int larger = 0;
        int applyDate = account.size();
        if(period[0]<period[1]){
            larger = 1;
            applyDate -= period[1];
        }else{
            applyDate -= period[0];
        }
        
        for(int b = lastElementInd-largerPeriod; b>=0; b--){
            if(larger == 0){
                MACD[b] = EMA1[b] - EMA0[b];
            }else{
                MACD[b] = EMA0[b] - EMA1[b];
            }
        }

        ///////////////////////////////////////////////////////////////////////////

        int signalPeriod = period[2];

        //Use difference to create 9-days EMA/signalLine
        signalLine = new double[account.size()];
        double nineDaySeriesSum = 0.0;

        double nineAlpha1 = signalPeriod+1;
        double nineAlpha2 = 2/nineAlpha1;
        double nineDayAlpha = nineAlpha2;

        for(int j = 0; j<signalPeriod; j++){
            double r = (Math.pow((1-nineDayAlpha), j))*MACD[applyDate-j];
            nineDaySeriesSum += r;
        }

        applyDate = applyDate-signalPeriod;

        double nineEMA = nineDayAlpha*nineDaySeriesSum;
        signalLine[applyDate+1] = nineEMA;

        //Calculate EMA whole chart
        for(int k = applyDate; k>=endDate; k--){
            signalLine[k] = signalLine[k+1] + (nineDayAlpha*(MACD[k]-signalLine[k+1]));
        }

    }

     public void MACDPerform(int initDate, int endDate, int period[]){
        MACDApply( initDate, endDate, period, "Perform");
    }

    public String[] MACDSignals(int initDate, int endDate, int period[]){
        MACDApply( initDate, endDate, period, "Signal");
        return MACDSig;
    }

    public void MACDApply(int initDate, int endDate, int period[], String action){
        ///////////////////////////////////////////////////////////////////////////
        final int same = 1;
        final int higher = 2;
        final int lower = 3;
        int compare = same;

        ArrayList<DayAccount> account = pf.getAccount();
        
        for(int apply = initDate; apply>=endDate; apply--){
            DayAccount today = account.get(apply);
            pf.setDate(today.getDate(), apply);
            today.setPeriod(period[0]);
            today.setPeriod2(period[1]);
            today.setPeriod3(period[2]);
            
            if(apply == initDate){
                if(MACD[apply]>signalLine[apply]){
                    compare = higher;
                }else if(MACD[apply]<signalLine[apply]){
                    compare = lower;
                }
            }
            else{
                //Becomes Higher
                if((compare == lower || compare == same) && MACD[apply]>signalLine[apply]){
                    compare = higher;

                    if(action.equals("Perform")){
                        //BUY MAXIMUM
                        double singleP = today.getPrice()*(1+pf.getTransactionCost());
                        int quantity = (int)(today.getCash()/singleP);
                        pf.buy(0, quantity);
                    }else{
                        MACDSig[apply] = "B";
                    }
                    
                }
                //Becomes Lower
                else if((compare == higher || compare == same) && MACD[apply]<signalLine[apply]){
                    compare = lower;

                    if(action.equals("Perform")){
                        //SELL MAXIMUM
                        if(today.getQuantity()>0){
                            pf.sell(0, today.getQuantity());
                        }
                    }else{
                        MACDSig[apply] = "S";
                    }
                }
                else if (MACD[apply]==signalLine[apply]){
                    compare = same;
                }
            }
        }
    }

    /*
     * Relative Strength Index
     */
    public void RSICalc(int initDate, int endDate, int period){
        ArrayList<DayAccount> account = pf.getAccount();
        change = new double[account.size()];
        up = new double[account.size()];
        down = new double[account.size()];
       
        //Calculate value of upward and downward changes
        for(int i = initDate; i>endDate; i--){
            DayAccount yesterday = account.get(i);
            DayAccount today = account.get(i-1);

            double closeYest = yesterday.getPrice();
            double closeToday = today.getPrice();

            change[i-1] = closeToday - closeYest;

            //upward change
            if(change[i-1]>=0){
                up[i-1] = change[i-1];
                down[i-1] = 0;
            }
            //downward change
            else if(change[i-1]<0){
                up[i-1] = 0;
                down[i-1] = Math.abs(change[i-1]);
            }
        }

        /*
         * Calculate EMA(U) and EMA(D)
         */
        //Calculate alpha
        double alpha1 = period+1;
        double alpha2 = 2/alpha1;
        double upSeriesSum = 0, downSeriesSum = 0;
        upEMA = new double[account.size()];
        downEMA = new double[account.size()];

        //Calculate first EMA(U) and EMA(D)
        for(int j = 0; j<period; j++){
            double upR = (Math.pow((1-alpha2), j))*up[account.size()-(j+2)]; //account.size - 2 because that's where the first difference is
            upSeriesSum += upR;
            double downR = (Math.pow((1-alpha2), j))*down[account.size()-(j+2)];
            downSeriesSum += downR;
          
         }

        upEMA[account.size()-(period+1)] = alpha2 * upSeriesSum;
        downEMA[account.size()-(period+1)] = alpha2 * downSeriesSum;
        
        //Calculate EMA whole chart
        for(int k = account.size()-(period+2); k>=endDate; k--){
            upEMA[k] = upEMA[k+1] + (alpha2*(up[k]-upEMA[k+1]));
            downEMA[k] = downEMA[k+1] + (alpha2*(down[k]-downEMA[k+1]));
        }

        //Calculate RS and RSI
        RS = new double[account.size()];
        RSI = new double[account.size()];

        for(int l = initDate; l>=endDate; l--){
            RS[l] = upEMA[l]/downEMA[l];

            RSI[l] = 100 - (100/(1+RS[l]));
        }

    }

    public void RSIPerform(int initDate, int endDate, int period){
        RSIApply( initDate, endDate, period, "Perform");
    }

    public String[] RSISignals(int initDate, int endDate, int period){
        RSIApply( initDate, endDate, period, "Signal");
        return RSISig;
    }

    public void RSIApply(int initDate, int endDate, int period, String action){

        ArrayList<DayAccount> account = pf.getAccount();

        //Apply
        final int same = 1;
        final int higher = 2;
        final int lower = 3;
        int compare = same;

        for(int apply = initDate; apply >= endDate; apply--){
            DayAccount today = account.get(apply);
            pf.setDate(today.getDate(), apply);
            today.setPeriod(period);
            
            //Compare if stock price is initially lower or higher than moving average
            if(apply == initDate){
                if(RSI[apply]>70){
                    compare = higher;
                }
                else if(RSI[apply]<30){
                    compare = lower;
                }else{
                    compare = same;
                }
            }
            else{
                //Becomes lower
                if((compare == lower||compare == same) && RSI[apply]>70){
                    compare = higher;

                    if(action.equals("Perform")){
                        //SELL MAXIMUM
                        if(today.getQuantity()>0){
                            pf.sell(0, today.getQuantity());
                        }
                    }else{
                        RSISig[apply] = "S";
                    }
                }
                //Becomes higher
                else if((compare == higher || compare == same) && RSI[apply]<30){
                    compare = lower;
                    
                    if(action.equals("Perform")){
                        //BUY MAXIMUM
                        double singleP = today.getPrice()*(1+pf.getTransactionCost());
                        int quantity = (int)(today.getCash()/singleP);
                        pf.buy(0, quantity);
                    }else{
                        RSISig[apply] = "B";
                    }
                }
                //Becomes same
                else if(RSI[apply]>=30 && RSI[apply]<=70){
                    compare = same;
                }
            }
        }
    }

    private void print(String s){
        System.out.println(s);
    }
}
