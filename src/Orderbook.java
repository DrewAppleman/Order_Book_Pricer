
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.File;



public class Orderbook {

	final int Target_Size=200;
	
	public static void main(String[] args) {
		LinkedHashMap<String,orderData> buyOrders = new LinkedHashMap<String, orderData>();
		LinkedHashMap<String,orderData> sellOrders = new LinkedHashMap<String, orderData>();	
		//orderData currentOrder = new orderData();
		List<orderData> buyOrderList = new ArrayList<orderData>();
		List<orderData> sellOrderList = new ArrayList<orderData>();
		

       // File file = new File("smallSample.txt");
        
        try {

            File file = new File("smallSample");
            
            Scanner scanner = new Scanner(file);
            
            //**********************
            //**Loop To Read input**
            //**********************
            while (scanner.hasNext()) {
            	orderData currentOrder = new orderData();
                String timeStamp = scanner.next();
                String add_Red = scanner.next();	//An 'A' if it is adding an order to book, an R if it is reducing order
                
                //Adding a Limit order to books
                if(add_Red.equals("A")){
                	String order_ID = scanner.next();
                	order_ID.replace(" ", "");		// Delete Leading and trailing zeroes
                	String side = scanner.next(); 	//'B' if it is a bid, 'S' if it is an ask 
                	String price = scanner.next();	//Limit price of order
                	String size = scanner.next();	// Size of order in shares
                	
                	currentOrder.price=Double.parseDouble(price);
                	currentOrder.size=Integer.parseInt(size);
                	
                	//For a bid limit order
                	if(side.equals("B")){
                		System.out.println(order_ID);
                		buyOrders.put(order_ID, currentOrder);
                		buyOrderList.add(currentOrder);
                		//buyOrders.put(order_ID, currentOrder);
                	}
                	
                	//For a limit ask
                	else{
                		sellOrders.put(order_ID, currentOrder);
                	}
                }
                
                //Reducing a Limit order
                else if(add_Red==("R")){
                	String order_ID = scanner.next();
                	String size = scanner.next();	// Size of order in shares
                
                	//Executes reduce order for buy orders
                	if(buyOrders.containsKey(order_ID)){
                		
                		//Reduces part of an order
                		if(Integer.parseInt(size)<buyOrders.get(order_ID).size){
                			buyOrders.get(order_ID).size=buyOrders.get(order_ID).size-Integer.parseInt(size);
                		}
                		else if(Integer.parseInt(size)==buyOrders.get(order_ID).size){			//gets rid of full order
                			buyOrders.remove(order_ID);
                		}
                		else{
                			System.out.println("Error: Attempted to reduce order by more than the order size");
                		}
                	}
                	
                	//Executes reduce order for sell orders
                	else if(sellOrders.containsKey(order_ID)){
                		if(Integer.parseInt(size)<sellOrders.get(order_ID).size){
                			sellOrders.get(order_ID).size=sellOrders.get(order_ID).size-Integer.parseInt(size);
                		}
                		else if(Integer.parseInt(size)==sellOrders.get(order_ID).size){			//gets rid of full order
                			sellOrders.remove(order_ID);
                		}
                		else{
                			System.out.println("Error: Attempted to reduce order by more than the order size");
                		}
                	}
                	
                	//Give error message and go to next line if there is an issue with input
                	else{
            			System.out.println("Error: Invalid input");
                        scanner.nextLine();
                	}
                }
                
                //System.out.println(line);
            }
            
            //System.out.println(buyOrders.get("c").price);
            buyOrderList.remove(buyOrders.get("f"));
            
            //Test print arraylist
            for(int i=0; i<buyOrderList.size();i++){
            	System.out.println(buyOrderList.get(i).price);
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	public double checkPrices(ArrayList<orderData> orders){
		int remainingTarget=Target_Size;
		int totalCost=0;
		
		
		for(int i=0; i<orders.size(); i++){
			if(remainingTarget>orders.get(i).size){
				remainingTarget=remainingTarget-orders.get(i).size;
				totalCost+=orders.get(i).size*orders.get(i).price;
			}
			else{
				totalCost+=remainingTarget*orders.get(i).price;
				return totalCost;
			}
		}
		return -1;
	}

}
