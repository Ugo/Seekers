/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package seekers;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.mongodb.client.MongoCursor;

import seekers.mongo.MongoActions;
import seekers.mongo.MongoUtils;

/**
 * Class to test the calls on the different services of the backend. The class
 * extends MongoUtils just to be able to perform basic check in the mongo db
 * after the calls.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ServiceControllerTests extends MongoUtils {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Simple test on the average service
	 */
	@Test
	public void callOnAverage() throws Exception {

		double priceTest = 10;
		long dateTest = System.currentTimeMillis();
		MongoActions.insertNewPrice(priceTest, dateTest);

		this.mockMvc.perform(get("/average")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value(priceTest));
	}

	/**
	 * Simple test on the insertPrice service
	 */
	@Test
	public void callOnInsertPrice() throws Exception {

		double price = 10;
		long date = System.currentTimeMillis();
		// call on the webservice
		this.mockMvc.perform(get("/addprice").param("price", "" + price).param("date", "" + date)).andDo(print())
				.andExpect(status().isOk());

		// then check that the db is not empty
		MongoCursor<Document> cursor = getAllDocuments().iterator();
		int count = 0;
		double priceTemp = 0;
		long dateTemp = 0;
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			priceTemp = (Double) doc.get(MongoActions.connection.getPriceField());
			dateTemp = (Long) doc.get(MongoActions.connection.getPricetimeField());
			count++;
		}
		assertEquals(count, 1);
		assertEquals(price, priceTemp, Double.MIN_VALUE);
		assertEquals(date, dateTemp);
	}
}
