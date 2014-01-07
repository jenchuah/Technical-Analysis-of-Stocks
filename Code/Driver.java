
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.net.*;

/**
 *
 * @author jen_chuah
 */
public class Driver {
	
    static BufferedReader br;
    static Downloader dl;
    static Portfolio pf;
    static Portfolio training_pf;
	
    static String url1 = "http://ichart.finance.yahoo.com/table.csv?s=";
    static String url2 = ".AX&a=0&b=1&c=2005&d=3&e=31&f=2011&g=d&ignore=.csv";
	
    static int date_change = 0, quantity = 0;
    static int option;
    final static int autorunOp = 1;
	
    /*
     * Storage for part 2
     */
    static double MA_s0[] = new double[12];
    static double MA_s1[] = new double[12];
    static double MA_s2[] = new double[12];
    static double MA_s3[] = new double[12];
    static double MA_s4[] = new double[12];
    static double MA_s5[] = new double[12];
    static double MA_s6[] = new double[12];
    static double MA_s7[] = new double[12];
    static double MA_s8[] = new double[12];
    static double MA_s9[] = new double[12];
	
    static double PM_s0[] = new double[12];
    static double PM_s1[] = new double[12];
    static double PM_s2[] = new double[12];
    static double PM_s3[] = new double[12];
    static double PM_s4[] = new double[12];
    static double PM_s5[] = new double[12];
    static double PM_s6[] = new double[12];
    static double PM_s7[] = new double[12];
    static double PM_s8[] = new double[12];
    static double PM_s9[] = new double[12];
	
    static double ROC_s0[] = new double[12];
    static double ROC_s1[] = new double[12];
    static double ROC_s2[] = new double[12];
    static double ROC_s3[] = new double[12];
    static double ROC_s4[] = new double[12];
    static double ROC_s5[] = new double[12];
    static double ROC_s6[] = new double[12];
    static double ROC_s7[] = new double[12];
    static double ROC_s8[] = new double[12];
    static double ROC_s9[] = new double[12];
	
    static double MACD_s0[] = new double[12];
    static double MACD_s1[] = new double[12];
    static double MACD_s2[] = new double[12];
    static double MACD_s3[] = new double[12];
    static double MACD_s4[] = new double[12];
    static double MACD_s5[] = new double[12];
    static double MACD_s6[] = new double[12];
    static double MACD_s7[] = new double[12];
    static double MACD_s8[] = new double[12];
    static double MACD_s9[] = new double[12];
	
    static double RSI_s0[] = new double[12];
    static double RSI_s1[] = new double[12];
    static double RSI_s2[] = new double[12];
    static double RSI_s3[] = new double[12];
    static double RSI_s4[] = new double[12];
    static double RSI_s5[] = new double[12];
    static double RSI_s6[] = new double[12];
    static double RSI_s7[] = new double[12];
    static double RSI_s8[] = new double[12];
    static double RSI_s9[] = new double[12];
	
	
    public static void main(String args[]){
        //Uncomment the following 3 lines to use in case of proxy
        //Authenticator.setDefault (new MyAuthenticator());
		//System.setProperty("http.proxyHost", "www-proxy.adelaide.edu.au");
		//System.setProperty("http.proxyPort", "8080");
        
        br = new BufferedReader(new InputStreamReader(System.in));
        dl = new Downloader();
        pf = new Portfolio();
        training_pf = new Portfolio();
        download();
        command();
    }
	
