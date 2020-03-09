package example.hello;
	
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.net.*;
import java.io.*;

import example.hello.Order;
import example.hello.CustomerOrder;

//justHungry main server
public class justHungry extends UnicastRemoteObject implements FrontServerInterface {
	private List<Order> orderList;
	private List<CustomerOrder> customerOrderList;

	protected justHungry(List<Order> list, List<CustomerOrder> list2) throws RemoteException {
		super();
		this.orderList = list;
		this.customerOrderList = list2;
	}
	
	public List<Order> listOrders() throws RemoteException {
		return orderList;
	}

	public List<CustomerOrder> listCustomerOrders() throws RemoteException {
		return customerOrderList;
	}

	public List<CustomerOrder> updateCustomerOrders(List<CustomerOrder> list) throws RemoteException {
		this.customerOrderList = list;
		return customerOrderList;
	}

	public int addCustomerOrder(String postCode, String Order) throws RemoteException{

		int id = this.customerOrderList.size() + 1;
		
		try{
			this.customerOrderList.add(new CustomerOrder(Order, postCode, id));
			//Success, it is added to this servers list. but who are the other two?
			//check them, and update them.
			try{
				Registry registry = LocateRegistry.getRegistry("localhost",1098);
				FrontServerInterface stub = (FrontServerInterface) registry.lookup("justHungry1");
				List<CustomerOrder> list = stub.updateCustomerOrders(this.customerOrderList);
			} catch(Exception f) {
				System.out.println("JS");
			}
			try{
				Registry registry = LocateRegistry.getRegistry("localhost",1097);
				FrontServerInterface stub = (FrontServerInterface) registry.lookup("justHungry2");
				List<CustomerOrder> list = stub.updateCustomerOrders(this.customerOrderList);
			} catch(Exception g) {
				System.out.println("JS");
			}

			return id;
		} catch(Exception e){
			return id;
		}
		
    }

	public Boolean verifyPostcode(String postcode) throws IOException{
		try{

			URL urlForGetRequest = new URL("https://api.postcodes.io/postcodes/"+postcode+"/validate");
			String readLine = null;
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			conection.setRequestMethod("GET");
			conection.setRequestProperty("userId", "a1bcdef"); // set userId its a sample here
			int responseCode = conection.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(
				new InputStreamReader(conection.getInputStream()));
				StringBuffer response = new StringBuffer();
				while ((readLine = in .readLine()) != null) {
					response.append(readLine);
					System.out.println(readLine);
				} in .close();
				// print result
				System.out.println("JSON String Result " + response.toString());
				String acceptedResult = "\"result\":true";
				if(response.toString().toLowerCase().contains(acceptedResult.toLowerCase())){
					return true;
				} else {
					return false; //is result:"true" isn't in the response code 200 string then it's not valid.
				}
				
			} else {
				return false; //If the response code is not 200
			}
		}catch(MalformedURLException ex){
			return false; //If URL is incorrect
		}
	}
	
	private static List<Order> initializeList(){
		List<Order> listOfOrders = new ArrayList<>();
		listOfOrders.add(new Order("Sunset Burger", 8.25));
		listOfOrders.add(new Order("Grilled Chicken Burger", 6.75));
		listOfOrders.add(new Order("Grilled Chicken Wrap", 6.75));
		listOfOrders.add(new Order("3x Chicken Wings", 5.95));
		listOfOrders.add(new Order("Caesar Salad", 7.00));
		return listOfOrders;
	}

	private static List<CustomerOrder> initializeCustomerList(){
		List<CustomerOrder> listOfCustomerOrders = new ArrayList<>();
		return listOfCustomerOrders;
	}
	
    public static void main(String args[]) {
		try {

			java.rmi.registry.LocateRegistry.createRegistry(1000);
			Registry registry = LocateRegistry.getRegistry("127.0.0.10", 1000);

			registry.rebind("justHungry", new justHungry(initializeList(),initializeCustomerList()));
			// Bind the remote object's stub in the registry
			

			// Write ready message to console
			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
    }
}
