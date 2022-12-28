
package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.datamanagement.CovidReader;
import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.PropertyData;

import java.text.DecimalFormat;
import java.util.*;

public class Processor {

	protected CovidReader covidReader = null;
	protected CSVPopulationReader popReader = null;
	protected CSVPropertyReader propertyReader = null;

	protected HashMap<String, HashMap<String, CovidData>> covidData = null;
	protected HashMap<String, ArrayList<PropertyData>> propertyData = null;
	protected HashMap<String, Integer> popData = null;
	// get the args
	protected TreeMap<String, String> args;

	// memorization
	// Q2
	private boolean calPop = false;
	private static int totalPopulation = 0;
	// Q3
	private HashMap<String, TreeMap<String, String>> fullZipCapita = new HashMap<String, TreeMap<String, String>>();
	private HashMap<String, TreeMap<String, String>> partZipCapita = new HashMap<String, TreeMap<String, String>>();
	// Q4
	private static HashMap<String, String> avgMktVal = new HashMap<String, String>();
	// Q5
	private static HashMap<String, String> avgArea = new HashMap<String, String>();
	// Q6
	private static HashMap<String, Double> zipTotalVaule = new HashMap<String, Double>();
	// Q7
	private static Double maxDeathRate;
	// maximum marketing value / livable area
	private static Double maxMARate;


	protected Logger logger = Logger.getInstance();//get instance from logger

	public Processor(CovidReader covidReader, CSVPopulationReader popReader, CSVPropertyReader propertyReader,
					 TreeMap<String, String> seen) throws Exception {

		this.covidReader = covidReader;
		this.popReader = popReader;
		this.propertyReader = propertyReader;

		if (covidReader != null) {
			this.covidData = covidReader.getAllInfo();
		}
		if (propertyReader != null) {
			this.propertyData = propertyReader.getAllInfo();
		}
		if (popReader != null) {
			this.popData = popReader.getAllInfo();
		}
		this.args = seen;
	}

	/*
	 * Q1. get dataSets
	 */
	public void getDataSets() {
		for (Map.Entry<String, String>
				entry : this.args.entrySet())
			System.out.println(entry.getKey());
	}

	/*
	 * Q2. getTotalPopulation
	 * return total population
	 * this should be 1603797
	 */
	public int getTotalPopulation() {
		if (calPop) {
			return totalPopulation;
		}

		if (popReader == null) {return 0;}
		// popReader != null or covidReader is null
		int totalPop = 0;

		for (String key : this.popData.keySet()) {
			totalPop += this.popData.get(key);
		}

		totalPopulation = totalPop;
		calPop = true;
		return totalPopulation;
	}

	/*
	 * Q3.getVaccinationperCapita
	 */
	public void getVaccinatedPerCapita(String vaccine, String date) {
		// no input data for calculation
		if (covidData == null || popData == null) {
			System.out.println(0);
		} else {
			// round to 4 digits
			TreeMap<String, String> result = new TreeMap<String, String>();
			if (vaccine.equals("partial")) {
				// partial
				// check whether already store the result
				if (partZipCapita.containsKey(date)) {
					result = partZipCapita.get(date);
				} else {
					for (Map.Entry<String, HashMap<String, CovidData>> entry : covidData.entrySet()) {
						String zipcode = entry.getKey();
						HashMap<String, CovidData> timeMap = entry.getValue();
						Integer population = popData.get(zipcode);
						// only calculate the data with exciting records
						if (population != null && population != 0) {
							if (timeMap.containsKey(date)) {
								CovidData partial = timeMap.get(date);
								Integer pv = partial.getPartiallyVaccinated();
								if (pv != null && pv != 0) {
									String capita = new DecimalFormat("#.0000").format(pv * 1.0 / population);
									String capita1 = Double.toString(Double.parseDouble(capita));
									result.put(zipcode, capita1);
								}
							}
						}
					}
				}
			} else {
				// full
				// check whether already store the result
				if (fullZipCapita.containsKey(date)) {
					result = fullZipCapita.get(date);
				} else {
					for (Map.Entry<String, HashMap<String, CovidData>> entry : covidData.entrySet()) {
						String zipcode = entry.getKey();
						HashMap<String, CovidData> timeMap = entry.getValue();
						Integer population = popData.get(zipcode);
						// only calculate the data with exciting records
						if (population != null && population != 0) {
							if (timeMap.containsKey(date)) {
								CovidData full = timeMap.get(date);
								Integer pv = full.getFullyVaccinated();
								if (pv != null && pv != 0) {
									String capita = new DecimalFormat("#.0000").format(pv * 1.0 / population);
									String capita1 = Double.toString(Double.parseDouble(capita));
									result.put(zipcode, capita1);
								}
							}
						}
					}
				}
			}
			if (result.isEmpty()) {
				// if no records, then print 0
				System.out.println(0);
			} else {
				if (vaccine.equals("partial") && !partZipCapita.containsKey(date)) {
					partZipCapita.put(date, result);
				} else if (vaccine.equals("full") && !fullZipCapita.containsKey(date)) {
					fullZipCapita.put(date, result);
				}

				for (Map.Entry<String, String> entry : result.entrySet()) {
					String zipcode = entry.getKey();
					String capita = entry.getValue();
					System.out.println(zipcode + " " + capita);
				}
			}
		}
	}


