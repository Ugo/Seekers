package com.rest.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * The purpose of this class is to call a webservice at regular intervals with a
 * random price and the time of creation of the price.
 */
public class dataProducerClient {

	private static int INTERVAL_IN_MS = 1000;
	private static String SERVICE_ADDRESS = "http://localhost:8080";
	private static String SERVICE_NAME = "addprice";
	private static String PRICE_ARGUMENT_NAME = "price";
	private static String DATE_ARGUMENT_NAME = "date";

	private static DecimalFormat df = new DecimalFormat("###.##");

	public static void main(String[] args) throws InterruptedException {
		// only for logging purposes
		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");

		while (true) {
			long date = System.currentTimeMillis();
			double price = getRandomPrice();
			try {
				sendPrice(price, date);
				System.out.println("Price sent: " + price + " created at " + time_formatter.format(date));
			} catch (IOException e) {
				e.printStackTrace();
			}

			// sleep to have intervals between two prices
			Thread.sleep(INTERVAL_IN_MS);
		}

	}

	/**
	 * @return a random price between 100 and 200
	 */
	public static double getRandomPrice() {
		Double initialPrice = 100d;
		// a decimal format is used here to have only 2 decimals in the price
		return Double.parseDouble(df.format(initialPrice * (1 + Math.random())));
	}

	/**
	 * Method that will call the webservice of the server with the price and
	 * date
	 * 
	 * @param price
	 * @param date
	 * @throws IOException
	 */
	public static void sendPrice(double price, long date) throws IOException {
		// Define the url where the query will be performed
		URL url = new URL(SERVICE_ADDRESS + "/" + SERVICE_NAME);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");

		// build the json that will be sent
		// note: there is probably a better way to this
		String jsonInput = "{\"" + PRICE_ARGUMENT_NAME + "\":" + price + ",\"" + DATE_ARGUMENT_NAME + "\":" + date + "}";
		OutputStream os = conn.getOutputStream();
		os.write(jsonInput.getBytes());
		os.flush();

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		conn.disconnect();
	}

}