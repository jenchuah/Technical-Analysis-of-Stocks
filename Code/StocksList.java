
import java.util.ArrayList;

/**
 *
 * @author jen_chuah
 */
public class StocksList {

    private ArrayList<Stock> shares_list;

    public StocksList(){
//      In sample stocks
        Stock BHP = new Stock("BHP");
        Stock AAC = new Stock("AAC");
        Stock DJS = new Stock("DJS");
        Stock FLT = new Stock("FLT");
        Stock MYR = new Stock("MYR");
        Stock QAN = new Stock("QAN");
        Stock RIO = new Stock("RIO");
        Stock STO = new Stock("STO");
        Stock TLS = new Stock("TLS");
        Stock WBC = new Stock("WBC");

        Stock WPL = new Stock("WPL");
        Stock NCM = new Stock("NCM");
        Stock ORG = new Stock("ORG");
        Stock CBA = new Stock("CBA");
        Stock NAB = new Stock("NAB");
        Stock WOW = new Stock("WOW");
        Stock ANZ = new Stock("ANZ");
        Stock QBE = new Stock("QBE");
        Stock HST = new Stock("HST");
        Stock MAH = new Stock("MAH");

        Stock ROC = new Stock("ROC");
        Stock MCR = new Stock("MCR");
        Stock AJL = new Stock("AJL");
        Stock MML = new Stock("MML");
        Stock AGO = new Stock("AGO");
        Stock IFL = new Stock("IFL");
        Stock PNA = new Stock("PNA");
        Stock AAX = new Stock("AAX");
        Stock ABC = new Stock("ABC");
        Stock AIO = new Stock("AIO");

//        Out of sample stocks
//        Stock CAB = new Stock("CAB");
//        Stock CCL = new Stock("CCL");
//        Stock CDU = new Stock("CDU");
//        Stock CEU = new Stock("CEU");
//        Stock CFX = new Stock("CFX");
//        Stock CGF = new Stock("CGF");
//        Stock CHC = new Stock("CHC");
//        Stock CMJ = new Stock("CMJ");
//        Stock COH = new Stock("COH");
//        Stock CPA = new Stock("CPA");

        shares_list = new ArrayList<Stock>();

//      In sample stocks
        shares_list.add(BHP);
        shares_list.add(AAC);
        shares_list.add(DJS);
        shares_list.add(FLT);
        shares_list.add(MYR);
        shares_list.add(QAN);
        shares_list.add(RIO);
        shares_list.add(STO);
        shares_list.add(TLS);
        shares_list.add(WBC);

        shares_list.add(WPL);
        shares_list.add(NCM);
        shares_list.add(ORG);
        shares_list.add(CBA);
        shares_list.add(NAB);
        shares_list.add(WOW);
        shares_list.add(ANZ);
        shares_list.add(QBE);
        shares_list.add(HST);
        shares_list.add(MAH);

        shares_list.add(ROC);
        shares_list.add(MCR);
        shares_list.add(AJL);
        shares_list.add(MML);
        shares_list.add(AGO);
        shares_list.add(IFL);
        shares_list.add(PNA);
        shares_list.add(AAX);
        shares_list.add(ABC);
        shares_list.add(AIO);

//        Out of sample stocks
//        shares_list.add(CAB);
//        shares_list.add(CCL);
//        shares_list.add(CDU);
//        shares_list.add(CEU);
//        shares_list.add(CFX);
//        shares_list.add(CGF);
//        shares_list.add(CHC);
//        shares_list.add(CMJ);
//        shares_list.add(COH);
//        shares_list.add(CPA);
        
    }

    public ArrayList<Stock> getStocksList(){
        return shares_list;
    }
}

