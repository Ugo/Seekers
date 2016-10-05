package seekers.mongo;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoActions {

	public static MongoConnectionProps connection = new MongoConnectionProps();

	public static void insertNewPrice(double price, long date) {
		// connect to mongoDb using MongoClient
		MongoClient mongo = new MongoClient(connection.getHost(), connection.getPort());

		try {
			// Get database, if database doesn't exists, MongoDB will create it
			MongoDatabase db = mongo.getDatabase(connection.getDBName());

			// Get collection / table from db', if collection doesn't exists,
			// MongoDB will create it for you
			MongoCollection<Document> table = db.getCollection(connection.getCollectionName());

			// insert an object in the table
			Document document = new Document();
			document.put(connection.getPriceField(), price);
			document.put(connection.getPricetimeField(), date);
			table.insertOne(document);
		} catch (MongoException e) {
			e.printStackTrace();
		} finally {
			// then close the mongo connection whatever happens
			mongo.close();
		}
	}

	public static Double getAverageLastPrices(int numLastPrices) {
		MongoClient mongo = new MongoClient(connection.getHost(), connection.getPort());
		Double average = 0.0;
		try {
			// Get db, if it doesn't exists, MongoDB will create it
			MongoDatabase db = mongo.getDatabase(connection.getDBName());

			// Get collection / table from db, if collection doesn't exists,
			// MongoDB will create it
			MongoCollection<Document> table = db.getCollection(connection.getCollectionName());

			// Here the query to the mongo DB will take place, results are
			// sorted from newest to oldest with a limit on the number of
			// results
			MongoCursor<Document> cursor = table.find().sort(new BasicDBObject(connection.getPricetimeField(), -1))
					.limit(numLastPrices).iterator();

			int count = 0;
			double sum = .0;
			while (cursor.hasNext()) {
				double val = (Double) cursor.next().get(connection.getPriceField());
				sum = sum + val;
				System.out.println("val:" + val);
				count++;
			}
			average = sum / count;
			System.out.println("average is " + sum / count);
			System.out.println("Done");

		} catch (MongoException e) {
			e.printStackTrace();
		} finally {
			// then close the connection whatever happens
			mongo.close();
		}

		return average;
	}
}
