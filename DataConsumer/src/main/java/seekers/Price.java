package seekers;

import java.io.Serializable;

public class Price implements Serializable{
	private static final long serialVersionUID = -4800552895886192433L;

	double price;
	long date;
	
	public Price() {
	}
	
	public Price(double price, long date) {
		this.price = price;
		this.date = date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double Price) {
		this.price = Price;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
}
