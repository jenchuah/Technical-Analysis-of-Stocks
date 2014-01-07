
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author jen_chuah
 */
public class AdaptiveStrategy {
    Portfolio training_pf;
    Portfolio testing_pf;
    ArrayList<SolutionHistory> history;
    SolutionHistory bestSol;
    int randSize = 20;
    Random random;

    Strategy training_strategy, testing_strategy;

    //Combination
    String MASig[];
    String ROCSig[];
    String RSISig[];
    String PMSig[];
    String MACDSig[];

   int winSize = 100, winExtension = 20;

    public AdaptiveStrategy(Portfolio training_pf, Portfolio testing_pf){
        this.training_pf = training_pf;
        this.testing_pf = testing_pf;
        random = new Random( System.currentTimeMillis() );
        updateWin();
    }

    public void updateWin(){
       winSize = training_pf.winSize;
       winExtension = training_pf.winExtension;
    }
    

    public void rotateStock(String strategy){
        ArrayList<Stock> sl = testing_pf.getDownloadedStocks();

        for(int stock = 0; stock<sl.size(); stock++){
            System.out.print(sl.get(stock).getName());
            if(strategy.equals("MA") || strategy.equals("ROC") || strategy.equals("RSI")){
                singleParamAdaptive(stock, strategy, "Perform");
            }else if(strategy.equals("PM")){
                doubleParamAdaptive(stock, strategy, "Perform");
            }else{
                tripleParamAdaptive(stock, strategy, "Perform");
            }
            System.out.print(" - Completed.\n");
        }
    }

    //Adaptive for MA, ROC, RSI
    public void singleParamAdaptive(int stock, String strategy, String action){
        double acceptance = 0.7;

        //Stocks
        ArrayList<Stock> sl = training_pf.getDownloadedStocks();

		//Initialization
        training_pf.setStock(sl.get(stock));
        testing_pf.setStock(sl.get(stock));

        //Strategies
        training_strategy = new Strategy(training_pf);
        testing_strategy = new Strategy(testing_pf);

        if(action.equals("Signal")){
            testing_strategy.initSignals(strategy);
        }

        updateWin();
        int windowSize = winSize;
        int windowExtension = winExtension;

        testing_pf.initStock(testing_pf.getStock());

        //For each training period
        int accountSize = training_pf.getAccount().size();
        while(windowSize <= accountSize){

            //Reinitialise
            history = new ArrayList<SolutionHistory>();


            /*Simulated Annealing*/

            //Annealing Adjustments
            double initial_temperature = 30;
            double final_temperature = 0.5;
            double alpha = 0.9;

            //Initialise variables
            boolean use_new = false;

            double temperature = initial_temperature;

            int current_solution = 1;
            int working_solution = 1;
            int best_solution = 1;

            //Apply
            if(strategy.equals("MA")){
                training_strategy.movingAverageCalc(accountSize-1, accountSize-windowSize, working_solution);
                training_strategy.movingAveragePerform(accountSize-2, accountSize-windowSize, working_solution);
            }else if(strategy.equals("ROC")){
                training_strategy.rateOfChangeCalc(accountSize-1, accountSize-windowSize, working_solution);
                training_strategy.rateOfChangePerform(accountSize-1, accountSize-windowSize, working_solution);
            }else if(strategy.equals("RSI")){
                training_strategy.RSICalc(accountSize-1, accountSize-windowSize, working_solution);
                training_strategy.RSIPerform(accountSize-1, accountSize-windowSize, working_solution);
            }

            double current_energy = computeEnergy(current_solution);
            double working_energy = computeEnergy(working_solution);
            double best_energy = computeEnergy(best_solution);

            //Main loop continue until we reach lowest temperature
            while ( temperature > final_temperature ){

                //Reinitialise stock
                training_pf.initStock(training_pf.getStock());

                use_new = false;

                //Nearest neighbour
                working_solution = tweakPeriod(working_solution);

                //Apply
                if(strategy.equals("MA")){
                    training_strategy.movingAverageCalc(accountSize-1, accountSize-windowSize, working_solution);
                    training_strategy.movingAveragePerform(accountSize-2, accountSize-windowSize, working_solution);
                }else if(strategy.equals("ROC")){
                    training_strategy.rateOfChangeCalc(accountSize-1, accountSize-windowSize, working_solution);
                    training_strategy.rateOfChangePerform(accountSize-1, accountSize-windowSize, working_solution);
                }else if(strategy.equals("RSI")){
                    training_strategy.RSICalc(accountSize-1, accountSize-windowSize, working_solution);
                    training_strategy.RSIPerform(accountSize-1, accountSize-windowSize, working_solution);
                }

                //Evaluate
                working_energy = computeEnergy(working_solution);

                //If new solution is better than current, use new solution
                if(working_energy > current_energy ){

                    use_new = true;

                }else{

                    double random_test = smallRandomGen();

                    if(random_test>acceptance){
                        use_new = true;
                    }
                }

                if( use_new ){

                    //Copy working solution to current solution
                    use_new = false;
                    current_solution = working_solution;
                    current_energy = working_energy;

                    //If our new solution is better than the best solution so far - make it the best solution
                    if( current_energy > best_energy ){

                        best_solution = current_solution;
                        best_energy = current_energy;

                    }
                }else{

                    working_solution = current_solution;
                    working_energy = current_energy;

                }

                temperature *= alpha;
            }

            int initDate = accountSize-windowSize;
            int endDate = Math.max(0, initDate - windowExtension + 1);

			//Determine strategy and whether to perform transactions or to just record the signals
            if(strategy.equals("MA")){
                testing_strategy.movingAverageCalc(accountSize-1, endDate, best_solution);
                if(action.equals("Perform")){
                    testing_strategy.movingAveragePerform(initDate, endDate, best_solution);
                }else{
                    MASig = testing_strategy.movingAverageSignals(initDate, endDate, best_solution);
                }
            }else if(strategy.equals("ROC")){
                testing_strategy.rateOfChangeCalc(accountSize-1, endDate, best_solution);
                if(action.equals("Perform")){
                    testing_strategy.rateOfChangePerform(initDate, endDate, best_solution);
                }else{
                    ROCSig = testing_strategy.rateOfChangeSignals(initDate, endDate, best_solution);
                }
            }else if(strategy.equals("RSI")){
                testing_strategy.RSICalc(accountSize-1, endDate, best_solution);
                if(action.equals("Perform")){
                    testing_strategy.RSIPerform(initDate, endDate, best_solution);
                }else{
                    RSISig = testing_strategy.RSISignals(initDate, endDate, best_solution);
                }
            }

			//Extend windowSize
            windowSize += windowExtension;
        }
        if(action.equals("Perform")){
            testing_pf.printSingleParamCSV("Adaptive " + strategy, testing_pf.getStock().getName() + "_Adaptive_" + strategy + "(" + testing_pf.getTransactionCost() +").csv");
        }
    }

