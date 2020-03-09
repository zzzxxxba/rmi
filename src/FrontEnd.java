package example.hello;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;

import javax.swing.JOptionPane;
import javax.swing.JList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import example.hello.FrontServerInterface;
import example.hello.Order;
import example.hello.CustomerOrder;
import java.io.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FrontEnd extends UnicastRemoteObject implements ClientFrontInterface{

	protected FrontEnd() throws RemoteException{
		super();
	}
    public static void main(String args[]) {
	
	try {
		FrontEnd obj = new FrontEnd();

	    // Create remote object stub from server object
		LocateRegistry.createRegistry(1888);
  		Registry registry = LocateRegistry.getRegistry("localhost", 1888);

	    // Bind the remote object's stub in the registry
	    registry.bind("hello", obj);
		System.err.println("Front End Server ready");
	} catch (Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
	}
    }

	public Object[] invokePostCodeValidation(String postCode) throws RemoteException{
		Boolean failed = false;
		Boolean validated = false;
		
		//Checking which servers are alive
		try{
			Registry registry = LocateRegistry.getRegistry("127.0.0.10",1000);
			FrontServerInterface stub = (FrontServerInterface) registry.lookup("justHungry");
			validated = stub.verifyPostcode(postCode);
		} catch(Exception e){
			try{
				Registry registry = LocateRegistry.getRegistry("localhost",1098);
				FrontServerInterface stub = (FrontServerInterface) registry.lookup("justHungry1");
				validated = stub.verifyPostcode(postCode);
			} catch(Exception f){
				try{
					Registry registry = LocateRegistry.getRegistry("localhost",1097);
					FrontServerInterface stub = (FrontServerInterface) registry.lookup("justHungry2");
					validated = stub.verifyPostcode(postCode);
				} catch(Exception g){
					failed = true;
				} 
			}
		}

		return new Object[] {failed, validated};
			
    }

	public Object[] invokeNewCustomerOrder(String postCode, String order) throws RemoteException{
		Boolean failed = false;
		int id = -1;

		try{
			Registry registry = LocateRegistry.getRegistry("127.0.0.10",1000);
			FrontServerInterface stub = (FrontServerInterface) registry.lookup("justHungry");
			id = stub.addCustomerOrder(postCode, order);
		} catch(Exception e){
			try{
				Registry registry = LocateRegistry.getRegistry("localhost",1098);
				FrontServerInterface stub = (FrontServerInterface) registry.lookup("justHungry1");
				id = stub.addCustomerOrder(postCode, order);
			} catch(Exception f){
				try{
					Registry registry = LocateRegistry.getRegistry("localhost",1097);
					FrontServerInterface stub = (FrontServerInterface) registry.lookup("justHungry2");
					id = stub.addCustomerOrder(postCode, order);
				} catch(Exception g){
					failed = true;
				} 
			}
		}
		
		return new Object[] {failed, id};
		
    }
	
	public Object[] invokeCustomerOrders(int id) throws RemoteException{
		Boolean failed = false;
		StringBuilder message = new StringBuilder();

		try{
			Registry registry = LocateRegistry.getRegistry("127.0.0.10",1000);
			FrontServerInterface stub = (FrontServerInterface) registry.lookup("justHungry");
			List<CustomerOrder> list = stub.listCustomerOrders();
			list.forEach(x -> {
				if(x.getId() == id){
					message.append(x.toString() + "\n");
				}
			});
		} catch(Exception e){
			try{
				Registry registry = LocateRegistry.getRegistry("localhost",1098);
				FrontServerInterface stub = (FrontServerInterface) registry.lookup("justHungry1");
				List<CustomerOrder> list = stub.listCustomerOrders();
				list.forEach(x -> {
					if(x.getId() == id){
						message.append(x.toString() + "\n");
					}
				});
			} catch(Exception f){
				try{
					Registry registry = LocateRegistry.getRegistry("localhost",1097);
					FrontServerInterface stub = (FrontServerInterface) registry.lookup("justHungry2");
					List<CustomerOrder> list = stub.listCustomerOrders();
					list.forEach(x -> {
						if(x.getId() == id){
							message.append(x.toString() + "\n");
						}
					});
				} catch(Exception g){
					failed = true;
				} 
			}
		}
		
		return new Object[] {failed, message};

    } 


}
