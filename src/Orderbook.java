
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

	final static int Target_Size=200;
	
	public static void main(String[] args) {
		LinkedHashMap<String,orderData> mapOrders = new LinkedHashMap<String, orderData>();
		//LinkedHashMap<String,orderData> sellOrders = new LinkedHashMap<String, orderData>();	
		//orderData currentOrder = new orderData();
		List<orderData> buyOrderList = new ArrayList<orderData>();
		List<orderData> sellOrderList = new ArrayList<orderData>();
		double lowestBuyPrice=9999999;
		double highestSellPrice=0;
		int check=0;

       // File file = new File("smallSample.txt");
        
        try {

            File file = new File("smallSample");
            
            Scanner scanner = new Scanner(file);
            
            //**********************
            //**Loop To Read input**
            //**********************
            while (scanner.hasNext()) {
            	//check++;						//Keeps track of iterations
            	//System.out.println(check);
            	
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
                	
                	//Set the current order
                	currentOrder.price=Double.parseDouble(price);
                	currentOrder.size=Integer.parseInt(size);
                	
                	//BID ORDER
                	if(side.equals("B")){
                		mapOrders.put(order_ID, currentOrder);
                		buyOrderList.add(currentOrder);
                		
                		//Sort the list from most expensive to cheapest
                		Collections.sort(buyOrderList, new reverseOrderDataComparator());
                		if(highestSellPrice<checkPrices(buyOrderList) && checkPrices(buyOrderList)!=-1){
                			highestSellPrice=checkPrices(buyOrderList);
                			System.out.println(timeStamp+" "+ "S" + " "+ highestSellPrice);
                		}
                		else if(checkPrices(buyOrderList)==-1 && highestSellPrice!=0){
                			System.out.println("NA");
                			highestSellPrice=0;
                		}
                	}
                	
                	//ASK ORDER
                	else{
                		mapOrders.put(order_ID, currentOrder);
                		sellOrderList.add(currentOrder);
                		
                		//Sort the list from cheapest to most expensive
                		Collections.sort(sellOrderList, new orderDataComparator());
                		
                		//Check to see if Buy price changed
                		if(lowestBuyPrice>checkPrices(sellOrderList) && checkPrices(sellOrderList)!=-1){
                			lowestBuyPrice=checkPrices(sellOrderList);
                			System.out.println(timeStamp+" "+ "B" + " "+ lowestBuyPrice);
                		}
                		else if(checkPrices(sellOrderList)==-1 && lowestBuyPrice!=9999999){
                			System.out.println("NA");
                			lowestBuyPrice=9999999;
                		}
                	}
                }
                
                //Reducing a Limit order
                else if(add_Red.equals("R")){
                	String order_ID = scanner.next();
                	order_ID.replace(" ", "");		// Delete Leading and trailing zeroes
                	String size = scanner.next();	// Size of order in shares
                
                	//Executes reduce order for buy orders
                	if(mapOrders.containsKey(order_ID)){
                		
                		//Reduces part of an order
                		if(Integer.parseInt(size)<mapOrders.get(order_ID).size){
                			mapOrders.get(order_ID).size=mapOrders.get(order_ID).size-Integer.parseInt(size);
                		}
                		else if(Integer.parseInt(size)==mapOrders.get(order_ID).size){			//gets rid of full order
                			if(sellOrderList.contains(mapOrders.get(order_ID))) sellOrderList.remove(mapOrders.get(order_ID));
                			else if(buyOrderList.contains(mapOrders.get(order_ID))) buyOrderList.remove(mapOrders.get(order_ID));
                			mapOrders.remove(order_ID);
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
                	
                	
            		//Sort the list from cheapest to most expensive
            		Collections.sort(sellOrderList, new orderDataComparator());
            		Collections.sort(buyOrderList, new reverseOrderDataComparator());
            		
            		//Check to see if Buy price changed
            		if(lowestBuyPrice>checkPrices(sellOrderList) && checkPrices(sellOrderList)!=-1){
            			lowestBuyPrice=checkPrices(sellOrderList);
            			System.out.println(timeStamp+" "+ "B" + " "+ lowestBuyPrice);
            		}
            		else if(checkPrices(sellOrderList)==-1 && lowestBuyPrice!=9999999){
            			System.out.println("NA");
            			lowestBuyPrice=9999999;
            		}
            		
            		//Check to see if sell price changed
            		Collections.sort(buyOrderList, new reverseOrderDataComparator());
            		if(highestSellPrice<checkPrices(buyOrderList) && checkPrices(buyOrderList)!=-1){
            			highestSellPrice=checkPrices(buyOrderList);
            			System.out.println(timeStamp+" "+ "S" + " "+ highestSellPrice);
            		}
            		else if(checkPrices(buyOrderList)==-1 && highestSellPrice!=0){
            			System.out.println("NA");
            			highestSellPrice=0;
            		}
                }
                
                
                //TEST PRINTS
               /* for(int i=0; i<buyOrderList.size(); i++){
                	System.out.println("*"+buyOrderList.get(i).size+" "+ buyOrderList.get(i).price);
                }
                for(int i=0; i<sellOrderList.size(); i++){
                	System.out.println("!"+sellOrderList.get(i).size+" "+ sellOrderList.get(i).price);
                }*/
            }
            
            //System.out.println(buyOrders.get("c").price);
           // buyOrderList.remove(mapOrders.get("f"));
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	
	
	
	//Checks for the best available price to buy or sell and returns total price
	//List parameter must be sorted
	public static double checkPrices(List<orderData> orders){
		int remainingTarget=Target_Size;
		double totalCost=0.0;
		
		
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