    //Adaptive for PM
    public void doubleParamAdaptive(int stock, String strategy, String action){
        double acceptance = 0.7;

        //Stocks
        ArrayList<Stock> sl = training_pf.getDownloadedStocks();

        //Initialization
        training_pf.setStock(sl.get(stock));
        testing_pf.setStock(sl.get(stock));

        //Strategies
        training_strategy = new Strategy(training_pf);
        testing_strategy = new Strategy(testing_pf);

        if(action.equals("Signal")){
            testing_strategy.initSignals(strategy);
        }

        updateWin();
        int windowSize = winSize;
        int windowExtension = winExtension;

        testing_pf.initStock(testing_pf.getStock());

        //For each training period
        int accountSize = training_pf.getAccount().size();
        while(windowSize <= accountSize){

            //Reinitialise
            history = new ArrayList<SolutionHistory>();


            //Simulated Annealing


            //Annealing Adjustments
            double initial_temperature = 30;
            double final_temperature = 0.5;
            double alpha = 0.9;

            //Initialise variables
            boolean use_new = false;

            double temperature = initial_temperature;

            PMSolution current_solution = new PMSolution(10,0.0);
            PMSolution working_solution = new PMSolution(10,0.0);
            PMSolution best_solution = new PMSolution(10,0.0);

            //Apply
            training_strategy.priceMomentumCalc(accountSize-1, accountSize-windowSize, working_solution.period, working_solution.thres);
            training_strategy.priceMomentumPerform(accountSize-1, accountSize-windowSize, working_solution.period, working_solution.thres);

            double current_energy = computeEnergy(current_solution);
            double working_energy = computeEnergy(working_solution);
            double best_energy = computeEnergy(best_solution);

            //Main loop continue until we reach lowest temperature
            while ( temperature > final_temperature ){

                use_new = false;

                //Nearest neighbour
                working_solution.period = tweakPeriod(working_solution.period);

                for(int i = 0; i<10; i++){
                    
					working_solution = tweakThres(working_solution);
					
					//Reinitialise stock
                    training_pf.initStock(training_pf.getStock());

                    //Apply
                    training_strategy.priceMomentumCalc(accountSize-1, accountSize-windowSize, working_solution.period, working_solution.thres);
                    training_strategy.priceMomentumPerform(accountSize-1, accountSize-windowSize, working_solution.period, working_solution.thres);

                    //Evaluate
                    working_energy = computeEnergy(working_solution);

                    //If new solution is better than current, use new solution
                    if(working_energy > current_energy ){

                        use_new = true;

                    }else{

                        double random_test = smallRandomGen();

                        if(random_test>acceptance){
                            use_new = true;
                        }
                    }

                    if( use_new ){

                        //Copy working solution to current solution
                        use_new = false;
                        current_solution = working_solution;
                        current_energy = working_energy;

                        //If our new solution is better than the best solution so far - make it the best solution
                        if( current_energy > best_energy ){

                            best_solution = current_solution;
                            best_energy = current_energy;
                        }
                    }else{

                        working_solution.thres = current_solution.thres;
                        working_energy = current_energy;
                    }

                }
                working_solution = current_solution;
                working_energy = current_energy;
                temperature *= alpha;
            }

            int initDate = accountSize-windowSize;
            int endDate = Math.max(0, initDate - windowExtension + 1);

            testing_strategy.priceMomentumCalc(accountSize-1, endDate, best_solution.period, best_solution.thres);

            if(action.equals("Perform")){
                testing_strategy.priceMomentumPerform(initDate, endDate, best_solution.period, best_solution.thres);
            }else{
                PMSig = testing_strategy.priceMomentumSignals(initDate, endDate, best_solution.period, best_solution.thres);
            }
			//update window
            windowSize += windowExtension;

            if(action.equals("Perform")){
                testing_pf.printDoubleParamCSV("Adaptive PM", testing_pf.getStock().getName() + "_Adaptive_" + strategy + "(" + testing_pf.getTransactionCost() +").csv");
            }
        }
    }

