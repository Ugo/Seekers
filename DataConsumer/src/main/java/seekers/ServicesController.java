package seekers;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import seekers.mongo.MongoActions;

@RestController
public class ServicesController {

	private final AtomicLong counter = new AtomicLong();

	/**
	 * Webservice to compute the average of the last N prices
	 * @param content
	 * @return
	 */
	@CrossOrigin(origins = "http://localhost:9090")
	@RequestMapping("/average")
	public AverageResponse computeAverage(@RequestParam(value = "content", defaultValue = "1") String content) {

		int numberLastPrices = 0;
		try {
			// simple check on the content sent by the form
			numberLastPrices = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			return new AverageResponse(counter.incrementAndGet(),
					"Please enter an integer");
		}
		DecimalFormat df = new DecimalFormat("###.##");
		return new AverageResponse(counter.incrementAndGet(),
				df.format(MongoActions.getAverageLastPrices(numberLastPrices)));
	}
	
	/**
	 * Webservice to insert a new price in the backend
	 * @param price
	 * @param date
	 */
	@RequestMapping("/addprice")
	public void addPrice(@RequestParam(value = "price") double price, @RequestParam(value = "date") long date) {

		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
		System.out.println("Price: " + price + " received at " + time_formatter.format(date));
		MongoActions.insertNewPrice(price, date);
	}
}