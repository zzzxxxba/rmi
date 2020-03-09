package example.hello;
import java.io.Serializable;

public class Order implements Serializable{
	private static final long serialVersionUID = 1190476516911661470L;
	private String title;
	//private String isbn;
	private double cost;
	/*
	public Book(String isbn){
		this.isbn = isbn;
	}
	*/
	public Order(String title, double cost){
		this.title = title;
		//this.isbn = isbn;
		this.cost = cost;
	}

	public String getTitle() {
		return title;
	}

	public double getCost() {
		return cost;
	}
	
	public String toString(){
		return "> " + this.title + " ($" + this.cost + ")";
	}
}