    //Adaptive for MACD
    public void tripleParamAdaptive(int stock, String strategy, String action){
        double acceptance = 0.7;

        PMSolution current_solution = new PMSolution(0,0,0);
        PMSolution working_solution = new PMSolution(0,0,0);
        PMSolution best_solution = new PMSolution(0,0,0);

        //Stocks
        ArrayList<Stock> sl = training_pf.getDownloadedStocks();

        //Initialization
        training_pf.setStock(sl.get(stock));
        testing_pf.setStock(sl.get(stock));

        //Strategies
        training_strategy = new Strategy(training_pf);
        testing_strategy = new Strategy(testing_pf);

        if(action.equals("Signal")){
            testing_strategy.initSignals(strategy);
        }

        updateWin();
        int windowSize = winSize;
        int windowExtension = winExtension;

        testing_pf.initStock(testing_pf.getStock());

        //For each training period
        int accountSize = training_pf.getAccount().size();
        while(windowSize <= accountSize){

            //Reinitialise
            history = new ArrayList<SolutionHistory>();

            //Simulated Annealing

            //Annealing Adjustments
            double initial_temperature = 30;
            double final_temperature = 0.5;
            double alpha = 0.9;

            //Initialise variables
            boolean use_new = false;

            double temperature = initial_temperature;

            int period [] = new int[3];
            period[0] = 12;
            period[1] = 26;
            period[2] = 10;

            current_solution = new PMSolution(period[0], period[1], period[2]);
            working_solution = new PMSolution(period[0], period[1], period[2]);
            best_solution = new PMSolution(period[0], period[1], period[2]);

            //Apply
            int larger = Math.max(period[0], period[1]);
            int applyDate = accountSize - (larger + period[2]);

            training_strategy.MACDCalc(accountSize-1, accountSize-windowSize, period);
            training_strategy.MACDPerform(applyDate, accountSize-windowSize, period);

            double current_energy = computeEnergy(current_solution);
            double working_energy = computeEnergy(working_solution);
            double best_energy = computeEnergy(best_solution);

            //Main loop continue until we reach lowest temperature
            while ( temperature > final_temperature ){

                use_new = false;

                //Nearest neighbour for period[0]
                working_solution = tweakMACD(working_solution,0);

                for(int period2 = 0; period2<10; period2++){

                    //Nearest neighbour for period[1]
                    working_solution = tweakMACD(working_solution,1);

                    for(int period3 = 0; period3<5; period3++){

                        //Nearest neighbour for period[2]
                        working_solution = tweakMACD(working_solution,2);

                        //Reinitialise stock
                        training_pf.initStock(training_pf.getStock());

                        period[0] = working_solution.period;
                        period[1] = working_solution.period2;
                        period[2] = working_solution.period3;
                        //Apply
                        training_strategy.MACDCalc(accountSize-1, accountSize-windowSize, period);
                        training_strategy.MACDPerform(applyDate, accountSize-windowSize, period);

                        //Evaluate
                        working_energy = computeEnergy(working_solution);

                        //If new solution is better than current, use new solution
                        if(working_energy > current_energy ){

                            use_new = true;

                        }else{

                            double random_test = smallRandomGen();

                            if(random_test>acceptance){
                                use_new = true;
                            }
                        }

                        if( use_new ){

                            //Copy working solution to current solution
                            use_new = false;
                            current_solution = working_solution;
                            current_energy = working_energy;

                            //If our new solution is better than the best solution so far - make it the best solution
                            if( current_energy > best_energy ){

                                best_solution = current_solution;
                                best_energy = current_energy;
                            }
                        }else{

                            working_solution.period3 = current_solution.period3;
                            working_energy = current_energy;
                        }
                    }//end period3 for loop
                    working_solution.period2 = current_solution.period2;
                    working_solution.period3 = current_solution.period3;
                    working_energy = current_energy;

                }//end period2 for loop
                working_solution = current_solution;
                working_energy = current_energy;
                temperature *= alpha;
            }//end while loop

            //Apply strategy
            int initDate = accountSize-windowSize;
            int endDate = Math.max(0, initDate - windowExtension + 1);

            int bestPeriod[] = new int[3];
            bestPeriod[0] = best_solution.period;
            bestPeriod[1] = best_solution.period2;
            bestPeriod[2] = best_solution.period3;

            testing_strategy.MACDCalc(accountSize-1, endDate, bestPeriod);
            if(action.equals("Perform")){
                testing_strategy.MACDPerform(initDate, endDate, bestPeriod);
            }else{
                MACDSig = testing_strategy.MACDSignals(initDate, endDate, bestPeriod);
            }

			//update window
            windowSize += windowExtension;
            }
		
            if(action.equals("Perform")){
                testing_pf.printTripleParamCSV("Adaptive MACD", testing_pf.getStock().getName() + "_Adaptive_" + strategy +"(" + testing_pf.getTransactionCost() + ").csv");
            }
    }

