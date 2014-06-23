
import java.util.LinkedHashMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.File;



public class Orderbook {
	public static void main(String[] args) {
		LinkedHashMap<String,orderData> Buy = new LinkedHashMap<String, orderData>();
		LinkedHashMap<String,orderData> Sell = new LinkedHashMap<String, orderData>();		 
		

       // File file = new File("smallSample.txt");
        
        try {
            //
            // Create a new Scanner object which will read the data 
            // from the file passed in. To check if there are more 
            // line to read from it we check by calling the 
            // scanner.hasNextLine() method. We then read line one 
            // by one till all line is read.
            //

            File file = new File("smallSample");
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.next();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}

}
