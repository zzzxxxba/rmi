package example.hello;

import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.*;

public interface FrontServerInterface extends Remote {
    public List<Order> listOrders() throws RemoteException;
    public Boolean verifyPostcode(String postcode) throws IOException;
    public List<CustomerOrder> listCustomerOrders() throws RemoteException;
    public int addCustomerOrder(String postCode, String Order) throws RemoteException;
    public List<CustomerOrder> updateCustomerOrders(List<CustomerOrder> list) throws RemoteException;
}
