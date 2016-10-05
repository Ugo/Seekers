package seekers.mongo;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;

/**
 * Class to test the different methods interacting with mongoDB
 */
public class MongoTests extends MongoUtils {

	/**
	 * Simple test for one insertion in the collection to test MongoActions.insertNewPrice() method.
	 * @throws Exception
	 */
	@Test
	public void insertNewPriceOneInsertion() throws Exception {
		// insert one element
		double priceTest = 10;
		long dateTest = System.currentTimeMillis();
		MongoActions.insertNewPrice(priceTest, dateTest);

		MongoCursor<Document> cursor = getAllDocuments().iterator();
		int count = 0;
		double priceQueried = 0;
		long dateQueried = 0;
		if (cursor.hasNext()) {
			Document doc = cursor.next();
			priceQueried = (Double) doc.get(MongoActions.connection.getPriceField());
			dateQueried = (Long) doc.get(MongoActions.connection.getPricetimeField());
			count++;
		}

		assertEquals(count, 1);
		assertEquals(priceQueried, priceTest, Double.MIN_VALUE);
		assertEquals(dateQueried, dateTest);
	}
	/**
	 * Simple test for several insertions in the collection to test MongoActions.insertNewPrice() method.
	 * @throws Exception
	 */
	@Test
	public void insertNewPriceSeveralInsertion() throws Exception {
		// insert one element in the db
		List<Double> listPrices = new ArrayList<>();
		List<Long> listDates = new ArrayList<>();
		for (int iter = 0; iter < 10; iter++) {
			listPrices.add(10d * iter);
			listDates.add(System.currentTimeMillis());
			Thread.sleep(100);
		}

		// insert data
		for (int iter = 0; iter < listPrices.size(); iter++) {
			MongoActions.insertNewPrice(listPrices.get(iter), listDates.get(iter));
		}

		MongoCursor<Document> cursor = getAllDocuments()
				.sort(new BasicDBObject(MongoActions.connection.getPricetimeField(), 1)).iterator();
		List<Double> listRetrievedPrices = new ArrayList<>();
		List<Long> listRetrievedDates = new ArrayList<>();
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			listRetrievedPrices.add((Double) doc.get(MongoActions.connection.getPriceField()));
			listRetrievedDates.add((Long) doc.get(MongoActions.connection.getPricetimeField()));
		}

		assertEquals(listDates.size(), listRetrievedDates.size());
		assertEquals(listPrices.size(), listRetrievedPrices.size());
		for (int iter = 0; iter < listRetrievedPrices.size(); iter++) {
			assertEquals(listPrices.get(iter), listRetrievedPrices.get(iter));
			assertEquals(listDates.get(iter), listRetrievedDates.get(iter));
		}
	}

	/**
	 * Simple test for computing the average in the collection to test MongoActions.getAverageLastPrices() method.
	 * @throws Exception
	 */
	@Test
	public void getAverageLastPricesTest() throws Exception {
		List<Double> listPrices = new ArrayList<>();
		List<Long> listDates = new ArrayList<>();
		for (int iter = 0; iter < 5; iter++) {
			listPrices.add(10d * iter);
			listDates.add(System.currentTimeMillis());
			Thread.sleep(100);
		}
		// insert data
		for (int iter = 0; iter < listPrices.size(); iter++) {
			MongoActions.insertNewPrice(listPrices.get(iter), listDates.get(iter));
		}

		// compare average
		assertEquals(MongoActions.getAverageLastPrices(1), listPrices.get(4));
		assertEquals(MongoActions.getAverageLastPrices(2), new Double((listPrices.get(4) + listPrices.get(3)) / 2));
		assertEquals(MongoActions.getAverageLastPrices(3),
				new Double((listPrices.get(4) + listPrices.get(3) + listPrices.get(2)) / 3));
		assertEquals(MongoActions.getAverageLastPrices(4),
				new Double((listPrices.get(4) + listPrices.get(3) + listPrices.get(2) + listPrices.get(1)) / 4));
		assertEquals(MongoActions.getAverageLastPrices(5), new Double(
				(listPrices.get(4) + listPrices.get(3) + listPrices.get(2) + listPrices.get(1) + listPrices.get(0))
						/ 5));
	}
}
