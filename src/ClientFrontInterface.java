package example.hello;

import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;

import java.io.*;

public interface ClientFrontInterface extends Remote {
    public Object[] invokePostCodeValidation(String postCode) throws RemoteException;
    public Object[] invokeNewCustomerOrder(String postCode, String order) throws RemoteException;
    public Object[] invokeCustomerOrders(int id) throws RemoteException;
}
