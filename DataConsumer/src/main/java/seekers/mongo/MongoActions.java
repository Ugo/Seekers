package seekers.mongo;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

/**
 * Class defining all the methods that will allow to get / insert data from / in
 * mongoDB
 */
public class MongoActions {

	public static MongoConnectionProps props = new MongoConnectionProps();
	public static MongoClient mongo = new MongoClient(props.getHost(), props.getPort());

	/**
	 * Method to insert a new price in the mongoDB collection
	 * 
	 * @param price
	 *            price to insert
	 * @param date
	 *            date of the price, it is stored as a long
	 */
	public static void insertNewPrice(double price, long date) {
		if (mongo == null) {
			mongo = new MongoClient(props.getHost(), props.getPort());
		}

		try {
			// Get database, if database doesn't exists, MongoDB will create it
			// then get collection in this db
			MongoCollection<Document> table = mongo.getDatabase(props.getDBName())
					.getCollection(props.getCollectionName());

			// insert an object in the table
			Document document = new Document();
			document.put(props.getPriceField(), price);
			document.put(props.getPricetimeField(), date);
			table.insertOne(document);
		} catch (MongoException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Simple method computing the average of the last N prices stored in the
	 * mongoDB collection.
	 * 
	 * @param numLastPrices
	 *            number of last prices to compute the average
	 * @return the average of the last prices
	 */
	public static Double getAverageLastPrices(int numLastPrices) {
		if (mongo == null) {
			mongo = new MongoClient(props.getHost(), props.getPort());
		}
		
		Double average = 0.0;
		try {
			// get collection
			MongoCollection<Document> collection = mongo.getDatabase(props.getDBName())
					.getCollection(props.getCollectionName());

			// Here the query to the mongo DB will take place, results are
			// sorted from newest to oldest with a limit on the number of
			// results
			MongoCursor<Document> cursor = collection.find().sort(new BasicDBObject(props.getPricetimeField(), -1))
					.limit(numLastPrices).iterator();

			// simple computation of the average
			int count = 0;
			double sum = .0;
			while (cursor.hasNext()) {
				double val = (Double) cursor.next().get(props.getPriceField());
				sum = sum + val;
				count++;
			}
			average = sum / count;
			System.out.println("The average is " + sum / count + " from the last " + count + " prices.");

		} catch (MongoException e) {
			e.printStackTrace();
		} 

		return average;
	}
}
