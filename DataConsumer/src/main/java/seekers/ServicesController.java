package seekers;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import seekers.mongo.MongoActions;

/**
 * This class defines the different services that are available
 */
@RestController
public class ServicesController {

	private final AtomicLong counter = new AtomicLong();

	/**
	 * Webservice to compute the average of the last N prices
	 * 
	 * NOTE: if CrossOrigin is not defined with the port used by the server (in
	 * case it is on localhost as well) from which request are coming, then the
	 * queries can be blocked so this should be defined.
	 * 
	 * @param content
	 * @return
	 */
	@CrossOrigin(origins = "http://localhost:9090")
	@RequestMapping("/average")
	public AverageResponse computeAverage(@RequestParam(value = "content", defaultValue = "1") String content) {
		DecimalFormat df = new DecimalFormat("###.##");
		int numberLastPrices = 0;
		try {
			// simple check on the content sent by the form
			numberLastPrices = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			return new AverageResponse(counter.incrementAndGet(), "Please enter an integer");
		}
		
		return new AverageResponse(counter.incrementAndGet(),
				df.format(MongoActions.getAverageLastPrices(numberLastPrices)));
	}

	/**
	 * Webservice to insert a new price in mongoDB
	 * 
	 * @param price
	 * @param date
	 */
	@RequestMapping(value = "/addprice", method = RequestMethod.POST, consumes = "application/json")
	public void addPrice(@RequestBody Price price) {

		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
		System.out.println(
				"Price received: " + price.getPrice() + " created at " + time_formatter.format(price.getDate()) + " has been inserted");
		MongoActions.insertNewPrice(price.getPrice(), price.getDate());
	}
}