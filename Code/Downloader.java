
/**
 *
 * @author jen_chuah
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class Downloader {

    Parser parser = new Parser();
    URL u;
    InputStream is = null;
    DataInputStream dis;
    BufferedWriter out = null;
    FileReader reader;
    String s, filename;

    public ArrayList<Day> download (String URLname, String filename) {
        try {

            this.filename = filename;
            
            /*Open URL stream and writer*/
            u = new URL(URLname);
            is = u.openStream();         
            dis = new DataInputStream(new BufferedInputStream(is));
            out = new BufferedWriter(new FileWriter(filename + ".csv"));

            /*Write data from URL to file*/
            while ((s = dis.readLine()) != null) {
                out.write(s);
                out.newLine();
            }

        } catch (MalformedURLException mue) {
            System.out.println("There is an error with the URL");
            return null;

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
            return null;
        }
          finally {
            try {
                is.close();
                out.close();
            } catch (IOException ioe) {
                System.out.println("Error closing input stream and output stream");

            } catch(Exception e){
                e.printStackTrace();
            }
        }

        ArrayList<Day> data = parseFile();
        return data;
    }

    private ArrayList<Day> parseFile(){
        ArrayList<Day> data = null;
        try{
            /*Parse downloaded file*/
            File appBase = new File("."); //current directory
            String dir = appBase.getAbsolutePath();
            String path = dir.substring(0, dir.length()-1) + filename + ".csv";

            reader = new FileReader(path);

            data = parser.read(reader, filename);

        }
        catch(FileNotFoundException fnf){
            fnf.printStackTrace();
        }

        return data;
    }
}
