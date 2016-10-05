package seekers.mongo;

public class MongoConnectionProps {

	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 27017;
	public static final String DEFAULT_DB_NAME = "pricesDB";
	public static final String DEFAULT_COLLECTION_NAME = "prices";
	public static final String DEFAULT_PRICETIME_FIELD = "priceTime";
	public static final String DEFAULT_PRICE_FIELD = "price";
	
	private String host;
	private int port;
	private String dbName;
	private String collectionName;
	private String pricetimeField;
	private String priceField;
	
	public MongoConnectionProps() {
		this.host = DEFAULT_HOST;
		this.port = DEFAULT_PORT;
		this.dbName= DEFAULT_DB_NAME;
		this.collectionName = DEFAULT_COLLECTION_NAME;
		this.pricetimeField = DEFAULT_PRICETIME_FIELD;
		this.priceField = DEFAULT_PRICE_FIELD;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String mongoHost) {
		this.host = mongoHost;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int mongoPort) {
		this.port = mongoPort;
	}

	public String getDBName() {
		return dbName;
	}

	public void setDBName(String mongoDBName) {
		this.dbName = mongoDBName;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String mongoCollectionName) {
		this.collectionName = mongoCollectionName;
	}

	public String getPricetimeField() {
		return pricetimeField;
	}

	public void setPricetimeField(String mongoPricetimeField) {
		this.pricetimeField = mongoPricetimeField;
	}

	public String getPriceField() {
		return priceField;
	}

	public void setPriceField(String mongoPriceField) {
		this.priceField = mongoPriceField;
	}
	
}