    static class MyAuthenticator extends Authenticator {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication (getUserName(), getPassword());
		}
    }
	
    /*
     * Prints Option box
     */
    private static void command(){
        print("\n***********************************************************");
        print("Choose an option from the following by entering the number.");
        print("0 - Exit");
        print("1 - Select stock");
		
        if(pf.getAccount()!=null){
            print("\nASSIGNMENT PART 1 - RANDOM");
            print("3 - Random");
            print("4 - ROI");
            print("5 - Volatility");
			
            print("\nASSIGNMENT PART 2 - STRATEGIES");
            print("6 - Moving Average");
            print("7 - Price Momentum");
            print("8 - Rate Of Change");
            print("9 - Moving Average Convergence-Divergence");
            print("10 - Relative Strength Index");
        }
		
        print("\nASSIGNMENT PART 3 - ADAPTIVE STRATEGIES");
        print("11 - Adaptive Moving Average");
        print("12 - Adaptive Price Momentum");
        print("13 - Adaptive Rate Of Change");
        print("14 - Adaptive Moving Average Convergence-Divergence");
        print("15 - Adaptive Relative Strength Index");
        print("16 - Combination Adaptive");
		
        print("\nEDITING TOOLS");
        print("20 - Set Transaction Cost");
        print("21 - Get Transaction Cost");
        print("22 - Edit window for adaptive strategies");
		
        print("\nAutorun");
        print("30 - Autorun Part 1");
        print("31 - Autorun Part 2");
		
        print("***********************************************************\n");
        System.out.print("\nEnter OPTION >");
		
        receiveInput();
		
        print("");
    }
	
    /*
     * Handle user input
     */
    private static void receiveInput(){
        String input;
        int command;
		
        try {
            input = br.readLine();
            command = Integer.parseInt(input);
            option = command;
            pf.setOption(option);
            switch(command){
					
                case 0:
                    print("Exiting...");
                    br.close();
                    System.exit(0);
					
                case 1:
                    selectStock();
                    command();
                    break;
					
                case 3:
                    random();
                    command();
                    break;
					
                case 4:
                    ROI();
                    command();
                    break;
					
                case 5:
                    Volatility();
                    command();
                    break;
					
                case 6:
                    MovingAverage();
                    command();
                    break;
					
                case 7:
                    PriceMomentum();
                    command();
                    break;
					
                case 8:
                    ROC();
                    command();
                    break;
					
                case 9:
                    MACD();
                    command();
                    break;
					
                case 10:
                    RSI();
                    command();
                    break;
					
                case 11:
                    AdaptiveMA();
                    command();
                    break;
					
                case 12:
                    AdaptivePriceMomentum();
                    command();
                    break;
					
                case 13:
                    AdaptiveROC();
                    command();
                    break;
					
                case 14:
                    AdaptiveMACD();
                    command();
                    break;
					
                case 15:
                    AdaptiveRSI();
                    command();
                    break;
					
                case 16:
                    CombinationAdaptive();
                    command();
                    break;
					
                case 20:
                    setTC();
                    command();
                    break;
					
                case 21:
                    getTC();
                    command();
                    break;
					
                case 22:
                    editWindow();
                    command();
                    break;
					
                case 30:
                    AutoPart1();
                    command();
                    break;
					
                case 31:
                    AutoPart2();
                    command();
                    break;
					
                default:
                    print("Invalid option number.");
                    command();
                    break;
            }
        }
		
        catch (IOException ioe) {
            print("Input error");
            command();
        }
		
        catch (NumberFormatException ex) {
            print("ReceiveInput - Input is not a number");
            command();
        }
    }
	
    private static int receiveInt(){
        String input;
        int command;
		
        try {
            input = br.readLine();
            command = Integer.parseInt(input);
            return command;
			
        }catch (IOException ioe) {
            print("Input error");
            return -1;
        }
		
        catch (NumberFormatException ex) {
            print("Input is not a number");
            command();
            return -1;
        }
    }
	
    private static double receiveDbl(){
        String input;
        double command;
		
        try {
            input = br.readLine();
            command = Double.parseDouble(input);
            return command;
			
        }catch (IOException ioe) {
            print("Input error");
            return -1.0;
        }
		
        catch (NumberFormatException ex) {
            print("Input is not a number");
            command();
            return -1.0;
        }
    }
	
    /*
     * Download stocks and market data
     */
    private static void download(){
        downloadStocks();
        downloadMarket();
    }
	
    private static void downloadStocks(){
		
        ArrayList<Stock> sl = pf.getStocksList().getStocksList();
        for(int i = 0; i<sl.size(); i++){
            String name = sl.get(i).getName();
            String url = url1 + name + url2;
            System.out.println("Downloading stock: " + name + ", URL: " + url);
            ArrayList<Day> dlStock = dl.download(url, name);
            if(dlStock != null){
                Stock newStock = new Stock(name);
                newStock.setData(dlStock);
                pf.addDownloadedStock(newStock);
                training_pf.addDownloadedStock(newStock);
            }
        }
		
    }
	
    private static void downloadMarket(){
        ArrayList<Day> dlMarket = dl.download("http://ichart.finance.yahoo.com/table.csv?s=%5EAXJO&d=3&e=31&f=2011&g=d&a=0&b=1&c=2005&ignore=.csv", "^AXJO");
        pf.createMarket(dlMarket);
        training_pf.createMarket(dlMarket);
		
    }
	
    /*
     * Select stock
     */
    private static void selectStock(){
        printStockList();
        System.out.print(">");
        int result = receiveInt();
        if(result<0){
            selectStock();
        }else{
            ArrayList<Stock> sl = pf.getDownloadedStocks();
            pf.initStock(sl.get(result));
        }
        print("");
    }
	
    private static void printStockList(){
        print("Select one stock to use in portfolio");
        ArrayList<Stock> sl = pf.getDownloadedStocks();
        for(int i = 0; i<sl.size(); i++){
            print(i + " - " + sl.get(i).getName());
        }
    }
	
    /*
     * Set/get transaction cost
     */
    private static void setTC(){
        print("Enter new transaction cost in double format");
        System.out.print(">");
        double tc = receiveDbl();
        if(tc == -1){
            print("Input is not a number");
            setTC();
        }
        pf.setTransactionCost(tc);
        training_pf.setTransactionCost(tc);
        print("");
    }
	
    private static void getTC(){
        print("Transaction cost is " + pf.getTransactionCost());
    }
	
    /*
     * Edit window for adaptive strategies
     */
    private static void editWindow(){
        int winSize = editWinSize();
        int winExt = editWinExt();
		
        pf.editWindow(winSize, winExt);
        training_pf.editWindow(winSize, winExt);
    }
	
    private static int editWinSize(){
        print("Enter new window update size in integer format");
        System.out.print(">");
        int winSize = receiveInt();
        if(winSize == -1){
            print("Input is not a number");
            editWinSize();
        }
        return winSize;
	}
	
    private static int editWinExt(){
        print("Enter length of period before next update in integer format");
        System.out.print(">");
        int winExt = receiveInt();
        if(winExt == -1){
            print("Input is not a number");
            editWinExt();
        }
        return winExt;
    }
	
    /*
     * Calculate performances - ROI and Volatility
     */
    private static void ROI(){
        double roi [] = pf.ROI();
        print(pf.getStock().getName() + "'s ROI: " + roi[0] + ", market index's ROI: " + roi[1]);
    }
	
    private static void Volatility(){
        double vol [] = pf.volatility();
        print(pf.getStock().getName() + "'s vol: " + vol[0] + ", market index's vol: " + vol[1]);
		
    }
	
    /*************************************\
     * Assignment Part 1 - Random Trading *
	 \*************************************/
	
    /*
     * Generate random variables
     */
    private static void generate(){
        quantity = Integer.parseInt(Math.round(Math.random()*500)+"");
        date_change = Integer.parseInt(Math.round(Math.random()*100)+"");
    }
	
    /*
     * Buy/Sell Transactions
     */
    private static void buy(){
        generate();
        if(option != autorunOp){
            print("Quantity to buy is " + quantity + "\n Date_change is " + date_change);
        }
        pf.buy(date_change, quantity);
		
    }
	
    private static void sell(){
        generate();
        if(option != autorunOp){
            print("Quantity to sell is " + quantity + "\n Date_change is " + date_change);
        }
        pf.sell(date_change, quantity);
    }
	
    /*
     * Decide to buy or sell
     */
    private static void random(){
        double r = Math.ceil(Math.random()*10);
        if(option != autorunOp){
            print("r is " + r);
        }
        if(r>= 5){
            buy();
        }else{
            sell();
        }
    }
	
	
    /*
     * Autorun Part 1 of Assignment
     */
    private static void AutoPart1(){
        System.out.println("stockCount,runCount,ROI,MarketROI,Volatility,MarketVolatility");
        //For each stock
		//        ArrayList<Stock> sl = pf.getStocksList().getStocksList();
        ArrayList<Stock> sl = pf.getDownloadedStocks();
        for(int stockCount = 0; stockCount<10; stockCount++){
			//            for(int runCount = 0; runCount<5; runCount++){
			pf.initStock(sl.get(stockCount));
			//                if (runCount==0){
			//                    pf.setTransactionCost(0.005);
			//                }
			//                else{
			//                    pf.setTransactionCost(pf.getTransactionCost()+0.005);
			//                }
			//Randomise 8 transactions and produce result
			for(int transactionCount = 0; transactionCount<200; transactionCount++){
				random();
			}
			pf.ROI();
			pf.volatility();
			pf.printSingleParamCSV("Random", pf.getStock().getName() + "(" + pf.getTransactionCost() + ").csv");
			//                pf.DVS(runCount);
			//            }
			
        }
        print("Stock,runCount,Date,Total");
		//        pf.DVSPrint();
    }
	
    private static void AutoPart2(){
        ArrayList<Stock> sl = pf.getDownloadedStocks();
        //For each strategy
        for(int str = 0; str < 5; str++){
            //For the first 10 stocks
            for(int stockCount = 0; stockCount<=9; stockCount++){
                //Parameter value to be used
                for(int p = 5; p<=60; p+=5){
                    //Initialise stock
                    pf.setStock(sl.get(stockCount));
                    Strategy s = new Strategy(pf);
					
                    //Moving average
                    if(str == 0){
                        s.movingAverageCalc(pf.getAccount().size()-1, 0, p);
                        s.movingAveragePerform(pf.getAccount().size()-2, 0, p);
						
                        //Prints .csv file
                        String filename = pf.getStock().getName() + "_MA(P"+ p +"_TC" + pf.getTransactionCost() + ").csv";
                        pf.printSingleParamCSV("MA", filename);
						
                        //Records result (total value) in array
                        switch(stockCount){
                            case 0:
                                MA_s0[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 1:
                                MA_s1[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 2:
                                MA_s2[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 3:
                                MA_s3[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 4:
                                MA_s4[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 5:
                                MA_s5[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 6:
                                MA_s6[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 7:
                                MA_s7[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 8:
                                MA_s8[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 9:
                                MA_s9[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                        }
                    }
                    //Rate of change
                    else if(str == 1){
                        s.rateOfChangeCalc(pf.getAccount().size()-1, 0, p);
                        s.rateOfChangePerform(pf.getAccount().size()-1, 0, p);
						
                        //Prints .csv file
                        String filename = pf.getStock().getName() + "_ROC(P"+ p +"_TC" + pf.getTransactionCost() + ").csv";
                        pf.printSingleParamCSV("ROC", filename);
						
                        //Records result (total value) in array
                        switch(stockCount){
                            case 0:
                                ROC_s0[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 1:
                                ROC_s1[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 2:
                                ROC_s2[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 3:
                                ROC_s3[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 4:
                                ROC_s4[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 5:
                                ROC_s5[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 6:
                                ROC_s6[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 7:
                                ROC_s7[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 8:
                                ROC_s8[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 9:
                                ROC_s9[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                        }
                    }
                    //Relative strength index
                    else if(str == 2){
                        s.RSICalc(pf.getAccount().size()-1, 0, p);
                        s.RSIPerform(pf.getAccount().size()-1, 0, p);
						
                        //Prints .csv file
                        String filename = pf.getStock().getName() + "_RSI(P"+ p +"_TC" + pf.getTransactionCost() + ").csv";
                        pf.printSingleParamCSV("RSI", filename);
						
                        //Records result (total value) in array
                        switch(stockCount){
                            case 0:
                                RSI_s0[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 1:
                                RSI_s1[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 2:
                                RSI_s2[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 3:
                                RSI_s3[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 4:
                                RSI_s4[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 5:
                                RSI_s5[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 6:
                                RSI_s6[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 7:
                                RSI_s7[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 8:
                                RSI_s8[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 9:
                                RSI_s9[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                        }
                    }
                    //Price momentum
                    else if(str == 3){
                        s.priceMomentumCalc(pf.getAccount().size()-1, 0, p, 0.02);
                        s.priceMomentumPerform(pf.getAccount().size()-1, 0, p, 0.02);
						
                        //Prints .csv file
                        String filename = pf.getStock().getName() + "_PM(P"+ p +"_TC" + pf.getTransactionCost() + ").csv";
                        pf.printDoubleParamCSV("PM", filename);
						
                        //Records result (total value) in array
                        switch(stockCount){
                            case 0:
                                PM_s0[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 1:
                                PM_s1[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 2:
                                PM_s2[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 3:
                                PM_s3[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 4:
                                PM_s4[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 5:
                                PM_s5[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 6:
                                PM_s6[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 7:
                                PM_s7[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 8:
                                PM_s8[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 9:
                                PM_s9[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                        }
                    }
                    //MACD
                    else if(str == 4){
                        int period[] = new int[3];
                        period[0] = 12;
                        period[1] = 26;
                        period[2] = p;
                        s.MACDCalc(pf.getAccount().size()-1, 0, period);
                        s.MACDPerform(pf.getAccount().size()-1, 0, period);
						
                        //Prints .csv file
                        String filename = pf.getStock().getName() + "_MACD(P"+ period[2] +"_TC" + pf.getTransactionCost() + ").csv";
                        pf.printTripleParamCSV("MACD", filename);
						
                        //Records result (total value) in array
                        switch(stockCount){
                            case 0:
                                MACD_s0[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 1:
                                MACD_s1[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 2:
                                MACD_s2[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 3:
                                MACD_s3[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 4:
                                MACD_s4[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 5:
                                MACD_s5[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 6:
                                MACD_s6[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 7:
                                MACD_s7[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 8:
                                MACD_s8[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                            case 9:
                                MACD_s9[(p/5)-1] = pf.getAccount().get(0).getTotal();
                                break;
								
                        }
                    }
					
                }
            }
        }
        //Prints summary in .csv file
        printPart2();
    }
	
    /*
     * Get directory path
     */
    private static String getCurrentDir(){
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
	
    /*
     * Creates new directory if non-existent
     */
    private static String setNewDir(String secDir){
        String dirName = getCurrentDir() + secDir;
		
        File dir = new File(dirName);
        if(dir.exists() == false){
            dir.mkdir();
        }
        return dirName;
    }
	
    /*
     * Prints summary of static strategy autorun
     */
    public static void printPart2(){
        try {
            ArrayList<Stock> sl = pf.getStocksList().getStocksList();
            //For the first 10 stocks
            for(int stock = 0; stock < 10; stock++){
                //For each strategy
                for(int strat = 0; strat <5; strat++ ){
                    if(strat == 0){
                        String filename = sl.get(stock).getName() + "_MA.csv";
                        String secDir = "Part2";
                        setNewDir("/Output");
                        String filePath = setNewDir("/Output/"+ secDir) + "/" + filename;
                        BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
                        for(int el = 0; el<12; el++){
                            switch(stock){
                                case 0:
									out.write(((el+1)*5) + "," + MA_s0[el]);
									break;
									
								case 1:
									out.write(((el+1)*5) + "," + MA_s1[el]);
									break;
									
								case 2:
									out.write(((el+1)*5) + "," + MA_s2[el]);
									break;
									
								case 3:
									out.write(((el+1)*5) + "," + MA_s3[el]);
									break;
									
								case 4:
									out.write(((el+1)*5) + "," + MA_s4[el]);
									break;
									
								case 5:
									out.write(((el+1)*5) + "," + MA_s5[el]);
									break;
									
								case 6:
									out.write(((el+1)*5) + "," + MA_s6[el]);
									break;
									
								case 7:
									out.write(((el+1)*5) + "," + MA_s7[el]);
									break;
									
								case 8:
									out.write(((el+1)*5) + "," + MA_s8[el]);
									break;
									
								case 9:
									out.write(((el+1)*5) + "," + MA_s9[el]);
									break;
                            }
                            out.write("\n");
                        }
                        out.flush();
                        out.close();
                    }
                    else if(strat == 1){
                        String filename = sl.get(stock).getName() + "_ROC.csv";
                        String secDir = "Part2";
                        setNewDir("/Output");
                        String filePath = setNewDir("/Output/"+ secDir) + "/" + filename;
                        BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
                        for(int el = 0; el<12; el++){
                            switch(stock){
                                case 0:
									out.write(((el+1)*5) + "," + ROC_s0[el]);
									break;
									
								case 1:
									out.write(((el+1)*5) + "," + ROC_s1[el]);
									break;
									
								case 2:
									out.write(((el+1)*5) + "," + ROC_s2[el]);
									break;
									
								case 3:
									out.write(((el+1)*5) + "," + ROC_s3[el]);
									break;
									
								case 4:
									out.write(((el+1)*5) + "," + ROC_s4[el]);
									break;
									
								case 5:
									out.write(((el+1)*5) + "," + ROC_s5[el]);
									break;
									
								case 6:
									out.write(((el+1)*5) + "," + ROC_s6[el]);
									break;
									
								case 7:
									out.write(((el+1)*5) + "," + ROC_s7[el]);
									break;
									
								case 8:
									out.write(((el+1)*5) + "," + ROC_s8[el]);
									break;
									
								case 9:
									out.write(((el+1)*5) + "," + ROC_s9[el]);
									break;
                            }
                            out.write("\n");
                        }
                        out.flush();
                        out.close();
                    }
                    else if(strat == 2){
                        String filename = sl.get(stock).getName() + "_RSI.csv";
                        String secDir = "Part2";
                        setNewDir("/Output");
                        String filePath = setNewDir("/Output/"+ secDir) + "/" + filename;
                        BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
                        for(int el = 0; el<12; el++){
                            switch(stock){
                                case 0:
									out.write(((el+1)*5) + "," + RSI_s0[el]);
									break;
									
								case 1:
									out.write(((el+1)*5) + "," + RSI_s1[el]);
									break;
									
								case 2:
									out.write(((el+1)*5) + "," + RSI_s2[el]);
									break;
									
								case 3:
									out.write(((el+1)*5) + "," + RSI_s3[el]);
									break;
									
								case 4:
									out.write(((el+1)*5) + "," + RSI_s4[el]);
									break;
									
								case 5:
									out.write(((el+1)*5) + "," + RSI_s5[el]);
									break;
									
								case 6:
									out.write(((el+1)*5) + "," + RSI_s6[el]);
									break;
									
								case 7:
									out.write(((el+1)*5) + "," + RSI_s7[el]);
									break;
									
								case 8:
									out.write(((el+1)*5) + "," + RSI_s8[el]);
									break;
									
								case 9:
									out.write(((el+1)*5) + "," + RSI_s9[el]);
									break;
                            }
                            out.write("\n");
                        }
                        out.flush();
                        out.close();
                    }
                    else if(strat == 3){
                        String filename = sl.get(stock).getName() + "_PM.csv";
                        String secDir = "Part2";
                        setNewDir("/Output");
                        String filePath = setNewDir("/Output/"+ secDir) + "/" + filename;
                        BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
                        for(int el = 0; el<12; el++){
                            switch(stock){
                                case 0:
									out.write(((el+1)*5) + "," + PM_s0[el]);
									break;
									
								case 1:
									out.write(((el+1)*5) + "," + PM_s1[el]);
									break;
									
								case 2:
									out.write(((el+1)*5) + "," + PM_s2[el]);
									break;
									
								case 3:
									out.write(((el+1)*5) + "," + PM_s3[el]);
									break;
									
								case 4:
									out.write(((el+1)*5) + "," + PM_s4[el]);
									break;
									
								case 5:
									out.write(((el+1)*5) + "," + PM_s5[el]);
									break;
									
								case 6:
									out.write(((el+1)*5) + "," + PM_s6[el]);
									break;
									
								case 7:
									out.write(((el+1)*5) + "," + PM_s7[el]);
									break;
									
								case 8:
									out.write(((el+1)*5) + "," + PM_s8[el]);
									break;
									
								case 9:
									out.write(((el+1)*5) + "," + PM_s9[el]);
									break;
                            }
                            out.write("\n");
                        }
                        out.flush();
                        out.close();
                    }
                    else if(strat == 4){
                        String filename = sl.get(stock).getName() + "_MACD.csv";
                        String secDir = "Part2";
                        setNewDir("/Output");
                        String filePath = setNewDir("/Output/"+ secDir) + "/" + filename;
                        BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
                        for(int el = 0; el<12; el++){
                            switch(stock){
                                case 0:
									out.write(((el+1)*5) + "," + MACD_s0[el]);
									break;
									
								case 1:
									out.write(((el+1)*5) + "," + MACD_s1[el]);
									break;
									
								case 2:
									out.write(((el+1)*5) + "," + MACD_s2[el]);
									break;
									
								case 3:
									out.write(((el+1)*5) + "," + MACD_s3[el]);
									break;
									
								case 4:
									out.write(((el+1)*5) + "," + MACD_s4[el]);
									break;
									
								case 5:
									out.write(((el+1)*5) + "," + MACD_s5[el]);
									break;
									
								case 6:
									out.write(((el+1)*5) + "," + MACD_s6[el]);
									break;
									
								case 7:
									out.write(((el+1)*5) + "," + MACD_s7[el]);
									break;
									
								case 8:
									out.write(((el+1)*5) + "," + MACD_s8[el]);
									break;
									
								case 9:
									out.write(((el+1)*5) + "," + MACD_s9[el]);
									break;
                            }
                            out.write("\n");
                        }
                        out.flush();
                        out.close();
                    }
                }
            }
			
			
        } catch (IOException e) {
        }
    }
	
    /********************************\
	 * Assignment Part 2 - Strategies *
	 \********************************/
	
    /*
     * Strategy - Moving Average
     */
    private static void MovingAverage(){
        int period = MAPrompt();
        int initDate = pf.getAccount().size()-1;
        int endDate = 0;
		
        Strategy strategy = new Strategy(pf);
        strategy.movingAverageCalc(initDate, endDate, period);
        strategy.movingAveragePerform(initDate-1, endDate, period);
		
        String filename = pf.getStock().getName() + "_MA(P"+ period +"_TC" + pf.getTransactionCost() + ").csv";
        pf.printSingleParamCSV("MA", filename);
    }
	
    private static int MAPrompt(){
        print("Enter the period for the moving average strategy in integer format.");
        int period = receiveInt();
        if(period == -1){
            print("Input is not a number");
            MAPrompt();
        }
        return period;
    }
	
    /*
     * Strategy - Price Momentum
     */
    private static void PriceMomentum(){
        int period = PMPromptPeriod();
        double threshold = PMPromptThreshold();
        int initDate = pf.getAccount().size()-1;
        int endDate = 0;
		
        Strategy strategy = new Strategy(pf);
        strategy.priceMomentumCalc(initDate, endDate, period, threshold);
        strategy.priceMomentumPerform(initDate, endDate, period, threshold);
		
        String filename = pf.getStock().getName() + "_PM(P"+ period +"_T " + threshold + "_TC" + pf.getTransactionCost() + ").csv";
        pf.printDoubleParamCSV("PM", filename);
    }
	
    private static int PMPromptPeriod(){
        print("Enter the period for the price momentum strategy in integer format.");
        int period = receiveInt();
        if(period == -1){
            print("Input is not a number");
            PMPromptPeriod();
        }
        return period;
    }
	
    private static double PMPromptThreshold(){
        print("Enter the threshold for the price momentum strategy in double format.");
        double threshold = receiveDbl();
        if(threshold == -1.0){
            print("Input is not a number");
            PMPromptThreshold();
        }
        return threshold;
    }
	
    /*
     * Strategy - Rate of Change
     */
    private static void ROC(){
        int period = ROCPromptPeriod();
        int initDate = pf.getAccount().size()-1;
        int endDate = 0;
		
        Strategy strategy = new Strategy(pf);
        strategy.rateOfChangeCalc(initDate, endDate, period);
        strategy.rateOfChangePerform(initDate, endDate, period);
		
        String filename = pf.getStock().getName() + "_ROC(P"+ period + "_TC" + pf.getTransactionCost() + ").csv";
        pf.printSingleParamCSV("ROC", filename);
		
		
    }
	
    private static int ROCPromptPeriod(){
        print("Enter the period for the Rate-Of-Change strategy in integer format.");
        int period = receiveInt();
        if(period == -1){
            print("Input is not a number");
            PMPromptPeriod();
        }
        return period;
    }
	
    /*
     * Strategy - Moving Average Convergence-Divergence
     */
    private static void MACD(){
        int period[] = new int[3];
        for(int i = 0; i<3; i++){
            period[i] = MACDPromptPeriod(i);
        }
        int initDate = pf.getAccount().size()-1;
        int larger = Math.max(period[0], period[1]);
        int applyDate = pf.getAccount().size() - (larger + period[2]);
        System.out.println("applyDate " + applyDate);
        int endDate = 0;
		
        Strategy strategy = new Strategy(pf);
        strategy.MACDCalc(initDate, endDate, period);
        strategy.MACDPerform(applyDate, endDate, period);
		
        String filename = pf.getStock().getName() + "_MACD(P1:"+ period[0] + ",P2:" + period[1] + ",P3:" + period[2] + "_TC" + pf.getTransactionCost() + ").csv";
        pf.printTripleParamCSV("MACD", filename);
		
    }
	
    private static int MACDPromptPeriod(int i){
        if(i <= 1){
            print("Enter period " + i + " to calculate the MACD for the MACD strategy in integer format.");
        }else{
            print("Enter the period to calculate the Signal Line for the MACD strategy in integer format.");
        }
        int period = receiveInt();
        if(period == -1){
            print("Input is not a number");
            MACDPromptPeriod(i);
        }
        return period;
    }
	
    /*
     * Strategy - Relative Strength Index
     */
    private static void RSI(){
        int period = RSIPromptPeriod();
        int initDate = pf.getAccount().size()-1;
        int endDate = 0;
		
        Strategy strategy = new Strategy(pf);
        strategy.RSICalc(initDate, endDate, period);
        strategy.RSIPerform(initDate, endDate, period);
		
        String filename = pf.getStock().getName() + "_RSI(P"+ period + "_TC" + pf.getTransactionCost() + ").csv";
        pf.printSingleParamCSV("RSI", filename);
		
    }
	
    private static int RSIPromptPeriod(){
        print("Enter period for the RSI strategy in integer format.");
        int period = receiveInt();
        if(period == -1){
            print("Input is not a number");
            RSIPromptPeriod();
        }
        return period;
    }
	
    /*****************************************\
	 * Assignment Part 3 - Adaptive Strategies *
	 \*****************************************/
    private static void AdaptiveMA(){
        AdaptiveStrategy adapt = new AdaptiveStrategy(training_pf, pf);
        print("Adaptive MA strategy...");
        adapt.rotateStock("MA");
    }
	
    private static void AdaptivePriceMomentum(){
        AdaptiveStrategy adapt = new AdaptiveStrategy(training_pf, pf);
        print("Adaptive PM strategy...");
        adapt.rotateStock("PM");
    }
	
    private static void AdaptiveROC(){
        AdaptiveStrategy adapt = new AdaptiveStrategy(training_pf, pf);
        print("Adaptive ROC strategy...");
        adapt.rotateStock("ROC");
    }
	
    private static void AdaptiveMACD(){
        AdaptiveStrategy adapt = new AdaptiveStrategy(training_pf, pf);
        print("Adaptive MACD strategy...");
        adapt.rotateStock("MACD");
    }
	
    private static void AdaptiveRSI(){
        AdaptiveStrategy adapt = new AdaptiveStrategy(training_pf, pf);
        print("Adaptive RSI strategy...");
        adapt.rotateStock("RSI");
    }
	
    private static void CombinationAdaptive(){
        AdaptiveStrategy comb = new AdaptiveStrategy(training_pf, pf);
        comb.combinationAdaptive();
    }
    /*
     * Helper method
     */
    private static void print(String s){
        System.out.println(s);
    }
	
	private static String getUserName(){
        return "a1180639";
    }
	
    private static char[] getPassword(){
        char[] password ={ 'w', 'a', 'y', 't', 'o', 'j', 'a', 'm', '@', '1', '2', '8' };
        return password;
    }
	
	
}
