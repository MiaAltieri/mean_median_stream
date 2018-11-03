
/*
Data Set
- JSON files for Users Records
- small-user-data.json for easily looking at structure.
- Each Element has information about User and Number of Unread Messages
- Each User has Number of Friends
- Each User has a favorite fruit
- Each User registered on a date

Task
- Print Summary of User Data including:
  * Users registered in each Year
  * Median for Number of Friends
  * Median age for Users
  * Mean Balance Amount
  * Mean for number of Unread messages for Active females

Technical Requirements
- Do not read entire data file in memory. Use Streaming.
- Print Summary after every 1000 records processed.
- Code should be organized modularly such that implementing new Queries 
	is easy.
- Use as much as available Java or 3rd Party Libraries as possible.
- You can use any Json Parsing Library (like GSON or Jackson)

Submission
- Upload the project on GitHub
- README on how to build and execute
- Do not upload the data files. README should mention how to make data 
	files available to the program.
- Screenshot/GIF video of Program working on Data and printing Summary.

*/


/*
	Mias personal scrum
		- print out needed JSON entries
*/

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
 
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.util.*;


import java.lang.Object; 

public class DataSummary
{
	private Hashtable years; 

	private double median_friends;
	private Hashtable friends; 

	private double median_age;
	private Hashtable ages; 

	private double mean_balance;
	private SummaryStatistics balance_stats;

	private double mean_unread;
	private SummaryStatistics unread_stats;

	private int count;

	
	DataSummary (){
		years = new Hashtable();

		median_friends = 0;
		friends = new Hashtable();

		median_age = 0;
		ages = new Hashtable();

		mean_balance = 0;
		balance_stats = new SummaryStatistics();

		mean_unread = 0;
		unread_stats = new SummaryStatistics();

		count = 0;
	}

	void printSummary() {
		System.out.println("Summary: ");
		System.out.println("------- Year Count: ");
		Set<int> keys = years.keySet();
        for(int key: keys){
            System.out.println("------- "+key+" "+years.get(key));
        }

		System.out.println("------- Medium Number of Friends: "+ median_friends);
		System.out.println("------- Medium Age for User: "+ median_age);
		System.out.println("------- Mean Balance Amount: "+ mean_balance);
		System.out.println("------- Mean Number of Unread Messages for Active Females: "+ mean_unread);
	}

	/* computeNeededValues computes the median given a hashtable and
	 * number of values
	 */	
	double computeMedian(Hashtable values, int size){
		double middle = size/2;
		double remaining = middle;
		double median = 0;

		Set<Integer> keySet = values.keySet();

		while (true) {
    		int min = Collections.min(keySet);
			keySet.remove(min);
			int number_of_values = (int)(values.get(min));

			// indicates that median is within this range
			if (number_of_values > remaining) {
				median = min;
				break;
			}
			// indicates that median is inbetween this value and the 
			// following value
			if (number_of_values == remaining) {
				int next_min = Collections.min(keySet);
				median = (min+next_min)/2;
				break;
			}

			remaining -= number_of_values;
		}

		return median; 
	}

		
	/* computeNeededValues computes means and medians for data processed
	 * thus far 
	 */	
	void computeNeededValues(){
		count+=1000;

		mean_balance = balance_stats.getMean();
		mean_unread = unread_stats.getMean();
		median_age = computeMedian(ages, count);
		median_friends = computeMedian(friends, count);
	}



	/* computeMedianFriends updates the hashtable friends, to understand
	 * the implementation of this hashtable and how it is used to 
	 * calculate the median please see the README
	 */
	void computeMedianFriends(String[] usr_friends){
		int number_of_friends = usr_friends.length;

		if (!friends.containsKey(number_of_friends)){
			friends.put(number_of_friends, 0);
		}
		
		int newFriendCount = friends.get(number_of_friends) + 1;
		years.put(number_of_friends, newFriendCount);
	}

	/* computeMedianAge updates the hashtable age, to understand
	 * the implementation of this hashtable and how it is used to 
	 * calculate the median please see the README
	 */
	void computeMedianAge(int age){
		if (!ages.containsKey(age)){
			ages.put(age, 0);
		}
		
		int newAgeCount = ages.get(age) + 1;
		ages.put(year, newAgeCount);
	}

	/* computeMeanUnread updates the field unread_stats based off a Usr
	 * this keeps track off number of Unread messages for Active females
	 * NOTICE: we do NOT calculate the mean here as a way to save time
	 */
	void computeMeanUnread(User Usr){
		// skip males and inactive users
		if (Usr.getGender().equals("male") || !Usr.getIsActive()){
			return;
		}

		String[] greeting = (Usr.getGreeting()).split(" ");
		message_index = greeting.length - 3;
		new_messages = Integer.parseInt(greeting[message_index]);
		unread_stats.addValue(new_messages);
	}

	/* computeMeanBalance updates the field balance_stats based off a new_balance
	 * NOTICE: we do NOT calculate the mean here as a way to save time
	 */
	void computeMeanBalance(int new_balance) {
		balance_stats.addValue(new_balance);
	}

	/* computeYear grabs a year from a string containing the registration
	 * string and increases its corresponding value in the hash table
	 * dates
	 */
	void computeYear (String unproccessedYear){
		String[] registration = unproccessedYear.split(":");  
		int year = registration[0];
		if (!years.containsKey(year)){
			years.put(year, 0);
		}
		
		int newYearCount = int(years.get(year))+ 1;
		years.put(year, newYearCount);
	}


	/* processData processes data in increments of k records at a time
	 * while reading it computes various aspects 
	 */
	void processData(int k, InputStream stream){
		try {
	        JsonReader reader = new JsonReader(new InputStreamReader(
	        	stream, "UTF-8"));
	        Gson gson = new GsonBuilder().create();
	        System.out.print("Progress: ");
	        int count = 0;

	        // Read file in stream mode
	        reader.beginArray();
	        while (reader.hasNext()) {
	            // Read data into object model
	            User Usr = gson.fromJson(reader, User.class);
				count++; 

				computeYear(Usr.getRegistered());
				computeMeanBalance(Usr.getBalance());
				computeMeanUnread(Usr);
				computeMedianFriends(Usr.getFriends());
				computeMedianAge(Usr.getAge());
				

	            // prints out one period per 100 records processed
	            if (count%100 == 0) {
	            	System.out.print(".");
	            }

	            // when we have processed k records we print out a Summary
	            if (count%k == 0) {
	            	System.out.println();
	            	printSummary();
	            	count = 0;
	            	System.out.print("Progress: ");
	            }
	        }
	        reader.close();

	    } catch (UnsupportedEncodingException ex) {
	    	System.out.println("UnsupportedOperationException");
	    } catch (IOException ex) {
	        System.out.println("IOException");
	    }
	}

	public static void main (String [] args) throws IOException {
		InputStream stream = new FileInputStream(new File("~/user-1.json"));
	    DataSummary d = new DataSummary();
	    d.processData(1000, stream);
	}
}