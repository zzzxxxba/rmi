package example.hello;

import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.*;

public interface ServersInterface extends Remote {
    public int addCustomerOrder(String postCode, String Order) throws RemoteException;
    public List<CustomerOrder> updateCustomerOrders(List<CustomerOrder> list) throws RemoteException;
}