	/*
	 * strategy design
	 */
	public String getAverage(String zipcode, PropertySelector selector) {

		// no property data
		if (this.propertyData == null) {
			return "0";
		}

		// zip code does not exist
		if (!this.propertyData.containsKey(zipcode)) {
			return "0";
		}

		double total = 0.0;
		double count = 0;

		for (PropertyData data : this.propertyData.get(zipcode)) {
			if (selector.getField(data) != null) {
				total += selector.getField(data);
				count++;
			}
		}

		if (count == 0) {
			return "0";
		}

		double average = total / count;

		return new DecimalFormat("#").format(average);
	}

	/*
	 * Q4.GetAverageMarketvalue
	 * return the average
	 */
	public String getAvgMktValue(String zipcode) {

		if (!avgMktVal.isEmpty()) {
			if (avgMktVal.containsKey(zipcode)) {
				return avgMktVal.get(zipcode);
			}
		}
		String result = getAverage(zipcode, new MktSelector());
		avgMktVal.put(zipcode, result);
		return result;
	}

	/*
	 * Q5.GetAverageLiverable area
	 * return averageliverable area
	 */
	public String getAvgLivArea(String zipcode) {

		if (!avgArea.isEmpty()) {
			if (avgArea.containsKey(zipcode)) {
				return avgArea.get(zipcode);
			}
		}
		String result = getAverage(zipcode, new AreaSelector());
		avgArea.put(zipcode, result);
		return result;
	}

	/*
	 * Q6 GetResMktValPerCapita
	 * return average
	 */
	public String getSumMktValPerCapita(String zipcode) {

		Double pMktValueTotal = 0.0;
		Integer population = 0;
		if (propertyData == null || popData == null) {
			return "0";
		} else {
			if (zipTotalVaule.containsKey(zipcode)) {
				pMktValueTotal = zipTotalVaule.get(zipcode);
			} else {
				// zipcode not found in the property
				if (!propertyData.containsKey(zipcode)) {
					return "0";
				} else {
					ArrayList<PropertyData> pData = propertyData.get(zipcode);
					for (PropertyData mktValue : pData) {
						pMktValueTotal += mktValue.getMarketValue();
					}
					zipTotalVaule.put(zipcode, pMktValueTotal);
				}
			}
			// zipcode not found in the population
			if (!popData.containsKey(zipcode)) {
				return "0";
			} else {
				population = popData.get(zipcode);
			}
			if (population==0 || pMktValueTotal==0) {
				return "0";
			} else {
				return new DecimalFormat("#").format(pMktValueTotal/population);
			}
		}
	}


	/*
	 * Q7. return the highest death rate and max market value/livable area on 2021-03-25
	 */
	public void getDeathRateMktValue(){
		if (maxMARate == null) {
			if (propertyData==null) {
				maxMARate = 0.0;
			} else {
				Double maRate;
				for (Map.Entry<String,ArrayList<PropertyData>> entry : propertyData.entrySet()) {
					ArrayList<PropertyData> propertyValue = entry.getValue();
					for (PropertyData pData : propertyValue) {
						Double area = pData.getTotalLivableArea();
						if (area != null && area!=0) {
							Double mktValue = pData.getMarketValue();
							if (mktValue == null) {
								maRate = 0.0;
							} else {
								maRate = pData.getMarketValue() / pData.getTotalLivableArea();
							}
						} else {
							maRate = 0.0;
						}
						if (maxMARate == null || maRate > maxMARate) { maxMARate = maRate; }
					}
				}
			}
		}

		if (maxDeathRate == null) {
			if (covidData==null || popData==null) {
				maxDeathRate = 0.0;
			} else {
				Double deathRate = 0.0;
				for (Map.Entry<String, HashMap<String, CovidData>> entry : covidData.entrySet()) {
					String zipcode = entry.getKey();
					HashMap<String, CovidData> deathData = entry.getValue();
					if (deathData.containsKey("2021-03-25") && popData.containsKey(zipcode)) {
						Integer population = popData.get(zipcode);
						Integer death = deathData.get("2021-03-25").getDeaths();
						if (population==null || death==null) {
							deathRate = 0.0;
						} else {
							deathRate = death * 1.0 / population;
						}
					}
					else {
						deathRate = 0.0;
					}
					if (maxDeathRate == null || deathRate > maxDeathRate) { maxDeathRate = deathRate; }
				}
			}
		}
		String maxDeathRate1 = new DecimalFormat("#.0000").format(maxDeathRate);
		System.out.println(Double.parseDouble(maxDeathRate1) + " " + maxMARate);
	}
}