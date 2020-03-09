package example.hello;
import java.io.Serializable;


public class CustomerOrder implements Serializable{
	private static final long serialVersionUID = 1190476516911661470L;
	private String title;
	private String postcode;
	private int id;

	public CustomerOrder(String title, String postcode, int id){
		this.title = title;
		this.id = id;
		this.postcode = postcode;
	}

	public String getTitle() {
		return title;
	}


	public String getPostcode(){
		return postcode;
	}

	public int getId(){
		return id;
	}
	
	public String toString(){
		return this.postcode+" -> " + this.title;
	}
}