    //Combination Adaptive
    public void combinationAdaptive(){
        ArrayList<Stock> sl = testing_pf.getDownloadedStocks();
        
        for(int stock = 0; stock<sl.size(); stock++){
            print("Combination Adaptive for " + sl.get(stock).getName() + "...");
            print("Calculating MA...");
            singleParamAdaptive(stock, "MA", "Signal");
            print("Calculating ROC...");
            singleParamAdaptive(stock, "ROC", "Signal");
            print("Calculating RSI...");
            singleParamAdaptive(stock, "RSI", "Signal");
            print("Calculating PM...");
            doubleParamAdaptive(stock, "PM", "Signal");
            print("Calculating MACD...");
            tripleParamAdaptive(stock, "MACD", "Signal");
            
            ArrayList<DayAccount> account = testing_pf.getAccount();

            for(int i = account.size()-1; i>=0; i--){

                DayAccount today = account.get(i);
                testing_pf.setDate(today.getDate(), i);

                int buy = 0;
                int sell = 0;

                if(MASig[i]!=null){
                    if(MASig[i].equals("B")) buy++;
                    else if(MASig[i].equals("S")) sell++;
                }

                if(PMSig[i]!=null){
                    if(PMSig[i].equals("B")) buy++;
                    else if(PMSig[i].equals("S")) sell++;
                }

                if(ROCSig[i]!=null){
                    if(ROCSig[i].equals("B")) buy++;
                    else if(ROCSig[i].equals("S")) sell++;
                }

                if(MACDSig[i]!=null){
                    if(MACDSig[i].equals("B")) buy++;
                    else if(MACDSig[i].equals("S")) sell++;
                }

                if(RSISig[i]!=null){
                    if(RSISig[i].equals("B")) buy++;
                    else if(RSISig[i].equals("S")) sell++;
                }

                if(buy>=2){
                    //BUY MAXIMUM
                    double singleP = today.getPrice()*(1+testing_pf.getTransactionCost());
                    int quantity = (int)(today.getCash()/singleP);
                    testing_pf.buy(0, quantity);
                }
                if(sell>=2){
                    //SELL all
                    if(today.getQuantity()>0){
                        testing_pf.sell(0, today.getQuantity());
                    }
                }

                
            }
            testing_pf.printCombiCSV(testing_pf.getStock().getName() + "_CombinationAdaptive(" + testing_pf.getTransactionCost() + ").csv");
            print("Combination Adaptive Strategies completed.");
        }
    }
    
