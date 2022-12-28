package edu.upenn.cit594.ui;


import java.util.Scanner;
import java.util.regex.Pattern;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;

public class UserInterface {
	
	protected Processor processor;
	protected Scanner in;
	protected Logger logger = Logger.getInstance();
    protected boolean running = true;
	
	public UserInterface(Processor processor){
		this.processor = processor;
		this.in = new Scanner(System.in);
	}

	public void start() throws Exception {
		while(this.running) {
			System.out.println("0. Exit the program. \n " +
					"1. Show the available data sets. \n " +
					"2. Show the total population for all ZIP Codes \n " +
					"3. Show the total vaccinations per capita for each ZIP Code for the specified date \n " +
					"4. Show the average market value for properties in a specified ZIP Code \n" +
					"5. Show the average total livable area for properties in a specified ZIP Code \n" +
					"6. Show the total market value of properties, per capita, for a specified ZIP Code. \n" +
					"7. Show the Max Death Rate and Max Market value over livable area on 2021-03-25 ");
			System.out.println("Please Enter a number for 0-7 to make the desired action");
			System.out.print("> ");
			System.out.flush();
			
			String choice = in.nextLine();
			
			String time = Long.toString(System.currentTimeMillis());
			time += " " + choice; //String.valueOf(choice);
			logger.logString(time);

			int input;
			try {
				input = Integer.parseInt(choice);
				// exit program if enter 0;
				if (input == 0) {
					System.out.println("Exit the program");
					break;
				}
				//get the available data sets from processer
				else if (input== 1) {
					getDataSets();
				}
				//get the total population from processor by zip code
				else if (input == 2) {
					getTotalPopulation();
				}
				//getTotalVaccinationsPerCapita from processor
				else if (input == 3) {
					getVaccinationsPerCapita();
				}
				//get average market value from processor
				else if (input == 4) {
					getAvgMktValue();
				}
				//get average total liveable area for properties
				else if(input == 5) {
					getAvgLivArea();
				}
				//get total market value for properties
				else if(input == 6) {
					getSumMktValPerCapita();
				}
				//get max market value by zipcode
				else if(input == 7) {
					getDeathRateMktValue();
				}
				else {
					System.out.println("Error: Invalid input");
					System.out.println("Please type again");
				}
			}
			catch(NumberFormatException e) {
				System.out.println("Error: Invalid input");
				System.out.println("Please type again");
			}

		}
	}
	

	protected void getDataSets() {
		// sorted list of the names of the data sets not includes log file; not file name
		System.out.println("BEGIN OUTPUT");
		this.processor.getDataSets();
		System.out.println("END OUTPUT");
		//this.running = false;
	}
	
	/*
	 * Must equals to  1603797
	 */
	protected void getTotalPopulation() {
		System.out.println("BEGIN OUTPUT");
		System.out.println(this.processor.getTotalPopulation());
		System.out.println("END OUTPUT");;
		//this.running = false;
	}
	
	/*
	 * Get partial and full vaccinations
	 */
	protected void getVaccinationsPerCapita() throws Exception {

		System.out.println("Type partial or full for partial or full vaccinations");
		System.out.print("> ");
		System.out.flush();
		String vaccine = in.nextLine();
		vaccine = vaccine.toLowerCase();

		String time = Long.toString(System.currentTimeMillis());
		time += " " + vaccine;
		logger.logString(time);
	
		if (vaccine.equals("partial") || vaccine.equals("full")) {
			System.out.println("Type the date you are interessted in -- format: YYYY-MM-DD");
			System.out.print("> ");
			System.out.flush();
			String date = in.nextLine();

			time = Long.toString(System.currentTimeMillis());
			time += " " + date;
			logger.logString(time);

			Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
			boolean validDate = DATE_PATTERN.matcher(date).matches();
			if (validDate) {
				System.out.println("BEGIN OUTPUT");
				this.processor.getVaccinatedPerCapita(vaccine, date);
				System.out.println("END OUTPUT");
				//this.running = false;
			} else {
				System.out.println("Error: Invalid input");
			}
		} else {
			System.out.println("Error: Invalid input");
		}
		
	}
	
	/*
	 * Get AvgMktValue
	 */
	protected void getAvgMktValue() throws Exception {
		System.out.println("Please enter a 5 digit-zipcode");
		System.out.print("> ");
		System.out.flush();
		
		String val = in.nextLine();

		String time = Long.toString(System.currentTimeMillis());
		time += " " + val;
		logger.logString(time);

		// invalid zip code
		if (val.length() != 5) {
			System.out.println("Error: Invalid input");
		} else {
			try {
				// check whether is valid zipcode
				int zip = Integer.parseInt(val);
				System.out.println("BEGIN OUTPUT");
				System.out.println(this.processor.getAvgMktValue(val));
				System.out.println("END OUTPUT");
				//this.running = false;
			}
			catch(NumberFormatException e) {
				System.out.println("Error: Invalid input");
			}
		}
	}
	
	/*
	 * Get averageLiveAread
	 */
	protected void getAvgLivArea() throws Exception {
		System.out.println("Please enter a 5 digit-zipcode");
		System.out.print("> ");
		System.out.flush();
		String val = in.nextLine();

		String time = Long.toString(System.currentTimeMillis());
		time += " " + val;
		logger.logString(time);

		// invalid zip code
		if (val.length() != 5) {
			System.out.println("Error: Invalid input");
		} else {
			try {
				// check whether is valid zipcode
				int zip = Integer.parseInt(val);
				System.out.println("BEGIN OUTPUT");
				System.out.println(this.processor.getAvgLivArea(val));
				System.out.println("END OUTPUT");
				//this.running = false;
			}
			catch(NumberFormatException e) {
				System.out.println("Error: Invalid input");
			}
		}
	}
	
	/*
	 * Get market value per Capita
	 */
	protected void getSumMktValPerCapita() throws Exception {
		System.out.println("Please enter a 5 digit-zipcode");
		System.out.print("> ");
		System.out.flush();
		String val = in.nextLine();
		
		String time = Long.toString(System.currentTimeMillis());
		time += " " + val;
		logger.logString(time);

		// invalid zip code
		if (val.length() != 5) {
			System.out.println("Error: Invalid input");
		} else {
			try {
				// check whether is valid zipcode
				int zip = Integer.parseInt(val);
				System.out.println("BEGIN OUTPUT");
				System.out.println(this.processor.getSumMktValPerCapita(val));
				System.out.println("END OUTPUT");
				//this.running = false;
			}
			catch(NumberFormatException e) {
				System.out.println("Error: Invalid input");
			}
		}
	}

	protected void getDeathRateMktValue() {
		System.out.println("BEGIN OUTPUT");
		this.processor.getDeathRateMktValue();
		System.out.println("END OUTPUT");;
		//this.running = false;
	}
}