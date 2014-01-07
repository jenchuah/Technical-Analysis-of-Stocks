
/**
 *
 * @author jen_chuah
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Parser {

    
    Day day;
    String type;

    public ArrayList<Day> read(Reader r, String filename){
    ArrayList <Day> dataArray = new ArrayList<Day>();
        
        try{
            BufferedReader br = new BufferedReader(r);
            String strLine = "";
            StringTokenizer st = null;
            int lineNumber = 0, tokenNumber = 0;
          
            while( (strLine = br.readLine()) != null)
            {
                
                day = new Day();
                
                lineNumber++;
                if(lineNumber == 1){
                    //ignore
                }else{
                    st = new StringTokenizer(strLine, ",");

                    while(st.hasMoreTokens())
                    {
                        switch(tokenNumber){
                            case 0:
                                day.setDate(st.nextToken());
                                break;

                            case 1:
                                day.setOpen(Float.parseFloat(st.nextToken()));
                                break;

                            case 2:
                                day.setHigh(Float.parseFloat(st.nextToken()));
                                break;

                            case 3:
                                day.setLow(Float.parseFloat(st.nextToken()));
                                break;

                            case 4:
                                day.setClose(Float.parseFloat(st.nextToken()));
                                break;

                            case 5:
                                day.setVolume(Long.parseLong(st.nextToken()));
                                break;

                            case 6:
                                day.setAdjClose(Float.parseFloat(st.nextToken()));
                                break;

                        }
                        tokenNumber++;
                    }

                    tokenNumber = 0;

                    dataArray.add(day);
                   
                }
            }

            
            
            
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return dataArray;
    }
}


