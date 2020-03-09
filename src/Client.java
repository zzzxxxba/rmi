package example.hello;

import java.rmi.registry.LocateRegistry;

import javax.swing.JOptionPane;
import javax.swing.JList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.rmi.registry.Registry;
import java.rmi.Naming;
public class Client {

    private Client() {}

    public static void main(String[] args) {

	String host = (args.length < 1) ? null : args[0];
	try {

	    // Get registry
	    Registry registry = LocateRegistry.getRegistry("localhost",1888);

	    // Lookup the remote object "Hello" from registry
	    // and create a stub for it
	    ClientFrontInterface stub = (ClientFrontInterface) registry.lookup("hello");

        boolean findmore = true;
			do{
				String[] options = {"Make an order", "Track Existing Order", "Exit"};
				int choice = JOptionPane.showOptionDialog(null, "Choose an action", "Option dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				switch(choice){
					case 0: // Make an order

						JList<String> menu = new JList<>(new String[] {
							"Sunset Burger -> $8.25",
							"Grilled Chicken Burger -> $6.75", 
							"Grilled Chicken Wrap -> $6.75",
							"3x Chicken Wings -> $5.95",
							"Caesar Salad -> $7.00"});

						JList<String> menuNames = new JList<>(new String[] {
							"Sunset Burger",
							"Grilled Chicken Burger", 
							"Grilled Chicken Wrap",
							"3x Chicken Wings",
							"Caesar Salad"});
						JOptionPane.showMessageDialog(
						null, menu, "Multi-Select Example", JOptionPane.PLAIN_MESSAGE);
						if(Arrays.toString(menu.getSelectedIndices()).length() != 3){
							JOptionPane.showMessageDialog(null, "Please select a single order.");
							continue;
						} else {
							String postCode = JOptionPane.showInputDialog("Enter your postcode");
							// check postcode
							Object[] response = stub.invokePostCodeValidation(postCode);
							Object offline = response[0];
							Boolean isOffline = (Boolean) offline;
							if(isOffline == true){
								System.err.println("Servers are offline, please try again later.");
								System.exit(0);
							} else {
								Object valid = response[1];
								Boolean isValid = (Boolean) valid;
								if(isValid == true){
									Object[] addedResponse = stub.invokeNewCustomerOrder(postCode, 
									menuNames.getModel().getElementAt(menu.getSelectedIndices()[0]));
									Object id = addedResponse[1];
									JOptionPane.showMessageDialog(null, "Your order id is "+id);
								} else {
									JOptionPane.showMessageDialog(null, "Incorrect postcode.");
								}
								
							}

						}

						break;
					case 1: // View order
						String OrderID = JOptionPane.showInputDialog("Enter your orderID");
						int id = Integer.parseInt(OrderID);
						Object[] customerOrders = stub.invokeCustomerOrders(id);
						Object offline = customerOrders[0];
						Boolean isOffline = (Boolean) offline;
						if(isOffline == true){
							System.err.println("Servers are offline, please try again later.");
							System.exit(0);
						} else {
							Object order = customerOrders[1];
							String orderToString = String.valueOf(order);
							System.err.println(orderToString);
							if(orderToString != null && !orderToString.isEmpty()){
								JOptionPane.showMessageDialog(null, orderToString);
							} else {
								JOptionPane.showMessageDialog(null, "Your order id is invalid.");
							}
							
						}
		
						break;
					default:
						System.out.println("Exiting");
						System.exit(0);
						break;
				}
				findmore = (JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION);
			}while(findmore);

	} catch (Exception e) {
		System.err.println("Front-End Exception: " + e.toString());
		e.printStackTrace();
	}
    }
}
