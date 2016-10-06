package seekers.mongo;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * This class contains all the basic tools for the unit testing classes with
 * mongoDB. It will initiate and terminate the connection before and after each
 * test. It also provides some util methods that can be used in the tests.
 */
public abstract class MongoUtils {

	// change the dbName and collectionName in order to use separate
	// db/collection for the unit tests
	{
		MongoActions.props.setDBName("unitTestDB");
		MongoActions.props.setCollectionName("unitTestCollection");
	}

	MongoClient mongoClient;

	/**
	 * Before each test, the client is open and the whole collection is cleared.
	 */
	@Before
	public void init() {
		mongoClient = new MongoClient(MongoActions.props.getHost(), MongoActions.props.getPort());
		clearCollection();
	}

	/**
	 * After each test the client is closed
	 */
	@After
	public void closeMongoClient() {
		mongoClient.close();
	}

	/**
	 * Simple method to clear a collection in mongo
	 */
	public void clearCollection() {
		MongoDatabase db = mongoClient.getDatabase(MongoActions.props.getDBName());
		MongoCollection<Document> table = db.getCollection(MongoActions.props.getCollectionName());
		table.drop();
	}

	/**
	 * Simple method to return all the documents of a collection in mongo
	 */
	public FindIterable<Document> getAllDocuments() {
		MongoDatabase db = mongoClient.getDatabase(MongoActions.props.getDBName());
		MongoCollection<Document> table = db.getCollection(MongoActions.props.getCollectionName());
		FindIterable<Document> iterable = table.find();

		return iterable;
	}
}