    //For period parameter (strategies MA, PM, ROC, RIS)
    private int tweakPeriod(int sol){
        //int period = 0;
        boolean rerun = true;
        int trialSize = randSize/2;
        int possible_sol = 0;

        while(rerun){
            rerun = false;
            possible_sol = sol + randomGen();

            if( possible_sol <= 0 || possible_sol > 100 ){
                rerun = true;
            }
            for(int i = 0; i<history.size(); i++){
                if(history.get(i).period == possible_sol){
                    rerun = true;
                    trialSize--;
					
					//Increase value of random values generated
                    if(trialSize == 0){
                        randSize*=2;
                    }
                }
            }
        }
        
        sol = possible_sol;
        return sol;

    }

    //For period parameters (strategy MACD)
    private PMSolution tweakMACD(PMSolution sol, int index){

        boolean rerun = true;
        int trialSize = randSize/2;
        int possible_sol = 0;

        while(rerun){
            rerun = false;
            switch(index){
                case 0:
                    possible_sol = sol.period + randomGen();
                    break;

                case 1:
                    possible_sol = sol.period2 + randomGen();
                    break;

                case 2:
                    possible_sol = sol.period3 + randomGen();
                    break;
            }
            
            if( possible_sol <= 0 || possible_sol > 100 ){
                rerun = true;
            }
            for(int i = 0; i<history.size(); i++){

                PMSolution his = history.get(i).pms;

                switch(index){
                    case 0:
                        if(his.period == possible_sol){
                            rerun = true;
                            trialSize--;
                            if(trialSize == 0){
                                randSize*=2;
                            }
                        }
                        break;

                    case 1:
                        if(his.period == sol.period && his.period2 == possible_sol){
                            rerun = true;
                            trialSize--;
                            if(trialSize == 0){
                                randSize*=2;
                            }
                        }
                        break;

                    case 2:
                        if((his.period == sol.period) && (his.period2 == sol.period2) && (his.period3 == possible_sol)){
                            rerun = true;
                            trialSize--;
                            if(trialSize == 0){
                                randSize*=2;
                            }
                        }
                        break;
                }

            }
        }

        switch(index){
            case 0:
                sol.period = possible_sol;
                break;

            case 1:
                sol.period2 = possible_sol;
                break;

            case 2:
                sol.period3 = possible_sol;
                break;

        }

        return sol;

    }

    //For threshold parameter (strategy PM)
    private PMSolution tweakThres(PMSolution sol){

        boolean rerun = true;
        double possible_thres = sol.thres;
        int runCount = 0;

        while(rerun){
            rerun = false;
            possible_thres = tinyRandomGen();

            for(int i = 0; i<history.size(); i++){
                PMSolution his = history.get(i).pms;
                if(his.period == sol.period && his.thres == possible_thres){
                    rerun = true;
                }
            }
            runCount++;
            if(runCount==10){
                return sol;
            }
        }

        sol.thres = possible_thres;
        return sol;

    }

    //Compute energy for single parameter (MA, ROC, RSI)
    private double computeEnergy(int sol){
        double eval = eval();

        //Add evaluation results to history
        SolutionHistory temp = new SolutionHistory(sol, eval);
        history.add(temp);

        return eval;
    }

    //Compute energy for multiple paramaters (PM, MACD)
    private double computeEnergy(PMSolution sol){
        double eval = eval();

        //Add evaluation results to history
        SolutionHistory temp = new SolutionHistory(sol, eval);
        history.add(temp);

        return eval;
    }

    //Evaluate results
    private double eval(){
        double ROI[] = training_pf.ROI();
        double Vol[] = training_pf.volatility();

        double eval = (50*ROI[0])-(40*Vol[0]);
        return eval;
    }


    //Generate numbers
    private double tinyRandomGen(){
        return Math.random()*0.1;
        }

    private double smallRandomGen(){
        return random.nextDouble();
    }

    private int randomGen(){
        int rand = Integer.parseInt(Math.round(Math.random()*randSize)-(randSize/2) + "");
        return rand;
    }

    //Print
    private static void print(String s){
        System.out.println(s);
    }

}
